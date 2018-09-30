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
package org.bytemechanics.metrics.crawler.sensors;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bytemechanics.metrics.crawler.MeasureReducer;
import org.bytemechanics.metrics.crawler.MetricsService;
import org.bytemechanics.metrics.crawler.impl.DefaultMetricsServiceImpl;
import org.bytemechanics.metrics.crawler.internal.MetricsServiceSingleton;
import org.bytemechanics.metrics.crawler.internal.commons.string.SimpleFormat;

/**
 * Master abstract base class for sensors implements AutoCloseable in order to be usable as try-with-resource structure
 * @param <TYPE> type of this sensor
 * @see AutoCloseable
 * @author afarre
 * @since 1.0.0
 */
public abstract class AbstractSensor<TYPE> implements AutoCloseable{

	protected static Supplier<MetricsService> metricsServiceSupplier=MetricsServiceSingleton.getInstance()::getMetricsService;

	protected String name;
	protected final LocalDateTime timestamp;
	protected boolean skip;
	protected MetricsService metricService;
	protected MeasureReducer<TYPE> reducer;
	

	/**
	 * Abstract sensor constructor with the given parameters
	 * @param _reducer reducer for this sensor (mandatory)
	 * @param _service metrics metricService (optional) if no provided, a singleton instance of DefaultMetricsServiceImpl is created
	 * @param _name metric name (mandatory)
	 * @param _args placeholders for metric name (optional)
	 * @throws NullPointerException if metricsServiceSupplier returns null instance or _name is null
	 * @see DefaultMetricsServiceImpl
	 * @see MeasureReducer
	 * @see MetricsService
	 */
	protected AbstractSensor(final MeasureReducer<TYPE> _reducer,final Optional<MetricsService> _service,final String _name,final Object... _args){
		this.reducer=_reducer;
		this.metricService=_service.orElse(Optional.ofNullable(metricsServiceSupplier.get())
												.orElseThrow(() -> new NullPointerException("The current metricServiceSupplier has returned a null metric service")));
		this.name=Optional.ofNullable(_name)
					.map(nonNullName -> this.metricService.buildMetricName(nonNullName, _args))
					.orElseThrow(() -> new NullPointerException("Can not create null named sensor metric"));
		this.timestamp=LocalDateTime.now();
	}

	/**
	 * Retrieve sensor metric name
	 * @return sensor metric name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Retrieve current sensor measure
	 * @return current sensor measure
	 */
	public abstract TYPE getMeasure();

	/**
	 * Retrieve current sensor metric metricService
	 * @return current sensor metric metricService
	 * @see MetricsService
	 */
	public MetricsService getMetricService() {
		return metricService;
	}

	/**
	 * Flag this sensor to ignore any taken measure (not register in metric)
	 */
	public void skip() {
		this.skip=true;
	}
	/**
	 * Retrieve current sensor skip flag status
	 * @return current sensor skip flag status
	 */
	public boolean isSkip() {
		return skip;
	}

	/**
	 * Retrieve if the given measure must be ignored
	 * @param _measure measure to evaluate
	 * @return true if the given measure must be ignored
	 */
	protected final boolean mustSkip(final TYPE _measure) {
		return isSkip();
	}
	/**
	 * Register measure in the current MetricsService
	 * @param _measure measure to register
	 * @see MetricsService
	 */
	protected final void registerMeasure(final TYPE _measure){

		try{
			this.metricService.registerMeasure(this.name,this.timestamp, getMeasure(),this.reducer);
		}catch(Exception e){
			Logger.getLogger(AbstractSensor.class.getName()).log(Level.WARNING,e,() -> SimpleFormat.format("measure::{}::resgistry::failed::{}",this.name ,e.getMessage()));
		}
	}
	
	/**
	 * Close the sensor and register the current measure if present into the current  MetricsService
	 * @see MetricsService
	 * @see AutoCloseable
	 */
	@Override
	public void close() {
		Optional.ofNullable(getMeasure())
					.filter(this::mustSkip)
					.ifPresent(this::registerMeasure);
	}

	/**
	 * Register a new MetricsService Supplier in order to be used (should be called before any sensor is created, otherwise these sensors will register the measures into the internal singleton)
	 * @param _metricsServiceSupplier metric metricService supplier
	 * @throws NullPointerException if null _metricsServiceSupplier
	 * @see MetricsServiceSingleton
	 * @see MetricsService
	 */
	public static void registerMetricsServiceSupplier(final Supplier<MetricsService> _metricsServiceSupplier){
		if(_metricsServiceSupplier==null)
			throw new NullPointerException("Unable to register null _metricsServiceSupplier");
		metricsServiceSupplier=_metricsServiceSupplier;
	}
}

