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

package org.bytemechanics.metrics.crawler.impl

import spock.lang.Specification
import spock.lang.Unroll
import java.util.logging.*
import java.util.stream.*
import java.time.Duration
import java.text.NumberFormat
import java.time.LocalDateTime
import org.bytemechanics.metrics.crawler.internal.*

/**
 *
 * @author afarre
 */
class DefaultMetricsServiceImplSpec extends Specification{
	
	def setupSpec(){
		println(">>>>> DefaultMetricsServiceImplSpec >>>> setupSpec")
		final InputStream inputStream = DefaultMetricsServiceImplSpec.class.getResourceAsStream("/logging.properties");
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
	
	def "Create DefaultMetricsServiceImpl without parameters should instance a metrics service with #samplingSize sampling size"(){
		println(">>>>> DefaultMetricsServiceImplSpec >>>> Create DefaultMetricsServiceImpl without parameters should instance a metrics service with $samplingSize sampling size")

		when:
			def metricsService=new DefaultMetricsServiceImpl()
			
		then:
			metricsService.getSamplingSize()==samplingSize
			
		where:
			samplingSize = DefaultMetricsServiceImpl.DEFAULT_SAMPLING_SIZE
	}	

	@Unroll
	def "Create DefaultMetricsServiceImpl with parameters sampling size #samplingSize should create the instance with the sampling size provided"(){
		println(">>>>> DefaultMetricsServiceImplSpec >>>> Create DefaultMetricsServiceImpl with parameters sampling size $samplingSize should create the instance with the sampling size provided")

		when:
			def metricsService=new DefaultMetricsServiceImpl(samplingSize)
			
		then:
			metricsService.getSamplingSize()==samplingSize
			
		where:
			samplingSize << [1,2,3,4]
	}	

	@Unroll
	def "Retrieve a #type metric with a numberOfMeasures (#numberOfMeasures) and sampling size (#samplingSize) should return the reduced snapshot with #accumulated accumulated"(){
		println(">>>>> DefaultMetricsServiceImplSpec >>>> Retrieve a #type metric with a numberOfMeasures (#numberOfMeasures) below sampling size (#samplingSize) should return the reduced snapshot with #accumulated accumulated")

		setup:
			def metricsService=new DefaultMetricsServiceImpl(samplingSize)
			metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),1.0d,MeasureReducers.DOUBLE.get(),1)
			metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),5.0d,MeasureReducers.DOUBLE.get(),3)
		
		when:
			for(def measure:measures){
				metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),measure,type.get(),2)
			}
			def optional=metricsService.getMetric("myMeasure{}",2)
			
		then:
			optional.isPresent()
			optional.get().getAccumulatedSamples()==accumulated
			
		where:
			type						| measures														| samplingSize	| accumulated
			MeasureReducers.DOUBLE		| [2.0d]														| 1				| 2.0d
			MeasureReducers.DOUBLE		| [2.0d,3.0d]													| 2				| 5.0d
			MeasureReducers.DOUBLE		| [2.0d,3.0d,4.0d]												| 3				| 9.0d
			MeasureReducers.DOUBLE		| [2.0d,3.0d,4.0d]												| 1				| 4.0d
			MeasureReducers.DOUBLE		| [2.0d,3.0d,4.0d]												| 2				| 7.0d
			MeasureReducers.LONG		| [2l]															| 1				| 2l
			MeasureReducers.LONG		| [2l,3l]														| 2				| 5l
			MeasureReducers.LONG		| [2l,3l,4l]													| 3				| 9l
			MeasureReducers.LONG		| [2l,3l,4l]													| 1				| 4l
			MeasureReducers.LONG		| [2l,3l,4l]													| 2				| 7l
			MeasureReducers.DURATION	| [Duration.ofDays(2)]											| 1				| Duration.ofDays(2)
			MeasureReducers.DURATION	| [Duration.ofDays(2),Duration.ofDays(3)]						| 2				| Duration.ofDays(5)
			MeasureReducers.DURATION	| [Duration.ofDays(2),Duration.ofDays(3),Duration.ofDays(4)]	| 3				| Duration.ofDays(9)
			MeasureReducers.DURATION	| [Duration.ofDays(2),Duration.ofDays(3),Duration.ofDays(4)]	| 1				| Duration.ofDays(4)
			MeasureReducers.DURATION	| [Duration.ofDays(2),Duration.ofDays(3),Duration.ofDays(4)]	| 2				| Duration.ofDays(7)

			numberOfMeasures=measures.size()
	}	

	def "Retrieve an unnexistent metric should return an empty optional"(){
		println(">>>>> DefaultMetricsServiceImplSpec >>>> Retrieve an unnexistent metric should return an empty optional")

		setup:
			def metricsService=new DefaultMetricsServiceImpl()
			metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),22.0d,MeasureReducers.DOUBLE.get(),1)
		
		when:
			def optional=metricsService.getMetric("myMeasure{}",2)
			
		then:
			!optional.isPresent()
	}	

	def "Retrieve all metrics should return a list of metricSnapshot ordered by name"(){
		println(">>>>> DefaultMetricsServiceImplSpec >>>> Retrieve all metrics should return a list of metricSnapshot ordered by name")

		setup:
			def metricsService=new DefaultMetricsServiceImpl(4)
			metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),1.0d,MeasureReducers.DOUBLE.get(),1)
			metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),1l,MeasureReducers.LONG.get(),3)
			metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),1.0d,MeasureReducers.DOUBLE.get(),1)
			metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),1.0d,MeasureReducers.DOUBLE.get(),1)
			metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),1.0d,MeasureReducers.DOUBLE.get(),1)
			metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),1.0d,MeasureReducers.DOUBLE.get(),1)
			metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),Duration.ofDays(1),MeasureReducers.DURATION.get(),"02")
			metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),Duration.ofDays(1),MeasureReducers.DURATION.get(),"02")
			metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),Duration.ofDays(1),MeasureReducers.DURATION.get(),"02")
		
		when:
			def actualList=metricsService.getMetrics()
		
		then:
			actualList.size()==3
			actualList.get(0).getName()=="myMeasure02"
			actualList.get(0).getAccumulatedSamples()==Duration.ofDays(3)
			actualList.get(1).getName()=="myMeasure1"
			actualList.get(1).getAccumulatedSamples()==4.0d
			actualList.get(2).getName()=="myMeasure3"
			actualList.get(2).getAccumulatedSamples()==1l
	}	
}

