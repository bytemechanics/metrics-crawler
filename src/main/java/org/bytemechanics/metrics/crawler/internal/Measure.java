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
 * @author afarre
 * @param <TYPE>
 */
public class Measure<TYPE> {
	
	private final LocalDateTime timestamp;
	private final TYPE measure;
	private final MeasureReducer<TYPE> reducer;
	
	public Measure(final LocalDateTime _time,final TYPE _measure,final MeasureReducer<TYPE> _reducer){
		this.timestamp=_time;
		this.measure=_measure;
		this.reducer=_reducer;
	}

	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public Object getMeasure() {
		return measure;
	}

	public MetricSnapshot toMetricSnapshot(){
		return MetricSnapshot.builder(this.reducer)
								.samplingSize(1)
								.accumulatedSamples(this.measure)
								.maxMeasure(this.measure)
								.minMeasure(this.measure)
								.averageMeasure(this.measure)
								.lastMeasure(this.measure)
								.lastOccurrence(this.timestamp)
							.build();
	}
	
	@Override
	public String toString() {
		return SimpleFormat.format("Measure[timestamp={}, measureType={}, measure={},reducer={}]",
									Optional.ofNullable(timestamp).map(val -> val.format(DateTimeFormatter.ISO_DATE_TIME)).orElse("null"),
									Optional.ofNullable(measure).map(val -> val.getClass()).map(val -> val.toString()).orElse("null"),
									measure,
									reducer);
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 19 * hash + Objects.hashCode(this.timestamp);
		hash = 19 * hash + Objects.hashCode(this.measure);
		hash = 19 * hash + Objects.hashCode(this.reducer);
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
		final Measure<?> other = (Measure<?>) obj;
		if (!Objects.equals(this.timestamp, other.timestamp)) {
			return false;
		}
		if (!Objects.equals(this.measure, other.measure)) {
			return false;
		}
		return Objects.equals(this.reducer, other.reducer);
	}

	public static boolean notNull(final Measure _measure){
		return _measure!=null;
	}
}
