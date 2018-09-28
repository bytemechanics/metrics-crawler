package org.bytemechanics.metrics.crawler.beans;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import org.bytemechanics.metrics.crawler.MeasureReducer;
import org.bytemechanics.metrics.crawler.internal.commons.string.SimpleFormat;

/**
 * Object to represent the metric statistics in a certain moment
 * @author afarre
 * @param <TYPE> Type of the metric
 * @see MeasureReducer
 * @since 1.0.0
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
	private final LocalDateTime snapshotTimestamp;

	
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
		this.snapshotTimestamp=LocalDateTime.now();
	}
	

	/**
	 * Retrieve the metric snapshot name
	 * @return metric snapshot name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Retrieve the metric snapshot accumulated measures
	 * @return metric snapshot accumulated measures
	 */
	public TYPE getAccumulatedSamples() {
		return accumulatedSamples;
	}
	/**
	 * Retrieve the metric snapshot accumulated measures formatted to string
	 * @return metric snapshot accumulated measures formatted to string
	 */
	public String getFormatedAccumulatedSamples() {
		return this.measureReducer.toString(accumulatedSamples);
	}
	/**
	 * Retrieve the metric snapshot total number of samples taken in account to create this statistics
	 * @return metric snapshot total number of samples taken in account to create this statistics
	 */
	public long getSamplingSize() {
		return samplingSize;
	}
	/**
	 * Retrieve the metric snapshot total number of measures taken
	 * @return metric snapshot total number of measures taken
	 */
	public long getTotalHits() {
		return totalHits;
	}
	/**
	 * Retrieve the metric snapshot max measure
	 * @return metric snapshot max measure
	 */
	public TYPE getMaxMeasure() {
		return maxMeasure;
	}
	/**
	 * Retrieve the metric snapshot max measure formatted to string
	 * @return metric snapshot max measure formatted to string
	 */
	public String getFormatedMaxMeasure() {
		return this.measureReducer.toString(maxMeasure);
	}
	/**
	 * Retrieve the metric snapshot min measure 
	 * @return metric snapshot min measure
	 */
	public TYPE getMinMeasure() {
		return minMeasure;
	}
	/**
	 * Retrieve the metric snapshot min measure formatted to string
	 * @return metric snapshot min measure formatted to string
	 */
	public String getFormatedMinMeasure() {
		return this.measureReducer.toString(minMeasure);
	}
	/**
	 * Retrieve the metric snapshot average measure
	 * @return metric snapshot average measure 
	 */
	public TYPE getAverageMeasure() {
		return averageMeasure;
	}
	/**
	 * Retrieve the metric snapshot average measure formatted to string
	 * @return metric snapshot average measure formatted to string
	 */
	public String getFormatedAverageMeasure() {
		return this.measureReducer.toString(averageMeasure);
	}
	/**
	 * Retrieve the metric snapshot last measure
	 * @return metric snapshot last measure 
	 */
	public TYPE getLastMeasure() {
		return lastMeasure;
	}
	/**
	 * Retrieve the metric snapshot last measure formatted to string
	 * @return metric snapshot last measure formatted to string
	 */
	public String getFormatedLastMeasure() {
		return this.measureReducer.toString(lastMeasure);
	}
	/**
	 * Retrieve the metric snapshot last occurrence
	 * @return metric snapshot last occurrence
	 */
	public LocalDateTime getLastOccurrence() {
		return lastOccurrence;
	}
	/**
	 * Retrieve the metric snapshot generation timestamp
	 * @return metric snapshot generation timestamp
	 */
	public LocalDateTime getSnapshotTimestamp() {
		return snapshotTimestamp;
	}
	
	
	/**
	 * Perfom a reduction with the given _metric using the measure reducer
	 * @param _metric metric snapshot to reduce with
	 * @return reduced metric snapshot
	 * @see MeasureReducer
	 */
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
						.averageMeasure(this.measureReducer.accumulate(this.accumulatedSamples, _metric.accumulatedSamples)
															.flatMap(total -> this.measureReducer.average(total,this.samplingSize+_metric.samplingSize))
															.orElseGet(this.measureReducer::identity))
						.lastMeasure((this.lastOccurrence.isBefore(_metric.lastOccurrence))? _metric.lastMeasure : this.lastMeasure)
						.lastOccurrence((this.lastOccurrence.isBefore(_metric.lastOccurrence))? _metric.lastOccurrence : this.lastOccurrence)
					.build();
	}
	
	/** @see Object#hashCode()  */
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

	/** @see Object#toString() */
	@Override
	public String toString() {
		return SimpleFormat.format("MetricSnapshot[measureReducer={}, name={}, accumulatedSamples={}, samplingSize={}, totalHits={}, maxMeasure={}, minMeasure={}, averageMeasure={}, lastMeasure={}, lastOccurrence={}"
											, measureReducer , name, accumulatedSamples, samplingSize, totalHits, maxMeasure, minMeasure, averageMeasure, lastMeasure, lastOccurrence);
	}
	
	/**
	 * Metric builder to perform more semantic development
	 * @param <TYPE> type of the metricSnapshot to create
	 */
	public static class MetricSnapshotBuilder<TYPE> {
		
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

		/**
		 * Creates a metric snapshot builder from the given _measureReducer
		 * @param _measureReducer measure reducer for this metric snapshot builder
		 */
		public MetricSnapshotBuilder(final MeasureReducer<TYPE> _measureReducer) {
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
		/**
		 * Creates a metric snapshot clone with this_measureReducer
		 * @param _measureReducer measure reducer for this metric snapshot builder
		 * @param _metricSnapshot original metric snapshot to clone
		 */
		public MetricSnapshotBuilder(final MeasureReducer<TYPE> _measureReducer,final MetricSnapshot<TYPE> _metricSnapshot) {
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
		
		
		/**
		 * sets the name and return the current builder instance
		 * @param _name name to set
		 * @return current builder instance
		 */
		public MetricSnapshotBuilder name(final String _name) {
			this.name = _name;
			return this;
		}
		/**
		 * sets the accumulatedSamples and return the current builder instance
		 * @param _accumulatedSamples accumulatedSamples to set
		 * @return current builder instance
		 */
		public MetricSnapshotBuilder accumulatedSamples(final TYPE _accumulatedSamples) {
			this.accumulatedSamples = _accumulatedSamples;
			return this;
		}
		/**
		 * sets the samplingSize and return the current builder instance
		 * @param _samplingSize samplingSize to set
		 * @return current builder instance
		 */
		public MetricSnapshotBuilder samplingSize(final long _samplingSize) {
			this.samplingSize = _samplingSize;
			return this;
		}
		/**
		 * sets the totalHits and return the current builder instance
		 * @param _totalHits totalHits to set
		 * @return current builder instance
		 */
		public MetricSnapshotBuilder totalHits(final long _totalHits) {
			this.totalHits = _totalHits;
			return this;
		}
		/**
		 * sets the maxMeasure and return the current builder instance
		 * @param _maxMeasure maxMeasure to set
		 * @return current builder instance
		 */
		public MetricSnapshotBuilder maxMeasure(final TYPE _maxMeasure) {
			this.maxMeasure = _maxMeasure;
			return this;
		}
		/**
		 * sets the minMeasure and return the current builder instance
		 * @param _minMeasure minMeasure to set
		 * @return current builder instance
		 */
		public MetricSnapshotBuilder minMeasure(final TYPE _minMeasure) {
			this.minMeasure = _minMeasure;
			return this;
		}
		/**
		 * sets the averageMeasure and return the current builder instance
		 * @param _averageMeasure averageMeasure to set
		 * @return current builder instance
		 */
		public MetricSnapshotBuilder averageMeasure(final TYPE _averageMeasure) {
			this.averageMeasure = _averageMeasure;
			return this;
		}
		/**
		 * sets the lastMeasure and return the current builder instance
		 * @param _lastMeasure lastMeasure to set
		 * @return current builder instance
		 */
		public MetricSnapshotBuilder lastMeasure(final TYPE _lastMeasure) {
			this.lastMeasure = _lastMeasure;
			return this;
		}
		/**
		 * sets the lastOccurrence and return the current builder instance
		 * @param _lastOccurrence lastOccurrence to set
		 * @return current builder instance
		 */
		public MetricSnapshotBuilder lastOccurrence(final LocalDateTime _lastOccurrence) {
			this.lastOccurrence = _lastOccurrence;
			return this;
		}

		/**
		 * Builds the actual metric snaphot represented by this builder
		 * @return new metric snapshot instance
		 */
		public MetricSnapshot build() {
			return new MetricSnapshot<>(this.measureReducer,name, accumulatedSamples, samplingSize, totalHits, maxMeasure, minMeasure,averageMeasure, lastMeasure, lastOccurrence);
		}
	}

	/**
	 * Retrieves a new MetricSnapshotBuilder with the given _measureReducer
	 * @param <T> Type of the metric to create
	 * @param _measureReducer measure reducer to use to create the new builder
	 * @return new MetricSnapshotBuilder
	 */
	@java.lang.SuppressWarnings("all")
	public static <T> MetricSnapshotBuilder builder(final MeasureReducer<T> _measureReducer) {
		return new MetricSnapshotBuilder(_measureReducer);
	}	
	/**
	 * Retrieves a new MetricSnapshotBuilder that clones the given _metricSnapshot
	 * @param <T> Type of the metric to create
	 * @param _measureReducer measure reducer to use to create the new builder
	 * @param _metricSnapshot metric snapshot to clone
	 * @return new MetricSnapshotBuilder
	 */
	public static <T> MetricSnapshotBuilder builder(final MeasureReducer<T> _measureReducer,final MetricSnapshot<T> _metricSnapshot) {
		return new MetricSnapshotBuilder(_measureReducer,_metricSnapshot);
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
