package org.bytemechanics.metrics.crawler.beans;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import org.bytemechanics.metrics.crawler.MeasureReducer;
import org.bytemechanics.metrics.crawler.internal.commons.string.SimpleFormat;

/**
 * @author E103880
 * @param <TYPE>
 */
public class MetricSnapshot<TYPE>{

	private final MeasureReducer<TYPE> measureReducer;

	private final String name;
	private final TYPE accumulatedSamples;
	private final long samplingSize;
	private final long totalHits;
	private final TYPE maxMeasure;
	private final TYPE minMeasure;
	private final TYPE averageMeasure;
	private final TYPE lastMeasure;
	private final LocalDateTime lastOccurrence;

	
	protected MetricSnapshot(final MeasureReducer<TYPE> _measureReducer,final String _name,final TYPE _accumulatedSamples,final long _samplingSize,final long _totalHits,final TYPE _maxMeasure,final TYPE _minMeasure,final TYPE _averageMeasure,final TYPE _lastMeasure,final LocalDateTime _lastOccurrence) {
		this.measureReducer=_measureReducer;
		this.name = _name;
		this.accumulatedSamples = _accumulatedSamples;
		this.samplingSize = _samplingSize;
		this.totalHits = _totalHits;
		this.maxMeasure = _maxMeasure;
		this.minMeasure = _minMeasure;
		this.averageMeasure=_averageMeasure;
		this.lastMeasure = _lastMeasure;
		this.lastOccurrence = _lastOccurrence;
	}
	

	public String getName() {
		return name;
	}
	public TYPE getAccumulatedSamples() {
		return accumulatedSamples;
	}
	public String getFormatedAccumulatedSamples() {
		return this.measureReducer.toString(accumulatedSamples);
	}
	public long getSamplingSize() {
		return samplingSize;
	}
	public long getTotalHits() {
		return totalHits;
	}
	public TYPE getMaxMeasure() {
		return maxMeasure;
	}
	public String getFormatedMaxMeasure() {
		return this.measureReducer.toString(maxMeasure);
	}
	public TYPE getMinMeasure() {
		return minMeasure;
	}
	public String getFormatedMinMeasure() {
		return this.measureReducer.toString(minMeasure);
	}
	public TYPE getAverageMeasure() {
		return averageMeasure;
	}
	public String getFormatedAverageMeasure() {
		return this.measureReducer.toString(averageMeasure);
	}
	public TYPE getLastMeasure() {
		return lastMeasure;
	}
	public String getFormatedLastMeasure() {
		return this.measureReducer.toString(lastMeasure);
	}
	public LocalDateTime getLastOccurrence() {
		return lastOccurrence;
	}
	
	
	public MetricSnapshot<TYPE> reduce(final MetricSnapshot<TYPE> _metric) {

		return MetricSnapshot.builder(this.measureReducer)
						.name(this.name)
						.accumulatedSamples(this.measureReducer.accumulate(this.accumulatedSamples, _metric.accumulatedSamples)
																		.orElseGet(this.measureReducer::identity))
						.samplingSize(this.samplingSize+_metric.samplingSize)
						.totalHits(this.totalHits+_metric.totalHits)
						.maxMeasure(this.measureReducer.max(this.maxMeasure,_metric.maxMeasure)
														.orElseGet(this.measureReducer::identity))
						.minMeasure(this.measureReducer.min(this.minMeasure,_metric.minMeasure)
														.orElseGet(this.measureReducer::identity))
						.averageMeasure(this.measureReducer.accumulate(this.averageMeasure, _metric.averageMeasure)
															.flatMap(total -> this.measureReducer.average(total,2l))
															.orElseGet(this.measureReducer::identity))
						.lastMeasure((this.lastOccurrence.isBefore(_metric.lastOccurrence))? _metric.lastMeasure : this.lastMeasure)
						.lastOccurrence((this.lastOccurrence.isBefore(_metric.lastOccurrence))? _metric.lastOccurrence : this.lastOccurrence)
					.build();
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 89 * hash + Objects.hashCode(this.measureReducer);
		hash = 89 * hash + Objects.hashCode(this.name);
		hash = 89 * hash + Objects.hashCode(this.accumulatedSamples);
		hash = 89 * hash + (int) (this.samplingSize ^ (this.samplingSize >>> 32));
		hash = 89 * hash + (int) (this.totalHits ^ (this.totalHits >>> 32));
		hash = 89 * hash + Objects.hashCode(this.maxMeasure);
		hash = 89 * hash + Objects.hashCode(this.minMeasure);
		hash = 89 * hash + Objects.hashCode(this.averageMeasure);
		hash = 89 * hash + Objects.hashCode(this.lastMeasure);
		hash = 89 * hash + Objects.hashCode(this.lastOccurrence);
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
		final MetricSnapshot<?> other = (MetricSnapshot<?>) obj;
		if (this.samplingSize != other.samplingSize) {
			return false;
		}
		if (this.totalHits != other.totalHits) {
			return false;
		}
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		if (!Objects.equals(this.measureReducer, other.measureReducer)) {
			return false;
		}
		if (!Objects.equals(this.accumulatedSamples, other.accumulatedSamples)) {
			return false;
		}
		if (!Objects.equals(this.maxMeasure, other.maxMeasure)) {
			return false;
		}
		if (!Objects.equals(this.minMeasure, other.minMeasure)) {
			return false;
		}
		if (!Objects.equals(this.averageMeasure, other.averageMeasure)) {
			return false;
		}
		if (!Objects.equals(this.lastMeasure, other.lastMeasure)) {
			return false;
		}
		return Objects.equals(this.lastOccurrence, other.lastOccurrence);
	}

