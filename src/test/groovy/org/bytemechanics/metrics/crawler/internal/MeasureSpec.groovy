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

package org.bytemechanics.metrics.crawler.internal

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

/**
 * @author afarre
 */
class MeasureSpec extends Specification{
	
	def setupSpec(){
		println(">>>>> MeasureSpec >>>> setupSpec")
		final InputStream inputStream = MeasureSpec.class.getResourceAsStream("/logging.properties");
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

	def "When try to create null _timestamp measure should raise NullPointerException"(){
		println(">>>>> MetricSpec >>>> When try to register null _timestamp measure should raise NullPointerException")

		when:
			def metric=new Measure(null,1.0d,MeasureReducers.DOUBLE.get())

		then:
			def e=thrown(NullPointerException)
			e.getMessage()=="Can not create null _timestamp measure"
	}	
	def "When try to create null _value measure should raise NullPointerException"(){
		println(">>>>> MetricSpec >>>> When try to register null _value measure should raise NullPointerException")

		when:
			def metric=new Measure(LocalDateTime.now(),null,MeasureReducers.DOUBLE.get())

		then:
			def e=thrown(NullPointerException)
			e.getMessage()=="Can not create null _value measure"
	}	
	def "When try to create null _reducer measure should raise NullPointerException"(){
		println(">>>>> MetricSpec >>>> When try to register null _reducer measure should raise NullPointerException")

		when:
			def metric=new Measure(LocalDateTime.now(),1.0d,null)

		then:
			def e=thrown(NullPointerException)
			e.getMessage()=="Can not create null _reducer measure"
	}	

	@Unroll
	def "When Measure is created with _time:#time,_measure:#measure,_reducer:#reducer of type #type the getTimestamp() returns #time"(){
		println(">>>>> MeasureSpec >>>> When Measure is created with _time:$time,_measure:$measure,_reducer:$reducer of type $type the getTimestamp() returns $time")

		when:
			def obj=new Measure(time,measure,reducer)
			
		then:
			obj!=null
			obj.getTimestamp()==time;
			
		where:
			time						| measure					| reducer							| type
			LocalDateTime.now()			| Duration.ofSeconds(10)	| MeasureReducers.DURATION.get()	| Duration.class
			LocalDateTime.now()			| 10l						| MeasureReducers.LONG.get()		| Long.class			
			LocalDateTime.now()			| 10.2d						| MeasureReducers.DOUBLE.get()		| Double.class		
	}	

	@Unroll
	def "When Measure is created with _time:#time,_measure:#measure,_reducer:#reducer of type #type the getMeasure() returns #measure"(){
		println(">>>>> MeasureSpec >>>> When Measure is created with _time:$time,_measure:$measure,_reducer:$reducer of type $type the getMeasure() returns $measure")

		when:
			def obj=new Measure(time,measure,reducer)
			
		then:
			obj!=null
			obj.getValue()==measure;
			
		where:
			time						| measure					| reducer							| type
			LocalDateTime.now()			| Duration.ofSeconds(10)	| MeasureReducers.DURATION.get()	| Duration.class
			LocalDateTime.now()			| 10l						| MeasureReducers.LONG.get()		| Long.class			
			LocalDateTime.now()			| 10.2d						| MeasureReducers.DOUBLE.get()		| Double.class		
	}	

	@Unroll
	def "When Measure is created with _time:#time,_measure:#measure,_reducer:#reducer of type #type the toString() returns #expected"(){
		println(">>>>> MeasureSpec >>>> When Measure is created with _time:$time,_measure:$measure,_reducer:$reducer of type $type the toString() returns $expected")

		when:
			def obj=new Measure(time,measure,reducer)
			
		then:
			obj!=null
			obj.toString()==expected;
			
		where:
			time						| measure					| reducer							| type				
			LocalDateTime.now()			| Duration.ofSeconds(10)	| MeasureReducers.DURATION.get()	| Duration.class	
			LocalDateTime.now()			| 10l						| MeasureReducers.LONG.get()		| Long.class		
			LocalDateTime.now()			| 10.2d						| MeasureReducers.DOUBLE.get()		| Double.class		
			
			expected = SimpleFormat.format("Measure[timestamp={}, measureType={}, measure={},reducer={}]",
									Optional.ofNullable(time).map({val -> val.format(DateTimeFormatter.ISO_DATE_TIME)}).orElse("null"),
									Optional.ofNullable(measure).map({val -> val.getClass()}).map({val -> val.toString()}).orElse("null")
									,measure
									,reducer);
	}	

	@Unroll
	def "When Measure is created with _time:#time,_measure:#measure,_reducer:#reducer of type #type the toMetricSnapshot() returns #expected"(){
		println(">>>>> MeasureSpec >>>> When Measure is created with _time:$time,_measure:$measure,_reducer:$reducer of type $type the toMetricSnapshot() returns $expected")

		when:
			def obj=new Measure(time,measure,reducer)
			
		then:
			obj!=null
			obj.toMetricSnapshot().getSamplingSize()==expected.getSamplingSize();
			obj.toMetricSnapshot().getAccumulatedSamples()==expected.getAccumulatedSamples();
			obj.toMetricSnapshot().getLastOccurrence()==expected.getLastOccurrence();
			
		where:
			time						| measure					| reducer							| type				
			LocalDateTime.now()			| Duration.ofSeconds(10)	| MeasureReducers.DURATION.get()	| Duration.class	
			LocalDateTime.now()			| 10l						| MeasureReducers.LONG.get()		| Long.class		
			LocalDateTime.now()			| 10.2d						| MeasureReducers.DOUBLE.get()		| Double.class		
			
			expected = MetricSnapshot.builder(reducer)
								.samplingSize(1)
								.accumulatedSamples(measure)
								.lastOccurrence(time)
							.build();
	}		

	@Unroll
	def "Two measures of _time:#time,_measure:#measure,_reducer:#reducer of type #type must return the same hashcode()"(){
		println(">>>>> MeasureSpec >>>> Two measures of _time:$time,_measure:$measure,_reducer:$reducer of type $type must return the same hashcode()")

		when:
			def obj1=new Measure(time,measure,reducer)
			def obj2=new Measure(time,measure,reducer)
			
		then:
			obj1.hashCode()==obj2.hashCode()
			
		where:
			time						| measure					| reducer							| type				
			LocalDateTime.now()			| Duration.ofSeconds(10)	| MeasureReducers.DURATION.get()	| Duration.class	
			LocalDateTime.now()			| 10l						| MeasureReducers.LONG.get()		| Long.class		
			LocalDateTime.now()			| 10.2d						| MeasureReducers.DOUBLE.get()		| Double.class		
	}		


	@Unroll
	def "Two measures of _time:#time,_measure:#measure,_reducer:#reducer of type #type must be equals()"(){
		println(">>>>> MeasureSpec >>>> Two measures of _time:$time,_measure:$measure,_reducer:$reducer of type $type must be equals()")

		when:
			def obj1=new Measure(time,measure,reducer)
			def obj2=new Measure(time,measure,reducer)
			
		then:
			obj1.equals(obj2)
			obj2.equals(obj1)
			
		where:
			time						| measure					| reducer							| type				
			LocalDateTime.now()			| Duration.ofSeconds(10)	| MeasureReducers.DURATION.get()	| Duration.class	
			LocalDateTime.now()			| 10l						| MeasureReducers.LONG.get()		| Long.class		
			LocalDateTime.now()			| 10.2d						| MeasureReducers.DOUBLE.get()		| Double.class		
	}	
}

