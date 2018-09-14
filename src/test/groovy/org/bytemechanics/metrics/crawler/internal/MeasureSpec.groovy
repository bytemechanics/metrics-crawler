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
			LocalDateTime.now()			| Duration.ofDays(10)		| null								| Duration.class
			LocalDateTime.now()			| null						| MeasureReducers.DURATION.get()	| Duration.class
			null						| Duration.ofHours(10)		| MeasureReducers.DURATION.get()	| Duration.class			
			LocalDateTime.now()			| 10l						| MeasureReducers.LONG.get()		| Long.class			
			LocalDateTime.now()			| -9l						| null								| Long.class			
			LocalDateTime.now()			| null						| MeasureReducers.LONG.get()		| Long.class			
			null						| -111l						| MeasureReducers.LONG.get()		| Long.class			
			LocalDateTime.now()			| 10.2d						| MeasureReducers.DOUBLE.get()		| Double.class		
			LocalDateTime.now()			| -7.8d						| null								| Double.class
			LocalDateTime.now()			| null						| MeasureReducers.DOUBLE.get()		| Double.class			
			null						| -2.8d						| MeasureReducers.DOUBLE.get()		| Double.class			
			null						| null						| MeasureReducers.DURATION.get()	| Double.class			
			null						| null						| null								| Double.class
	}	

	@Unroll
	def "When Measure is created with _time:#time,_measure:#measure,_reducer:#reducer of type #type the getMeasure() returns #measure"(){
		println(">>>>> MeasureSpec >>>> When Measure is created with _time:$time,_measure:$measure,_reducer:$reducer of type $type the getMeasure() returns $measure")

		when:
			def obj=new Measure(time,measure,reducer)
			
		then:
			obj!=null
			obj.getMeasure()==measure;
			
		where:
			time						| measure					| reducer							| type
			LocalDateTime.now()			| Duration.ofSeconds(10)	| MeasureReducers.DURATION.get()	| Duration.class
			LocalDateTime.now()			| Duration.ofDays(10)		| null								| Duration.class
			LocalDateTime.now()			| null						| MeasureReducers.DURATION.get()	| Duration.class
			null						| Duration.ofHours(10)		| MeasureReducers.DURATION.get()	| Duration.class			
			LocalDateTime.now()			| 10l						| MeasureReducers.LONG.get()		| Long.class			
			LocalDateTime.now()			| -9l						| null								| Long.class			
			LocalDateTime.now()			| null						| MeasureReducers.LONG.get()		| Long.class			
			null						| -111l						| MeasureReducers.LONG.get()		| Long.class			
			LocalDateTime.now()			| 10.2d						| MeasureReducers.DOUBLE.get()		| Double.class		
			LocalDateTime.now()			| -7.8d						| null								| Double.class
			LocalDateTime.now()			| null						| MeasureReducers.DOUBLE.get()		| Double.class			
			null						| -2.8d						| MeasureReducers.DOUBLE.get()		| Double.class			
			null						| null						| MeasureReducers.DURATION.get()	| Double.class			
			null						| null						| null								| Double.class
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
			LocalDateTime.now()			| Duration.ofDays(10)		| null								| Duration.class	
			LocalDateTime.now()			| null						| MeasureReducers.DURATION.get()	| Duration.class	
			null						| Duration.ofHours(10)		| MeasureReducers.DURATION.get()	| Duration.class	
			LocalDateTime.now()			| 10l						| MeasureReducers.LONG.get()		| Long.class		
			LocalDateTime.now()			| -9l						| null								| Long.class		
			LocalDateTime.now()			| null						| MeasureReducers.LONG.get()		| Long.class		
			null						| -111l						| MeasureReducers.LONG.get()		| Long.class		
			LocalDateTime.now()			| 10.2d						| MeasureReducers.DOUBLE.get()		| Double.class		
			LocalDateTime.now()			| -7.8d						| null								| Double.class		
			LocalDateTime.now()			| null						| MeasureReducers.DOUBLE.get()		| Double.class		
			null						| -2.8d						| MeasureReducers.DOUBLE.get()		| Double.class		
			null						| null						| MeasureReducers.DURATION.get()	| Double.class		
			null						| null						| null								| Double.class		
			
			expected = SimpleFormat.format("Measure[timestamp={}, measureType={}, measure={},reducer={}]",
									Optional.ofNullable(time).map({val -> val.format(DateTimeFormatter.ISO_DATE_TIME)}).orElse("null"),
									Optional.ofNullable(measure).map({val -> val.getClass()}).map({val -> val.toString()}).orElse("null")
									,measure
									,reducer);
	}	

	@Unroll
	def "When notNull() is called over #measure then returns #expected"(){
		println(">>>>> MeasureSpec >>>> When notNull() is called over $measure then returns $expected")

		when:
			def actual=Measure.notNull(measure)
			
		then:
			actual==expected;
			
		where:
			measure << [new Measure(LocalDateTime.now()	,Duration.ofDays(10),MeasureReducers.DURATION.get()),null]
			expected = measure!=null;
	}	

	@Unroll
	def "When Measure is created with _time:#time,_measure:#measure,_reducer:#reducer of type #type the toMetricSnapshot() returns #expected"(){
		println(">>>>> MeasureSpec >>>> When Measure is created with _time:$time,_measure:$measure,_reducer:$reducer of type $type the toMetricSnapshot() returns $expected")

		when:
			def obj=new Measure(time,measure,reducer)
			
		then:
			obj!=null
			obj.toMetricSnapshot().getSamplingSize()==expected.getSamplingSize();
			obj.toMetricSnapshot().getSamplingAccumulatedMeasure()==expected.getSamplingAccumulatedMeasure();
			obj.toMetricSnapshot().getTimestamp()==expected.getTimestamp();
			
		where:
			time						| measure					| reducer							| type				
			LocalDateTime.now()			| Duration.ofSeconds(10)	| MeasureReducers.DURATION.get()	| Duration.class	
			LocalDateTime.now()			| null						| MeasureReducers.DURATION.get()	| Duration.class	
			null						| Duration.ofHours(10)		| MeasureReducers.DURATION.get()	| Duration.class	
			LocalDateTime.now()			| 10l						| MeasureReducers.LONG.get()		| Long.class		
			LocalDateTime.now()			| null						| MeasureReducers.LONG.get()		| Long.class		
			null						| -111l						| MeasureReducers.LONG.get()		| Long.class		
			LocalDateTime.now()			| 10.2d						| MeasureReducers.DOUBLE.get()		| Double.class		
			LocalDateTime.now()			| null						| MeasureReducers.DOUBLE.get()		| Double.class		
			null						| -2.8d						| MeasureReducers.DOUBLE.get()		| Double.class		
			null						| null						| MeasureReducers.DURATION.get()	| Double.class		
			
			expected = MetricSnapshot.builder(reducer)
								.samplingSize(1)
								.samplingAccumulatedMeasure(measure)
								.lastOccurrence(time)
							.build();
	}		
}

