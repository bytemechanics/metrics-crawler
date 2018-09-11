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
import org.bytemechanics.metrics.crawler.internal.MeasureReducers;

/**
 *
 * @author afarre
 */
public class LongStackSensor extends AbstractNumericStackSensor<Long> {

	protected LongStackSensor(final Optional<Long> _measure,final String _name,final Object... _args){
		super(MeasureReducers.LONG.get(Long.class),Optional.empty(),_measure,_name,_args);
	}

	
	public static LongStackSensor get(final String _name,final Object... _args){
		return new LongStackSensor(Optional.empty(),_name,_args);
	}
	public static LongStackSensor get(final Long _measure,final String _name,final Object... _args){
		return new LongStackSensor(Optional.ofNullable(_measure),_name,_args);
	}
}

