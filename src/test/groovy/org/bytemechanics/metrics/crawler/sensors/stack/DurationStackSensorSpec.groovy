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

package org.bytemechanics.metrics.crawler.sensors.stack

import spock.lang.Specification
import spock.lang.Unroll
import java.util.Optional
import java.util.logging.*
import java.util.stream.*
import java.time.LocalDateTime
import java.time.Duration
import java.time.format.DateTimeFormatter
import org.bytemechanics.metrics.crawler.beans.MetricSnapshot
import org.bytemechanics.metrics.crawler.internal.commons.string.SimpleFormat
import org.bytemechanics.metrics.crawler.internal.*
import org.bytemechanics.metrics.crawler.impl.*
import org.bytemechanics.metrics.crawler.MetricsService


/**
 * @author afarre
 */
class DurationStackSensorSpec extends Specification{
	
	def setupSpec(){
		println(">>>>> DurationStackSensorSpec >>>> setupSpec")
		final InputStream inputStream = DurationStackSensorSpec.class.getResourceAsStream("/logging.properties");
		try{
			LogManager.getLogManager().readConfiguration(inputStream);
		}catch (final IOException e){
			Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
			Logger.getAnonymousLogger().severe(e.getMessage());
		}finally{
			if(inputStream!=null)
				inputStream.close();
		}
	}
	def cleanup(){
		println(">>>>> DurationStackSensorSpec >>>> cleanup")
		AbstractStackSensor.CURRENT_NAME.set(null)
	}

	def "Create a null name metric sensor should raise NullPointerException"(){
		println(">>>>> DurationStackSensorSpec >>>> Create a null name metric sensor should raise NullPointerException")

		when:
			DurationStackSensor.get(null)
			
		then:
			def e=thrown(NullPointerException)
			e.getMessage()=="Can not create null named sensor metric"
	}	

	@Unroll
	def "When create a duration stack sensor with get(_name:#name,_args:#args) and then call getName() should return #expected"(){
		println(">>>>> DurationStackSensorSpec >>>> When create a duration stack sensor with get(_name:$name,_args:$args) and then call getName() should return $expected")

		when:
			def obj=DurationStackSensor.get(name,(Object[])args)
			
		then:
			obj.getName()!=null
			obj.getName()==expected;
			
		where:
			name		| args					| expected		
			"a"			| [1,2.0d,"string"]		| "a"			
			"{}b{}c{}"	| []					| "nullbnullcnull"	
			"{}b{}c{}"	| [1]					| "1bnullcnull"	
			"{}b{}c{}"	| [1,2.0d]				| "1b2.0cnull"	
			"{}b{}c{}"	| [1,2.0d,"string"]		| "1b2.0cstring"	
			"{}b{}c{}"	| [null,2.0d,"string"]	| "nullb2.0cstring"	
			"{}b{}c{}"	| [1,null,"string"]		| "1bnullcstring"	
			"{}b{}c{}"	| [1,2.0d,null]			| "1b2.0cnull"	
	}	


	@Unroll
	def "When create a duration stack sensor with get(_name:#name,_args:#args) and then call isSkip() should return false"(){
		println(">>>>> DurationStackSensorSpec >>>> When create a duration stack sensor with get(_name:$name,_args:$args) and then call isSkip() should return false")

		when:
			def obj=DurationStackSensor.get(name,(Object[])args)
			
		then:
			obj.isSkip()==false
			
		where:
			name		| args					| expected			
			"a"			| [1,2.0d,"string"]		| "a"				
			"{}b{}c{}"	| []					| "nullbnullcnull"	
			"{}b{}c{}"	| [1]					| "1bnullcnull"		
			"{}b{}c{}"	| [1,2.0d]				| "1b2.0cnull"		
			"{}b{}c{}"	| [1,2.0d,"string"]		| "1b2.0cstring"	
			"{}b{}c{}"	| [null,2.0d,"string"]	| "nullb2.0cstring"	
			"{}b{}c{}"	| [1,null,"string"]		| "1bnullcstring"	
			"{}b{}c{}"	| [1,2.0d,null]			| "1b2.0cnull"		
	}
	
	@Unroll
	def "When create a duration stack sensor with get(_measure:#measure,_name:#name,_args:#args) and call skip() then isSkip() return true"(){
		println(">>>>> DurationStackSensorSpec >>>> When create a duration stack sensor with get(_measure:#measure,_name:$name,_args:$args) and call skip() then isSkip() return true")

		when:
			def obj=DurationStackSensor.get(name,(Object[])args)
			obj.skip()
			
		then:
			obj.isSkip()==true
			
		where:
			name		| args					| expected			
			"a"			| [1,2.0d,"string"]		| "a"				
			"{}b{}c{}"	| []					| "nullbnullcnull"	
			"{}b{}c{}"	| [1]					| "1bnullcnull"		
			"{}b{}c{}"	| [1,2.0d]				| "1b2.0cnull"		
			"{}b{}c{}"	| [1,2.0d,"string"]		| "1b2.0cstring"	
			"{}b{}c{}"	| [null,2.0d,"string"]	| "nullb2.0cstring"	
			"{}b{}c{}"	| [1,null,"string"]		| "1bnullcstring"	
			"{}b{}c{}"	| [1,2.0d,null]			| "1b2.0cnull"		
	}	

	@Unroll
	def "When close() sensor with name:#name,args:#arg metric must add to metricservice the registered measure"(){
		println(">>>>> DurationStackSensorSpec >>>> When close() sensor with name:$name,args:$args metric must add to metricservice the registered measure")

		given:
			def metricsService = Mock(MetricsService.class)
			metricsService.buildMetricName(name,args) >> "name"
			DurationStackSensor.registerMetricsServiceSupplier({ -> metricsService})

		when:
			def obj=DurationStackSensor.get(name,(Object[])args)
			obj.close()
			
		then:
			1 * metricsService.registerMeasure('name',!null,!null,MeasureReducers.DURATION.get(Duration.class), [])
			
		cleanup:
			AbstractStackSensor.registerMetricsServiceSupplier({ -> MetricsServiceSingleton.getInstance().getMetricsService()})

		where:
			name		| args					
			"a"			| [1,2.0d,"string"]		
			"{}b{}c{}"	| []					
			"{}b{}c{}"	| [1,2.0d]				
			"{}b{}c{}"	| [1,2.0d,"string"]		
			"{}b{}c{}"	| [null,2.0d,"string"]	
			"{}b{}c{}"	| [1,null,"string"]		
			"{}b{}c{}"	| [1,2.0d,null]			
	}	
}

