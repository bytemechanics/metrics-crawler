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
import java.lang.NullPointerException
import java.time.Duration
import java.time.format.DateTimeFormatter
import org.bytemechanics.metrics.crawler.beans.MetricSnapshot
import org.bytemechanics.metrics.crawler.internal.commons.string.SimpleFormat
import spock.lang.Shared
import org.bytemechanics.metrics.crawler.exceptions.IncorrectSamplingSize
import org.bytemechanics.metrics.crawler.internal.commons.collections.FastDropLastQueue

/**
 * @author afarre
 */
class MetricSpec extends Specification{
	
	@Shared def persistentMetric=new Metric("c",2,MeasureReducers.DOUBLE.get())

	
	def setupSpec(){
		println(">>>>> MetricSpec >>>> setupSpec")
		final InputStream inputStream = MetricSpec.class.getResourceAsStream("/logging.properties");
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
	def "When Metric is created with _name:#name,_samplingSize:#samplingSize,_reducer:#reducer of type #type the getName() returns #name"(){
		println(">>>>> MetricSpec >>>> When Metric is created with _name:$name,_samplingSize:$samplingSize,_reducer:$reducer of type $type the getName() returns $name")

		when:
			def obj=new Metric(name,samplingSize,reducer)
			
		then:
			obj!=null
			obj.getName()==name;
			
		where:
			name		| samplingSize	| reducer							| type
			"a"			| 1				| MeasureReducers.DURATION.get()	| Duration.class
			"c"			| 1000			| MeasureReducers.LONG.get()		| Long.class
			"fdsf"		| 1000000		| MeasureReducers.DOUBLE.get()		| Double.class			
	}	

	@Unroll
	def "When Metric is created with _name:#name,_samplingSize:#samplingSize,_reducer:#reducer of type #type the getHits() returns 0"(){
		println(">>>>> MetricSpec >>>> When Metric is created with _name:$name,_samplingSize:$samplingSize,_reducer:$reducer of type $type the getHits() returns 0")

		when:
			def obj=new Metric(name,samplingSize,reducer)
			
		then:
			obj!=null
			obj.getHits()==0;
			
		where:
			name		| samplingSize	| reducer							| type
			"a"			| 1				| MeasureReducers.DURATION.get()	| Duration.class
			"c"			| 1000			| MeasureReducers.LONG.get()		| Long.class
			"fdsf"		| 1000000		| MeasureReducers.DOUBLE.get()		| Double.class			
	}	

	@Unroll
	def "When Metric is created with _name:#name,_samplingSize:#samplingSize,_reducer:#reducer of type #type the getMeasures() returns empty queue"(){
		println(">>>>> MetricSpec >>>> When Metric is created with _name:$name,_samplingSize:$samplingSize,_reducer:$reducer of type $type the getMeasures() returns empty")

		when:
			def obj=new Metric(name,samplingSize,reducer)
			
		then:
			obj!=null
			obj.getMeasures().isEmpty();
			
		where:
			name		| samplingSize	| reducer							| type
			"a"			| 1				| MeasureReducers.DURATION.get()	| Duration.class
			"c"			| 1000			| MeasureReducers.LONG.get()		| Long.class
			"fdsf"		| 1000000		| MeasureReducers.DOUBLE.get()		| Double.class			
	}	

	@Unroll
	def "When Metric is created with _name:#name,_samplingSize:#samplingSize,_reducer:#reducer of type #type the getReducer() returns empty queue"(){
		println(">>>>> MetricSpec >>>> When Metric is created with _name:$name,_samplingSize:$samplingSize,_reducer:$reducer of type $type the getReducer() returns empty")

		when:
			def obj=new Metric(name,samplingSize,reducer)
			
		then:
			obj!=null
			obj.getReducer()==reducer;
			
		where:
			name		| samplingSize	| reducer							| type
			"a"			| 1				| MeasureReducers.DURATION.get()	| Duration.class
			"c"			| 1000			| MeasureReducers.LONG.get()		| Long.class
			"fdsf"		| 1000000		| MeasureReducers.DOUBLE.get()		| Double.class			
	}	
	
	@Unroll
	def "When Metric is created with _name:#name,_samplingSize:#samplingSize,_reducer:#reducer of type #type the toString() returns empty #expected"(){
		println(">>>>> MetricSpec >>>> When Metric is created with _name:$name,_samplingSize:$samplingSize,_reducer:$reducer of type $type the toString() returns $expected")

		when:
			def obj=new Metric(name,samplingSize,reducer)
			
		then:
			obj!=null
			obj.getMeasures().isEmpty();
			
		where:
			name		| samplingSize	| reducer							| type
			"a"			| 1				| MeasureReducers.DURATION.get()	| Duration.class
			"c"			| 1000			| MeasureReducers.LONG.get()		| Long.class
			"fdsf"		| 1000000		| MeasureReducers.DOUBLE.get()		| Double.class		
			
			expected = SimpleFormat.format("Metric[name={}, hits={}, measures={}, reducer={}]", name, 0, new FastDropLastQueue<>(samplingSize), reducer);
	}	
	
	def "Try to create with Metric with _name:#name,_samplingSize:#samplingSize,_reducer:#reducer should raise #exception"(){
		println(">>>>> MetricSpec >>>> Try to create with Metric with _name:$name,_samplingSize:$samplingSize,_reducer:$reducer should raise $exception")

		when:
			def obj=new Metric(name,samplingSize,reducer)
			
		then:
			def e=thrown(exception.class)
			e.getMessage()==exception.getMessage()
			
		where:
			name		| samplingSize	| reducer							| exception
			null		| 1				| MeasureReducers.DURATION.get()	| new NullPointerException("Name can not be null to create a Metric")
			"ab"		| -1			| null								| new NullPointerException(SimpleFormat.format("Metric {} reducer can not be null to create a Metric",name))
			"a"			| -100			| MeasureReducers.LONG.get()		| new IncorrectSamplingSize(name, samplingSize)
			"a"			| -1			| MeasureReducers.DURATION.get()	| new IncorrectSamplingSize(name, samplingSize)
			"c"			| 0				| MeasureReducers.DOUBLE.get()		| new IncorrectSamplingSize(name, samplingSize)
	}	

	@Unroll
	def "When Metric #metric1 is compared with #metric2 with equals the result should be #expected"(){
		println(">>>>> MetricSpec >>>> When Metric $metric1 is compared with $metric2 with equals the result should be $expected")

		when:
			def actual=metric1.equals(metric2)
			
		then:
			actual==expected
			
		where:
			metric1												| metric2											| expected							
			new Metric("c",1,MeasureReducers.DURATION.get())	| new Metric("c",1,MeasureReducers.DURATION.get())	| true
			new Metric("c",2,MeasureReducers.DURATION.get())	| new Metric("c",1,MeasureReducers.DURATION.get())	| true
			new Metric("c",1,MeasureReducers.DURATION.get())	| new Metric("c",2,MeasureReducers.DURATION.get())	| true
			new Metric("1",1,MeasureReducers.DURATION.get())	| new Metric("c",1,MeasureReducers.DURATION.get())	| false
			new Metric("c",1,MeasureReducers.DURATION.get())	| new Metric("1",1,MeasureReducers.DURATION.get())	| false
			new Metric("c",1,MeasureReducers.DOUBLE.get())		| new Metric("c",1,MeasureReducers.DURATION.get())	| false
			new Metric("c",1,MeasureReducers.DOUBLE.get())		| new Metric("c",1,MeasureReducers.LONG.get())		| false
			new Metric("c",1,MeasureReducers.DURATION.get())	| new Metric("c",1,MeasureReducers.LONG.get())		| false
	}

	@Unroll
	def "When Metric #metric1 is compared with #metric2 with hashCode the result comparison should be #expected"(){
		println(">>>>> MetricSpec >>>> When Metric $metric1 is compared with $metric2 with equals the result comparison should be $expected")

		when:
			def actual=(metric1.hashCode()==metric2.hashCode())
		then:
			actual==expected
			
		where:
			metric1												| metric2											| expected						
			new Metric("c",1,MeasureReducers.DURATION.get())	| new Metric("c",1,MeasureReducers.DURATION.get())	| true
			new Metric("c",2,MeasureReducers.DURATION.get())	| new Metric("c",1,MeasureReducers.DURATION.get())	| true
			new Metric("c",1,MeasureReducers.DURATION.get())	| new Metric("c",2,MeasureReducers.DURATION.get())	| true
			new Metric("1",1,MeasureReducers.DURATION.get())	| new Metric("c",1,MeasureReducers.DURATION.get())	| false
			new Metric("c",1,MeasureReducers.DURATION.get())	| new Metric("1",1,MeasureReducers.DURATION.get())	| false
			new Metric("c",1,MeasureReducers.DOUBLE.get())		| new Metric("c",1,MeasureReducers.DURATION.get())	| false
			new Metric("c",1,MeasureReducers.DOUBLE.get())		| new Metric("c",1,MeasureReducers.LONG.get())		| false
			new Metric("c",1,MeasureReducers.DURATION.get())	| new Metric("c",1,MeasureReducers.LONG.get())		| false
	}

	@Unroll
	def "When Metric #metric is called with addMeasure(_time:#time,_measure:#measure) the to string should return #expected"(){
		println(">>>>> MetricSpec >>>> When Metric $metric is called with addMeasure(_time:$time,_measure:$measure) the to string should return $expected")

		when:
			metric.addMeasure(time,measure)
			
		then:
			metric.toString()==expected
			
		where:
			metric												| time					| measure					
			new Metric("c",1,MeasureReducers.DURATION.get())	| LocalDateTime.now()	| Duration.ofSeconds(10)	
			new Metric("c",1,MeasureReducers.DOUBLE.get())		| LocalDateTime.now()	| 1.1d						
			new Metric("c",1,MeasureReducers.LONG.get())		| LocalDateTime.now()	| 3l						
			
			expected=SimpleFormat.format("Metric[name={}, hits={}, measures={}, reducer={}]", "c", 1, [new Measure(time,measure,metric.getReducer())],metric.getReducer())
	}

	@Unroll
	def "When Metric metric with 2 sampling size is called with addMeasure(_time:#time,_measure:#measure) #times times getMeasures() should return #expected"(){
		println(">>>>> MetricSpec >>>> When Metric metric with 2 sampling size is called with addMeasure(_time:$time,_measure:$measure) $times times getMeasures() should return $expected")

		when:
			persistentMetric.addMeasure(time,measure)
			
		then:
			persistentMetric.getMeasures()==expected
			
		where:
			time						| measure	| times	| expected						
			LocalDateTime.of(1,1,1,1,1)	| 1.0d		| 1		| [new Measure(LocalDateTime.of(1,1,1,1,1),1.0d,MeasureReducers.DOUBLE.get())]
			LocalDateTime.of(2,2,2,2,2)	| 2.0d		| 2		| [new Measure(LocalDateTime.of(1,1,1,1,1),1.0d,MeasureReducers.DOUBLE.get()),new Measure(LocalDateTime.of(2,2,2,2,2),2.0d,MeasureReducers.DOUBLE.get())]
			LocalDateTime.of(3,3,3,3,3)	| 3.0d		| 3		| [new Measure(LocalDateTime.of(2,2,2,2,2),2.0d,MeasureReducers.DOUBLE.get()),new Measure(LocalDateTime.of(3,3,3,3,3),3.0d,MeasureReducers.DOUBLE.get())]
			LocalDateTime.of(4,4,4,4,4)	| 4.0d		| 4		| [new Measure(LocalDateTime.of(3,3,3,3,3),3.0d,MeasureReducers.DOUBLE.get()),new Measure(LocalDateTime.of(4,4,4,4,4),4.0d,MeasureReducers.DOUBLE.get())]
			LocalDateTime.of(5,5,5,5,5)	| 5.0d		| 5		| [new Measure(LocalDateTime.of(4,4,4,4,4),4.0d,MeasureReducers.DOUBLE.get()),new Measure(LocalDateTime.of(5,5,5,5,5),5.0d,MeasureReducers.DOUBLE.get())]
			
	}

	
	def "Double metric should convert correctly to snapshot"(){
		println(">>>>> MetricSpec >>>> Double metric should convert correctly to snapshot")
			
		given:
			def metric=new Metric("mNAme",3,MeasureReducers.DOUBLE.get())
			metric.addMeasure(LocalDateTime.of(1,1,1,1,1),1.0d)
			metric.addMeasure(LocalDateTime.of(2,2,2,2,2),2.0d)
			metric.addMeasure(LocalDateTime.of(3,3,3,3,3),3.0d)
			metric.addMeasure(LocalDateTime.of(4,4,4,4,4),4.0d)
			metric.addMeasure(LocalDateTime.of(5,5,5,5,5),5.0d)
			def expected=new MetricSnapshot(MeasureReducers.DOUBLE.get(),"mNAme",12.0d,3,5,5.0d,3.0d,4.0d,5.0d,LocalDateTime.of(5,5,5,5,5))

		expect:
			metric.toSnapshot()==expected
	
	}	
	
	def "Long metric should convert correctly to snapshot"(){
		println(">>>>> MetricSpec >>>> Double metric should convert correctly to snapshot")
			
		given:
			def metric=new Metric("mNAme",3,MeasureReducers.LONG.get())
			metric.addMeasure(LocalDateTime.of(1,1,1,1,1),1l)
			metric.addMeasure(LocalDateTime.of(2,2,2,2,2),2l)
			metric.addMeasure(LocalDateTime.of(3,3,3,3,3),3l)
			metric.addMeasure(LocalDateTime.of(4,4,4,4,4),4l)
			metric.addMeasure(LocalDateTime.of(5,5,5,5,5),5l)
			def expected=new MetricSnapshot(MeasureReducers.LONG.get(),"mNAme",12l,3,5,5l,3l,4l,5l,LocalDateTime.of(5,5,5,5,5))

		expect:
			metric.toSnapshot()==expected
	
	}	
	
	def "Duration metric should convert correctly to snapshot"(){
		println(">>>>> MetricSpec >>>> Double metric should convert correctly to snapshot")
			
		given:
			def metric=new Metric("mNAme",3,MeasureReducers.DURATION.get())
			metric.addMeasure(LocalDateTime.of(1,1,1,1,1),Duration.ofSeconds(1))
			metric.addMeasure(LocalDateTime.of(2,2,2,2,2),Duration.ofSeconds(2))
			metric.addMeasure(LocalDateTime.of(3,3,3,3,3),Duration.ofSeconds(3))
			metric.addMeasure(LocalDateTime.of(4,4,4,4,4),Duration.ofSeconds(4))
			metric.addMeasure(LocalDateTime.of(5,5,5,5,5),Duration.ofSeconds(5))
			def expected=new MetricSnapshot(MeasureReducers.DURATION.get(),"mNAme",Duration.ofSeconds(12),3,5,Duration.ofSeconds(5),Duration.ofSeconds(3),Duration.ofSeconds(4),Duration.ofSeconds(5),LocalDateTime.of(5,5,5,5,5))

		expect:
			metric.toSnapshot()==expected
	
	}	
}

