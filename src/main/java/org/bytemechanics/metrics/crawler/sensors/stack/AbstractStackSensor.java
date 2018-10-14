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
import org.bytemechanics.metrics.crawler.sensors.AbstractSensor;

/**
 * Manual abstract base class for sensors extends AbstractStackSensor adding capacity to stack metric names automatically
 * This means that the final sensor name will be the accumulation of the previous open sensors in the same thread separated by dot (.)
 * @param <TYPE> sensor type
 * @see AbstractSensor
 * @author afarre
 */
public abstract class AbstractStackSensor<TYPE> extends AbstractSensor<TYPE> {

	private static final ThreadLocal<String> CURRENT_NAME=new ThreadLocal<>();

	private final String segment;
	
	/** 
	 * Abstract sensor constructor with the given parameters the sensor name will be the accumulation of the previous open sensors in the same thread separated by dot (.)
	 * @param _reducer reducer for this sensor (mandatory)
	 * @param _service metrics metricService (optional) if no provided, a singleton instance of DefaultMetricsServiceImpl is created
	 * @param _name metric name (mandatory)
	 * @param _args placeholders for metric name (optional)
	 * @throws NullPointerException if metricsServiceSupplier returns null instance or _name is null
	 * @see AbstractSensor#AbstractSensor(org.bytemechanics.metrics.crawler.MeasureReducer, java.util.Optional, java.lang.String, java.lang.Object...) 
	 */
	protected AbstractStackSensor(final MeasureReducer<TYPE> _reducer,final Optional<MetricsService> _service,final String _name,final Object... _args){
		super(_reducer,_service, _name, _args);
		this.segment=this.name;
		this.name=Optional.ofNullable(AbstractStackSensor.CURRENT_NAME.get())
							.filter(parentMeasure -> !parentMeasure.isEmpty())
							.map(parentMeasure -> String.join(".", parentMeasure,this.segment))
							.orElse(this.segment);			
		AbstractStackSensor.CURRENT_NAME.set(this.name);
	}

	/**
	 * Close the sensor and remove the name from the sensors stack
	 * @see AbstractSensor#close() 
	 */
	@Override
	public void close() {
		super.close();
		AbstractStackSensor.CURRENT_NAME.set(Optional.ofNullable(AbstractStackSensor.CURRENT_NAME.get())
											.filter(current -> current.length()>(segment.length()+1))
											.map(current -> current.substring(0,current.length()-(segment.length()+1)))
											.orElse(""));
	}
}
