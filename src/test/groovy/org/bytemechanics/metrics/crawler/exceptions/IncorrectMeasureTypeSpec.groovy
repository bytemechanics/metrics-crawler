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

package org.bytemechanics.metrics.crawler.exceptions

import spock.lang.Specification
import spock.lang.Unroll
import java.util.Optional
import java.util.logging.*
import java.util.stream.*
import spock.lang.Shared

/**
 * @author afarre
 */
class IncorrectMeasureTypeSpec extends Specification{
	
	def setupSpec(){
		println(">>>>> IncorrectMeasureTypeSpec >>>> setupSpec")
		final InputStream inputStream = IncorrectMeasureTypeSpec.class.getResourceAsStream("/logging.properties");
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
	def "When IncorrectMeasureType is instantiated with metricName:#metricName,originalType:#originalType and wrongType:#wrongType them getMetricName() must return #metricName" (){
		println(">>>>> IncorrectMeasureTypeSpec >>>> When IncorrectSamplingSize is instantiated with metricName:$metricName,originalType:$originalType and wrongType:$wrongType them getMetricName() must return $metricName")

		given:
			def exception=new IncorrectMeasureType(metricName,originalType,wrongType)
			
		expect:
			exception!=null
			exception.getMetricName()==metricName
			
		where:
			metricName	| originalType	| wrongType
			"ada"		| String.class	| Integer.class
			"ada1"		| Integer.class	| Double.class
			"ada2"		| Double.class	| Long.class
			"ada3"		| Long.class	| null
			"ada4"		| null			| char.class
			null		| char.class	| String.class
	}
	
	@Unroll
	def "When IncorrectMeasureType is instantiated with metricName:#metricName,originalType:#originalType and wrongType:#wrongType them getOriginalType() must return #originalType" (){
		println(">>>>> IncorrectMeasureTypeSpec >>>> When IncorrectSamplingSize is instantiated with metricName:$metricName,originalType:$originalType and wrongType:$wrongType them getOriginalType() must return $originalType")

		given:
			def exception=new IncorrectMeasureType(metricName,originalType,wrongType)
			
		expect:
			exception!=null
			exception.getOriginalType()==originalType
			
		where:
			metricName	| originalType		| wrongType
			"ada"		| String.class		| Integer.class
			"ada1"		| Integer.class		| Double.class
			"ada2"		| Double.class		| Long.class
			"ada3"		| Long.class		| null
			"ada4"		| null				| Character.class
			null		| Character.class	| String.class
	}
	
	@Unroll
	def "When IncorrectMeasureType is instantiated with metricName:#metricName,originalType:#originalType and wrongType:#wrongType them getWrongType() must return #wrongType" (){
		println(">>>>> IncorrectMeasureTypeSpec >>>> When IncorrectSamplingSize is instantiated with metricName:$metricName,originalType:$originalType and wrongType:$wrongType them getWrongType() must return $wrongType")

		given:
			def exception=new IncorrectMeasureType(metricName,originalType,wrongType)
			
		expect:
			exception!=null
			exception.getWrongType()==wrongType
			
		where:
			metricName	| originalType		| wrongType
			"ada"		| String.class		| Integer.class
			"ada1"		| Integer.class		| Double.class
			"ada2"		| Double.class		| Long.class
			"ada3"		| Long.class		| null
			"ada4"		| null				| Character.class
			null		| Character.class	| String.class
	}

	@Unroll
	def "Two instances of IncorrectMeasureType with the same metricName:#metricName,originalType:#originalType and wrongType:#wrongType must have the same hashcode" (){
		println(">>>>> IncorrectMeasureTypeSpec >>>> Two instances of IncorrectSamplingSize with the same metricName:$metricName,originalType:$originalType and wrongType:$wrongType must have the same hashcode")

		given:
			def exception1=new IncorrectMeasureType(metricName,originalType,wrongType)
			def exception2=new IncorrectMeasureType(metricName,originalType,wrongType)
			
		expect:
			exception1.hashCode()==exception2.hashCode()
			
		where:
			metricName	| originalType		| wrongType
			"ada"		| String.class		| Integer.class
			"ada1"		| Integer.class		| Double.class
			"ada2"		| Double.class		| Long.class
			"ada3"		| Long.class		| null
			"ada4"		| null				| Character.class
			null		| Character.class	| String.class
	}

	@Unroll
	def "Two instances of IncorrectMeasureType with the same metricName:#metricName,originalType:#originalType and wrongType:#wrongType must be equals()" (){
		println(">>>>> IncorrectMeasureTypeSpec >>>> Two instances of IncorrectSamplingSize with the same metricName:$metricName,originalType:$originalType and wrongType:$wrongType must be equals()")

		given:
			def exception1=new IncorrectMeasureType(metricName,originalType,wrongType)
			def exception2=new IncorrectMeasureType(metricName,originalType,wrongType)
			
		expect:
			exception1.equals(exception2)
			
		where:
			metricName	| originalType		| wrongType
			"ada"		| String.class		| Integer.class
			"ada1"		| Integer.class		| Double.class
			"ada2"		| Double.class		| Long.class
			"ada3"		| Long.class		| null
			"ada4"		| null				| Character.class
			null		| Character.class	| String.class
	}
}

