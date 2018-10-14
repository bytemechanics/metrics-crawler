package org.bytemechanics.metrics.crawler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.bytemechanics.metrics.crawler.beans.MetricSnapshot;
import org.bytemechanics.metrics.crawler.exceptions.IncorrectMeasureType;
import org.bytemechanics.metrics.crawler.internal.commons.string.SimpleFormat;

/**
 * Adapter of a service to store metrics
 * @author afarre
 * @since 1.0.0
 */
public interface MetricsService {

	/**
	 * Builds a metric name replacing the _name with the giving _placeholders the replacement follows the order of the arguments replacing the string "{}"
	 * @param _name name where to replace the values
	 * @param _placeholders replacement values
	 * @return the _name with the {} replaced by _placeholders
	 * @throws NullPointerException if name is null
	 */
	public default String buildMetricName(final String _name,final Object... _placeholders){
		return Optional.ofNullable(_name)
					.map(name -> SimpleFormat.format(name, _placeholders))
					.orElseThrow(() -> new NullPointerException("Can not create null named sensor metric"));
	}
	
	/**
	 * Retrieve the current sampling size for this metric service
	 * @return the size of the samples allowed, -1 implies has no limit
	 */
	public int getSamplingSize();

	/**
	 * Retrieve the metric snapshot with the given _name and _placeholders
	 * @param _name metric name where to replace the values
	 * @param _placeholders metric name replacement values
	 * @return an Optional with the metric snaphot if no metric exist return an empty optional
	 * @see MetricsService#buildMetricName(java.lang.String, java.lang.Object...) 
	 * @see MetricSnapshot
	 */
	public Optional<MetricSnapshot> getMetric(final String _name,final Object... _placeholders);

	/**
	 * Register a new measure into the metric with the given _name replaced with _placeholders and the given _reducer
	 * @param <TYPE> Type of the measure
	 * @param _name metric name where to replace the values
	 * @param _time Local date time when measure was taken
	 * @param _measure measure value
	 * @param _reducer reducer for this type of measure
	 * @param _placeholders metric name replacement values
	 * @throws IncorrectMeasureType if the metric already exist with another TYPE
	 * @see MetricsService#buildMetricName(java.lang.String, java.lang.Object...) 
	 * @see IncorrectMeasureType
	 * @see LocalDateTime
	 */
	public <TYPE> void registerMeasure(final String _name,final LocalDateTime _time,final TYPE _measure,final MeasureReducer<TYPE> _reducer,final Object... _placeholders);
	
	/**
	 * Return an ordered list by name of the existent snapshot metrics 
	 * @return List of metrics snapshot
	 * @see MetricSnapshot
	 */
	public List<MetricSnapshot> getMetrics();

	/**
	 * Removes all current metrics in the service
	 */
	public void clear();
}
