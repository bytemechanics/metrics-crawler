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
package org.bytemechanics.metrics.crawler.sensors.stack;

import java.util.Optional;
import org.bytemechanics.metrics.crawler.MeasureReducer;
import org.bytemechanics.metrics.crawler.MetricsService;
import org.bytemechanics.metrics.crawler.impl.DefaultMetricsServiceImpl;
import org.bytemechanics.metrics.crawler.sensors.AbstractSensor;

/**
 * Manual abstract base class for sensors extends AbstractStackSensor adding capacity to register a measure manually, additionally to the capacity of stack metric names.
 * This means that the final sensor name will be the accumulation of the previous open sensors in the same thread separated by dot (.)
 * @param <TYPE> sensor type
 * @see AbstractStackSensor
 * @author afarre
 * @since 1.0.0
 */
public abstract class AbstractManualStackSensor<TYPE extends Number> extends AbstractStackSensor<TYPE>{
	
	/** Stores the measured value between the instantiation and close() */
	protected TYPE measure;
	
	/**
	 * Abstract sensor constructor with the given parameters
	 * @param _reducer reducer for this sensor (mandatory)
	 * @param _service metrics metricService (optional) if no provided, a singleton instance of DefaultMetricsServiceImpl is created
	 * @param _measure measure to register (optional)
	 * @param _name metric name (mandatory)
	 * @param _args placeholders for metric name (optional)
	 * @throws NullPointerException if metricsServiceSupplier returns null instance or _name is null
	 * @see DefaultMetricsServiceImpl
	 * @see MeasureReducer
	 * @see MetricsService
	 */
	protected AbstractManualStackSensor(final MeasureReducer<TYPE> _reducer,final Optional<MetricsService> _service,final Optional<TYPE> _measure,final String _name,final Object... _args){
		super(_reducer,_service, _name, _args);
		this.measure=_measure.orElse(null);
	}

	
	/**
	 * Add the current measure to register (can be called several times overriding the value, and will take effect only when close the sensor)
	 * @param _measure measure to register
	 */
	public void setMeasure(final TYPE _measure){
		this.measure=_measure;
	}
	
	/** @see AbstractSensor#getMeasure() */
	@Override
	public TYPE getMeasure() {
		return this.measure;
	}
}
