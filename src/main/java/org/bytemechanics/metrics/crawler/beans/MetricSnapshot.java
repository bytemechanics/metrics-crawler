package org.bytemechanics.metrics.crawler.beans;

import java.time.LocalDateTime;
import org.bytemechanics.metrics.crawler.MeasureReducer;
import org.bytemechanics.metrics.crawler.internal.commons.string.SimpleFormat;

/**
 * @author E103880
 * @param <TYPE>
 */
public class MetricSnapshot<TYPE> implements Comparable<MetricSnapshot>{

	private final MeasureReducer<TYPE> measureReducer;

	private final String name;
	private final TYPE samplingAccumulatedMeasure;
	private final long samplingSize;
	private final long totalHits;
	private final TYPE maxMeasure;
	private final TYPE minMeasure;
	private final TYPE averageMeasure;
	private final TYPE lastMeasure;
	private final LocalDateTime lastOccurrence;

	
	public MetricSnapshot(final MeasureReducer<TYPE> _measureReducer,final String _name,final TYPE _accumulated,final long _hits,final long _globalHits,final TYPE _maxMeasure,final TYPE _minMeasure,final TYPE _averageMeasure,final TYPE _lastMeasure,final LocalDateTime _lastOccurrence) {
		this.measureReducer=_measureReducer;
		this.name = _name;
		this.samplingAccumulatedMeasure = _accumulated;
		this.samplingSize = _hits;
		this.totalHits = _globalHits;
		this.maxMeasure = _maxMeasure;
		this.minMeasure = _minMeasure;
		this.averageMeasure=_averageMeasure;
		this.lastMeasure = _lastMeasure;
		this.lastOccurrence = _lastOccurrence;
	}
	

	public String getName() {
		return name;
	}
	public TYPE getSamplingAccumulatedMeasure() {
		return samplingAccumulatedMeasure;
	}
	public String getFormatedSamplingAccumulatedMeasure() {
		return this.measureReducer.toString(samplingAccumulatedMeasure);
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
	public TYPE getLatestMeasure() {
		return lastMeasure;
	}
	public String getFormatedLatestMeasure() {
		return this.measureReducer.toString(lastMeasure);
	}
	public LocalDateTime getTimestamp() {
		return lastOccurrence;
	}
	
	
	public MetricSnapshot<TYPE> reduce(final MetricSnapshot<TYPE> _metric) {

		return MetricSnapshot.builder(this.measureReducer)
						.name(this.name)
						.samplingAccumulatedMeasure(this.measureReducer.accumulate(this.samplingAccumulatedMeasure, _metric.samplingAccumulatedMeasure))
						.samplingSize(this.samplingSize+_metric.samplingSize)
						.totalHits(this.totalHits+_metric.totalHits)
						.maxMeasure(this.measureReducer.max(this.maxMeasure,_metric.maxMeasure))
						.minMeasure(this.measureReducer.min(this.minMeasure,_metric.minMeasure))
						.averageMeasure(this.measureReducer.min(this.averageMeasure,_metric.minMeasure))
						.lastMeasure((this.lastOccurrence.isBefore(_metric.lastOccurrence))? _metric.lastMeasure : this.lastMeasure)
						.lastOccurrence((this.lastOccurrence.isBefore(_metric.lastOccurrence))? _metric.lastOccurrence : this.lastOccurrence)
					.build();
	}

	
	@Override
	public int compareTo(final MetricSnapshot _metric) {
		return this.name.compareTo(_metric.name);
	}


	@Override
	public String toString() {
		return SimpleFormat.format("MetricSnapshot[name={}, samplingAccumulatedMeasure={}, samplingSize={}, totalHits={}, maxMeasure={}, minMeasure={}, averageMeasure={}, lastMeasure={}, lastOccurrence={}"
											,name, samplingAccumulatedMeasure, samplingSize, totalHits, maxMeasure, minMeasure, averageMeasure, lastMeasure, lastOccurrence);
	}
	
	public static class MetricBuilder<TYPE> {
		
		private final MeasureReducer<TYPE> measureReducer;
		private String name;
		private TYPE samplingAccumulatedMeasure;
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
			this.samplingAccumulatedMeasure=this.measureReducer.identity();
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
			this.samplingAccumulatedMeasure = _metricSnapshot.getSamplingAccumulatedMeasure();
			this.samplingSize = _metricSnapshot.getSamplingSize();
			this.totalHits = _metricSnapshot.getTotalHits();
			this.maxMeasure = _metricSnapshot.getMaxMeasure();
			this.minMeasure = _metricSnapshot.getMinMeasure();
			this.averageMeasure=_metricSnapshot.getAverageMeasure();
			this.lastMeasure = _metricSnapshot.getLatestMeasure();
			this.lastOccurrence = _metricSnapshot.getTimestamp();
		}
		
		
		public MetricBuilder name(final String _name) {
			this.name = _name;
			return this;
		}
		public MetricBuilder samplingAccumulatedMeasure(final TYPE _samplingAccumulatedMeasure) {
			this.samplingAccumulatedMeasure = _samplingAccumulatedMeasure;
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
			return new MetricSnapshot<>(this.measureReducer,name, samplingAccumulatedMeasure, samplingSize, totalHits, maxMeasure, minMeasure,averageMeasure, lastMeasure, lastOccurrence);
		}
	}

	@java.lang.SuppressWarnings("all")
	public static <T> MetricBuilder builder(final MeasureReducer<T> _measureReducer) {
		return new MetricBuilder(_measureReducer);
	}	
	public static <T> MetricBuilder builder(final MeasureReducer<T> _measureReducer,final MetricSnapshot<T> _metricSnapshot) {
		return new MetricBuilder(_measureReducer,_metricSnapshot);
	}	
}
