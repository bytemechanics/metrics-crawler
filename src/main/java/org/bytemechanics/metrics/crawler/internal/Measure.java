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
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import org.bytemechanics.metrics.crawler.MeasureReducer;
import org.bytemechanics.metrics.crawler.beans.MetricSnapshot;
import org.bytemechanics.metrics.crawler.internal.commons.string.SimpleFormat;

/**
 * Measure representation bean
 * @author afarre
 * @param <TYPE> type of the measure
 * @since 1.0.0
 */
public class Measure<TYPE> {
	
	private final LocalDateTime timestamp;
	private final TYPE value;
	private final MeasureReducer<TYPE> reducer;
	
	/**
	 * Measure constructor
	 * @param _timestamp timestamp for this measure (mandatory)
	 * @param _value measure value (mandatory)
	 * @param _reducer reducer for this measure (mandatory)
	 * @throws NullPointerException if either _time, _measure or _reducer are null
	 */
	public Measure(final LocalDateTime _timestamp,final TYPE _value,final MeasureReducer<TYPE> _reducer){
		if(_timestamp==null)
			throw new NullPointerException("Can not create null _timestamp measure");
		this.timestamp=_timestamp;
		if(_value==null)
			throw new NullPointerException("Can not create null _value measure");
		this.value=_value;
		if(_reducer==null)
			throw new NullPointerException("Can not create null _reducer measure");
		this.reducer=_reducer;
	}

	
	/**
	 * Retrieves the measure timestamp
	 * @return the measure timestamp
	 */
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	/**
	 * Retrieves the measure value
	 * @return the measure value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Convert the current measure in a metricSnapshot
	 * @return MetricSnapshot of the same TYPE
	 * @see MetricSnapshot
	 */
	public MetricSnapshot<TYPE> toMetricSnapshot(){
		return MetricSnapshot.builder(this.reducer)
								.samplingSize(1)
								.accumulatedSamples(this.value)
								.maxMeasure(this.value)
								.minMeasure(this.value)
								.averageMeasure(this.value)
								.lastMeasure(this.value)
								.lastOccurrence(this.timestamp)
							.build();
	}

	/** @see Object#hashCode() */
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 19 * hash + Objects.hashCode(this.timestamp);
		hash = 19 * hash + Objects.hashCode(this.value);
		hash = 19 * hash + Objects.hashCode(this.reducer);
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
		final Measure<?> other = (Measure<?>) obj;
		if (!Objects.equals(this.timestamp, other.timestamp)) {
			return false;
		}
		if (!Objects.equals(this.value, other.value)) {
			return false;
		}
		return Objects.equals(this.reducer, other.reducer);
	}	
	
	/** @see Object#toString()   */
	@Override
	public String toString() {
		return SimpleFormat.format("Measure[timestamp={}, measureType={}, measure={},reducer={}]",
									Optional.ofNullable(timestamp).map(val -> val.format(DateTimeFormatter.ISO_DATE_TIME)).orElse("null"),
									Optional.ofNullable(value).map(val -> val.getClass()).map(val -> val.toString()).orElse("null"),
									value,
									reducer);
	}
}
