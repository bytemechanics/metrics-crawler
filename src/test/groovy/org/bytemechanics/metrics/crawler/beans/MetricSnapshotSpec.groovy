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

package org.bytemechanics.metrics.crawler.beans


import spock.lang.Specification
import spock.lang.Unroll
import java.util.Optional
import java.util.logging.*
import java.util.stream.*
import spock.lang.Shared
import org.bytemechanics.metrics.crawler.internal.*
import java.time.Duration
import java.time.LocalDateTime
import org.bytemechanics.metrics.crawler.internal.commons.string.SimpleFormat

/**
 *
 * @author afarre
 */
class MetricSnapshotSpec extends Specification{
	
	def setupSpec(){
		println(">>>>> MetricSnapshotSpec >>>> setupSpec")
		final InputStream inputStream = MetricSnapshotSpec.class.getResourceAsStream("/logging.properties");
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
	def "When MetricSnapshot is created with measureReducer:#measureReducer,name:#name,accumulatedSamples:#accumulatedSamples,samplingSize:#samplingSize,totalHits:#totalHits,maxMeasure:#maxMeasure,minMeasure:#minMeasure,averageMeasure:#averageMeasure,lastMeasure:#lastMeasure,lastOccurrence:#lastOccurrence then getName() should return #name"(){
		println(">>>>> MetricSnapshotSpec >>>> When MetricSnapshot is created with measureReducer:$measureReducer,name:$name,accumulatedSamples:$accumulatedSamples,samplingSize:$samplingSize,totalHits:$totalHits,maxMeasure:$maxMeasure,minMeasure:$minMeasure,averageMeasure:$averageMeasure,lastMeasure:$lastMeasure,lastOccurrence:$lastOccurrence then getName() should return $name")

		given:
			def metric=MetricSnapshot.builder(measureReducer)
											.name(name)
											.accumulatedSamples(accumulatedSamples)
											.samplingSize(samplingSize)
											.totalHits(totalHits)
											.maxMeasure(maxMeasure)
											.minMeasure(minMeasure)
											.averageMeasure(averageMeasure)
											.lastMeasure(lastMeasure)
											.lastOccurrence(lastOccurrence)
										.build()
			
		expect:
			metric.getName()==name
			
		where:
			measureReducerName	| name		| accumulatedSamples		 | samplingSize	| totalHits		| maxMeasure				| minMeasure				| averageMeasure			| lastMeasure				| lastOccurrence
			"DURATION"			| null		| Duration.ofSeconds(100000) | 8l			| 17l			| Duration.ofSeconds(10000)	| Duration.ofSeconds(1000)	| Duration.ofSeconds(100)	| Duration.ofSeconds(10)	| LocalDateTime.now()			
			"DURATION"			| "name1"	| null						 | 9l			| 18l			| Duration.ofSeconds(20000)	| Duration.ofSeconds(2000)	| Duration.ofSeconds(200)	| Duration.ofSeconds(20)	| LocalDateTime.now()			
			"DURATION"			| "name2"	| Duration.ofSeconds(200000) | 10l			| 19l			| Duration.ofSeconds(30000)	| Duration.ofSeconds(3000)	| Duration.ofSeconds(300)	| Duration.ofSeconds(30)	| LocalDateTime.now()			
			"DURATION"			| "name3"	| Duration.ofSeconds(300000) | 11l			| 110l			| Duration.ofSeconds(40000)	| Duration.ofSeconds(4000)	| Duration.ofSeconds(400)	| Duration.ofSeconds(40)	| LocalDateTime.now()			
			"DURATION"			| "name4"	| Duration.ofSeconds(400000) | 12l			| 111l			| null						| Duration.ofSeconds(5000)	| Duration.ofSeconds(500)	| Duration.ofSeconds(50)	| LocalDateTime.now()			
			"DURATION"			| "name5"	| Duration.ofSeconds(500000) | 13l			| 112l			| Duration.ofSeconds(50000)	| null						| Duration.ofSeconds(600)	| Duration.ofSeconds(60)	| LocalDateTime.now()			
			"DURATION"			| "name6"	| Duration.ofSeconds(600000) | 14l			| 113l			| Duration.ofSeconds(60000)	| Duration.ofSeconds(6000)	| null						| Duration.ofSeconds(80)	| LocalDateTime.now()			
			"DURATION"			| "name7"	| Duration.ofSeconds(700000) | 15l			| 114l			| Duration.ofSeconds(70000)	| Duration.ofSeconds(7000)	| Duration.ofSeconds(700)	| null						| LocalDateTime.now()			
			"DURATION"			| "name8"	| Duration.ofSeconds(800000) | 16l			| 115l			| Duration.ofSeconds(80000)	| Duration.ofSeconds(8000)	| Duration.ofSeconds(800)	| Duration.ofSeconds(70)	| null			
			"DURATION"			| "name9"	| Duration.ofSeconds(900000) | 17l			| 116l			| Duration.ofSeconds(90000)	| Duration.ofSeconds(9000)	| Duration.ofSeconds(900)	| Duration.ofSeconds(90)	| LocalDateTime.now()			
			"LONG"				| null		| 100000l					 | 18l			| 117l			| 10000l					| 1000l						| 100l						| 10l						| LocalDateTime.now()			
			"LONG"				| "name11"	| null						 | 19l			| 118l			| 20000l					| 2000l						| 200l						| 20l						| LocalDateTime.now()			
			"LONG"				| "name2l"	| 200000l					 | 20l			| 119l			| 30000l					| 3000l						| 300l						| 30l						| LocalDateTime.now()			
			"LONG"				| "name3l"	| 300000l					 | 21l			| 120l			| 40000l					| 4000l						| 400l						| 40l						| LocalDateTime.now()			
			"LONG"				| "name4l"	| 400000l					 | 22l			| 121l			| null						| 5000l						| 500l						| 50l						| LocalDateTime.now()			
			"LONG"				| "name5l"	| 500000l					 | 23l			| 122l			| 50000l					| null						| 600l						| 60l						| LocalDateTime.now()			
			"LONG"				| "name6l"	| 500000l					 | 24l			| 123l			| 60000l					| 6000l						| null						| 70l						| LocalDateTime.now()			
			"LONG"				| "name7l"	| 700000l					 | 25l			| 124l			| 70000l					| 7000l						| 700l						| null						| LocalDateTime.now()			
			"LONG"				| "name8l"	| 800000l					 | 26l			| 125l			| 80000l					| 8000l						| 800l						| 80l						| LocalDateTime.now()
			"LONG"				| "name9l"	| 900000l					 | 27l			| 126l			| 90000l					| 9000l						| 900l						| 90l						| LocalDateTime.now()			
			"DOUBLE"			| null		| 100000.0d					 | 21l			| 127l			| 10000.0d					| 1000.0d					| 100.0d					| 10.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name1d"	| null						 | 21l			| 128l			| 20000.0d					| 2000.0d					| 200.0d					| 20.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name2d"	| 200000.0d					 | 30l			| 129l			| 30000.0d					| 3000.0d					| 300.0d					| 30.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name3d"	| 300000.0d					 | 31l			| 130l			| 40000.0d					| 4000.0d					| 400.0d					| 40.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name4d"	| 400000.0d					 | 32l			| 131l			| null						| 5000.0d					| 500.0d					| 50.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name5d"	| 500000.0d					 | 33l			| 132l			| 50000.0d					| null						| 600.0d					| 60.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name6d"	| 600000.0d					 | 34l			| 133l			| 60000.0d					| 6000.0d					| null						| 70.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name7d"	| 700000.0d					 | 35l			| 134l			| 70000.0d					| 7000.0d					| 700.0d					| null						| LocalDateTime.now()			
			"DOUBLE"			| "name8d"	| 800000.0d					 | 36l			| 135l			| 80000.0d					| 8000.0d					| 800.0d					| 80.0d						| null			
			"DOUBLE"			| "name9d"	| 900000.0d					 | 37l			| 136l			| 90000.0d					| 9000.0d					| 900.0d					| 90.0d						| LocalDateTime.now()			

			measureReducer=MeasureReducers.valueOf(measureReducerName).get()
	}

	@Unroll
	def "When MetricSnapshot is created with measureReducer:#measureReducer,name:#name,accumulatedSamples:#accumulatedSamples,samplingSize:#samplingSize,totalHits:#totalHits,maxMeasure:#maxMeasure,minMeasure:#minMeasure,averageMeasure:#averageMeasure,lastMeasure:#lastMeasure,lastOccurrence:#lastOccurrence then getAccumulatedSamples() should return #accumulatedSamples"(){
		println(">>>>> MetricSnapshotSpec >>>> When MetricSnapshot is created with measureReducer:$measureReducer,name:$name,accumulatedSamples:$accumulatedSamples,samplingSize:$samplingSize,totalHits:$totalHits,maxMeasure:$maxMeasure,minMeasure:$minMeasure,averageMeasure:$averageMeasure,lastMeasure:$lastMeasure,lastOccurrence:$lastOccurrence then getAccumulatedSamples() should return $accumulatedSamples")

		given:
			def metric=MetricSnapshot.builder(measureReducer)
											.name(name)
											.accumulatedSamples(accumulatedSamples)
											.samplingSize(samplingSize)
											.totalHits(totalHits)
											.maxMeasure(maxMeasure)
											.minMeasure(minMeasure)
											.averageMeasure(averageMeasure)
											.lastMeasure(lastMeasure)
											.lastOccurrence(lastOccurrence)
										.build()
			
		expect:
			metric.getAccumulatedSamples()==accumulatedSamples
			
		where:
			measureReducerName	| name		| accumulatedSamples		 | samplingSize	| totalHits		| maxMeasure				| minMeasure				| averageMeasure			| lastMeasure				| lastOccurrence
			"DURATION"			| null		| Duration.ofSeconds(100000) | 8l			| 17l			| Duration.ofSeconds(10000)	| Duration.ofSeconds(1000)	| Duration.ofSeconds(100)	| Duration.ofSeconds(10)	| LocalDateTime.now()			
			"DURATION"			| "name1"	| null						 | 9l			| 18l			| Duration.ofSeconds(20000)	| Duration.ofSeconds(2000)	| Duration.ofSeconds(200)	| Duration.ofSeconds(20)	| LocalDateTime.now()			
			"DURATION"			| "name2"	| Duration.ofSeconds(200000) | 10l			| 19l			| Duration.ofSeconds(30000)	| Duration.ofSeconds(3000)	| Duration.ofSeconds(300)	| Duration.ofSeconds(30)	| LocalDateTime.now()			
			"DURATION"			| "name3"	| Duration.ofSeconds(300000) | 11l			| 110l			| Duration.ofSeconds(40000)	| Duration.ofSeconds(4000)	| Duration.ofSeconds(400)	| Duration.ofSeconds(40)	| LocalDateTime.now()			
			"DURATION"			| "name4"	| Duration.ofSeconds(400000) | 12l			| 111l			| null						| Duration.ofSeconds(5000)	| Duration.ofSeconds(500)	| Duration.ofSeconds(50)	| LocalDateTime.now()			
			"DURATION"			| "name5"	| Duration.ofSeconds(500000) | 13l			| 112l			| Duration.ofSeconds(50000)	| null						| Duration.ofSeconds(600)	| Duration.ofSeconds(60)	| LocalDateTime.now()			
			"DURATION"			| "name6"	| Duration.ofSeconds(600000) | 14l			| 113l			| Duration.ofSeconds(60000)	| Duration.ofSeconds(6000)	| null						| Duration.ofSeconds(80)	| LocalDateTime.now()			
			"DURATION"			| "name7"	| Duration.ofSeconds(700000) | 15l			| 114l			| Duration.ofSeconds(70000)	| Duration.ofSeconds(7000)	| Duration.ofSeconds(700)	| null						| LocalDateTime.now()			
			"DURATION"			| "name8"	| Duration.ofSeconds(800000) | 16l			| 115l			| Duration.ofSeconds(80000)	| Duration.ofSeconds(8000)	| Duration.ofSeconds(800)	| Duration.ofSeconds(70)	| null			
			"DURATION"			| "name9"	| Duration.ofSeconds(900000) | 17l			| 116l			| Duration.ofSeconds(90000)	| Duration.ofSeconds(9000)	| Duration.ofSeconds(900)	| Duration.ofSeconds(90)	| LocalDateTime.now()			
			"LONG"				| null		| 100000l					 | 18l			| 117l			| 10000l					| 1000l						| 100l						| 10l						| LocalDateTime.now()			
			"LONG"				| "name11"	| null						 | 19l			| 118l			| 20000l					| 2000l						| 200l						| 20l						| LocalDateTime.now()			
			"LONG"				| "name2l"	| 200000l					 | 20l			| 119l			| 30000l					| 3000l						| 300l						| 30l						| LocalDateTime.now()			
			"LONG"				| "name3l"	| 300000l					 | 21l			| 120l			| 40000l					| 4000l						| 400l						| 40l						| LocalDateTime.now()			
			"LONG"				| "name4l"	| 400000l					 | 22l			| 121l			| null						| 5000l						| 500l						| 50l						| LocalDateTime.now()			
			"LONG"				| "name5l"	| 500000l					 | 23l			| 122l			| 50000l					| null						| 600l						| 60l						| LocalDateTime.now()			
			"LONG"				| "name6l"	| 500000l					 | 24l			| 123l			| 60000l					| 6000l						| null						| 70l						| LocalDateTime.now()			
			"LONG"				| "name7l"	| 700000l					 | 25l			| 124l			| 70000l					| 7000l						| 700l						| null						| LocalDateTime.now()			
			"LONG"				| "name8l"	| 800000l					 | 26l			| 125l			| 80000l					| 8000l						| 800l						| 80l						| LocalDateTime.now()
			"LONG"				| "name9l"	| 900000l					 | 27l			| 126l			| 90000l					| 9000l						| 900l						| 90l						| LocalDateTime.now()			
			"DOUBLE"			| null		| 100000.0d					 | 21l			| 127l			| 10000.0d					| 1000.0d					| 100.0d					| 10.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name1d"	| null						 | 21l			| 128l			| 20000.0d					| 2000.0d					| 200.0d					| 20.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name2d"	| 200000.0d					 | 30l			| 129l			| 30000.0d					| 3000.0d					| 300.0d					| 30.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name3d"	| 300000.0d					 | 31l			| 130l			| 40000.0d					| 4000.0d					| 400.0d					| 40.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name4d"	| 400000.0d					 | 32l			| 131l			| null						| 5000.0d					| 500.0d					| 50.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name5d"	| 500000.0d					 | 33l			| 132l			| 50000.0d					| null						| 600.0d					| 60.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name6d"	| 600000.0d					 | 34l			| 133l			| 60000.0d					| 6000.0d					| null						| 70.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name7d"	| 700000.0d					 | 35l			| 134l			| 70000.0d					| 7000.0d					| 700.0d					| null						| LocalDateTime.now()			
			"DOUBLE"			| "name8d"	| 800000.0d					 | 36l			| 135l			| 80000.0d					| 8000.0d					| 800.0d					| 80.0d						| null			
			"DOUBLE"			| "name9d"	| 900000.0d					 | 37l			| 136l			| 90000.0d					| 9000.0d					| 900.0d					| 90.0d						| LocalDateTime.now()			

			measureReducer=MeasureReducers.valueOf(measureReducerName).get()
	}
	@Unroll
	def "When MetricSnapshot is created with measureReducer:#measureReducer,name:#name,accumulatedSamples:#accumulatedSamples,samplingSize:#samplingSize,totalHits:#totalHits,maxMeasure:#maxMeasure,minMeasure:#minMeasure,averageMeasure:#averageMeasure,lastMeasure:#lastMeasure,lastOccurrence:#lastOccurrence then getFormatedAccumulatedSamples() should return #expected"(){
		println(">>>>> MetricSnapshotSpec >>>> When MetricSnapshot is created with measureReducer:$measureReducer,name:$name,accumulatedSamples:$accumulatedSamples,samplingSize:$samplingSize,totalHits:$totalHits,maxMeasure:$maxMeasure,minMeasure:$minMeasure,averageMeasure:$averageMeasure,lastMeasure:$lastMeasure,lastOccurrence:$lastOccurrence then getFormatedAccumulatedSamples() should return $expected")

		given:
			def metric=MetricSnapshot.builder(measureReducer)
											.name(name)
											.accumulatedSamples(accumulatedSamples)
											.samplingSize(samplingSize)
											.totalHits(totalHits)
											.maxMeasure(maxMeasure)
											.minMeasure(minMeasure)
											.averageMeasure(averageMeasure)
											.lastMeasure(lastMeasure)
											.lastOccurrence(lastOccurrence)
										.build()
			
		expect:
			metric.getFormatedAccumulatedSamples()==expected
			
		where:
			measureReducerName	| name		| accumulatedSamples		 | samplingSize	| totalHits		| maxMeasure				| minMeasure				| averageMeasure			| lastMeasure				| lastOccurrence
			"DURATION"			| null		| Duration.ofSeconds(100000) | 8l			| 17l			| Duration.ofSeconds(10000)	| Duration.ofSeconds(1000)	| Duration.ofSeconds(100)	| Duration.ofSeconds(10)	| LocalDateTime.now()			
			"DURATION"			| "name1"	| null						 | 9l			| 18l			| Duration.ofSeconds(20000)	| Duration.ofSeconds(2000)	| Duration.ofSeconds(200)	| Duration.ofSeconds(20)	| LocalDateTime.now()			
			"DURATION"			| "name2"	| Duration.ofSeconds(200000) | 10l			| 19l			| Duration.ofSeconds(30000)	| Duration.ofSeconds(3000)	| Duration.ofSeconds(300)	| Duration.ofSeconds(30)	| LocalDateTime.now()			
			"DURATION"			| "name3"	| Duration.ofSeconds(300000) | 11l			| 110l			| Duration.ofSeconds(40000)	| Duration.ofSeconds(4000)	| Duration.ofSeconds(400)	| Duration.ofSeconds(40)	| LocalDateTime.now()			
			"DURATION"			| "name4"	| Duration.ofSeconds(400000) | 12l			| 111l			| null						| Duration.ofSeconds(5000)	| Duration.ofSeconds(500)	| Duration.ofSeconds(50)	| LocalDateTime.now()			
			"DURATION"			| "name5"	| Duration.ofSeconds(500000) | 13l			| 112l			| Duration.ofSeconds(50000)	| null						| Duration.ofSeconds(600)	| Duration.ofSeconds(60)	| LocalDateTime.now()			
			"DURATION"			| "name6"	| Duration.ofSeconds(600000) | 14l			| 113l			| Duration.ofSeconds(60000)	| Duration.ofSeconds(6000)	| null						| Duration.ofSeconds(80)	| LocalDateTime.now()			
			"DURATION"			| "name7"	| Duration.ofSeconds(700000) | 15l			| 114l			| Duration.ofSeconds(70000)	| Duration.ofSeconds(7000)	| Duration.ofSeconds(700)	| null						| LocalDateTime.now()			
			"DURATION"			| "name8"	| Duration.ofSeconds(800000) | 16l			| 115l			| Duration.ofSeconds(80000)	| Duration.ofSeconds(8000)	| Duration.ofSeconds(800)	| Duration.ofSeconds(70)	| null			
			"DURATION"			| "name9"	| Duration.ofSeconds(900000) | 17l			| 116l			| Duration.ofSeconds(90000)	| Duration.ofSeconds(9000)	| Duration.ofSeconds(900)	| Duration.ofSeconds(90)	| LocalDateTime.now()			
			"LONG"				| null		| 100000l					 | 18l			| 117l			| 10000l					| 1000l						| 100l						| 10l						| LocalDateTime.now()			
			"LONG"				| "name11"	| null						 | 19l			| 118l			| 20000l					| 2000l						| 200l						| 20l						| LocalDateTime.now()			
			"LONG"				| "name2l"	| 200000l					 | 20l			| 119l			| 30000l					| 3000l						| 300l						| 30l						| LocalDateTime.now()			
			"LONG"				| "name3l"	| 300000l					 | 21l			| 120l			| 40000l					| 4000l						| 400l						| 40l						| LocalDateTime.now()			
			"LONG"				| "name4l"	| 400000l					 | 22l			| 121l			| null						| 5000l						| 500l						| 50l						| LocalDateTime.now()			
			"LONG"				| "name5l"	| 500000l					 | 23l			| 122l			| 50000l					| null						| 600l						| 60l						| LocalDateTime.now()			
			"LONG"				| "name6l"	| 500000l					 | 24l			| 123l			| 60000l					| 6000l						| null						| 70l						| LocalDateTime.now()			
			"LONG"				| "name7l"	| 700000l					 | 25l			| 124l			| 70000l					| 7000l						| 700l						| null						| LocalDateTime.now()			
			"LONG"				| "name8l"	| 800000l					 | 26l			| 125l			| 80000l					| 8000l						| 800l						| 80l						| LocalDateTime.now()
			"LONG"				| "name9l"	| 900000l					 | 27l			| 126l			| 90000l					| 9000l						| 900l						| 90l						| LocalDateTime.now()			
			"DOUBLE"			| null		| 100000.0d					 | 21l			| 127l			| 10000.0d					| 1000.0d					| 100.0d					| 10.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name1d"	| null						 | 21l			| 128l			| 20000.0d					| 2000.0d					| 200.0d					| 20.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name2d"	| 200000.0d					 | 30l			| 129l			| 30000.0d					| 3000.0d					| 300.0d					| 30.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name3d"	| 300000.0d					 | 31l			| 130l			| 40000.0d					| 4000.0d					| 400.0d					| 40.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name4d"	| 400000.0d					 | 32l			| 131l			| null						| 5000.0d					| 500.0d					| 50.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name5d"	| 500000.0d					 | 33l			| 132l			| 50000.0d					| null						| 600.0d					| 60.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name6d"	| 600000.0d					 | 34l			| 133l			| 60000.0d					| 6000.0d					| null						| 70.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name7d"	| 700000.0d					 | 35l			| 134l			| 70000.0d					| 7000.0d					| 700.0d					| null						| LocalDateTime.now()			
			"DOUBLE"			| "name8d"	| 800000.0d					 | 36l			| 135l			| 80000.0d					| 8000.0d					| 800.0d					| 80.0d						| null			
			"DOUBLE"			| "name9d"	| 900000.0d					 | 37l			| 136l			| 90000.0d					| 9000.0d					| 900.0d					| 90.0d						| LocalDateTime.now()			

			measureReducer=MeasureReducers.valueOf(measureReducerName).get()
			expected=measureReducer.toString(accumulatedSamples)
	}
		
	@Unroll
	def "When MetricSnapshot is created with measureReducer:#measureReducer,name:#name,accumulatedSamples:#accumulatedSamples,samplingSize:#samplingSize,totalHits:#totalHits,maxMeasure:#maxMeasure,minMeasure:#minMeasure,averageMeasure:#averageMeasure,lastMeasure:#lastMeasure,lastOccurrence:#lastOccurrence then getSamplingSize() should return #samplingSize"(){
		println(">>>>> MetricSnapshotSpec >>>> When MetricSnapshot is created with measureReducer:$measureReducer,name:$name,accumulatedSamples:$accumulatedSamples,samplingSize:$samplingSize,totalHits:$totalHits,maxMeasure:$maxMeasure,minMeasure:$minMeasure,averageMeasure:$averageMeasure,lastMeasure:$lastMeasure,lastOccurrence:$lastOccurrence then getSamplingSize() should return $samplingSize")

		given:
			def metric=MetricSnapshot.builder(measureReducer)
											.name(name)
											.accumulatedSamples(accumulatedSamples)
											.samplingSize(samplingSize)
											.totalHits(totalHits)
											.maxMeasure(maxMeasure)
											.minMeasure(minMeasure)
											.averageMeasure(averageMeasure)
											.lastMeasure(lastMeasure)
											.lastOccurrence(lastOccurrence)
										.build()
			
		expect:
			metric.getSamplingSize()==samplingSize
			
		where:
			measureReducerName	| name		| accumulatedSamples		 | samplingSize	| totalHits		| maxMeasure				| minMeasure				| averageMeasure			| lastMeasure				| lastOccurrence
			"DURATION"			| null		| Duration.ofSeconds(100000) | 8l			| 17l			| Duration.ofSeconds(10000)	| Duration.ofSeconds(1000)	| Duration.ofSeconds(100)	| Duration.ofSeconds(10)	| LocalDateTime.now()			
			"DURATION"			| "name1"	| null						 | 9l			| 18l			| Duration.ofSeconds(20000)	| Duration.ofSeconds(2000)	| Duration.ofSeconds(200)	| Duration.ofSeconds(20)	| LocalDateTime.now()			
			"DURATION"			| "name2"	| Duration.ofSeconds(200000) | 10l			| 19l			| Duration.ofSeconds(30000)	| Duration.ofSeconds(3000)	| Duration.ofSeconds(300)	| Duration.ofSeconds(30)	| LocalDateTime.now()			
			"DURATION"			| "name3"	| Duration.ofSeconds(300000) | 11l			| 110l			| Duration.ofSeconds(40000)	| Duration.ofSeconds(4000)	| Duration.ofSeconds(400)	| Duration.ofSeconds(40)	| LocalDateTime.now()			
			"DURATION"			| "name4"	| Duration.ofSeconds(400000) | 12l			| 111l			| null						| Duration.ofSeconds(5000)	| Duration.ofSeconds(500)	| Duration.ofSeconds(50)	| LocalDateTime.now()			
			"DURATION"			| "name5"	| Duration.ofSeconds(500000) | 13l			| 112l			| Duration.ofSeconds(50000)	| null						| Duration.ofSeconds(600)	| Duration.ofSeconds(60)	| LocalDateTime.now()			
			"DURATION"			| "name6"	| Duration.ofSeconds(600000) | 14l			| 113l			| Duration.ofSeconds(60000)	| Duration.ofSeconds(6000)	| null						| Duration.ofSeconds(80)	| LocalDateTime.now()			
			"DURATION"			| "name7"	| Duration.ofSeconds(700000) | 15l			| 114l			| Duration.ofSeconds(70000)	| Duration.ofSeconds(7000)	| Duration.ofSeconds(700)	| null						| LocalDateTime.now()			
			"DURATION"			| "name8"	| Duration.ofSeconds(800000) | 16l			| 115l			| Duration.ofSeconds(80000)	| Duration.ofSeconds(8000)	| Duration.ofSeconds(800)	| Duration.ofSeconds(70)	| null			
			"DURATION"			| "name9"	| Duration.ofSeconds(900000) | 17l			| 116l			| Duration.ofSeconds(90000)	| Duration.ofSeconds(9000)	| Duration.ofSeconds(900)	| Duration.ofSeconds(90)	| LocalDateTime.now()			
			"LONG"				| null		| 100000l					 | 18l			| 117l			| 10000l					| 1000l						| 100l						| 10l						| LocalDateTime.now()			
			"LONG"				| "name11"	| null						 | 19l			| 118l			| 20000l					| 2000l						| 200l						| 20l						| LocalDateTime.now()			
			"LONG"				| "name2l"	| 200000l					 | 20l			| 119l			| 30000l					| 3000l						| 300l						| 30l						| LocalDateTime.now()			
			"LONG"				| "name3l"	| 300000l					 | 21l			| 120l			| 40000l					| 4000l						| 400l						| 40l						| LocalDateTime.now()			
			"LONG"				| "name4l"	| 400000l					 | 22l			| 121l			| null						| 5000l						| 500l						| 50l						| LocalDateTime.now()			
			"LONG"				| "name5l"	| 500000l					 | 23l			| 122l			| 50000l					| null						| 600l						| 60l						| LocalDateTime.now()			
			"LONG"				| "name6l"	| 500000l					 | 24l			| 123l			| 60000l					| 6000l						| null						| 70l						| LocalDateTime.now()			
			"LONG"				| "name7l"	| 700000l					 | 25l			| 124l			| 70000l					| 7000l						| 700l						| null						| LocalDateTime.now()			
			"LONG"				| "name8l"	| 800000l					 | 26l			| 125l			| 80000l					| 8000l						| 800l						| 80l						| LocalDateTime.now()
			"LONG"				| "name9l"	| 900000l					 | 27l			| 126l			| 90000l					| 9000l						| 900l						| 90l						| LocalDateTime.now()			
			"DOUBLE"			| null		| 100000.0d					 | 21l			| 127l			| 10000.0d					| 1000.0d					| 100.0d					| 10.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name1d"	| null						 | 21l			| 128l			| 20000.0d					| 2000.0d					| 200.0d					| 20.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name2d"	| 200000.0d					 | 30l			| 129l			| 30000.0d					| 3000.0d					| 300.0d					| 30.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name3d"	| 300000.0d					 | 31l			| 130l			| 40000.0d					| 4000.0d					| 400.0d					| 40.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name4d"	| 400000.0d					 | 32l			| 131l			| null						| 5000.0d					| 500.0d					| 50.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name5d"	| 500000.0d					 | 33l			| 132l			| 50000.0d					| null						| 600.0d					| 60.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name6d"	| 600000.0d					 | 34l			| 133l			| 60000.0d					| 6000.0d					| null						| 70.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name7d"	| 700000.0d					 | 35l			| 134l			| 70000.0d					| 7000.0d					| 700.0d					| null						| LocalDateTime.now()			
			"DOUBLE"			| "name8d"	| 800000.0d					 | 36l			| 135l			| 80000.0d					| 8000.0d					| 800.0d					| 80.0d						| null			
			"DOUBLE"			| "name9d"	| 900000.0d					 | 37l			| 136l			| 90000.0d					| 9000.0d					| 900.0d					| 90.0d						| LocalDateTime.now()			

			measureReducer=MeasureReducers.valueOf(measureReducerName).get()
	}
		
	@Unroll
	def "When MetricSnapshot is created with measureReducer:#measureReducer,name:#name,accumulatedSamples:#accumulatedSamples,samplingSize:#samplingSize,totalHits:#totalHits,maxMeasure:#maxMeasure,minMeasure:#minMeasure,averageMeasure:#averageMeasure,lastMeasure:#lastMeasure,lastOccurrence:#lastOccurrence then getTotalHits() should return #totalHits"(){
		println(">>>>> MetricSnapshotSpec >>>> When MetricSnapshot is created with measureReducer:$measureReducer,name:$name,accumulatedSamples:$accumulatedSamples,samplingSize:$samplingSize,totalHits:$totalHits,maxMeasure:$maxMeasure,minMeasure:$minMeasure,averageMeasure:$averageMeasure,lastMeasure:$lastMeasure,lastOccurrence:$lastOccurrence then getTotalHits() should return $totalHits")

		given:
			def metric=MetricSnapshot.builder(measureReducer)
											.name(name)
											.accumulatedSamples(accumulatedSamples)
											.samplingSize(samplingSize)
											.totalHits(totalHits)
											.maxMeasure(maxMeasure)
											.minMeasure(minMeasure)
											.averageMeasure(averageMeasure)
											.lastMeasure(lastMeasure)
											.lastOccurrence(lastOccurrence)
										.build()
			
		expect:
			metric.getTotalHits()==totalHits
			
		where:
			measureReducerName	| name		| accumulatedSamples		 | samplingSize	| totalHits		| maxMeasure				| minMeasure				| averageMeasure			| lastMeasure				| lastOccurrence
			"DURATION"			| null		| Duration.ofSeconds(100000) | 8l			| 17l			| Duration.ofSeconds(10000)	| Duration.ofSeconds(1000)	| Duration.ofSeconds(100)	| Duration.ofSeconds(10)	| LocalDateTime.now()			
			"DURATION"			| "name1"	| null						 | 9l			| 18l			| Duration.ofSeconds(20000)	| Duration.ofSeconds(2000)	| Duration.ofSeconds(200)	| Duration.ofSeconds(20)	| LocalDateTime.now()			
			"DURATION"			| "name2"	| Duration.ofSeconds(200000) | 10l			| 19l			| Duration.ofSeconds(30000)	| Duration.ofSeconds(3000)	| Duration.ofSeconds(300)	| Duration.ofSeconds(30)	| LocalDateTime.now()			
			"DURATION"			| "name3"	| Duration.ofSeconds(300000) | 11l			| 110l			| Duration.ofSeconds(40000)	| Duration.ofSeconds(4000)	| Duration.ofSeconds(400)	| Duration.ofSeconds(40)	| LocalDateTime.now()			
			"DURATION"			| "name4"	| Duration.ofSeconds(400000) | 12l			| 111l			| null						| Duration.ofSeconds(5000)	| Duration.ofSeconds(500)	| Duration.ofSeconds(50)	| LocalDateTime.now()			
			"DURATION"			| "name5"	| Duration.ofSeconds(500000) | 13l			| 112l			| Duration.ofSeconds(50000)	| null						| Duration.ofSeconds(600)	| Duration.ofSeconds(60)	| LocalDateTime.now()			
			"DURATION"			| "name6"	| Duration.ofSeconds(600000) | 14l			| 113l			| Duration.ofSeconds(60000)	| Duration.ofSeconds(6000)	| null						| Duration.ofSeconds(80)	| LocalDateTime.now()			
			"DURATION"			| "name7"	| Duration.ofSeconds(700000) | 15l			| 114l			| Duration.ofSeconds(70000)	| Duration.ofSeconds(7000)	| Duration.ofSeconds(700)	| null						| LocalDateTime.now()			
			"DURATION"			| "name8"	| Duration.ofSeconds(800000) | 16l			| 115l			| Duration.ofSeconds(80000)	| Duration.ofSeconds(8000)	| Duration.ofSeconds(800)	| Duration.ofSeconds(70)	| null			
			"DURATION"			| "name9"	| Duration.ofSeconds(900000) | 17l			| 116l			| Duration.ofSeconds(90000)	| Duration.ofSeconds(9000)	| Duration.ofSeconds(900)	| Duration.ofSeconds(90)	| LocalDateTime.now()			
			"LONG"				| null		| 100000l					 | 18l			| 117l			| 10000l					| 1000l						| 100l						| 10l						| LocalDateTime.now()			
			"LONG"				| "name11"	| null						 | 19l			| 118l			| 20000l					| 2000l						| 200l						| 20l						| LocalDateTime.now()			
			"LONG"				| "name2l"	| 200000l					 | 20l			| 119l			| 30000l					| 3000l						| 300l						| 30l						| LocalDateTime.now()			
			"LONG"				| "name3l"	| 300000l					 | 21l			| 120l			| 40000l					| 4000l						| 400l						| 40l						| LocalDateTime.now()			
			"LONG"				| "name4l"	| 400000l					 | 22l			| 121l			| null						| 5000l						| 500l						| 50l						| LocalDateTime.now()			
			"LONG"				| "name5l"	| 500000l					 | 23l			| 122l			| 50000l					| null						| 600l						| 60l						| LocalDateTime.now()			
			"LONG"				| "name6l"	| 500000l					 | 24l			| 123l			| 60000l					| 6000l						| null						| 70l						| LocalDateTime.now()			
			"LONG"				| "name7l"	| 700000l					 | 25l			| 124l			| 70000l					| 7000l						| 700l						| null						| LocalDateTime.now()			
			"LONG"				| "name8l"	| 800000l					 | 26l			| 125l			| 80000l					| 8000l						| 800l						| 80l						| LocalDateTime.now()
			"LONG"				| "name9l"	| 900000l					 | 27l			| 126l			| 90000l					| 9000l						| 900l						| 90l						| LocalDateTime.now()			
			"DOUBLE"			| null		| 100000.0d					 | 21l			| 127l			| 10000.0d					| 1000.0d					| 100.0d					| 10.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name1d"	| null						 | 21l			| 128l			| 20000.0d					| 2000.0d					| 200.0d					| 20.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name2d"	| 200000.0d					 | 30l			| 129l			| 30000.0d					| 3000.0d					| 300.0d					| 30.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name3d"	| 300000.0d					 | 31l			| 130l			| 40000.0d					| 4000.0d					| 400.0d					| 40.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name4d"	| 400000.0d					 | 32l			| 131l			| null						| 5000.0d					| 500.0d					| 50.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name5d"	| 500000.0d					 | 33l			| 132l			| 50000.0d					| null						| 600.0d					| 60.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name6d"	| 600000.0d					 | 34l			| 133l			| 60000.0d					| 6000.0d					| null						| 70.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name7d"	| 700000.0d					 | 35l			| 134l			| 70000.0d					| 7000.0d					| 700.0d					| null						| LocalDateTime.now()			
			"DOUBLE"			| "name8d"	| 800000.0d					 | 36l			| 135l			| 80000.0d					| 8000.0d					| 800.0d					| 80.0d						| null			
			"DOUBLE"			| "name9d"	| 900000.0d					 | 37l			| 136l			| 90000.0d					| 9000.0d					| 900.0d					| 90.0d						| LocalDateTime.now()			

			measureReducer=MeasureReducers.valueOf(measureReducerName).get()
	}

	@Unroll
	def "When MetricSnapshot is created with measureReducer:#measureReducer,name:#name,accumulatedSamples:#accumulatedSamples,samplingSize:#samplingSize,totalHits:#totalHits,maxMeasure:#maxMeasure,minMeasure:#minMeasure,averageMeasure:#averageMeasure,lastMeasure:#lastMeasure,lastOccurrence:#lastOccurrence then getMaxMeasure() should return #maxMeasure"(){
		println(">>>>> MetricSnapshotSpec >>>> When MetricSnapshot is created with measureReducer:$measureReducer,name:$name,accumulatedSamples:$accumulatedSamples,samplingSize:$samplingSize,totalHits:$totalHits,maxMeasure:$maxMeasure,minMeasure:$minMeasure,averageMeasure:$averageMeasure,lastMeasure:$lastMeasure,lastOccurrence:$lastOccurrence then getMaxMeasure() should return $maxMeasure")

		given:
			def metric=MetricSnapshot.builder(measureReducer)
											.name(name)
											.accumulatedSamples(accumulatedSamples)
											.samplingSize(samplingSize)
											.totalHits(totalHits)
											.maxMeasure(maxMeasure)
											.minMeasure(minMeasure)
											.averageMeasure(averageMeasure)
											.lastMeasure(lastMeasure)
											.lastOccurrence(lastOccurrence)
										.build()
			
		expect:
			metric.getMaxMeasure()==maxMeasure
			
		where:
			measureReducerName	| name		| accumulatedSamples		 | samplingSize	| totalHits		| maxMeasure				| minMeasure				| averageMeasure			| lastMeasure				| lastOccurrence
			"DURATION"			| null		| Duration.ofSeconds(100000) | 8l			| 17l			| Duration.ofSeconds(10000)	| Duration.ofSeconds(1000)	| Duration.ofSeconds(100)	| Duration.ofSeconds(10)	| LocalDateTime.now()			
			"DURATION"			| "name1"	| null						 | 9l			| 18l			| Duration.ofSeconds(20000)	| Duration.ofSeconds(2000)	| Duration.ofSeconds(200)	| Duration.ofSeconds(20)	| LocalDateTime.now()			
			"DURATION"			| "name2"	| Duration.ofSeconds(200000) | 10l			| 19l			| Duration.ofSeconds(30000)	| Duration.ofSeconds(3000)	| Duration.ofSeconds(300)	| Duration.ofSeconds(30)	| LocalDateTime.now()			
			"DURATION"			| "name3"	| Duration.ofSeconds(300000) | 11l			| 110l			| Duration.ofSeconds(40000)	| Duration.ofSeconds(4000)	| Duration.ofSeconds(400)	| Duration.ofSeconds(40)	| LocalDateTime.now()			
			"DURATION"			| "name4"	| Duration.ofSeconds(400000) | 12l			| 111l			| null						| Duration.ofSeconds(5000)	| Duration.ofSeconds(500)	| Duration.ofSeconds(50)	| LocalDateTime.now()			
			"DURATION"			| "name5"	| Duration.ofSeconds(500000) | 13l			| 112l			| Duration.ofSeconds(50000)	| null						| Duration.ofSeconds(600)	| Duration.ofSeconds(60)	| LocalDateTime.now()			
			"DURATION"			| "name6"	| Duration.ofSeconds(600000) | 14l			| 113l			| Duration.ofSeconds(60000)	| Duration.ofSeconds(6000)	| null						| Duration.ofSeconds(80)	| LocalDateTime.now()			
			"DURATION"			| "name7"	| Duration.ofSeconds(700000) | 15l			| 114l			| Duration.ofSeconds(70000)	| Duration.ofSeconds(7000)	| Duration.ofSeconds(700)	| null						| LocalDateTime.now()			
			"DURATION"			| "name8"	| Duration.ofSeconds(800000) | 16l			| 115l			| Duration.ofSeconds(80000)	| Duration.ofSeconds(8000)	| Duration.ofSeconds(800)	| Duration.ofSeconds(70)	| null			
			"DURATION"			| "name9"	| Duration.ofSeconds(900000) | 17l			| 116l			| Duration.ofSeconds(90000)	| Duration.ofSeconds(9000)	| Duration.ofSeconds(900)	| Duration.ofSeconds(90)	| LocalDateTime.now()			
			"LONG"				| null		| 100000l					 | 18l			| 117l			| 10000l					| 1000l						| 100l						| 10l						| LocalDateTime.now()			
			"LONG"				| "name11"	| null						 | 19l			| 118l			| 20000l					| 2000l						| 200l						| 20l						| LocalDateTime.now()			
			"LONG"				| "name2l"	| 200000l					 | 20l			| 119l			| 30000l					| 3000l						| 300l						| 30l						| LocalDateTime.now()			
			"LONG"				| "name3l"	| 300000l					 | 21l			| 120l			| 40000l					| 4000l						| 400l						| 40l						| LocalDateTime.now()			
			"LONG"				| "name4l"	| 400000l					 | 22l			| 121l			| null						| 5000l						| 500l						| 50l						| LocalDateTime.now()			
			"LONG"				| "name5l"	| 500000l					 | 23l			| 122l			| 50000l					| null						| 600l						| 60l						| LocalDateTime.now()			
			"LONG"				| "name6l"	| 500000l					 | 24l			| 123l			| 60000l					| 6000l						| null						| 70l						| LocalDateTime.now()			
			"LONG"				| "name7l"	| 700000l					 | 25l			| 124l			| 70000l					| 7000l						| 700l						| null						| LocalDateTime.now()			
			"LONG"				| "name8l"	| 800000l					 | 26l			| 125l			| 80000l					| 8000l						| 800l						| 80l						| LocalDateTime.now()
			"LONG"				| "name9l"	| 900000l					 | 27l			| 126l			| 90000l					| 9000l						| 900l						| 90l						| LocalDateTime.now()			
			"DOUBLE"			| null		| 100000.0d					 | 21l			| 127l			| 10000.0d					| 1000.0d					| 100.0d					| 10.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name1d"	| null						 | 21l			| 128l			| 20000.0d					| 2000.0d					| 200.0d					| 20.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name2d"	| 200000.0d					 | 30l			| 129l			| 30000.0d					| 3000.0d					| 300.0d					| 30.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name3d"	| 300000.0d					 | 31l			| 130l			| 40000.0d					| 4000.0d					| 400.0d					| 40.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name4d"	| 400000.0d					 | 32l			| 131l			| null						| 5000.0d					| 500.0d					| 50.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name5d"	| 500000.0d					 | 33l			| 132l			| 50000.0d					| null						| 600.0d					| 60.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name6d"	| 600000.0d					 | 34l			| 133l			| 60000.0d					| 6000.0d					| null						| 70.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name7d"	| 700000.0d					 | 35l			| 134l			| 70000.0d					| 7000.0d					| 700.0d					| null						| LocalDateTime.now()			
			"DOUBLE"			| "name8d"	| 800000.0d					 | 36l			| 135l			| 80000.0d					| 8000.0d					| 800.0d					| 80.0d						| null			
			"DOUBLE"			| "name9d"	| 900000.0d					 | 37l			| 136l			| 90000.0d					| 9000.0d					| 900.0d					| 90.0d						| LocalDateTime.now()			

			measureReducer=MeasureReducers.valueOf(measureReducerName).get()
	}
	@Unroll
	def "When MetricSnapshot is created with measureReducer:#measureReducer,name:#name,accumulatedSamples:#accumulatedSamples,samplingSize:#samplingSize,totalHits:#totalHits,maxMeasure:#maxMeasure,minMeasure:#minMeasure,averageMeasure:#averageMeasure,lastMeasure:#lastMeasure,lastOccurrence:#lastOccurrence then getFormatedMaxMeasure() should return #expected"(){
		println(">>>>> MetricSnapshotSpec >>>> When MetricSnapshot is created with measureReducer:$measureReducer,name:$name,accumulatedSamples:$accumulatedSamples,samplingSize:$samplingSize,totalHits:$totalHits,maxMeasure:$maxMeasure,minMeasure:$minMeasure,averageMeasure:$averageMeasure,lastMeasure:$lastMeasure,lastOccurrence:$lastOccurrence then getFormatedMaxMeasure() should return $expected")

		given:
			def metric=MetricSnapshot.builder(measureReducer)
											.name(name)
											.accumulatedSamples(accumulatedSamples)
											.samplingSize(samplingSize)
											.totalHits(totalHits)
											.maxMeasure(maxMeasure)
											.minMeasure(minMeasure)
											.averageMeasure(averageMeasure)
											.lastMeasure(lastMeasure)
											.lastOccurrence(lastOccurrence)
										.build()
			
		expect:
			metric.getFormatedMaxMeasure()==expected
			
		where:
			measureReducerName	| name		| accumulatedSamples		 | samplingSize	| totalHits		| maxMeasure				| minMeasure				| averageMeasure			| lastMeasure				| lastOccurrence
			"DURATION"			| null		| Duration.ofSeconds(100000) | 8l			| 17l			| Duration.ofSeconds(10000)	| Duration.ofSeconds(1000)	| Duration.ofSeconds(100)	| Duration.ofSeconds(10)	| LocalDateTime.now()			
			"DURATION"			| "name1"	| null						 | 9l			| 18l			| Duration.ofSeconds(20000)	| Duration.ofSeconds(2000)	| Duration.ofSeconds(200)	| Duration.ofSeconds(20)	| LocalDateTime.now()			
			"DURATION"			| "name2"	| Duration.ofSeconds(200000) | 10l			| 19l			| Duration.ofSeconds(30000)	| Duration.ofSeconds(3000)	| Duration.ofSeconds(300)	| Duration.ofSeconds(30)	| LocalDateTime.now()			
			"DURATION"			| "name3"	| Duration.ofSeconds(300000) | 11l			| 110l			| Duration.ofSeconds(40000)	| Duration.ofSeconds(4000)	| Duration.ofSeconds(400)	| Duration.ofSeconds(40)	| LocalDateTime.now()			
			"DURATION"			| "name4"	| Duration.ofSeconds(400000) | 12l			| 111l			| null						| Duration.ofSeconds(5000)	| Duration.ofSeconds(500)	| Duration.ofSeconds(50)	| LocalDateTime.now()			
			"DURATION"			| "name5"	| Duration.ofSeconds(500000) | 13l			| 112l			| Duration.ofSeconds(50000)	| null						| Duration.ofSeconds(600)	| Duration.ofSeconds(60)	| LocalDateTime.now()			
			"DURATION"			| "name6"	| Duration.ofSeconds(600000) | 14l			| 113l			| Duration.ofSeconds(60000)	| Duration.ofSeconds(6000)	| null						| Duration.ofSeconds(80)	| LocalDateTime.now()			
			"DURATION"			| "name7"	| Duration.ofSeconds(700000) | 15l			| 114l			| Duration.ofSeconds(70000)	| Duration.ofSeconds(7000)	| Duration.ofSeconds(700)	| null						| LocalDateTime.now()			
			"DURATION"			| "name8"	| Duration.ofSeconds(800000) | 16l			| 115l			| Duration.ofSeconds(80000)	| Duration.ofSeconds(8000)	| Duration.ofSeconds(800)	| Duration.ofSeconds(70)	| null			
			"DURATION"			| "name9"	| Duration.ofSeconds(900000) | 17l			| 116l			| Duration.ofSeconds(90000)	| Duration.ofSeconds(9000)	| Duration.ofSeconds(900)	| Duration.ofSeconds(90)	| LocalDateTime.now()			
			"LONG"				| null		| 100000l					 | 18l			| 117l			| 10000l					| 1000l						| 100l						| 10l						| LocalDateTime.now()			
			"LONG"				| "name11"	| null						 | 19l			| 118l			| 20000l					| 2000l						| 200l						| 20l						| LocalDateTime.now()			
			"LONG"				| "name2l"	| 200000l					 | 20l			| 119l			| 30000l					| 3000l						| 300l						| 30l						| LocalDateTime.now()			
			"LONG"				| "name3l"	| 300000l					 | 21l			| 120l			| 40000l					| 4000l						| 400l						| 40l						| LocalDateTime.now()			
			"LONG"				| "name4l"	| 400000l					 | 22l			| 121l			| null						| 5000l						| 500l						| 50l						| LocalDateTime.now()			
			"LONG"				| "name5l"	| 500000l					 | 23l			| 122l			| 50000l					| null						| 600l						| 60l						| LocalDateTime.now()			
			"LONG"				| "name6l"	| 500000l					 | 24l			| 123l			| 60000l					| 6000l						| null						| 70l						| LocalDateTime.now()			
			"LONG"				| "name7l"	| 700000l					 | 25l			| 124l			| 70000l					| 7000l						| 700l						| null						| LocalDateTime.now()			
			"LONG"				| "name8l"	| 800000l					 | 26l			| 125l			| 80000l					| 8000l						| 800l						| 80l						| LocalDateTime.now()
			"LONG"				| "name9l"	| 900000l					 | 27l			| 126l			| 90000l					| 9000l						| 900l						| 90l						| LocalDateTime.now()			
			"DOUBLE"			| null		| 100000.0d					 | 21l			| 127l			| 10000.0d					| 1000.0d					| 100.0d					| 10.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name1d"	| null						 | 21l			| 128l			| 20000.0d					| 2000.0d					| 200.0d					| 20.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name2d"	| 200000.0d					 | 30l			| 129l			| 30000.0d					| 3000.0d					| 300.0d					| 30.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name3d"	| 300000.0d					 | 31l			| 130l			| 40000.0d					| 4000.0d					| 400.0d					| 40.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name4d"	| 400000.0d					 | 32l			| 131l			| null						| 5000.0d					| 500.0d					| 50.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name5d"	| 500000.0d					 | 33l			| 132l			| 50000.0d					| null						| 600.0d					| 60.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name6d"	| 600000.0d					 | 34l			| 133l			| 60000.0d					| 6000.0d					| null						| 70.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name7d"	| 700000.0d					 | 35l			| 134l			| 70000.0d					| 7000.0d					| 700.0d					| null						| LocalDateTime.now()			
			"DOUBLE"			| "name8d"	| 800000.0d					 | 36l			| 135l			| 80000.0d					| 8000.0d					| 800.0d					| 80.0d						| null			
			"DOUBLE"			| "name9d"	| 900000.0d					 | 37l			| 136l			| 90000.0d					| 9000.0d					| 900.0d					| 90.0d						| LocalDateTime.now()			

			measureReducer=MeasureReducers.valueOf(measureReducerName).get()
			expected=measureReducer.toString(maxMeasure)
	}
		
	@Unroll
	def "When MetricSnapshot is created with measureReducer:#measureReducer,name:#name,accumulatedSamples:#accumulatedSamples,samplingSize:#samplingSize,totalHits:#totalHits,maxMeasure:#maxMeasure,minMeasure:#minMeasure,averageMeasure:#averageMeasure,lastMeasure:#lastMeasure,lastOccurrence:#lastOccurrence then getMinMeasure() should return #minMeasure"(){
		println(">>>>> MetricSnapshotSpec >>>> When MetricSnapshot is created with measureReducer:$measureReducer,name:$name,accumulatedSamples:$accumulatedSamples,samplingSize:$samplingSize,totalHits:$totalHits,maxMeasure:$maxMeasure,minMeasure:$minMeasure,averageMeasure:$averageMeasure,lastMeasure:$lastMeasure,lastOccurrence:$lastOccurrence then getMinMeasure() should return $minMeasure")

		given:
			def metric=MetricSnapshot.builder(measureReducer)
											.name(name)
											.accumulatedSamples(accumulatedSamples)
											.samplingSize(samplingSize)
											.totalHits(totalHits)
											.maxMeasure(maxMeasure)
											.minMeasure(minMeasure)
											.averageMeasure(averageMeasure)
											.lastMeasure(lastMeasure)
											.lastOccurrence(lastOccurrence)
										.build()
			
		expect:
			metric.getMinMeasure()==minMeasure
			
		where:
			measureReducerName	| name		| accumulatedSamples		 | samplingSize	| totalHits		| maxMeasure				| minMeasure				| averageMeasure			| lastMeasure				| lastOccurrence
			"DURATION"			| null		| Duration.ofSeconds(100000) | 8l			| 17l			| Duration.ofSeconds(10000)	| Duration.ofSeconds(1000)	| Duration.ofSeconds(100)	| Duration.ofSeconds(10)	| LocalDateTime.now()			
			"DURATION"			| "name1"	| null						 | 9l			| 18l			| Duration.ofSeconds(20000)	| Duration.ofSeconds(2000)	| Duration.ofSeconds(200)	| Duration.ofSeconds(20)	| LocalDateTime.now()			
			"DURATION"			| "name2"	| Duration.ofSeconds(200000) | 10l			| 19l			| Duration.ofSeconds(30000)	| Duration.ofSeconds(3000)	| Duration.ofSeconds(300)	| Duration.ofSeconds(30)	| LocalDateTime.now()			
			"DURATION"			| "name3"	| Duration.ofSeconds(300000) | 11l			| 110l			| Duration.ofSeconds(40000)	| Duration.ofSeconds(4000)	| Duration.ofSeconds(400)	| Duration.ofSeconds(40)	| LocalDateTime.now()			
			"DURATION"			| "name4"	| Duration.ofSeconds(400000) | 12l			| 111l			| null						| Duration.ofSeconds(5000)	| Duration.ofSeconds(500)	| Duration.ofSeconds(50)	| LocalDateTime.now()			
			"DURATION"			| "name5"	| Duration.ofSeconds(500000) | 13l			| 112l			| Duration.ofSeconds(50000)	| null						| Duration.ofSeconds(600)	| Duration.ofSeconds(60)	| LocalDateTime.now()			
			"DURATION"			| "name6"	| Duration.ofSeconds(600000) | 14l			| 113l			| Duration.ofSeconds(60000)	| Duration.ofSeconds(6000)	| null						| Duration.ofSeconds(80)	| LocalDateTime.now()			
			"DURATION"			| "name7"	| Duration.ofSeconds(700000) | 15l			| 114l			| Duration.ofSeconds(70000)	| Duration.ofSeconds(7000)	| Duration.ofSeconds(700)	| null						| LocalDateTime.now()			
			"DURATION"			| "name8"	| Duration.ofSeconds(800000) | 16l			| 115l			| Duration.ofSeconds(80000)	| Duration.ofSeconds(8000)	| Duration.ofSeconds(800)	| Duration.ofSeconds(70)	| null			
			"DURATION"			| "name9"	| Duration.ofSeconds(900000) | 17l			| 116l			| Duration.ofSeconds(90000)	| Duration.ofSeconds(9000)	| Duration.ofSeconds(900)	| Duration.ofSeconds(90)	| LocalDateTime.now()			
			"LONG"				| null		| 100000l					 | 18l			| 117l			| 10000l					| 1000l						| 100l						| 10l						| LocalDateTime.now()			
			"LONG"				| "name11"	| null						 | 19l			| 118l			| 20000l					| 2000l						| 200l						| 20l						| LocalDateTime.now()			
			"LONG"				| "name2l"	| 200000l					 | 20l			| 119l			| 30000l					| 3000l						| 300l						| 30l						| LocalDateTime.now()			
			"LONG"				| "name3l"	| 300000l					 | 21l			| 120l			| 40000l					| 4000l						| 400l						| 40l						| LocalDateTime.now()			
			"LONG"				| "name4l"	| 400000l					 | 22l			| 121l			| null						| 5000l						| 500l						| 50l						| LocalDateTime.now()			
			"LONG"				| "name5l"	| 500000l					 | 23l			| 122l			| 50000l					| null						| 600l						| 60l						| LocalDateTime.now()			
			"LONG"				| "name6l"	| 500000l					 | 24l			| 123l			| 60000l					| 6000l						| null						| 70l						| LocalDateTime.now()			
			"LONG"				| "name7l"	| 700000l					 | 25l			| 124l			| 70000l					| 7000l						| 700l						| null						| LocalDateTime.now()			
			"LONG"				| "name8l"	| 800000l					 | 26l			| 125l			| 80000l					| 8000l						| 800l						| 80l						| LocalDateTime.now()
			"LONG"				| "name9l"	| 900000l					 | 27l			| 126l			| 90000l					| 9000l						| 900l						| 90l						| LocalDateTime.now()			
			"DOUBLE"			| null		| 100000.0d					 | 21l			| 127l			| 10000.0d					| 1000.0d					| 100.0d					| 10.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name1d"	| null						 | 21l			| 128l			| 20000.0d					| 2000.0d					| 200.0d					| 20.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name2d"	| 200000.0d					 | 30l			| 129l			| 30000.0d					| 3000.0d					| 300.0d					| 30.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name3d"	| 300000.0d					 | 31l			| 130l			| 40000.0d					| 4000.0d					| 400.0d					| 40.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name4d"	| 400000.0d					 | 32l			| 131l			| null						| 5000.0d					| 500.0d					| 50.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name5d"	| 500000.0d					 | 33l			| 132l			| 50000.0d					| null						| 600.0d					| 60.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name6d"	| 600000.0d					 | 34l			| 133l			| 60000.0d					| 6000.0d					| null						| 70.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name7d"	| 700000.0d					 | 35l			| 134l			| 70000.0d					| 7000.0d					| 700.0d					| null						| LocalDateTime.now()			
			"DOUBLE"			| "name8d"	| 800000.0d					 | 36l			| 135l			| 80000.0d					| 8000.0d					| 800.0d					| 80.0d						| null			
			"DOUBLE"			| "name9d"	| 900000.0d					 | 37l			| 136l			| 90000.0d					| 9000.0d					| 900.0d					| 90.0d						| LocalDateTime.now()			

			measureReducer=MeasureReducers.valueOf(measureReducerName).get()
	}
	@Unroll
	def "When MetricSnapshot is created with measureReducer:#measureReducer,name:#name,accumulatedSamples:#accumulatedSamples,samplingSize:#samplingSize,totalHits:#totalHits,maxMeasure:#maxMeasure,minMeasure:#minMeasure,averageMeasure:#averageMeasure,lastMeasure:#lastMeasure,lastOccurrence:#lastOccurrence then getFormatedMinMeasure() should return #expected"(){
		println(">>>>> MetricSnapshotSpec >>>> When MetricSnapshot is created with measureReducer:$measureReducer,name:$name,accumulatedSamples:$accumulatedSamples,samplingSize:$samplingSize,totalHits:$totalHits,maxMeasure:$maxMeasure,minMeasure:$minMeasure,averageMeasure:$averageMeasure,lastMeasure:$lastMeasure,lastOccurrence:$lastOccurrence then getFormatedMinMeasure() should return $expected")

		given:
			def metric=MetricSnapshot.builder(measureReducer)
											.name(name)
											.accumulatedSamples(accumulatedSamples)
											.samplingSize(samplingSize)
											.totalHits(totalHits)
											.maxMeasure(maxMeasure)
											.minMeasure(minMeasure)
											.averageMeasure(averageMeasure)
											.lastMeasure(lastMeasure)
											.lastOccurrence(lastOccurrence)
										.build()
			
		expect:
			metric.getFormatedMinMeasure()==expected
			
		where:
			measureReducerName	| name		| accumulatedSamples		 | samplingSize	| totalHits		| maxMeasure				| minMeasure				| averageMeasure			| lastMeasure				| lastOccurrence
			"DURATION"			| null		| Duration.ofSeconds(100000) | 8l			| 17l			| Duration.ofSeconds(10000)	| Duration.ofSeconds(1000)	| Duration.ofSeconds(100)	| Duration.ofSeconds(10)	| LocalDateTime.now()			
			"DURATION"			| "name1"	| null						 | 9l			| 18l			| Duration.ofSeconds(20000)	| Duration.ofSeconds(2000)	| Duration.ofSeconds(200)	| Duration.ofSeconds(20)	| LocalDateTime.now()			
			"DURATION"			| "name2"	| Duration.ofSeconds(200000) | 10l			| 19l			| Duration.ofSeconds(30000)	| Duration.ofSeconds(3000)	| Duration.ofSeconds(300)	| Duration.ofSeconds(30)	| LocalDateTime.now()			
			"DURATION"			| "name3"	| Duration.ofSeconds(300000) | 11l			| 110l			| Duration.ofSeconds(40000)	| Duration.ofSeconds(4000)	| Duration.ofSeconds(400)	| Duration.ofSeconds(40)	| LocalDateTime.now()			
			"DURATION"			| "name4"	| Duration.ofSeconds(400000) | 12l			| 111l			| null						| Duration.ofSeconds(5000)	| Duration.ofSeconds(500)	| Duration.ofSeconds(50)	| LocalDateTime.now()			
			"DURATION"			| "name5"	| Duration.ofSeconds(500000) | 13l			| 112l			| Duration.ofSeconds(50000)	| null						| Duration.ofSeconds(600)	| Duration.ofSeconds(60)	| LocalDateTime.now()			
			"DURATION"			| "name6"	| Duration.ofSeconds(600000) | 14l			| 113l			| Duration.ofSeconds(60000)	| Duration.ofSeconds(6000)	| null						| Duration.ofSeconds(80)	| LocalDateTime.now()			
			"DURATION"			| "name7"	| Duration.ofSeconds(700000) | 15l			| 114l			| Duration.ofSeconds(70000)	| Duration.ofSeconds(7000)	| Duration.ofSeconds(700)	| null						| LocalDateTime.now()			
			"DURATION"			| "name8"	| Duration.ofSeconds(800000) | 16l			| 115l			| Duration.ofSeconds(80000)	| Duration.ofSeconds(8000)	| Duration.ofSeconds(800)	| Duration.ofSeconds(70)	| null			
			"DURATION"			| "name9"	| Duration.ofSeconds(900000) | 17l			| 116l			| Duration.ofSeconds(90000)	| Duration.ofSeconds(9000)	| Duration.ofSeconds(900)	| Duration.ofSeconds(90)	| LocalDateTime.now()			
			"LONG"				| null		| 100000l					 | 18l			| 117l			| 10000l					| 1000l						| 100l						| 10l						| LocalDateTime.now()			
			"LONG"				| "name11"	| null						 | 19l			| 118l			| 20000l					| 2000l						| 200l						| 20l						| LocalDateTime.now()			
			"LONG"				| "name2l"	| 200000l					 | 20l			| 119l			| 30000l					| 3000l						| 300l						| 30l						| LocalDateTime.now()			
			"LONG"				| "name3l"	| 300000l					 | 21l			| 120l			| 40000l					| 4000l						| 400l						| 40l						| LocalDateTime.now()			
			"LONG"				| "name4l"	| 400000l					 | 22l			| 121l			| null						| 5000l						| 500l						| 50l						| LocalDateTime.now()			
			"LONG"				| "name5l"	| 500000l					 | 23l			| 122l			| 50000l					| null						| 600l						| 60l						| LocalDateTime.now()			
			"LONG"				| "name6l"	| 500000l					 | 24l			| 123l			| 60000l					| 6000l						| null						| 70l						| LocalDateTime.now()			
			"LONG"				| "name7l"	| 700000l					 | 25l			| 124l			| 70000l					| 7000l						| 700l						| null						| LocalDateTime.now()			
			"LONG"				| "name8l"	| 800000l					 | 26l			| 125l			| 80000l					| 8000l						| 800l						| 80l						| LocalDateTime.now()
			"LONG"				| "name9l"	| 900000l					 | 27l			| 126l			| 90000l					| 9000l						| 900l						| 90l						| LocalDateTime.now()			
			"DOUBLE"			| null		| 100000.0d					 | 21l			| 127l			| 10000.0d					| 1000.0d					| 100.0d					| 10.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name1d"	| null						 | 21l			| 128l			| 20000.0d					| 2000.0d					| 200.0d					| 20.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name2d"	| 200000.0d					 | 30l			| 129l			| 30000.0d					| 3000.0d					| 300.0d					| 30.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name3d"	| 300000.0d					 | 31l			| 130l			| 40000.0d					| 4000.0d					| 400.0d					| 40.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name4d"	| 400000.0d					 | 32l			| 131l			| null						| 5000.0d					| 500.0d					| 50.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name5d"	| 500000.0d					 | 33l			| 132l			| 50000.0d					| null						| 600.0d					| 60.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name6d"	| 600000.0d					 | 34l			| 133l			| 60000.0d					| 6000.0d					| null						| 70.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name7d"	| 700000.0d					 | 35l			| 134l			| 70000.0d					| 7000.0d					| 700.0d					| null						| LocalDateTime.now()			
			"DOUBLE"			| "name8d"	| 800000.0d					 | 36l			| 135l			| 80000.0d					| 8000.0d					| 800.0d					| 80.0d						| null			
			"DOUBLE"			| "name9d"	| 900000.0d					 | 37l			| 136l			| 90000.0d					| 9000.0d					| 900.0d					| 90.0d						| LocalDateTime.now()			

			measureReducer=MeasureReducers.valueOf(measureReducerName).get()
			expected=measureReducer.toString(minMeasure)
	}
		
	@Unroll
	def "When MetricSnapshot is created with measureReducer:#measureReducer,name:#name,accumulatedSamples:#accumulatedSamples,samplingSize:#samplingSize,totalHits:#totalHits,maxMeasure:#maxMeasure,minMeasure:#minMeasure,averageMeasure:#averageMeasure,lastMeasure:#lastMeasure,lastOccurrence:#lastOccurrence then getAverageMeasure() should return #averageMeasure"(){
		println(">>>>> MetricSnapshotSpec >>>> When MetricSnapshot is created with measureReducer:$measureReducer,name:$name,accumulatedSamples:$accumulatedSamples,samplingSize:$samplingSize,totalHits:$totalHits,maxMeasure:$maxMeasure,minMeasure:$minMeasure,averageMeasure:$averageMeasure,lastMeasure:$lastMeasure,lastOccurrence:$lastOccurrence then getAverageMeasure() should return $averageMeasure")

		given:
			def metric=MetricSnapshot.builder(measureReducer)
											.name(name)
											.accumulatedSamples(accumulatedSamples)
											.samplingSize(samplingSize)
											.totalHits(totalHits)
											.maxMeasure(maxMeasure)
											.minMeasure(minMeasure)
											.averageMeasure(averageMeasure)
											.lastMeasure(lastMeasure)
											.lastOccurrence(lastOccurrence)
										.build()
			
		expect:
			metric.getAverageMeasure()==averageMeasure
			
		where:
			measureReducerName	| name		| accumulatedSamples		 | samplingSize	| totalHits		| maxMeasure				| minMeasure				| averageMeasure			| lastMeasure				| lastOccurrence
			"DURATION"			| null		| Duration.ofSeconds(100000) | 8l			| 17l			| Duration.ofSeconds(10000)	| Duration.ofSeconds(1000)	| Duration.ofSeconds(100)	| Duration.ofSeconds(10)	| LocalDateTime.now()			
			"DURATION"			| "name1"	| null						 | 9l			| 18l			| Duration.ofSeconds(20000)	| Duration.ofSeconds(2000)	| Duration.ofSeconds(200)	| Duration.ofSeconds(20)	| LocalDateTime.now()			
			"DURATION"			| "name2"	| Duration.ofSeconds(200000) | 10l			| 19l			| Duration.ofSeconds(30000)	| Duration.ofSeconds(3000)	| Duration.ofSeconds(300)	| Duration.ofSeconds(30)	| LocalDateTime.now()			
			"DURATION"			| "name3"	| Duration.ofSeconds(300000) | 11l			| 110l			| Duration.ofSeconds(40000)	| Duration.ofSeconds(4000)	| Duration.ofSeconds(400)	| Duration.ofSeconds(40)	| LocalDateTime.now()			
			"DURATION"			| "name4"	| Duration.ofSeconds(400000) | 12l			| 111l			| null						| Duration.ofSeconds(5000)	| Duration.ofSeconds(500)	| Duration.ofSeconds(50)	| LocalDateTime.now()			
			"DURATION"			| "name5"	| Duration.ofSeconds(500000) | 13l			| 112l			| Duration.ofSeconds(50000)	| null						| Duration.ofSeconds(600)	| Duration.ofSeconds(60)	| LocalDateTime.now()			
			"DURATION"			| "name6"	| Duration.ofSeconds(600000) | 14l			| 113l			| Duration.ofSeconds(60000)	| Duration.ofSeconds(6000)	| null						| Duration.ofSeconds(80)	| LocalDateTime.now()			
			"DURATION"			| "name7"	| Duration.ofSeconds(700000) | 15l			| 114l			| Duration.ofSeconds(70000)	| Duration.ofSeconds(7000)	| Duration.ofSeconds(700)	| null						| LocalDateTime.now()			
			"DURATION"			| "name8"	| Duration.ofSeconds(800000) | 16l			| 115l			| Duration.ofSeconds(80000)	| Duration.ofSeconds(8000)	| Duration.ofSeconds(800)	| Duration.ofSeconds(70)	| null			
			"DURATION"			| "name9"	| Duration.ofSeconds(900000) | 17l			| 116l			| Duration.ofSeconds(90000)	| Duration.ofSeconds(9000)	| Duration.ofSeconds(900)	| Duration.ofSeconds(90)	| LocalDateTime.now()			
			"LONG"				| null		| 100000l					 | 18l			| 117l			| 10000l					| 1000l						| 100l						| 10l						| LocalDateTime.now()			
			"LONG"				| "name11"	| null						 | 19l			| 118l			| 20000l					| 2000l						| 200l						| 20l						| LocalDateTime.now()			
			"LONG"				| "name2l"	| 200000l					 | 20l			| 119l			| 30000l					| 3000l						| 300l						| 30l						| LocalDateTime.now()			
			"LONG"				| "name3l"	| 300000l					 | 21l			| 120l			| 40000l					| 4000l						| 400l						| 40l						| LocalDateTime.now()			
			"LONG"				| "name4l"	| 400000l					 | 22l			| 121l			| null						| 5000l						| 500l						| 50l						| LocalDateTime.now()			
			"LONG"				| "name5l"	| 500000l					 | 23l			| 122l			| 50000l					| null						| 600l						| 60l						| LocalDateTime.now()			
			"LONG"				| "name6l"	| 500000l					 | 24l			| 123l			| 60000l					| 6000l						| null						| 70l						| LocalDateTime.now()			
			"LONG"				| "name7l"	| 700000l					 | 25l			| 124l			| 70000l					| 7000l						| 700l						| null						| LocalDateTime.now()			
			"LONG"				| "name8l"	| 800000l					 | 26l			| 125l			| 80000l					| 8000l						| 800l						| 80l						| LocalDateTime.now()
			"LONG"				| "name9l"	| 900000l					 | 27l			| 126l			| 90000l					| 9000l						| 900l						| 90l						| LocalDateTime.now()			
			"DOUBLE"			| null		| 100000.0d					 | 21l			| 127l			| 10000.0d					| 1000.0d					| 100.0d					| 10.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name1d"	| null						 | 21l			| 128l			| 20000.0d					| 2000.0d					| 200.0d					| 20.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name2d"	| 200000.0d					 | 30l			| 129l			| 30000.0d					| 3000.0d					| 300.0d					| 30.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name3d"	| 300000.0d					 | 31l			| 130l			| 40000.0d					| 4000.0d					| 400.0d					| 40.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name4d"	| 400000.0d					 | 32l			| 131l			| null						| 5000.0d					| 500.0d					| 50.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name5d"	| 500000.0d					 | 33l			| 132l			| 50000.0d					| null						| 600.0d					| 60.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name6d"	| 600000.0d					 | 34l			| 133l			| 60000.0d					| 6000.0d					| null						| 70.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name7d"	| 700000.0d					 | 35l			| 134l			| 70000.0d					| 7000.0d					| 700.0d					| null						| LocalDateTime.now()			
			"DOUBLE"			| "name8d"	| 800000.0d					 | 36l			| 135l			| 80000.0d					| 8000.0d					| 800.0d					| 80.0d						| null			
			"DOUBLE"			| "name9d"	| 900000.0d					 | 37l			| 136l			| 90000.0d					| 9000.0d					| 900.0d					| 90.0d						| LocalDateTime.now()			

			measureReducer=MeasureReducers.valueOf(measureReducerName).get()
	}
	@Unroll
	def "When MetricSnapshot is created with measureReducer:#measureReducer,name:#name,accumulatedSamples:#accumulatedSamples,samplingSize:#samplingSize,totalHits:#totalHits,maxMeasure:#maxMeasure,minMeasure:#minMeasure,averageMeasure:#averageMeasure,lastMeasure:#lastMeasure,lastOccurrence:#lastOccurrence then getFormatedAverageMeasure() should return #expected"(){
		println(">>>>> MetricSnapshotSpec >>>> When MetricSnapshot is created with measureReducer:$measureReducer,name:$name,accumulatedSamples:$accumulatedSamples,samplingSize:$samplingSize,totalHits:$totalHits,maxMeasure:$maxMeasure,minMeasure:$minMeasure,averageMeasure:$averageMeasure,lastMeasure:$lastMeasure,lastOccurrence:$lastOccurrence then getFormatedAverageMeasure() should return $expected")

		given:
			def metric=MetricSnapshot.builder(measureReducer)
											.name(name)
											.accumulatedSamples(accumulatedSamples)
											.samplingSize(samplingSize)
											.totalHits(totalHits)
											.maxMeasure(maxMeasure)
											.minMeasure(minMeasure)
											.averageMeasure(averageMeasure)
											.lastMeasure(lastMeasure)
											.lastOccurrence(lastOccurrence)
										.build()
			
		expect:
			metric.getFormatedAverageMeasure()==expected
			
		where:
			measureReducerName	| name		| accumulatedSamples		 | samplingSize	| totalHits		| maxMeasure				| minMeasure				| averageMeasure			| lastMeasure				| lastOccurrence
			"DURATION"			| null		| Duration.ofSeconds(100000) | 8l			| 17l			| Duration.ofSeconds(10000)	| Duration.ofSeconds(1000)	| Duration.ofSeconds(100)	| Duration.ofSeconds(10)	| LocalDateTime.now()			
			"DURATION"			| "name1"	| null						 | 9l			| 18l			| Duration.ofSeconds(20000)	| Duration.ofSeconds(2000)	| Duration.ofSeconds(200)	| Duration.ofSeconds(20)	| LocalDateTime.now()			
			"DURATION"			| "name2"	| Duration.ofSeconds(200000) | 10l			| 19l			| Duration.ofSeconds(30000)	| Duration.ofSeconds(3000)	| Duration.ofSeconds(300)	| Duration.ofSeconds(30)	| LocalDateTime.now()			
			"DURATION"			| "name3"	| Duration.ofSeconds(300000) | 11l			| 110l			| Duration.ofSeconds(40000)	| Duration.ofSeconds(4000)	| Duration.ofSeconds(400)	| Duration.ofSeconds(40)	| LocalDateTime.now()			
			"DURATION"			| "name4"	| Duration.ofSeconds(400000) | 12l			| 111l			| null						| Duration.ofSeconds(5000)	| Duration.ofSeconds(500)	| Duration.ofSeconds(50)	| LocalDateTime.now()			
			"DURATION"			| "name5"	| Duration.ofSeconds(500000) | 13l			| 112l			| Duration.ofSeconds(50000)	| null						| Duration.ofSeconds(600)	| Duration.ofSeconds(60)	| LocalDateTime.now()			
			"DURATION"			| "name6"	| Duration.ofSeconds(600000) | 14l			| 113l			| Duration.ofSeconds(60000)	| Duration.ofSeconds(6000)	| null						| Duration.ofSeconds(80)	| LocalDateTime.now()			
			"DURATION"			| "name7"	| Duration.ofSeconds(700000) | 15l			| 114l			| Duration.ofSeconds(70000)	| Duration.ofSeconds(7000)	| Duration.ofSeconds(700)	| null						| LocalDateTime.now()			
			"DURATION"			| "name8"	| Duration.ofSeconds(800000) | 16l			| 115l			| Duration.ofSeconds(80000)	| Duration.ofSeconds(8000)	| Duration.ofSeconds(800)	| Duration.ofSeconds(70)	| null			
			"DURATION"			| "name9"	| Duration.ofSeconds(900000) | 17l			| 116l			| Duration.ofSeconds(90000)	| Duration.ofSeconds(9000)	| Duration.ofSeconds(900)	| Duration.ofSeconds(90)	| LocalDateTime.now()			
			"LONG"				| null		| 100000l					 | 18l			| 117l			| 10000l					| 1000l						| 100l						| 10l						| LocalDateTime.now()			
			"LONG"				| "name11"	| null						 | 19l			| 118l			| 20000l					| 2000l						| 200l						| 20l						| LocalDateTime.now()			
			"LONG"				| "name2l"	| 200000l					 | 20l			| 119l			| 30000l					| 3000l						| 300l						| 30l						| LocalDateTime.now()			
			"LONG"				| "name3l"	| 300000l					 | 21l			| 120l			| 40000l					| 4000l						| 400l						| 40l						| LocalDateTime.now()			
			"LONG"				| "name4l"	| 400000l					 | 22l			| 121l			| null						| 5000l						| 500l						| 50l						| LocalDateTime.now()			
			"LONG"				| "name5l"	| 500000l					 | 23l			| 122l			| 50000l					| null						| 600l						| 60l						| LocalDateTime.now()			
			"LONG"				| "name6l"	| 500000l					 | 24l			| 123l			| 60000l					| 6000l						| null						| 70l						| LocalDateTime.now()			
			"LONG"				| "name7l"	| 700000l					 | 25l			| 124l			| 70000l					| 7000l						| 700l						| null						| LocalDateTime.now()			
			"LONG"				| "name8l"	| 800000l					 | 26l			| 125l			| 80000l					| 8000l						| 800l						| 80l						| LocalDateTime.now()
			"LONG"				| "name9l"	| 900000l					 | 27l			| 126l			| 90000l					| 9000l						| 900l						| 90l						| LocalDateTime.now()			
			"DOUBLE"			| null		| 100000.0d					 | 21l			| 127l			| 10000.0d					| 1000.0d					| 100.0d					| 10.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name1d"	| null						 | 21l			| 128l			| 20000.0d					| 2000.0d					| 200.0d					| 20.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name2d"	| 200000.0d					 | 30l			| 129l			| 30000.0d					| 3000.0d					| 300.0d					| 30.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name3d"	| 300000.0d					 | 31l			| 130l			| 40000.0d					| 4000.0d					| 400.0d					| 40.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name4d"	| 400000.0d					 | 32l			| 131l			| null						| 5000.0d					| 500.0d					| 50.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name5d"	| 500000.0d					 | 33l			| 132l			| 50000.0d					| null						| 600.0d					| 60.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name6d"	| 600000.0d					 | 34l			| 133l			| 60000.0d					| 6000.0d					| null						| 70.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name7d"	| 700000.0d					 | 35l			| 134l			| 70000.0d					| 7000.0d					| 700.0d					| null						| LocalDateTime.now()			
			"DOUBLE"			| "name8d"	| 800000.0d					 | 36l			| 135l			| 80000.0d					| 8000.0d					| 800.0d					| 80.0d						| null			
			"DOUBLE"			| "name9d"	| 900000.0d					 | 37l			| 136l			| 90000.0d					| 9000.0d					| 900.0d					| 90.0d						| LocalDateTime.now()			

			measureReducer=MeasureReducers.valueOf(measureReducerName).get()
			expected=measureReducer.toString(averageMeasure)
	}
	
	@Unroll
	def "When MetricSnapshot is created with measureReducer:#measureReducer,name:#name,accumulatedSamples:#accumulatedSamples,samplingSize:#samplingSize,totalHits:#totalHits,maxMeasure:#maxMeasure,minMeasure:#minMeasure,averageMeasure:#averageMeasure,lastMeasure:#lastMeasure,lastOccurrence:#lastOccurrence then getLastMeasure() should return #lastMeasure"(){
		println(">>>>> MetricSnapshotSpec >>>> When MetricSnapshot is created with measureReducer:$measureReducer,name:$name,accumulatedSamples:$accumulatedSamples,samplingSize:$samplingSize,totalHits:$totalHits,maxMeasure:$maxMeasure,minMeasure:$minMeasure,averageMeasure:$averageMeasure,lastMeasure:$lastMeasure,lastOccurrence:$lastOccurrence then getLastMeasure() should return $lastMeasure")

		given:
			def metric=MetricSnapshot.builder(measureReducer)
											.name(name)
											.accumulatedSamples(accumulatedSamples)
											.samplingSize(samplingSize)
											.totalHits(totalHits)
											.maxMeasure(maxMeasure)
											.minMeasure(minMeasure)
											.averageMeasure(averageMeasure)
											.lastMeasure(lastMeasure)
											.lastOccurrence(lastOccurrence)
										.build()
			
		expect:
			metric.getLastMeasure()==lastMeasure
			
		where:
			measureReducerName	| name		| accumulatedSamples		 | samplingSize	| totalHits		| maxMeasure				| minMeasure				| averageMeasure			| lastMeasure				| lastOccurrence
			"DURATION"			| null		| Duration.ofSeconds(100000) | 8l			| 17l			| Duration.ofSeconds(10000)	| Duration.ofSeconds(1000)	| Duration.ofSeconds(100)	| Duration.ofSeconds(10)	| LocalDateTime.now()			
			"DURATION"			| "name1"	| null						 | 9l			| 18l			| Duration.ofSeconds(20000)	| Duration.ofSeconds(2000)	| Duration.ofSeconds(200)	| Duration.ofSeconds(20)	| LocalDateTime.now()			
			"DURATION"			| "name2"	| Duration.ofSeconds(200000) | 10l			| 19l			| Duration.ofSeconds(30000)	| Duration.ofSeconds(3000)	| Duration.ofSeconds(300)	| Duration.ofSeconds(30)	| LocalDateTime.now()			
			"DURATION"			| "name3"	| Duration.ofSeconds(300000) | 11l			| 110l			| Duration.ofSeconds(40000)	| Duration.ofSeconds(4000)	| Duration.ofSeconds(400)	| Duration.ofSeconds(40)	| LocalDateTime.now()			
			"DURATION"			| "name4"	| Duration.ofSeconds(400000) | 12l			| 111l			| null						| Duration.ofSeconds(5000)	| Duration.ofSeconds(500)	| Duration.ofSeconds(50)	| LocalDateTime.now()			
			"DURATION"			| "name5"	| Duration.ofSeconds(500000) | 13l			| 112l			| Duration.ofSeconds(50000)	| null						| Duration.ofSeconds(600)	| Duration.ofSeconds(60)	| LocalDateTime.now()			
			"DURATION"			| "name6"	| Duration.ofSeconds(600000) | 14l			| 113l			| Duration.ofSeconds(60000)	| Duration.ofSeconds(6000)	| null						| Duration.ofSeconds(80)	| LocalDateTime.now()			
			"DURATION"			| "name7"	| Duration.ofSeconds(700000) | 15l			| 114l			| Duration.ofSeconds(70000)	| Duration.ofSeconds(7000)	| Duration.ofSeconds(700)	| null						| LocalDateTime.now()			
			"DURATION"			| "name8"	| Duration.ofSeconds(800000) | 16l			| 115l			| Duration.ofSeconds(80000)	| Duration.ofSeconds(8000)	| Duration.ofSeconds(800)	| Duration.ofSeconds(70)	| null			
			"DURATION"			| "name9"	| Duration.ofSeconds(900000) | 17l			| 116l			| Duration.ofSeconds(90000)	| Duration.ofSeconds(9000)	| Duration.ofSeconds(900)	| Duration.ofSeconds(90)	| LocalDateTime.now()			
			"LONG"				| null		| 100000l					 | 18l			| 117l			| 10000l					| 1000l						| 100l						| 10l						| LocalDateTime.now()			
			"LONG"				| "name11"	| null						 | 19l			| 118l			| 20000l					| 2000l						| 200l						| 20l						| LocalDateTime.now()			
			"LONG"				| "name2l"	| 200000l					 | 20l			| 119l			| 30000l					| 3000l						| 300l						| 30l						| LocalDateTime.now()			
			"LONG"				| "name3l"	| 300000l					 | 21l			| 120l			| 40000l					| 4000l						| 400l						| 40l						| LocalDateTime.now()			
			"LONG"				| "name4l"	| 400000l					 | 22l			| 121l			| null						| 5000l						| 500l						| 50l						| LocalDateTime.now()			
			"LONG"				| "name5l"	| 500000l					 | 23l			| 122l			| 50000l					| null						| 600l						| 60l						| LocalDateTime.now()			
			"LONG"				| "name6l"	| 500000l					 | 24l			| 123l			| 60000l					| 6000l						| null						| 70l						| LocalDateTime.now()			
			"LONG"				| "name7l"	| 700000l					 | 25l			| 124l			| 70000l					| 7000l						| 700l						| null						| LocalDateTime.now()			
			"LONG"				| "name8l"	| 800000l					 | 26l			| 125l			| 80000l					| 8000l						| 800l						| 80l						| LocalDateTime.now()
			"LONG"				| "name9l"	| 900000l					 | 27l			| 126l			| 90000l					| 9000l						| 900l						| 90l						| LocalDateTime.now()			
			"DOUBLE"			| null		| 100000.0d					 | 21l			| 127l			| 10000.0d					| 1000.0d					| 100.0d					| 10.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name1d"	| null						 | 21l			| 128l			| 20000.0d					| 2000.0d					| 200.0d					| 20.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name2d"	| 200000.0d					 | 30l			| 129l			| 30000.0d					| 3000.0d					| 300.0d					| 30.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name3d"	| 300000.0d					 | 31l			| 130l			| 40000.0d					| 4000.0d					| 400.0d					| 40.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name4d"	| 400000.0d					 | 32l			| 131l			| null						| 5000.0d					| 500.0d					| 50.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name5d"	| 500000.0d					 | 33l			| 132l			| 50000.0d					| null						| 600.0d					| 60.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name6d"	| 600000.0d					 | 34l			| 133l			| 60000.0d					| 6000.0d					| null						| 70.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name7d"	| 700000.0d					 | 35l			| 134l			| 70000.0d					| 7000.0d					| 700.0d					| null						| LocalDateTime.now()			
			"DOUBLE"			| "name8d"	| 800000.0d					 | 36l			| 135l			| 80000.0d					| 8000.0d					| 800.0d					| 80.0d						| null			
			"DOUBLE"			| "name9d"	| 900000.0d					 | 37l			| 136l			| 90000.0d					| 9000.0d					| 900.0d					| 90.0d						| LocalDateTime.now()			

			measureReducer=MeasureReducers.valueOf(measureReducerName).get()
	}
	@Unroll
	def "When MetricSnapshot is created with measureReducer:#measureReducer,name:#name,accumulatedSamples:#accumulatedSamples,samplingSize:#samplingSize,totalHits:#totalHits,maxMeasure:#maxMeasure,minMeasure:#minMeasure,averageMeasure:#averageMeasure,lastMeasure:#lastMeasure,lastOccurrence:#lastOccurrence then getFormatedLastMeasure() should return #expected"(){
		println(">>>>> MetricSnapshotSpec >>>> When MetricSnapshot is created with measureReducer:$measureReducer,name:$name,accumulatedSamples:$accumulatedSamples,samplingSize:$samplingSize,totalHits:$totalHits,maxMeasure:$maxMeasure,minMeasure:$minMeasure,averageMeasure:$averageMeasure,lastMeasure:$lastMeasure,lastOccurrence:$lastOccurrence then getFormatedLastMeasure() should return $expected")

		given:
			def metric=MetricSnapshot.builder(measureReducer)
											.name(name)
											.accumulatedSamples(accumulatedSamples)
											.samplingSize(samplingSize)
											.totalHits(totalHits)
											.maxMeasure(maxMeasure)
											.minMeasure(minMeasure)
											.averageMeasure(averageMeasure)
											.lastMeasure(lastMeasure)
											.lastOccurrence(lastOccurrence)
										.build()
			
		expect:
			metric.getFormatedLastMeasure()==expected
			
		where:
			measureReducerName	| name		| accumulatedSamples		 | samplingSize	| totalHits		| maxMeasure				| minMeasure				| averageMeasure			| lastMeasure				| lastOccurrence
			"DURATION"			| null		| Duration.ofSeconds(100000) | 8l			| 17l			| Duration.ofSeconds(10000)	| Duration.ofSeconds(1000)	| Duration.ofSeconds(100)	| Duration.ofSeconds(10)	| LocalDateTime.now()			
			"DURATION"			| "name1"	| null						 | 9l			| 18l			| Duration.ofSeconds(20000)	| Duration.ofSeconds(2000)	| Duration.ofSeconds(200)	| Duration.ofSeconds(20)	| LocalDateTime.now()			
			"DURATION"			| "name2"	| Duration.ofSeconds(200000) | 10l			| 19l			| Duration.ofSeconds(30000)	| Duration.ofSeconds(3000)	| Duration.ofSeconds(300)	| Duration.ofSeconds(30)	| LocalDateTime.now()			
			"DURATION"			| "name3"	| Duration.ofSeconds(300000) | 11l			| 110l			| Duration.ofSeconds(40000)	| Duration.ofSeconds(4000)	| Duration.ofSeconds(400)	| Duration.ofSeconds(40)	| LocalDateTime.now()			
			"DURATION"			| "name4"	| Duration.ofSeconds(400000) | 12l			| 111l			| null						| Duration.ofSeconds(5000)	| Duration.ofSeconds(500)	| Duration.ofSeconds(50)	| LocalDateTime.now()			
			"DURATION"			| "name5"	| Duration.ofSeconds(500000) | 13l			| 112l			| Duration.ofSeconds(50000)	| null						| Duration.ofSeconds(600)	| Duration.ofSeconds(60)	| LocalDateTime.now()			
			"DURATION"			| "name6"	| Duration.ofSeconds(600000) | 14l			| 113l			| Duration.ofSeconds(60000)	| Duration.ofSeconds(6000)	| null						| Duration.ofSeconds(80)	| LocalDateTime.now()			
			"DURATION"			| "name7"	| Duration.ofSeconds(700000) | 15l			| 114l			| Duration.ofSeconds(70000)	| Duration.ofSeconds(7000)	| Duration.ofSeconds(700)	| null						| LocalDateTime.now()			
			"DURATION"			| "name8"	| Duration.ofSeconds(800000) | 16l			| 115l			| Duration.ofSeconds(80000)	| Duration.ofSeconds(8000)	| Duration.ofSeconds(800)	| Duration.ofSeconds(70)	| null			
			"DURATION"			| "name9"	| Duration.ofSeconds(900000) | 17l			| 116l			| Duration.ofSeconds(90000)	| Duration.ofSeconds(9000)	| Duration.ofSeconds(900)	| Duration.ofSeconds(90)	| LocalDateTime.now()			
			"LONG"				| null		| 100000l					 | 18l			| 117l			| 10000l					| 1000l						| 100l						| 10l						| LocalDateTime.now()			
			"LONG"				| "name11"	| null						 | 19l			| 118l			| 20000l					| 2000l						| 200l						| 20l						| LocalDateTime.now()			
			"LONG"				| "name2l"	| 200000l					 | 20l			| 119l			| 30000l					| 3000l						| 300l						| 30l						| LocalDateTime.now()			
			"LONG"				| "name3l"	| 300000l					 | 21l			| 120l			| 40000l					| 4000l						| 400l						| 40l						| LocalDateTime.now()			
			"LONG"				| "name4l"	| 400000l					 | 22l			| 121l			| null						| 5000l						| 500l						| 50l						| LocalDateTime.now()			
			"LONG"				| "name5l"	| 500000l					 | 23l			| 122l			| 50000l					| null						| 600l						| 60l						| LocalDateTime.now()			
			"LONG"				| "name6l"	| 500000l					 | 24l			| 123l			| 60000l					| 6000l						| null						| 70l						| LocalDateTime.now()			
			"LONG"				| "name7l"	| 700000l					 | 25l			| 124l			| 70000l					| 7000l						| 700l						| null						| LocalDateTime.now()			
			"LONG"				| "name8l"	| 800000l					 | 26l			| 125l			| 80000l					| 8000l						| 800l						| 80l						| LocalDateTime.now()
			"LONG"				| "name9l"	| 900000l					 | 27l			| 126l			| 90000l					| 9000l						| 900l						| 90l						| LocalDateTime.now()			
			"DOUBLE"			| null		| 100000.0d					 | 21l			| 127l			| 10000.0d					| 1000.0d					| 100.0d					| 10.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name1d"	| null						 | 21l			| 128l			| 20000.0d					| 2000.0d					| 200.0d					| 20.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name2d"	| 200000.0d					 | 30l			| 129l			| 30000.0d					| 3000.0d					| 300.0d					| 30.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name3d"	| 300000.0d					 | 31l			| 130l			| 40000.0d					| 4000.0d					| 400.0d					| 40.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name4d"	| 400000.0d					 | 32l			| 131l			| null						| 5000.0d					| 500.0d					| 50.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name5d"	| 500000.0d					 | 33l			| 132l			| 50000.0d					| null						| 600.0d					| 60.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name6d"	| 600000.0d					 | 34l			| 133l			| 60000.0d					| 6000.0d					| null						| 70.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name7d"	| 700000.0d					 | 35l			| 134l			| 70000.0d					| 7000.0d					| 700.0d					| null						| LocalDateTime.now()			
			"DOUBLE"			| "name8d"	| 800000.0d					 | 36l			| 135l			| 80000.0d					| 8000.0d					| 800.0d					| 80.0d						| null			
			"DOUBLE"			| "name9d"	| 900000.0d					 | 37l			| 136l			| 90000.0d					| 9000.0d					| 900.0d					| 90.0d						| LocalDateTime.now()			

			measureReducer=MeasureReducers.valueOf(measureReducerName).get()
			expected=measureReducer.toString(lastMeasure)
	}
	
	@Unroll
	def "When MetricSnapshot is created with measureReducer:#measureReducer,name:#name,accumulatedSamples:#accumulatedSamples,samplingSize:#samplingSize,totalHits:#totalHits,maxMeasure:#maxMeasure,minMeasure:#minMeasure,averageMeasure:#averageMeasure,lastMeasure:#lastMeasure,lastOccurrence:#lastOccurrence then getLastOccurrence() should return #lastOccurrence"(){
		println(">>>>> MetricSnapshotSpec >>>> When MetricSnapshot is created with measureReducer:$measureReducer,name:$name,accumulatedSamples:$accumulatedSamples,samplingSize:$samplingSize,totalHits:$totalHits,maxMeasure:$maxMeasure,minMeasure:$minMeasure,averageMeasure:$averageMeasure,lastMeasure:$lastMeasure,lastOccurrence:$lastOccurrence then getLastOccurrence() should return $lastOccurrence")

		given:
			def metric=MetricSnapshot.builder(measureReducer)
											.name(name)
											.accumulatedSamples(accumulatedSamples)
											.samplingSize(samplingSize)
											.totalHits(totalHits)
											.maxMeasure(maxMeasure)
											.minMeasure(minMeasure)
											.averageMeasure(averageMeasure)
											.lastMeasure(lastMeasure)
											.lastOccurrence(lastOccurrence)
										.build()
			
		expect:
			metric.getLastOccurrence()==lastOccurrence
			
		where:
			measureReducerName	| name		| accumulatedSamples		 | samplingSize	| totalHits		| maxMeasure				| minMeasure				| averageMeasure			| lastMeasure				| lastOccurrence
			"DURATION"			| null		| Duration.ofSeconds(100000) | 8l			| 17l			| Duration.ofSeconds(10000)	| Duration.ofSeconds(1000)	| Duration.ofSeconds(100)	| Duration.ofSeconds(10)	| LocalDateTime.now()			
			"DURATION"			| "name1"	| null						 | 9l			| 18l			| Duration.ofSeconds(20000)	| Duration.ofSeconds(2000)	| Duration.ofSeconds(200)	| Duration.ofSeconds(20)	| LocalDateTime.now()			
			"DURATION"			| "name2"	| Duration.ofSeconds(200000) | 10l			| 19l			| Duration.ofSeconds(30000)	| Duration.ofSeconds(3000)	| Duration.ofSeconds(300)	| Duration.ofSeconds(30)	| LocalDateTime.now()			
			"DURATION"			| "name3"	| Duration.ofSeconds(300000) | 11l			| 110l			| Duration.ofSeconds(40000)	| Duration.ofSeconds(4000)	| Duration.ofSeconds(400)	| Duration.ofSeconds(40)	| LocalDateTime.now()			
			"DURATION"			| "name4"	| Duration.ofSeconds(400000) | 12l			| 111l			| null						| Duration.ofSeconds(5000)	| Duration.ofSeconds(500)	| Duration.ofSeconds(50)	| LocalDateTime.now()			
			"DURATION"			| "name5"	| Duration.ofSeconds(500000) | 13l			| 112l			| Duration.ofSeconds(50000)	| null						| Duration.ofSeconds(600)	| Duration.ofSeconds(60)	| LocalDateTime.now()			
			"DURATION"			| "name6"	| Duration.ofSeconds(600000) | 14l			| 113l			| Duration.ofSeconds(60000)	| Duration.ofSeconds(6000)	| null						| Duration.ofSeconds(80)	| LocalDateTime.now()			
			"DURATION"			| "name7"	| Duration.ofSeconds(700000) | 15l			| 114l			| Duration.ofSeconds(70000)	| Duration.ofSeconds(7000)	| Duration.ofSeconds(700)	| null						| LocalDateTime.now()			
			"DURATION"			| "name8"	| Duration.ofSeconds(800000) | 16l			| 115l			| Duration.ofSeconds(80000)	| Duration.ofSeconds(8000)	| Duration.ofSeconds(800)	| Duration.ofSeconds(70)	| null			
			"DURATION"			| "name9"	| Duration.ofSeconds(900000) | 17l			| 116l			| Duration.ofSeconds(90000)	| Duration.ofSeconds(9000)	| Duration.ofSeconds(900)	| Duration.ofSeconds(90)	| LocalDateTime.now()			
			"LONG"				| null		| 100000l					 | 18l			| 117l			| 10000l					| 1000l						| 100l						| 10l						| LocalDateTime.now()			
			"LONG"				| "name11"	| null						 | 19l			| 118l			| 20000l					| 2000l						| 200l						| 20l						| LocalDateTime.now()			
			"LONG"				| "name2l"	| 200000l					 | 20l			| 119l			| 30000l					| 3000l						| 300l						| 30l						| LocalDateTime.now()			
			"LONG"				| "name3l"	| 300000l					 | 21l			| 120l			| 40000l					| 4000l						| 400l						| 40l						| LocalDateTime.now()			
			"LONG"				| "name4l"	| 400000l					 | 22l			| 121l			| null						| 5000l						| 500l						| 50l						| LocalDateTime.now()			
			"LONG"				| "name5l"	| 500000l					 | 23l			| 122l			| 50000l					| null						| 600l						| 60l						| LocalDateTime.now()			
			"LONG"				| "name6l"	| 500000l					 | 24l			| 123l			| 60000l					| 6000l						| null						| 70l						| LocalDateTime.now()			
			"LONG"				| "name7l"	| 700000l					 | 25l			| 124l			| 70000l					| 7000l						| 700l						| null						| LocalDateTime.now()			
			"LONG"				| "name8l"	| 800000l					 | 26l			| 125l			| 80000l					| 8000l						| 800l						| 80l						| LocalDateTime.now()
			"LONG"				| "name9l"	| 900000l					 | 27l			| 126l			| 90000l					| 9000l						| 900l						| 90l						| LocalDateTime.now()			
			"DOUBLE"			| null		| 100000.0d					 | 21l			| 127l			| 10000.0d					| 1000.0d					| 100.0d					| 10.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name1d"	| null						 | 21l			| 128l			| 20000.0d					| 2000.0d					| 200.0d					| 20.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name2d"	| 200000.0d					 | 30l			| 129l			| 30000.0d					| 3000.0d					| 300.0d					| 30.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name3d"	| 300000.0d					 | 31l			| 130l			| 40000.0d					| 4000.0d					| 400.0d					| 40.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name4d"	| 400000.0d					 | 32l			| 131l			| null						| 5000.0d					| 500.0d					| 50.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name5d"	| 500000.0d					 | 33l			| 132l			| 50000.0d					| null						| 600.0d					| 60.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name6d"	| 600000.0d					 | 34l			| 133l			| 60000.0d					| 6000.0d					| null						| 70.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name7d"	| 700000.0d					 | 35l			| 134l			| 70000.0d					| 7000.0d					| 700.0d					| null						| LocalDateTime.now()			
			"DOUBLE"			| "name8d"	| 800000.0d					 | 36l			| 135l			| 80000.0d					| 8000.0d					| 800.0d					| 80.0d						| null			
			"DOUBLE"			| "name9d"	| 900000.0d					 | 37l			| 136l			| 90000.0d					| 9000.0d					| 900.0d					| 90.0d						| LocalDateTime.now()			

			measureReducer=MeasureReducers.valueOf(measureReducerName).get()
	}
	
	@Unroll
	def "When MetricSnapshot is created with measureReducer:#measureReducer,name:#name,accumulatedSamples:#accumulatedSamples,samplingSize:#samplingSize,totalHits:#totalHits,maxMeasure:#maxMeasure,minMeasure:#minMeasure,averageMeasure:#averageMeasure,lastMeasure:#lastMeasure,lastOccurrence:#lastOccurrence then toString() should return #expected"(){
		println(">>>>> MetricSnapshotSpec >>>> When MetricSnapshot is created with measureReducer:$measureReducer,name:$name,accumulatedSamples:$accumulatedSamples,samplingSize:$samplingSize,totalHits:$totalHits,maxMeasure:$maxMeasure,minMeasure:$minMeasure,averageMeasure:$averageMeasure,lastMeasure:$lastMeasure,lastOccurrence:$lastOccurrence then getFormatedLastMeasure() should return $expected")

		given:
			def metric=MetricSnapshot.builder(measureReducer)
											.name(name)
											.accumulatedSamples(accumulatedSamples)
											.samplingSize(samplingSize)
											.totalHits(totalHits)
											.maxMeasure(maxMeasure)
											.minMeasure(minMeasure)
											.averageMeasure(averageMeasure)
											.lastMeasure(lastMeasure)
											.lastOccurrence(lastOccurrence)
										.build()
			
		expect:
			metric.toString()==expected
			
		where:
			measureReducerName	| name		| accumulatedSamples		 | samplingSize	| totalHits		| maxMeasure				| minMeasure				| averageMeasure			| lastMeasure				| lastOccurrence
			"DURATION"			| null		| Duration.ofSeconds(100000) | 8l			| 17l			| Duration.ofSeconds(10000)	| Duration.ofSeconds(1000)	| Duration.ofSeconds(100)	| Duration.ofSeconds(10)	| LocalDateTime.now()			
			"DURATION"			| "name1"	| null						 | 9l			| 18l			| Duration.ofSeconds(20000)	| Duration.ofSeconds(2000)	| Duration.ofSeconds(200)	| Duration.ofSeconds(20)	| LocalDateTime.now()			
			"DURATION"			| "name2"	| Duration.ofSeconds(200000) | 10l			| 19l			| Duration.ofSeconds(30000)	| Duration.ofSeconds(3000)	| Duration.ofSeconds(300)	| Duration.ofSeconds(30)	| LocalDateTime.now()			
			"DURATION"			| "name3"	| Duration.ofSeconds(300000) | 11l			| 110l			| Duration.ofSeconds(40000)	| Duration.ofSeconds(4000)	| Duration.ofSeconds(400)	| Duration.ofSeconds(40)	| LocalDateTime.now()			
			"DURATION"			| "name4"	| Duration.ofSeconds(400000) | 12l			| 111l			| null						| Duration.ofSeconds(5000)	| Duration.ofSeconds(500)	| Duration.ofSeconds(50)	| LocalDateTime.now()			
			"DURATION"			| "name5"	| Duration.ofSeconds(500000) | 13l			| 112l			| Duration.ofSeconds(50000)	| null						| Duration.ofSeconds(600)	| Duration.ofSeconds(60)	| LocalDateTime.now()			
			"DURATION"			| "name6"	| Duration.ofSeconds(600000) | 14l			| 113l			| Duration.ofSeconds(60000)	| Duration.ofSeconds(6000)	| null						| Duration.ofSeconds(80)	| LocalDateTime.now()			
			"DURATION"			| "name7"	| Duration.ofSeconds(700000) | 15l			| 114l			| Duration.ofSeconds(70000)	| Duration.ofSeconds(7000)	| Duration.ofSeconds(700)	| null						| LocalDateTime.now()			
			"DURATION"			| "name8"	| Duration.ofSeconds(800000) | 16l			| 115l			| Duration.ofSeconds(80000)	| Duration.ofSeconds(8000)	| Duration.ofSeconds(800)	| Duration.ofSeconds(70)	| null			
			"DURATION"			| "name9"	| Duration.ofSeconds(900000) | 17l			| 116l			| Duration.ofSeconds(90000)	| Duration.ofSeconds(9000)	| Duration.ofSeconds(900)	| Duration.ofSeconds(90)	| LocalDateTime.now()			
			"LONG"				| null		| 100000l					 | 18l			| 117l			| 10000l					| 1000l						| 100l						| 10l						| LocalDateTime.now()			
			"LONG"				| "name11"	| null						 | 19l			| 118l			| 20000l					| 2000l						| 200l						| 20l						| LocalDateTime.now()			
			"LONG"				| "name2l"	| 200000l					 | 20l			| 119l			| 30000l					| 3000l						| 300l						| 30l						| LocalDateTime.now()			
			"LONG"				| "name3l"	| 300000l					 | 21l			| 120l			| 40000l					| 4000l						| 400l						| 40l						| LocalDateTime.now()			
			"LONG"				| "name4l"	| 400000l					 | 22l			| 121l			| null						| 5000l						| 500l						| 50l						| LocalDateTime.now()			
			"LONG"				| "name5l"	| 500000l					 | 23l			| 122l			| 50000l					| null						| 600l						| 60l						| LocalDateTime.now()			
			"LONG"				| "name6l"	| 500000l					 | 24l			| 123l			| 60000l					| 6000l						| null						| 70l						| LocalDateTime.now()			
			"LONG"				| "name7l"	| 700000l					 | 25l			| 124l			| 70000l					| 7000l						| 700l						| null						| LocalDateTime.now()			
			"LONG"				| "name8l"	| 800000l					 | 26l			| 125l			| 80000l					| 8000l						| 800l						| 80l						| LocalDateTime.now()
			"LONG"				| "name9l"	| 900000l					 | 27l			| 126l			| 90000l					| 9000l						| 900l						| 90l						| LocalDateTime.now()			
			"DOUBLE"			| null		| 100000.0d					 | 21l			| 127l			| 10000.0d					| 1000.0d					| 100.0d					| 10.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name1d"	| null						 | 21l			| 128l			| 20000.0d					| 2000.0d					| 200.0d					| 20.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name2d"	| 200000.0d					 | 30l			| 129l			| 30000.0d					| 3000.0d					| 300.0d					| 30.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name3d"	| 300000.0d					 | 31l			| 130l			| 40000.0d					| 4000.0d					| 400.0d					| 40.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name4d"	| 400000.0d					 | 32l			| 131l			| null						| 5000.0d					| 500.0d					| 50.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name5d"	| 500000.0d					 | 33l			| 132l			| 50000.0d					| null						| 600.0d					| 60.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name6d"	| 600000.0d					 | 34l			| 133l			| 60000.0d					| 6000.0d					| null						| 70.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name7d"	| 700000.0d					 | 35l			| 134l			| 70000.0d					| 7000.0d					| 700.0d					| null						| LocalDateTime.now()			
			"DOUBLE"			| "name8d"	| 800000.0d					 | 36l			| 135l			| 80000.0d					| 8000.0d					| 800.0d					| 80.0d						| null			
			"DOUBLE"			| "name9d"	| 900000.0d					 | 37l			| 136l			| 90000.0d					| 9000.0d					| 900.0d					| 90.0d						| LocalDateTime.now()			

			measureReducer=MeasureReducers.valueOf(measureReducerName).get()
			expected=SimpleFormat.format("MetricSnapshot[measureReducer={}, name={}, accumulatedSamples={}, samplingSize={}, totalHits={}, maxMeasure={}, minMeasure={}, averageMeasure={}, lastMeasure={}, lastOccurrence={}"
											, measureReducer , name, accumulatedSamples, samplingSize, totalHits, maxMeasure, minMeasure, averageMeasure, lastMeasure, lastOccurrence)
	}

	@Unroll
	def "When MetricSnapshot is created with measureReducer:#measureReducer,name:#name,accumulatedSamples:#accumulatedSamples,samplingSize:#samplingSize,totalHits:#totalHits,maxMeasure:#maxMeasure,minMeasure:#minMeasure,averageMeasure:#averageMeasure,lastMeasure:#lastMeasure,lastOccurrence:#lastOccurrence should return the same hashcode"(){
		println(">>>>> MetricSnapshotSpec >>>> When MetricSnapshot is created with measureReducer:$measureReducer,name:$name,accumulatedSamples:$accumulatedSamples,samplingSize:$samplingSize,totalHits:$totalHits,maxMeasure:$maxMeasure,minMeasure:$minMeasure,averageMeasure:$averageMeasure,lastMeasure:$lastMeasure,lastOccurrence:$lastOccurrence should return the same hashcode")

		given:
			def metric1=MetricSnapshot.builder(measureReducer)
											.name(name)
											.accumulatedSamples(accumulatedSamples)
											.samplingSize(samplingSize)
											.totalHits(totalHits)
											.maxMeasure(maxMeasure)
											.minMeasure(minMeasure)
											.averageMeasure(averageMeasure)
											.lastMeasure(lastMeasure)
											.lastOccurrence(lastOccurrence)
										.build()
			def metric2=MetricSnapshot.builder(measureReducer)
											.name(name)
											.accumulatedSamples(accumulatedSamples)
											.samplingSize(samplingSize)
											.totalHits(totalHits)
											.maxMeasure(maxMeasure)
											.minMeasure(minMeasure)
											.averageMeasure(averageMeasure)
											.lastMeasure(lastMeasure)
											.lastOccurrence(lastOccurrence)
										.build()
			
		expect:
			metric1.hashCode()==metric2.hashCode()
			
		where:
			measureReducerName	| name		| accumulatedSamples		 | samplingSize	| totalHits		| maxMeasure				| minMeasure				| averageMeasure			| lastMeasure				| lastOccurrence
			"DURATION"			| null		| Duration.ofSeconds(100000) | 8l			| 17l			| Duration.ofSeconds(10000)	| Duration.ofSeconds(1000)	| Duration.ofSeconds(100)	| Duration.ofSeconds(10)	| LocalDateTime.now()			
			"DURATION"			| "name1"	| null						 | 9l			| 18l			| Duration.ofSeconds(20000)	| Duration.ofSeconds(2000)	| Duration.ofSeconds(200)	| Duration.ofSeconds(20)	| LocalDateTime.now()			
			"DURATION"			| "name2"	| Duration.ofSeconds(200000) | 10l			| 19l			| Duration.ofSeconds(30000)	| Duration.ofSeconds(3000)	| Duration.ofSeconds(300)	| Duration.ofSeconds(30)	| LocalDateTime.now()			
			"DURATION"			| "name3"	| Duration.ofSeconds(300000) | 11l			| 110l			| Duration.ofSeconds(40000)	| Duration.ofSeconds(4000)	| Duration.ofSeconds(400)	| Duration.ofSeconds(40)	| LocalDateTime.now()			
			"DURATION"			| "name4"	| Duration.ofSeconds(400000) | 12l			| 111l			| null						| Duration.ofSeconds(5000)	| Duration.ofSeconds(500)	| Duration.ofSeconds(50)	| LocalDateTime.now()			
			"DURATION"			| "name5"	| Duration.ofSeconds(500000) | 13l			| 112l			| Duration.ofSeconds(50000)	| null						| Duration.ofSeconds(600)	| Duration.ofSeconds(60)	| LocalDateTime.now()			
			"DURATION"			| "name6"	| Duration.ofSeconds(600000) | 14l			| 113l			| Duration.ofSeconds(60000)	| Duration.ofSeconds(6000)	| null						| Duration.ofSeconds(80)	| LocalDateTime.now()			
			"DURATION"			| "name7"	| Duration.ofSeconds(700000) | 15l			| 114l			| Duration.ofSeconds(70000)	| Duration.ofSeconds(7000)	| Duration.ofSeconds(700)	| null						| LocalDateTime.now()			
			"DURATION"			| "name8"	| Duration.ofSeconds(800000) | 16l			| 115l			| Duration.ofSeconds(80000)	| Duration.ofSeconds(8000)	| Duration.ofSeconds(800)	| Duration.ofSeconds(70)	| null			
			"DURATION"			| "name9"	| Duration.ofSeconds(900000) | 17l			| 116l			| Duration.ofSeconds(90000)	| Duration.ofSeconds(9000)	| Duration.ofSeconds(900)	| Duration.ofSeconds(90)	| LocalDateTime.now()			
			"LONG"				| null		| 100000l					 | 18l			| 117l			| 10000l					| 1000l						| 100l						| 10l						| LocalDateTime.now()			
			"LONG"				| "name11"	| null						 | 19l			| 118l			| 20000l					| 2000l						| 200l						| 20l						| LocalDateTime.now()			
			"LONG"				| "name2l"	| 200000l					 | 20l			| 119l			| 30000l					| 3000l						| 300l						| 30l						| LocalDateTime.now()			
			"LONG"				| "name3l"	| 300000l					 | 21l			| 120l			| 40000l					| 4000l						| 400l						| 40l						| LocalDateTime.now()			
			"LONG"				| "name4l"	| 400000l					 | 22l			| 121l			| null						| 5000l						| 500l						| 50l						| LocalDateTime.now()			
			"LONG"				| "name5l"	| 500000l					 | 23l			| 122l			| 50000l					| null						| 600l						| 60l						| LocalDateTime.now()			
			"LONG"				| "name6l"	| 500000l					 | 24l			| 123l			| 60000l					| 6000l						| null						| 70l						| LocalDateTime.now()			
			"LONG"				| "name7l"	| 700000l					 | 25l			| 124l			| 70000l					| 7000l						| 700l						| null						| LocalDateTime.now()			
			"LONG"				| "name8l"	| 800000l					 | 26l			| 125l			| 80000l					| 8000l						| 800l						| 80l						| LocalDateTime.now()
			"LONG"				| "name9l"	| 900000l					 | 27l			| 126l			| 90000l					| 9000l						| 900l						| 90l						| LocalDateTime.now()			
			"DOUBLE"			| null		| 100000.0d					 | 21l			| 127l			| 10000.0d					| 1000.0d					| 100.0d					| 10.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name1d"	| null						 | 21l			| 128l			| 20000.0d					| 2000.0d					| 200.0d					| 20.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name2d"	| 200000.0d					 | 30l			| 129l			| 30000.0d					| 3000.0d					| 300.0d					| 30.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name3d"	| 300000.0d					 | 31l			| 130l			| 40000.0d					| 4000.0d					| 400.0d					| 40.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name4d"	| 400000.0d					 | 32l			| 131l			| null						| 5000.0d					| 500.0d					| 50.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name5d"	| 500000.0d					 | 33l			| 132l			| 50000.0d					| null						| 600.0d					| 60.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name6d"	| 600000.0d					 | 34l			| 133l			| 60000.0d					| 6000.0d					| null						| 70.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name7d"	| 700000.0d					 | 35l			| 134l			| 70000.0d					| 7000.0d					| 700.0d					| null						| LocalDateTime.now()			
			"DOUBLE"			| "name8d"	| 800000.0d					 | 36l			| 135l			| 80000.0d					| 8000.0d					| 800.0d					| 80.0d						| null			
			"DOUBLE"			| "name9d"	| 900000.0d					 | 37l			| 136l			| 90000.0d					| 9000.0d					| 900.0d					| 90.0d						| LocalDateTime.now()			

			measureReducer=MeasureReducers.valueOf(measureReducerName).get()
	}
	@Unroll
	def "When two instances of MetricSnapshot with measureReducer:#measureReducer,name:#name,accumulatedSamples:#accumulatedSamples,samplingSize:#samplingSize,totalHits:#totalHits,maxMeasure:#maxMeasure,minMeasure:#minMeasure,averageMeasure:#averageMeasure,lastMeasure:#lastMeasure,lastOccurrence:#lastOccurrence must return true to equals()"(){
		println(">>>>> MetricSnapshotSpec >>>> When two instances of MetricSnapshot with measureReducer:$measureReducer,name:$name,accumulatedSamples:$accumulatedSamples,samplingSize:$samplingSize,totalHits:$totalHits,maxMeasure:$maxMeasure,minMeasure:$minMeasure,averageMeasure:$averageMeasure,lastMeasure:$lastMeasure,lastOccurrence:$lastOccurrence must return true to equals()")

		given:
			def metric1=MetricSnapshot.builder(measureReducer)
											.name(name)
											.accumulatedSamples(accumulatedSamples)
											.samplingSize(samplingSize)
											.totalHits(totalHits)
											.maxMeasure(maxMeasure)
											.minMeasure(minMeasure)
											.averageMeasure(averageMeasure)
											.lastMeasure(lastMeasure)
											.lastOccurrence(lastOccurrence)
										.build()
			def metric2=MetricSnapshot.builder(measureReducer)
											.name(name)
											.accumulatedSamples(accumulatedSamples)
											.samplingSize(samplingSize)
											.totalHits(totalHits)
											.maxMeasure(maxMeasure)
											.minMeasure(minMeasure)
											.averageMeasure(averageMeasure)
											.lastMeasure(lastMeasure)
											.lastOccurrence(lastOccurrence)
										.build()
			
		expect:
			metric1.equals(metric2)
			metric2.equals(metric1)
			
		where:
			measureReducerName	| name		| accumulatedSamples		 | samplingSize	| totalHits		| maxMeasure				| minMeasure				| averageMeasure			| lastMeasure				| lastOccurrence
			"DURATION"			| null		| Duration.ofSeconds(100000) | 8l			| 17l			| Duration.ofSeconds(10000)	| Duration.ofSeconds(1000)	| Duration.ofSeconds(100)	| Duration.ofSeconds(10)	| LocalDateTime.now()			
			"DURATION"			| "name1"	| null						 | 9l			| 18l			| Duration.ofSeconds(20000)	| Duration.ofSeconds(2000)	| Duration.ofSeconds(200)	| Duration.ofSeconds(20)	| LocalDateTime.now()			
			"DURATION"			| "name2"	| Duration.ofSeconds(200000) | 10l			| 19l			| Duration.ofSeconds(30000)	| Duration.ofSeconds(3000)	| Duration.ofSeconds(300)	| Duration.ofSeconds(30)	| LocalDateTime.now()			
			"DURATION"			| "name3"	| Duration.ofSeconds(300000) | 11l			| 110l			| Duration.ofSeconds(40000)	| Duration.ofSeconds(4000)	| Duration.ofSeconds(400)	| Duration.ofSeconds(40)	| LocalDateTime.now()			
			"DURATION"			| "name4"	| Duration.ofSeconds(400000) | 12l			| 111l			| null						| Duration.ofSeconds(5000)	| Duration.ofSeconds(500)	| Duration.ofSeconds(50)	| LocalDateTime.now()			
			"DURATION"			| "name5"	| Duration.ofSeconds(500000) | 13l			| 112l			| Duration.ofSeconds(50000)	| null						| Duration.ofSeconds(600)	| Duration.ofSeconds(60)	| LocalDateTime.now()			
			"DURATION"			| "name6"	| Duration.ofSeconds(600000) | 14l			| 113l			| Duration.ofSeconds(60000)	| Duration.ofSeconds(6000)	| null						| Duration.ofSeconds(80)	| LocalDateTime.now()			
			"DURATION"			| "name7"	| Duration.ofSeconds(700000) | 15l			| 114l			| Duration.ofSeconds(70000)	| Duration.ofSeconds(7000)	| Duration.ofSeconds(700)	| null						| LocalDateTime.now()			
			"DURATION"			| "name8"	| Duration.ofSeconds(800000) | 16l			| 115l			| Duration.ofSeconds(80000)	| Duration.ofSeconds(8000)	| Duration.ofSeconds(800)	| Duration.ofSeconds(70)	| null			
			"DURATION"			| "name9"	| Duration.ofSeconds(900000) | 17l			| 116l			| Duration.ofSeconds(90000)	| Duration.ofSeconds(9000)	| Duration.ofSeconds(900)	| Duration.ofSeconds(90)	| LocalDateTime.now()			
			"LONG"				| null		| 100000l					 | 18l			| 117l			| 10000l					| 1000l						| 100l						| 10l						| LocalDateTime.now()			
			"LONG"				| "name11"	| null						 | 19l			| 118l			| 20000l					| 2000l						| 200l						| 20l						| LocalDateTime.now()			
			"LONG"				| "name2l"	| 200000l					 | 20l			| 119l			| 30000l					| 3000l						| 300l						| 30l						| LocalDateTime.now()			
			"LONG"				| "name3l"	| 300000l					 | 21l			| 120l			| 40000l					| 4000l						| 400l						| 40l						| LocalDateTime.now()			
			"LONG"				| "name4l"	| 400000l					 | 22l			| 121l			| null						| 5000l						| 500l						| 50l						| LocalDateTime.now()			
			"LONG"				| "name5l"	| 500000l					 | 23l			| 122l			| 50000l					| null						| 600l						| 60l						| LocalDateTime.now()			
			"LONG"				| "name6l"	| 500000l					 | 24l			| 123l			| 60000l					| 6000l						| null						| 70l						| LocalDateTime.now()			
			"LONG"				| "name7l"	| 700000l					 | 25l			| 124l			| 70000l					| 7000l						| 700l						| null						| LocalDateTime.now()			
			"LONG"				| "name8l"	| 800000l					 | 26l			| 125l			| 80000l					| 8000l						| 800l						| 80l						| LocalDateTime.now()
			"LONG"				| "name9l"	| 900000l					 | 27l			| 126l			| 90000l					| 9000l						| 900l						| 90l						| LocalDateTime.now()			
			"DOUBLE"			| null		| 100000.0d					 | 21l			| 127l			| 10000.0d					| 1000.0d					| 100.0d					| 10.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name1d"	| null						 | 21l			| 128l			| 20000.0d					| 2000.0d					| 200.0d					| 20.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name2d"	| 200000.0d					 | 30l			| 129l			| 30000.0d					| 3000.0d					| 300.0d					| 30.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name3d"	| 300000.0d					 | 31l			| 130l			| 40000.0d					| 4000.0d					| 400.0d					| 40.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name4d"	| 400000.0d					 | 32l			| 131l			| null						| 5000.0d					| 500.0d					| 50.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name5d"	| 500000.0d					 | 33l			| 132l			| 50000.0d					| null						| 600.0d					| 60.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name6d"	| 600000.0d					 | 34l			| 133l			| 60000.0d					| 6000.0d					| null						| 70.0d						| LocalDateTime.now()			
			"DOUBLE"			| "name7d"	| 700000.0d					 | 35l			| 134l			| 70000.0d					| 7000.0d					| 700.0d					| null						| LocalDateTime.now()			
			"DOUBLE"			| "name8d"	| 800000.0d					 | 36l			| 135l			| 80000.0d					| 8000.0d					| 800.0d					| 80.0d						| null			
			"DOUBLE"			| "name9d"	| 900000.0d					 | 37l			| 136l			| 90000.0d					| 9000.0d					| 900.0d					| 90.0d						| LocalDateTime.now()			

			measureReducer=MeasureReducers.valueOf(measureReducerName).get()
	}

	@Unroll
	def "When call compareNames(#metric1,#metric2) the result should be #expected"(){
		println(">>>>> MetricSnapshotSpec >>>> When call compareNames($name1,$name2) the result should be $expected")

		when:
			def result=MetricSnapshot.compareNames(metric1,metric2)
		
		then:
			result==expected
			
		where:
			name1	| name2		| expected
			null	| null		| 0
			""		| ""		| 0
			"das"	| "das"		| 0
			null	| ""		| -1
			null	| "das"		| -1
			""		| null		| 1
			"das"	| null		| 1
			"das1"	| "da3s"	| "das1".compareTo("da3s")
			"dasd"	| "da3s"	| "dasd".compareTo("da3s")
			"da5s"	| "da43s"	| "da5s".compareTo("da43s")
			"dvas"	| "da343s"	| "dvas".compareTo("da343s")
			"d4as"	| "dags"	| "d4as".compareTo("dags")
			"da5s"	| "dags"	| "da5s".compareTo("dags")
			"dasff"	| "da54s"	| "dasff".compareTo("da54s")
			"das6"	| "d43as"	| "das6".compareTo("d43as")
			"dash"	| "d32as"	| "dash".compareTo("d32as")
			"dasm"	| "dars"	| "dasm".compareTo("dars")
			"das23"	| "d3as"	| "das23".compareTo("d3as")
			metric1=MetricSnapshot.builder(MeasureReducers.DOUBLE.get())
									.name(name1)
								.build()
			metric2=MetricSnapshot.builder(MeasureReducers.DOUBLE.get())
									.name(name2)
								.build()
	}
}

