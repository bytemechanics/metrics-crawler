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

/**
 *
 * @author afarre
 * @param <TYPE>
 */
public abstract class AbstractManualStackSensor<TYPE extends Number> extends AbstractStackSensor<TYPE>{
	
	protected TYPE measure;
	
	
	protected AbstractManualStackSensor(final MeasureReducer<TYPE> _reducer,final Optional<MetricsService> _service,final Optional<TYPE> _measure,final String _name,final Object... _args){
		super(_reducer,_service, _name, _args);
		this.measure=_measure.orElse(null);
	}

	
	public void setMeasure(final TYPE _measure){
		this.measure=_measure;
	}
	
	@Override
	public TYPE getMeasure() {
		return this.measure;
	}
}
