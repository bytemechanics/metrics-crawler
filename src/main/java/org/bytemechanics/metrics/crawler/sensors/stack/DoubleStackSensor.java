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
 * Double stack sensor (must be informed manually)<br>
 * Usage:<pre>
 * {@code try(DoubleSensor sensor=DoubleSensor.get("myMeasure{}",1)){
 *		(...)
 *		sensor.setMeasure(1.0d)
 *		try(DoubleSensor sensor2=DoubleSensor.get("myMeasure2{}",2)){
 *			(...)
 *			sensor.setMeasure(1.2d)
 *			(...)
 *		}
 *  }
 * }</pre>
 * That will create two metrics:
 * <ul>
 *		<li>myMeasure11=1.0d</li>
 *		<li>myMeasure11.myMeasure22=1.2d</li>
 * </ul>
 * or<br><pre>
 * {@code try(DoubleSensor sensor=DoubleSensor.get(1.0d,"myMeasure{}",1)){
 *		(...)
 *		try(DoubleSensor sensor2=DoubleSensor.get("myMeasure2{}",2)){
 *			(...)
 *			sensor.setMeasure(1.0d)
 *			(...)
 *		}
 *		try(DoubleSensor sensor3=DoubleSensor.get(3.0d,"myMeasure3{}",3)){
 *			(...)
 *		}
 *  }
 * }</pre>
 * That will create three metrics:
 * <ul>
 *		<li>myMeasure11=1.0d</li>
 *		<li>myMeasure11.myMeasure22=1.2d</li>
 *		<li>myMeasure11.myMeasure33=3.0d</li>
 * </ul>
 * @see AbstractManualStackSensor
 * @author afarre
 * @since 1.0.0
 */
public class DoubleStackSensor extends AbstractManualStackSensor<Double> {

	/**
	 * Builds a double sensor with the name build from the given name and arguments and with the given measure<br>
	 * Example:<pre>
	 *	_name: "{}_name_{}"
	 *	_args: ["prefix","suffix"]
	 *	final name: prefix_name_suffix</pre>
	 * @param _measure Optional of double that will be the measure to use (can be null or Optional.empty())
	 * @param _name name of the measure
	 * @param _args arguments to replace to the measure name
	 */
	protected DoubleStackSensor(final Optional<Double> _measure,final String _name,final Object... _args){
		super(MeasureReducers.DOUBLE.get(Double.class),Optional.empty(),_measure,_name,_args);
	}

	
	/**
	 * Builds a double sensor with the name build from the given name and arguments<br>
	 * Example:<pre>
	 *	_name: "{}_name_{}"
	 *	_args: ["prefix","suffix"]
	 *	final name: prefix_name_suffix</pre>
	 * @param _name name of the measure
	 * @param _args arguments to replace to the measure name
	 * @return double sensor with the replaced name
	 */
	public static DoubleStackSensor get(final String _name,final Object... _args){
		return new DoubleStackSensor(Optional.empty(),_name,_args);
	}
	/**
	 * Builds a double sensor with the name build from the given name and arguments and arguments and with the given measure<br>
	 * Example:<pre>
	 *	_name: "{}_name_{}"
	 *	_args: ["prefix","suffix"]
	 *	final name: prefix_name_suffix</pre>
	 * @param _measure Optional of double that will be the measure to use (can be null or Optional.empty())
	 * @param _name name of the measure
	 * @param _args arguments to replace to the measure name
	 * @return double sensor with the replaced name and initialized with the given measure
	 */
	public static DoubleStackSensor get(final Double _measure,final String _name,final Object... _args){
		return new DoubleStackSensor(Optional.ofNullable(_measure),_name,_args);
	}

	/**
	 * Create a measure for the metric with the given name<br>
	 * Example:<pre>
	 *	_name: "{}_name_{}"
	 *	_args: ["prefix","suffix"]
	 *	final name: prefix_name_suffix</pre>
	 * @param _measure double that will be the measure to use
	 * @param _name name of the measure
	 * @param _args arguments to replace to the measure name
	 */
	public static void measure(final Double _measure,final String _name,final Object... _args){
		DoubleStackSensor.get(_measure,_name,_args)
					.close();
	}
}

