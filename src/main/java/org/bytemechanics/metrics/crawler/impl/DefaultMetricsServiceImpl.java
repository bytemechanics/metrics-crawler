package org.bytemechanics.metrics.crawler.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.bytemechanics.metrics.crawler.MeasureReducer;
import org.bytemechanics.metrics.crawler.MetricsService;
import org.bytemechanics.metrics.crawler.beans.MetricSnapshot;
import org.bytemechanics.metrics.crawler.internal.Metric;

/**
 * Default Metrics service implementation
 * @see MetricsService
 * @author afarre
 * @since 1.0.0
 */
public class DefaultMetricsServiceImpl implements MetricsService {

	public static final int DEFAULT_SAMPLING_SIZE=128;

	private final int samplingSize;
	private final Map<String,Metric> metrics;
	
	
	public DefaultMetricsServiceImpl(){
		this(DEFAULT_SAMPLING_SIZE);
	}
	public DefaultMetricsServiceImpl(final int _samplingSize){
		this.metrics=new ConcurrentHashMap<>(64);
		this.samplingSize=_samplingSize;
	}
	
	
	/** @see MetricsService#getSamplingSize()  */
	@Override
	public int getSamplingSize(){
		return this.samplingSize;
	}

	/**@see MetricsService#buildMetricName(java.lang.String, java.lang.Object...) */
	@Override
	public String buildMetricName(final String _name,final Object... _placeholders) {
		return MetricsService.super.buildMetricName(_name, _placeholders); //To change body of generated methods, choose Tools | Templates.
	}
	
	/** @see MetricsService#registerMeasure(java.lang.String, java.time.LocalDateTime, java.lang.Object, org.bytemechanics.metrics.crawler.MeasureReducer, java.lang.Object...)  */
	@Override
	public <TYPE> void registerMeasure(final String _name,final LocalDateTime _time,final TYPE _measure,final MeasureReducer<TYPE> _reducer,final Object... _placeholders){
		Optional.ofNullable(buildMetricName(_name,_placeholders))
					.map(effectiveName -> this.metrics.computeIfAbsent(effectiveName,name -> new Metric(name, this.samplingSize,_reducer)))
					.ifPresent(metric -> metric.addMeasure(_time, _measure));
	}
	
	/** @see MetricsService#getMetric(java.lang.String, java.lang.Object...)  */
	@Override
	public Optional<MetricSnapshot> getMetric(final String _measure,final Object... _placeholders) {
		return Optional.ofNullable(buildMetricName(_measure,_placeholders))
							.map(this.metrics::get)
							.map(Metric::toSnapshot);
	}

	/** @see MetricsService#getMetrics()  */
	@Override
	public List<MetricSnapshot> getMetrics(){
		return this.metrics.values()
								.stream()
									.map(Metric::toSnapshot)
									.sorted(MetricSnapshot::compareNames)
									.collect(Collectors.toList());
	}

	/** @see MetricsService#clear()  */
	@Override
	public void clear() {
		this.metrics.clear();
	}
}
