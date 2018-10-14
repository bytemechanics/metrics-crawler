/*
 * Copyright 2018 Byte Mechanics.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bytemechanics.metrics.crawler.internal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;
import org.bytemechanics.metrics.crawler.MeasureReducer;
import org.bytemechanics.metrics.crawler.beans.MetricSnapshot;
import org.bytemechanics.metrics.crawler.exceptions.IncorrectMeasureType;
import org.bytemechanics.metrics.crawler.exceptions.IncorrectSamplingSize;
import org.bytemechanics.metrics.crawler.internal.commons.collections.FastDropLastQueue;
import org.bytemechanics.metrics.crawler.internal.commons.string.SimpleFormat;

/**
 * Metric representation bean
 * @param <TYPE> metric type
 * @author afarre
 * @since 1.0.0
 */
public class Metric<TYPE> {
	
	private final String name;
	private final Queue<Measure<TYPE>> measures;
	private long hits;
	private final MeasureReducer<TYPE> reducer;

	
	/**
	 * Metric constructor
	 * @param _name metric name (mandatory)
	 * @param _samplingSize max samples to store
	 * @param _reducer reducer for this metric
	 * @throws NullPointerException if either _name or _reducer are null
	 */
	public Metric(final String _name,final int _samplingSize,final MeasureReducer<TYPE> _reducer) {
		if(_name==null)
			throw new NullPointerException("Name can not be null to create a Metric");
		this.name = _name;
		if(_reducer==null)
			throw new NullPointerException(SimpleFormat.format("Metric {} reducer can not be null to create a Metric",_name));
		this.reducer=_reducer;
		if(_samplingSize<=0)
			throw new IncorrectSamplingSize(_name, _samplingSize);
		this.measures = new FastDropLastQueue<>(_samplingSize);
		this.hits = 0l;
	}

	/**
	 * Retrieves metric name
	 * @return metric name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Retrieves the current list of measures. Note this metrics not necessary should come ordered
	 * @return measure metric list
	 */
	public List<Measure<TYPE>> getMeasures() {
		return measures.stream().collect(Collectors.toList());
	}
	/**
	 * Retrieves the current number of measures registered since it's creation
	 * @return the current number of measures registered since it's creation
	 */
	public long getHits() {
		return hits;
	}
	/**
	 * Retrieves the metric reducer
	 * @return the metric reducer
	 */
	public MeasureReducer<TYPE> getReducer() {
		return reducer;
	}

	
	/**
	 * Register a new measure for this metric
	 * @param _timestamp timestamp for this measure (mandatory)
	 * @param _measure measure to register (mandatory)
	 * @throws NullPointerException if any of _time or_measure are null
	 * @throws IncorrectMeasureType if the _measure type is not assignable to the type of the reducer of this metric
	 */
	public void addMeasure(final LocalDateTime _timestamp,final TYPE _measure){
		if(_timestamp==null)
			throw new NullPointerException(SimpleFormat.format("Can not register null _timestamp measure at metric {}",this.name));
		if(_measure==null)
			throw new NullPointerException(SimpleFormat.format("Can not register null _measure at metric {}",this.name));
		if(!this.reducer.getType().isAssignableFrom(_measure.getClass()))
			throw new IncorrectMeasureType(this.name, this.reducer.getType(), _measure.getClass());
		this.hits++;
		this.measures.offer(new Measure(_timestamp, _measure,this.reducer));
	}
	
	/**
	 * Retrieve a snapshot of the current metric status
	 * @return MetricSnapshot of the same TYPE
	 * @see MetricSnapshot
	 */
	public MetricSnapshot<TYPE> toSnapshot(){
		return this.measures.stream()
						.limit(this.measures.size())
						.filter(Metric::notNull)
						.map(Measure::toMetricSnapshot)
						.reduce(MetricSnapshot::reduce)
							.map(this::completeSnapshot)
							.orElseGet(this::defaultSnapshot);
	}
	/**
	 * Retrieve a new snapshot cloned from the given snapshot with the current hits as total hits
	 * @param _metricSnapshot metric snapshot to clone
	 * @return MetricSnapshot of the same TYPE cloned from the given snapshot with the current hits as total hits
	 * @see MetricSnapshot
	 */
	protected MetricSnapshot<TYPE> completeSnapshot(final MetricSnapshot _metricSnapshot){
		return MetricSnapshot.builder(this.reducer,_metricSnapshot)
									.name(this.name)
									.totalHits(this.hits)
								.build();
	}
	/**
	 * Retrieve the default empty snapshot for this metric (without any measure)
	 * @return MetricSnapshot of the same TYPE
	 * @see MetricSnapshot
	 */
	protected MetricSnapshot<TYPE> defaultSnapshot(){
		return MetricSnapshot.builder(this.reducer)
									.name(this.name)
								.build();
	}
	private static boolean notNull(final Measure _measure){
		return _measure!=null;
	}
	
	/** @see Object#hashCode() */
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 67 * hash + Objects.hashCode(this.name);
		hash = 67 * hash + Objects.hashCode(this.reducer);
		return hash;
	}

	/** @see Object#equals(java.lang.Object)  */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Metric<?> other = (Metric<?>) obj;
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		return Objects.equals(this.reducer, other.reducer);
	}
	
	/** @see Object#toString()   */
	@Override
	public String toString() {
		return SimpleFormat.format("Metric[name={}, hits={}, measures={}, reducer={}]", this.name, this.hits, this.measures, this.reducer);
	}
}
