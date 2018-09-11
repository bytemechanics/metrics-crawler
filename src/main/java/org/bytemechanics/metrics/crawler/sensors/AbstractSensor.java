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
import org.bytemechanics.metrics.crawler.internal.MetricsServiceSingleton;
import org.bytemechanics.metrics.crawler.internal.commons.string.SimpleFormat;

/**
 * @author afarre
 * @param <TYPE>
 */
public abstract class AbstractSensor<TYPE> implements AutoCloseable{

	protected static Supplier<MetricsService> metricsService=MetricsServiceSingleton.getInstance()::getMetricsService;

	protected String name;
	protected final LocalDateTime timestamp;
	protected boolean skip;
	protected MetricsService service;
	protected MeasureReducer<TYPE> reducer;
	

	protected AbstractSensor(final MeasureReducer<TYPE> _reducer,final Optional<MetricsService> _service,final String _name,final Object... _args){
		this.reducer=_reducer;
		this.service=_service.orElse(metricsService.get());
		this.name=this.service.buildMetricName(_name, _args);
		this.timestamp=LocalDateTime.now();
	}

	
	public void skip() {
		this.skip=true;
	}
	
	protected abstract TYPE getMeasure();
	
	@Override
	public void close() {
		try{
			if(!this.skip){
				service.registerMeasure(this.name,this.timestamp, getMeasure(),this.reducer);
			}
		}catch(Exception e){
			Logger.getLogger(AbstractSensor.class.getName()).log(Level.WARNING,e,() -> SimpleFormat.format("measure::{}::resgistry::failed::{}",this.name ,e.getMessage()));
		}
	}

	public static void registerMetricsService(final Supplier<MetricsService> _metricsService){
		metricsService=_metricsService;
	}
}

