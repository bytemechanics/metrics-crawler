package org.bytemechanics.metrics.crawler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.bytemechanics.metrics.crawler.beans.MetricSnapshot;
import org.bytemechanics.metrics.crawler.internal.commons.string.SimpleFormat;

/**
 * @author e103880
 */
public interface MetricsService {

	public default String buildMetricName(final String _name,final Object... _args){
		return (_args.length>0)? SimpleFormat.format(_name, _args) : String.valueOf(_name);
	}
	
	public int getSamplingSize();

	public Optional<MetricSnapshot> getMetric(final String _measure,final Object... _placeholders);

	public <TYPE> void registerMeasure(final String _name,final LocalDateTime _time,final TYPE _measure,final MeasureReducer<TYPE> _reducer,final Object... _placeholders);
	
	public List<MetricSnapshot> getMetrics();
}