	@Override
	public String toString() {
		return SimpleFormat.format("MetricSnapshot[measureReducer={}, name={}, accumulatedSamples={}, samplingSize={}, totalHits={}, maxMeasure={}, minMeasure={}, averageMeasure={}, lastMeasure={}, lastOccurrence={}"
											, measureReducer , name, accumulatedSamples, samplingSize, totalHits, maxMeasure, minMeasure, averageMeasure, lastMeasure, lastOccurrence);
	}
	
	public static class MetricBuilder<TYPE> {
		
		private final MeasureReducer<TYPE> measureReducer;
		private String name;
		private TYPE accumulatedSamples;
		private long samplingSize;
		private long totalHits;
		private TYPE maxMeasure;
		private TYPE minMeasure;
		private TYPE lastMeasure;
		private TYPE averageMeasure;
		private LocalDateTime lastOccurrence;

		public MetricBuilder(final MeasureReducer<TYPE> _measureReducer) {
			this.measureReducer=_measureReducer;
			this.name=null;
			this.accumulatedSamples=this.measureReducer.identity();
			this.samplingSize=0l;
			this.totalHits=0l;
			this.maxMeasure=this.measureReducer.identity();
			this.minMeasure=this.measureReducer.identity();
			this.averageMeasure=this.measureReducer.identity();
			this.lastMeasure=this.measureReducer.identity();
			this.lastOccurrence=null;
		}
		public MetricBuilder(final MeasureReducer<TYPE> _measureReducer,final MetricSnapshot<TYPE> _metricSnapshot) {
			this.measureReducer=_measureReducer;
			this.name = _metricSnapshot.getName();
			this.accumulatedSamples = _metricSnapshot.getAccumulatedSamples();
			this.samplingSize = _metricSnapshot.getSamplingSize();
			this.totalHits = _metricSnapshot.getTotalHits();
			this.maxMeasure = _metricSnapshot.getMaxMeasure();
			this.minMeasure = _metricSnapshot.getMinMeasure();
			this.averageMeasure=_metricSnapshot.getAverageMeasure();
			this.lastMeasure = _metricSnapshot.getLastMeasure();
			this.lastOccurrence = _metricSnapshot.getLastOccurrence();
		}
		
		
		public MetricBuilder name(final String _name) {
			this.name = _name;
			return this;
		}
		public MetricBuilder accumulatedSamples(final TYPE _accumulatedSamples) {
			this.accumulatedSamples = _accumulatedSamples;
			return this;
		}
		public MetricBuilder samplingSize(final long _samplingSize) {
			this.samplingSize = _samplingSize;
			return this;
		}
		public MetricBuilder totalHits(final long _totalHits) {
			this.totalHits = _totalHits;
			return this;
		}
		public MetricBuilder maxMeasure(final TYPE _maxMeasure) {
			this.maxMeasure = _maxMeasure;
			return this;
		}
		public MetricBuilder minMeasure(final TYPE _minMeasure) {
			this.minMeasure = _minMeasure;
			return this;
		}
		public MetricBuilder averageMeasure(final TYPE _averageMeasure) {
			this.averageMeasure = _averageMeasure;
			return this;
		}
		public MetricBuilder lastMeasure(final TYPE _lastMeasure) {
			this.lastMeasure = _lastMeasure;
			return this;
		}
		public MetricBuilder lastOccurrence(final LocalDateTime _lastOccurrence) {
			this.lastOccurrence = _lastOccurrence;
			return this;
		}
		public MetricSnapshot build() {
			return new MetricSnapshot<>(this.measureReducer,name, accumulatedSamples, samplingSize, totalHits, maxMeasure, minMeasure,averageMeasure, lastMeasure, lastOccurrence);
		}
	}

	@java.lang.SuppressWarnings("all")
	public static <T> MetricBuilder builder(final MeasureReducer<T> _measureReducer) {
		return new MetricBuilder(_measureReducer);
	}	
	public static <T> MetricBuilder builder(final MeasureReducer<T> _measureReducer,final MetricSnapshot<T> _metricSnapshot) {
		return new MetricBuilder(_measureReducer,_metricSnapshot);
	}	
	public static int compareNames(final MetricSnapshot _metric1,final MetricSnapshot _metric2) {
		return Optional.ofNullable(_metric1)
						.map(MetricSnapshot::getName)
						.map(leftName -> Optional.ofNullable(_metric2)
												.map(MetricSnapshot::getName)
												.map(rightName -> leftName.compareTo(rightName))
												.orElse(1))
						.orElseGet(() -> Optional.ofNullable(_metric2)
												.map(MetricSnapshot::getName)
												.map(metric -> -1)
												.orElse(0));
	}	
}
