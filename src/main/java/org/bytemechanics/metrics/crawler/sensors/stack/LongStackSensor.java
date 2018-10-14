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
 * Long stack sensor (must be informed manually)<br>
 * Usage:<pre>
 * {@code try(LongSensor sensor=LongSensor.get("myMeasure{}",1)){
 *		(...)
 *		sensor.setMeasure(1l)
 *		try(LongSensor sensor2=LongSensor.get("myMeasure2{}",2)){
 *			(...)
 *			sensor.setMeasure(2l)
 *			(...)
 *		}
 *  }
 * }</pre>
 * That will create two metrics:
 * <ul>
 *		<li>myMeasure11=1l</li>
 *		<li>myMeasure11.myMeasure22=2l</li>
 * </ul>
 * or<br><pre>
 * {@code try(LongSensor sensor=LongSensor.get(1l,"myMeasure{}",1)){
 *		(...)
 *		try(LongSensor sensor2=LongSensor.get("myMeasure2{}",2)){
 *			(...)
 *			sensor.setMeasure(2l)
 *			(...)
 *		}
 *		try(LongSensor sensor3=LongSensor.get(3l,"myMeasure3{}",3)){
 *			(...)
 *		}
 *  }
 * }</pre>
 * That will create three metrics:
 * <ul>
 *		<li>myMeasure11=1l</li>
 *		<li>myMeasure11.myMeasure22=2l</li>
 *		<li>myMeasure11.myMeasure33=3l</li>
 * </ul>
 * @see AbstractManualStackSensor
 * @author afarre
 * @since 1.0.0
 */
public class LongStackSensor extends AbstractManualStackSensor<Long> {

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
	protected LongStackSensor(final Optional<Long> _measure,final String _name,final Object... _args){
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
	public static LongStackSensor get(final String _name,final Object... _args){
		return new LongStackSensor(Optional.empty(),_name,_args);
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
	public static LongStackSensor get(final Long _measure,final String _name,final Object... _args){
		return new LongStackSensor(Optional.ofNullable(_measure),_name,_args);
	}

	/**
	 * Create a measure for the metric with the given name<br>
	 * Example:<pre>
	 *	_name: "{}_name_{}"
	 *	_args: ["prefix","suffix"]
	 *	final name: prefix_name_suffix</pre>
	 * @param _measure long that will be the measure to use
	 * @param _name name of the measure
	 * @param _args arguments to replace to the measure name
	 */
	public static void measure(final Long _measure,final String _name,final Object... _args){
		LongStackSensor.get(_measure,_name,_args)
					.close();
	}
}
