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

import java.util.Optional;
import org.bytemechanics.metrics.crawler.internal.MeasureReducers;

/**
 * Long sensor (must be informed manually)<br>
 * Usage:<pre>
 * {@code try(LongSensor sensor=LongSensor.get("myMeasure{}",1)){
 *		(...)
 *		sensor.setMeasure(1l)
 *		(...)
 *  }
 * }</pre>
 * or<br><pre>
 * {@code try(LongSensor sensor=LongSensor.get(1l,"myMeasure{}",1)){
 *		(...)
 *  }
 * }</pre>
 * @see AbstractManualSensor
 * @author afarre
 * @since 1.0.0
 */
public class LongSensor extends AbstractManualSensor<Long> {

	/**
	 * Builds a long sensor with the name build from the given name and arguments and with the given measure<br>
	 * Example:<pre>
	 *	_name: "{}_name_{}"
	 *	_args: ["prefix","suffix"]
	 *	final name: prefix_name_suffix</pre>
	 * @param _measure Optional of long that will be the measure to use (can be null or Optional.empty())
	 * @param _name name of the measure
	 * @param _args arguments to replace to the measure name
	 */
	protected LongSensor(final Optional<Long> _measure,final String _name,final Object... _args){
		super(MeasureReducers.LONG.get(Long.class),Optional.empty(),_measure,_name,_args);
	}

	
	/**
	 * Builds a long sensor with the name build from the given name and arguments<br>
	 * Example:<pre>
	 *	_name: "{}_name_{}"
	 *	_args: ["prefix","suffix"]
	 *	final name: prefix_name_suffix</pre>
	 * @param _name name of the measure
	 * @param _args arguments to replace to the measure name
	 * @return long sensor with the replaced name
	 */
	public static LongSensor get(final String _name,final Object... _args){
		return new LongSensor(Optional.empty(),_name,_args);
	}
	/**
	 * Builds a long sensor with the name build from the given name and arguments and arguments and with the given measure<br>
	 * Example:<pre>
	 *	_name: "{}_name_{}"
	 *	_args: ["prefix","suffix"]
	 *	final name: prefix_name_suffix</pre>
	 * @param _measure Optional of long that will be the measure to use (can be null or Optional.empty())
	 * @param _name name of the measure
	 * @param _args arguments to replace to the measure name
	 * @return long sensor with the replaced name and initialized with the given measure
	 */
	public static LongSensor get(final Long _measure,final String _name,final Object... _args){
		return new LongSensor(Optional.ofNullable(_measure),_name,_args);
	}
}

