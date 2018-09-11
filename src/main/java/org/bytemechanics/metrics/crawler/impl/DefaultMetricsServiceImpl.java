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
 * @author e103880
 */
public class DefaultMetricsServiceImpl implements MetricsService {

	private static final int DEFAULT_SAMPLING_SIZE=128;

	private final int samplingSize;
	private final Map<String,Metric> metrics;
	
	
	public DefaultMetricsServiceImpl(){
		this(DEFAULT_SAMPLING_SIZE);
	}
	public DefaultMetricsServiceImpl(final int _samplingSize){
		this.metrics=new ConcurrentHashMap<>(64);
		this.samplingSize=_samplingSize;
	}

	
	@Override
	public Optional<MetricSnapshot> getMetric(String _measure,final Object... _placeholders) {
		return Optional.ofNullable(buildMetricName(_measure,_placeholders))
							.map(this.metrics::get)
							.map(Metric::toSnapshot);
	}

	
	@Override
	public <TYPE> void registerMeasure(final String _name,final LocalDateTime _time,final TYPE _measure,final MeasureReducer<TYPE> _reducer){
		this.metrics.computeIfAbsent(_name,name -> new Metric(name, this.samplingSize,_reducer))
					.addMeasure(_time, _measure);
	}

	@Override
	public List<MetricSnapshot> getMetrics(){
		return this.metrics.values()
								.stream()
									.map(Metric::toSnapshot)
									.sorted()
									.collect(Collectors.toList());
	}
}
