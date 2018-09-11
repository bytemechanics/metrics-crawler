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

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import org.bytemechanics.metrics.crawler.MetricsService;
import org.bytemechanics.metrics.crawler.internal.MeasureReducers;

/**
 *
 * @author afarre
 */
public class DurationStackSensor extends AbstractStackSensor<Duration>{

	protected final Instant startTime;
	
	protected DurationStackSensor(final Optional<MetricsService> _service,final String _name,final Object... _args){
		super(MeasureReducers.DURATION.get(Duration.class),_service, _name, _args);
		this.startTime=Instant.now();
	}

	@Override
	protected Duration getMeasure() {
		return Duration.between(startTime, Instant.now());
	}

	public static DurationStackSensor get(final String _name,final Object... _args){
		return new DurationStackSensor(Optional.empty(),_name,_args);
	}
}

