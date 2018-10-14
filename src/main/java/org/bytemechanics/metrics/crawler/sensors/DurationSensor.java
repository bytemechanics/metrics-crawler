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

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import org.bytemechanics.metrics.crawler.internal.MeasureReducers;

/**
 * Duration sensor measures time between sensor creation and close()<br>
 * Usage:<pre>
 * {@code try(DurationSensor sensor=DurationSensor.get("myMeasure{}",1)){
 *		(...)
 *  }
 * }</pre>
 * @see AbstractSensor
 * @see Duration
 * @author afarre
 * @since 1.0.0
 */
public class DurationSensor extends AbstractSensor<Duration>{

	/** Measure start time */
	protected final Instant startTime;
	
	/**
	 * Builds a duration sensor with the name build from the given name and arguments and with the given measure<br>
	 * Example:<pre>
	 *	_name: "{}_name_{}"
	 *	_args: ["prefix","suffix"]
	 *	final name: prefix_name_suffix</pre>
	 * @param _name name of the measure
	 * @param _args arguments to replace to the measure name
	 */
	protected DurationSensor(final String _name,final Object... _args){
		super(MeasureReducers.DURATION.get(Duration.class),Optional.empty(), _name, _args);
		this.startTime=Instant.now();
	}

	/**
	 * Calculate the durantion from the creation time to this instant
	 * @return duration time between creation instance and now
	 * @see AbstractSensor#getMeasure() 
	 */
	@Override
	public Duration getMeasure() {
		return Duration.between(startTime, Instant.now());
	}

	/**
	 * Builds a duration sensor with the name build from the given name and arguments<br>
	 * Example:<pre>
	 *	_name: "{}_name_{}"
	 *	_args: ["prefix","suffix"]
	 *	final name: prefix_name_suffix</pre>
	 * @param _name name of the measure
	 * @param _args arguments to replace to the measure name
	 * @return duration sensor with the replaced name
	 */
	public static DurationSensor get(final String _name,final Object... _args){
		return new DurationSensor(_name,_args);
	}
}

