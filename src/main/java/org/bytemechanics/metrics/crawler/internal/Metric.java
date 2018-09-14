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
import org.bytemechanics.metrics.crawler.exceptions.IncorrectSamplingSize;
import org.bytemechanics.metrics.crawler.internal.commons.collections.FastDropLastQueue;
import org.bytemechanics.metrics.crawler.internal.commons.string.SimpleFormat;

/**
 * @author afarre
 * @param <TYPE>
 */
public class Metric<TYPE> {
	
	private final String name;
	private final Queue<Measure<TYPE>> measures;
	private long hits;
	private final MeasureReducer<TYPE> reducer;

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

	
	public String getName() {
		return name;
	}
	public List<Measure<TYPE>> getMeasures() {
		return measures.stream().collect(Collectors.toList());
	}
	public long getHits() {
		return hits;
	}
	public MeasureReducer<TYPE> getReducer() {
		return reducer;
	}

	
	public void addMeasure(final LocalDateTime _time,final TYPE _measure){
		this.hits++;
		this.measures.offer(new Measure(_time, _measure,this.reducer));
	}
	
	
	public MetricSnapshot toSnapshot(){
		return this.measures.stream()
						.limit(this.measures.size())
						.filter(Measure::notNull)
						.map(Measure::toMetricSnapshot)
						.reduce(MetricSnapshot::reduce)
							.map(this::completeSnapshot)
							.orElseGet(this::defaultSnapshot);
	}
	protected MetricSnapshot completeSnapshot(final MetricSnapshot _metricSnapshot){
		return MetricSnapshot.builder(this.reducer,_metricSnapshot)
									.name(this.name)
									.totalHits(this.hits)
								.build();
	}
	protected MetricSnapshot defaultSnapshot(){
		return MetricSnapshot.builder(this.reducer)
									.name(this.name)
								.build();
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 67 * hash + Objects.hashCode(this.name);
		hash = 67 * hash + Objects.hashCode(this.reducer);
		return hash;
	}

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
	


	@Override
	public String toString() {
		return SimpleFormat.format("Metric[name={}, hits={}, measures={}, reducer={}]", this.name, this.hits, this.measures, this.reducer);
	}
}
