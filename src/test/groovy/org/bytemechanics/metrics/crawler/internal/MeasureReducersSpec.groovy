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
import java.util.logging.*
import java.util.stream.*
import java.time.Duration
import java.text.NumberFormat

/**
 *
 * @author afarre
 */
class MeasureReducersSpec extends Specification{
	
	def setupSpec(){
		println(">>>>> MeasureReducersSpec >>>> setupSpec")
		final InputStream inputStream = MeasureReducersSpec.class.getResourceAsStream("/logging.properties");
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
	def "Reducer #reducer get() and supplier() should return the same instance"(){
		println(">>>>> MeasureReducersSpec >>>> Reducer $reducer get() and supplier() should return the same instance")

		when:
			def reducerInstance1=reducer.get(Object.class)
			def reducerInstance2=reducer.supplier(Object.class).get()
			
		then:
			reducerInstance1!=null
			reducerInstance2!=null
			reducerInstance1==reducerInstance2
			
		where:
			reducer	<< MeasureReducers.values()
	}	

	@Unroll
	def "When call identity() from #reducer the result must be #identity and the same of call get().identity()"(){
		println(">>>>> MeasureReducersSpec >>>> When call identity() from $reducer the result must be  $identity and the same of call get().identity()")

		when:
			def actualIdentity=reducer.identity(Object.class)
			def actualIdentity2=reducer.get(Object.class).identity()
			
		then:
			actualIdentity!=null
			actualIdentity==identity
			actualIdentity2!=null
			actualIdentity==actualIdentity2
			
		where:
			reducer						| identity
			MeasureReducers.DURATION	| Duration.ZERO
			MeasureReducers.LONG		| 0l
			MeasureReducers.DOUBLE		| 0.0d
	}
	

	@Unroll
	def "When call accumulate() from #reducer over #val1 and #val2 result must be #expected"(){
		println(">>>>> MeasureReducersSpec >>>> When call accumulate() from $reducer over $val1 and $val2 result must be $expected")

		when:
			def result=reducer.get(Object.class).accumulate(val1,val2)
			
		then:
			result!=null
			result.isPresent()==expected.isPresent()
			result==expected
			
		where:
			reducer						| val1						| val2					| expected
			MeasureReducers.DURATION	| Duration.ofSeconds(10)	| Duration.ofDays(10)	| Optional.ofNullable(Duration.parse("P10DT10S"))
			MeasureReducers.DURATION	| Duration.ofDays(10)		| Duration.ofDays(1)	| Optional.ofNullable(Duration.parse("P11D"))
			MeasureReducers.DURATION	| null						| Duration.ofDays(1)	| Optional.ofNullable(Duration.ofDays(1))
			MeasureReducers.DURATION	| Duration.ofDays(10)		| null					| Optional.ofNullable(Duration.ofDays(10))
			MeasureReducers.DURATION	| null						| null					| Optional.empty()
			MeasureReducers.LONG		| 10l						| -5l					| Optional.ofNullable(5l)
			MeasureReducers.LONG		| -9l						| 0l					| Optional.ofNullable(-9l)
			MeasureReducers.LONG		| null						| 0l					| Optional.ofNullable(0l)
			MeasureReducers.LONG		| -9l						| null					| Optional.ofNullable(-9l)
			MeasureReducers.LONG		| null						| null					| Optional.empty()
			MeasureReducers.DOUBLE		| 10.2d						| 7.5d					| Optional.ofNullable(17.7d)
			MeasureReducers.DOUBLE		| -7.8d						| -9.9d					| Optional.ofNullable(-17.7d)
			MeasureReducers.DOUBLE		| null						| -9.9d					| Optional.ofNullable(-9.9d)
			MeasureReducers.DOUBLE		| -7.8d						| null					| Optional.ofNullable(-7.8d)
			MeasureReducers.DOUBLE		| null						| null					| Optional.empty()
	}

	@Unroll
	def "When call max() from #reducer over #val1 and #val2 result must be #expected"(){
		println(">>>>> MeasureReducersSpec >>>> When call max() from $reducer over $val1 and $val2 result must be $expected")

		when:
			def result=reducer.get(Object.class).max(val1,val2)
			
		then:
			result!=null
			result.isPresent()==expected.isPresent()
			result==expected
			
		where:
			reducer						| val1						| val2						| expected
			MeasureReducers.DURATION	| Duration.ofSeconds(10)	| Duration.ofDays(10)		| Optional.ofNullable(Duration.ofDays(10))
			MeasureReducers.DURATION	| Duration.ofSeconds(121)	| Duration.ofMinutes(2)		| Optional.ofNullable(Duration.ofSeconds(121))
			MeasureReducers.DURATION	| null						| Duration.ofMinutes(2)		| Optional.ofNullable(Duration.ofMinutes(2))
			MeasureReducers.DURATION	| Duration.ofSeconds(121)	| null						| Optional.ofNullable(Duration.ofSeconds(121))
			MeasureReducers.DURATION	| null						| null						| Optional.empty()
			MeasureReducers.LONG		| 10l						| -5l						| Optional.ofNullable(10l)
			MeasureReducers.LONG		| -10l						| -5l						| Optional.ofNullable(-5l)
			MeasureReducers.LONG		| null						| -5l						| Optional.ofNullable(-5l)
			MeasureReducers.LONG		| -10l						| null						| Optional.ofNullable(-10l)
			MeasureReducers.LONG		| null						| null						| Optional.empty()
			MeasureReducers.DOUBLE		| 10.2d						| 7.5d						| Optional.ofNullable(10.2d)
			MeasureReducers.DOUBLE		| -10.2d					| -7.5d						| Optional.ofNullable(-7.5d)
			MeasureReducers.DOUBLE		| null						| -7.5d						| Optional.ofNullable(-7.5d)
			MeasureReducers.DOUBLE		| -10.2d					| null						| Optional.ofNullable(-10.2d)
			MeasureReducers.DOUBLE		| null						| null						| Optional.empty()
	}

	@Unroll
	def "When call min() from #reducer over #val1 and #val2 result must be #expected"(){
		println(">>>>> MeasureReducersSpec >>>> When call min() from $reducer over $val1 and $val2 result must be $expected")

		when:
			def result=reducer.get(Object.class).min(val1,val2)
			
		then:
			result!=null
			result.isPresent()==expected.isPresent()
			result==expected
			
		where:
			reducer						| val1						| val2						| expected
			MeasureReducers.DURATION	| Duration.ofSeconds(10)	| Duration.ofDays(10)		| Optional.ofNullable(Duration.ofSeconds(10))
			MeasureReducers.DURATION	| Duration.ofSeconds(121)	| Duration.ofMinutes(2)		| Optional.ofNullable(Duration.ofMinutes(2))
			MeasureReducers.DURATION	| null						| Duration.ofMinutes(2)		| Optional.ofNullable(Duration.ofMinutes(2))
			MeasureReducers.DURATION	| Duration.ofSeconds(121)	| null						| Optional.ofNullable(Duration.ofSeconds(121))
			MeasureReducers.DURATION	| null						| null						| Optional.empty()
			MeasureReducers.LONG		| 10l						| -5l						| Optional.ofNullable(-5l)
			MeasureReducers.LONG		| -10l						| -5l						| Optional.ofNullable(-10l)
			MeasureReducers.LONG		| null						| -5l						| Optional.ofNullable(-5l)
			MeasureReducers.LONG		| -10l						| null						| Optional.ofNullable(-10l)
			MeasureReducers.LONG		| null						| null						| Optional.empty()
			MeasureReducers.DOUBLE		| 10.2d						| 7.5d						| Optional.ofNullable(7.5d)
			MeasureReducers.DOUBLE		| -10.2d					| -7.5d						| Optional.ofNullable(-10.2d)
			MeasureReducers.DOUBLE		| null						| -7.5d						| Optional.ofNullable(-7.5d)
			MeasureReducers.DOUBLE		| -10.2d					| null						| Optional.ofNullable(-10.2d)
			MeasureReducers.DOUBLE		| null						| null						| Optional.empty()
	}

	@Unroll
	def "When call average() from #reducer over #val and #hits result must be #expected"(){
		println(">>>>> MeasureReducersSpec >>>> When call average() from $reducer over $val and $hits result must be $expected")

		when:
			def result=reducer.get(Object.class).average(val,hits)
			
		then:
			result!=null
			result.isPresent()==expected.isPresent()
			result==expected
			
		where:
			reducer						| val						| hits						| expected
			MeasureReducers.DURATION	| Duration.ofSeconds(10)	| 10l						| Optional.ofNullable(Duration.ofSeconds(1))
			MeasureReducers.DURATION	| Duration.ofSeconds(121)	| 2l						| Optional.ofNullable(Duration.parse("PT1M0.5S"))
			MeasureReducers.DURATION	| null						| 2l						| Optional.empty()
			MeasureReducers.LONG		| 10l						| 2l						| Optional.ofNullable(5l)
			MeasureReducers.LONG		| -10l						| 3l						| Optional.ofNullable(-3l)
			MeasureReducers.LONG		| null						| 4l						| Optional.empty()
			MeasureReducers.DOUBLE		| 10.2d						| 5l						| Optional.ofNullable(2.04d)
			MeasureReducers.DOUBLE		| -10.2d					| 2l						| Optional.ofNullable(-5.1d)
			MeasureReducers.DOUBLE		| null						| 10l						| Optional.empty()
	}


	@Unroll
	def "When call toString() from #reducer over #val result must be #expected"(){
		println(">>>>> MeasureReducersSpec >>>> When call toString() from $reducer over $val result must be $expected")

		when:
			def result=reducer.get(Object.class).toString(val)
			
		then:
			result!=null
			result==expected
			
		where:
			reducer						| val						| expected
			MeasureReducers.DURATION	| Duration.ofDays(10)		| "240:00:00"
			MeasureReducers.DURATION	| Duration.ofSeconds(121)	| "0:02:01"
			MeasureReducers.DURATION	| null						| "null"
			MeasureReducers.LONG		| 10l						| NumberFormat.getNumberInstance().format(10l);
			MeasureReducers.LONG		| -10l						| NumberFormat.getNumberInstance().format(-10l);
			MeasureReducers.LONG		| null						| "null"
			MeasureReducers.DOUBLE		| 10.2d						| NumberFormat.getNumberInstance().format(10.2d);
			MeasureReducers.DOUBLE		| -10.2d					| NumberFormat.getNumberInstance().format(-10.2d);
			MeasureReducers.DOUBLE		| null						| "null"
	}
}

