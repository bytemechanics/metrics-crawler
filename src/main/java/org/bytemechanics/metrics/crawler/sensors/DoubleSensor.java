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
 * Double sensor (must be informed manually)<br>
 * Usage:<pre>
 * {@code try(DoubleSensor sensor=DoubleSensor.get("myMeasure{}",1)){
 *		(...)
 *		sensor.setMeasure(1.0d)
 *		(...)
 *  }
 * }</pre>
 * or<br><pre>
 * {@code try(DoubleSensor sensor=DoubleSensor.get(1.0d,"myMeasure{}",1)){
 *		(...)
 *  }
 * }</pre>
 * @see AbstractManualSensor
 * @author afarre
 * @since 1.0.0
 */
public class DoubleSensor extends AbstractManualSensor<Double> {

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
	protected DoubleSensor(final Optional<Double> _measure,final String _name,final Object... _args){
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
	public static DoubleSensor get(final String _name,final Object... _args){
		return new DoubleSensor(Optional.empty(),_name,_args);
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
	public static DoubleSensor get(final Double _measure,final String _name,final Object... _args){
		return new DoubleSensor(Optional.ofNullable(_measure),_name,_args);
	}
}

