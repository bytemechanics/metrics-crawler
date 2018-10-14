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
class IncorrectSamplingSizeSpec extends Specification{
	
	def setupSpec(){
		println(">>>>> IncorrectSamplingSizeSpec >>>> setupSpec")
		final InputStream inputStream = IncorrectSamplingSizeSpec.class.getResourceAsStream("/logging.properties");
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
	def "When IncorrectSamplingSize is instantiated with metricName:#metricName and samplingSize:#samplingSize them getMetricName() must return #metricName"(){
		println(">>>>> IncorrectSamplingSizeSpec >>>> When IncorrectSamplingSize is instantiated with metricName:$metricName and samplingSize:$samplingSize them getMetricName() must return $metricName")

		given:
			def exception=new IncorrectSamplingSize(metricName,samplingSize)
			
		expect:
			exception!=null
			exception.getMetricName()==metricName
			
		where:
			metricName	| samplingSize
			"ada"		| 2
			"ada1"		| 3
			"ada2"		| 0
			"ada3"		| 5
			"ada4"		| 6
			null		| 6
	}
	
	@Unroll
	def "When IncorrectSamplingSize is instantiated with metricName:#metricName and samplingSize:#samplingSize them getSamplingSize() must return #samplingSize"(){
		println(">>>>> IncorrectSamplingSizeSpec >>>> When IncorrectSamplingSize is instantiated with metricName:$metricName and samplingSize:$samplingSize them getMetricName() must return $samplingSize")

		given:
			def exception=new IncorrectSamplingSize(metricName,samplingSize)
			
		expect:
			exception!=null
			exception.getSamplingSize()==samplingSize
			
		where:
			metricName	| samplingSize
			"ada"		| 2
			"ada1"		| 3
			"ada2"		| 0
			"ada3"		| 5
			"ada4"		| 6
			null		| 6
	}

	@Unroll
	def "Two instances of IncorrectSamplingSize with the same metricName:#metricName and samplingSize:#samplingSize must have the same hashcode"(){
		println(">>>>> IncorrectSamplingSizeSpec >>>> Two instances of IncorrectSamplingSize with the same metricName:$metricName and samplingSize:$samplingSize must have the same hashcode")

		given:
			def exception1=new IncorrectSamplingSize(metricName,samplingSize)
			def exception2=new IncorrectSamplingSize(metricName,samplingSize)
			
		expect:
			exception1.hashCode()==exception2.hashCode()
			
		where:
			metricName	| samplingSize
			"ada"		| 2
			"ada1"		| 3
			"ada2"		| 0
			"ada3"		| 5
			"ada4"		| 6
			null		| 6
	}

	@Unroll
	def "Two instances of IncorrectSamplingSize with the same metricName:#metricName and samplingSize:#samplingSize must be equals()"(){
		println(">>>>> IncorrectSamplingSizeSpec >>>> Two instances of IncorrectSamplingSize with the same metricName:$metricName and samplingSize:$samplingSize must be equals()")

		given:
			def exception1=new IncorrectSamplingSize(metricName,samplingSize)
			def exception2=new IncorrectSamplingSize(metricName,samplingSize)
			
		expect:
			exception1.equals(exception2)
			
		where:
			metricName	| samplingSize
			"ada"		| 2
			"ada1"		| 3
			"ada2"		| 0
			"ada3"		| 5
			"ada4"		| 6
			null		| 6
	}
}

