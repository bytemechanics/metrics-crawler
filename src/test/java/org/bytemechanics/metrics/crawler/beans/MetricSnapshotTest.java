/*
 * Copyright 2019 Byte Mechanics.
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
package org.bytemechanics.metrics.crawler.beans;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.bytemechanics.metrics.crawler.MeasureReducer;
import org.bytemechanics.metrics.crawler.internal.MeasureReducers;
import org.bytemechanics.metrics.crawler.internal.commons.functional.LambdaUnchecker;
import org.bytemechanics.metrics.crawler.internal.commons.string.SimpleFormat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 *
 * @author afarre
 */
public class MetricSnapshotTest {

	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> MetricSnapshotTest >>>> setup");
		try(InputStream inputStream = LambdaUnchecker.class.getResourceAsStream("/logging.properties")){
			LogManager.getLogManager().readConfiguration(inputStream);
		}catch (final IOException e){
			Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
			Logger.getAnonymousLogger().severe(e.getMessage());
		}
	}
	
	@BeforeEach
    void beforeEachTest(final TestInfo testInfo) {
        System.out.println(">>>>> "+this.getClass().getSimpleName()+" >>>> "+testInfo.getTestMethod().map(Method::getName).orElse("Unkown")+""+testInfo.getTags().toString()+" >>>> "+testInfo.getDisplayName());
    }
	
	static Stream<Arguments> metricSnapshotBuilderDatapack() {
	    return Stream.of(
					Arguments.of(MeasureReducers.DURATION.get(Duration.class),null	,Duration.ofSeconds(100000),8l,17l,Duration.ofSeconds(10000),Duration.ofSeconds(1000)	,Duration.ofSeconds(100),Duration.ofSeconds(10),LocalDateTime.now()),		
					Arguments.of(MeasureReducers.DURATION.get(Duration.class),"name1",null,9l,18l,Duration.ofSeconds(20000)	,Duration.ofSeconds(2000)	,Duration.ofSeconds(200),Duration.ofSeconds(20),LocalDateTime.now()),		
					Arguments.of(MeasureReducers.DURATION.get(Duration.class),"name2",Duration.ofSeconds(200000),10l,19l,Duration.ofSeconds(30000)	,Duration.ofSeconds(3000)	,Duration.ofSeconds(300),Duration.ofSeconds(30),LocalDateTime.now()),		
					Arguments.of(MeasureReducers.DURATION.get(Duration.class),"name3",Duration.ofSeconds(300000),11l,110l,Duration.ofSeconds(40000),Duration.ofSeconds(4000)	,Duration.ofSeconds(400),Duration.ofSeconds(40),LocalDateTime.now()),		
					Arguments.of(MeasureReducers.DURATION.get(Duration.class),"name4",Duration.ofSeconds(400000),12l,111l,null,Duration.ofSeconds(5000)	,Duration.ofSeconds(500),Duration.ofSeconds(50),LocalDateTime.now()),		
					Arguments.of(MeasureReducers.DURATION.get(Duration.class),"name5",Duration.ofSeconds(500000),13l,112l,Duration.ofSeconds(50000),null,Duration.ofSeconds(600),Duration.ofSeconds(60),LocalDateTime.now()),		
					Arguments.of(MeasureReducers.DURATION.get(Duration.class),"name6",Duration.ofSeconds(600000),14l,113l,Duration.ofSeconds(60000),Duration.ofSeconds(6000)	,null,Duration.ofSeconds(80),LocalDateTime.now()),		
					Arguments.of(MeasureReducers.DURATION.get(Duration.class),"name7",Duration.ofSeconds(700000),15l,114l,Duration.ofSeconds(70000),Duration.ofSeconds(7000)	,Duration.ofSeconds(700),null,LocalDateTime.now()),		
					Arguments.of(MeasureReducers.DURATION.get(Duration.class),"name8",Duration.ofSeconds(800000),16l,115l,Duration.ofSeconds(80000),Duration.ofSeconds(8000)	,Duration.ofSeconds(800),Duration.ofSeconds(70),null),
					Arguments.of(MeasureReducers.DURATION.get(Duration.class),"name9",Duration.ofSeconds(900000),17l,116l,Duration.ofSeconds(90000),Duration.ofSeconds(9000)	,Duration.ofSeconds(900),Duration.ofSeconds(90),LocalDateTime.now()),	
					Arguments.of(MeasureReducers.LONG.get(Long.class),null	,100000l,18l,117l,10000l,1000l,100l,10l,LocalDateTime.now()),		
					Arguments.of(MeasureReducers.LONG.get(Long.class),"name11",null	,19l,118l,20000l,2000l,200l,20l,LocalDateTime.now()),		
					Arguments.of(MeasureReducers.LONG.get(Long.class),"name2l",200000l,20l,119l,30000l,3000l,300l,30l,LocalDateTime.now()),		
					Arguments.of(MeasureReducers.LONG.get(Long.class),"name3l",300000l,21l,120l,40000l,4000l,400l,40l,LocalDateTime.now()),		
					Arguments.of(MeasureReducers.LONG.get(Long.class),"name4l",400000l,22l,121l,null	,5000l,500l,50l,LocalDateTime.now()),		
					Arguments.of(MeasureReducers.LONG.get(Long.class),"name5l",500000l,23l,122l,50000l,null,600l,60l,LocalDateTime.now()),		
					Arguments.of(MeasureReducers.LONG.get(Long.class),"name6l",500000l,24l,123l,60000l,6000l,null,70l,LocalDateTime.now()),		
					Arguments.of(MeasureReducers.LONG.get(Long.class),"name7l",700000l,25l,124l,70000l,7000l,700l,null,LocalDateTime.now()),		
					Arguments.of(MeasureReducers.LONG.get(Long.class),"name8l",800000l,26l,125l,80000l,8000l,800l,80l,LocalDateTime.now()),
					Arguments.of(MeasureReducers.LONG.get(Long.class),"name9l",900000l,27l,126l,90000l,9000l,900l,90l,LocalDateTime.now()),		
					Arguments.of(MeasureReducers.DOUBLE.get(Double.class),null	,100000.0d,21l,127l,10000.0d,1000.0d,100.0d,10.0d,LocalDateTime.now()),		
					Arguments.of(MeasureReducers.DOUBLE.get(Double.class),"name1d",null	,21l,128l,20000.0d,2000.0d,200.0d,20.0d,LocalDateTime.now()),		
					Arguments.of(MeasureReducers.DOUBLE.get(Double.class),"name2d",200000.0d,30l,129l,30000.0d,3000.0d,300.0d,30.0d,LocalDateTime.now()),		
					Arguments.of(MeasureReducers.DOUBLE.get(Double.class),"name3d",300000.0d,31l,130l,40000.0d,4000.0d,400.0d,40.0d,LocalDateTime.now()),		
					Arguments.of(MeasureReducers.DOUBLE.get(Double.class),"name4d",400000.0d,32l,131l,null	,5000.0d,500.0d,50.0d,LocalDateTime.now()),		
					Arguments.of(MeasureReducers.DOUBLE.get(Double.class),"name5d",500000.0d,33l,132l,50000.0d,null	,600.0d,60.0d,LocalDateTime.now()),		
					Arguments.of(MeasureReducers.DOUBLE.get(Double.class),"name6d",600000.0d,34l,133l,60000.0d,6000.0d,null,70.0d	,LocalDateTime.now()),		
					Arguments.of(MeasureReducers.DOUBLE.get(Double.class),"name7d",700000.0d,35l,134l,70000.0d,7000.0d,700.0d,null,LocalDateTime.now()),		
					Arguments.of(MeasureReducers.DOUBLE.get(Double.class),"name8d",800000.0d,36l,135l,80000.0d,8000.0d,800.0d,80.0d,null),
					Arguments.of(MeasureReducers.DOUBLE.get(Double.class),"name9d",900000.0d,37l,136l,90000.0d,9000.0d,900.0d,90.0d,LocalDateTime.now())		
				);
	}

	@ParameterizedTest(name ="When MetricSnapshot is created from another MetricSnapshot with measureReducer:{0},name:{1},accumulatedSamples:{2},samplingSize:{3},totalHits:{4},maxMeasure:{5},minMeasure:{6},averageMeasure:{7},lastMeasure:{8},lastOccurrence:{9} should clone the instance")
	@MethodSource("metricSnapshotBuilderDatapack")
	public <T> void buildFromAnother(final MeasureReducer<T> _measureReducer,final String _name,final T _accumulatedSamples,final long _samplingSize,final long _totalHits,final T _maxMeasure,final T _minMeasure,final T _averageMeasure,final T _lastMeasure,final LocalDateTime _lastOccurrence){
		
		LocalDateTime now=LocalDateTime.now();
		MetricSnapshot metric=MetricSnapshot.builder(_measureReducer,MetricSnapshot.builder(_measureReducer)
																						.name(_name)
																						.accumulatedSamples(_accumulatedSamples)
																						.samplingSize(_samplingSize)
																						.totalHits(_totalHits)
																						.maxMeasure(_maxMeasure)
																						.minMeasure(_minMeasure)
																						.averageMeasure(_averageMeasure)
																						.lastMeasure(_lastMeasure)
																						.lastOccurrence(_lastOccurrence)
																					.build())
											.name("my-name")
											.lastOccurrence(now)
											.samplingSize(2)
											.totalHits(4)
											.build();
		
		Assertions.assertEquals("my-name",metric.getName());
		Assertions.assertEquals(_accumulatedSamples,metric.getAccumulatedSamples());
		Assertions.assertEquals(2,metric.getSamplingSize());
		Assertions.assertEquals(4,metric.getTotalHits());
		Assertions.assertEquals(_maxMeasure,metric.getMaxMeasure());
		Assertions.assertEquals(_minMeasure,metric.getMinMeasure());
		Assertions.assertEquals(_averageMeasure,metric.getAverageMeasure());
		Assertions.assertEquals(_lastMeasure,metric.getLastMeasure());
		Assertions.assertEquals(now,metric.getLastOccurrence());
	}

	@ParameterizedTest(name ="When reduce two metrics with measureReducer:{0},name:{1},accumulatedSamples:{2},samplingSize:{3},totalHits:{4},maxMeasure:{5},minMeasure:{6},averageMeasure:{7},lastMeasure:{8},lastOccurrence:{9} should accumulate results")
	@MethodSource("metricSnapshotBuilderDatapack")
	public <T> void reduce(final MeasureReducer<T> _measureReducer,final String _name,final T _accumulatedSamples,final long _samplingSize,final long _totalHits,final T _maxMeasure,final T _minMeasure,final T _averageMeasure,final T _lastMeasure,final LocalDateTime _lastOccurrence) throws InterruptedException{
		
		LocalDateTime now=LocalDateTime.now();
		MetricSnapshot metric1=MetricSnapshot.builder(_measureReducer)
												.name(_name)
												.accumulatedSamples(_accumulatedSamples)
												.samplingSize(_samplingSize)
												.totalHits(_totalHits)
												.maxMeasure(_maxMeasure)
												.minMeasure(_minMeasure)
												.averageMeasure(_averageMeasure)
												.lastMeasure(_lastMeasure)
												.lastOccurrence(_lastOccurrence)
											.build();
		MetricSnapshot metric2=MetricSnapshot.builder(_measureReducer)
												.name("my-name2")
												.accumulatedSamples(_accumulatedSamples)
												.samplingSize(1000)
												.totalHits(200)
												.maxMeasure(_minMeasure)
												.minMeasure(_maxMeasure)
												.averageMeasure(_averageMeasure)
												.lastMeasure(_averageMeasure)
												.lastOccurrence(now)
											.build();
		Thread.sleep(10l);
		MetricSnapshot result=metric1.reduce(metric2);
		Assertions.assertEquals(_name,result.getName());
		Assertions.assertEquals(_measureReducer.accumulate(_accumulatedSamples,_accumulatedSamples)
												.orElseGet(_measureReducer::identity)
								,result.getAccumulatedSamples());
		Assertions.assertEquals(_samplingSize+1000,result.getSamplingSize());
		Assertions.assertEquals(_totalHits+200,result.getTotalHits());
		Assertions.assertEquals(_measureReducer.max(_maxMeasure,_minMeasure)
													.orElseGet(_measureReducer::identity)
								,result.getMaxMeasure());
		Assertions.assertEquals(_measureReducer.min(_minMeasure,_maxMeasure)
													.orElseGet(_measureReducer::identity)
								,result.getMinMeasure());
		Assertions.assertEquals(_measureReducer.accumulate(_accumulatedSamples, _accumulatedSamples)
												.flatMap(total -> _measureReducer.average(total,_samplingSize+1000))
												.orElseGet(_measureReducer::identity)
								,result.getAverageMeasure());
		Assertions.assertEquals(_averageMeasure,result.getLastMeasure());
		Assertions.assertEquals(now,result.getLastOccurrence());
		Assertions.assertTrue(result.getSnapshotTimestamp().isAfter(metric1.getSnapshotTimestamp()));
		Assertions.assertTrue(result.getSnapshotTimestamp().isAfter(metric2.getSnapshotTimestamp()));
	}

	@ParameterizedTest(name ="When MetricSnapshot is created with measureReducer:{0},name:{1},accumulatedSamples:{2},samplingSize:{3},totalHits:{4},maxMeasure:{5},minMeasure:{6},averageMeasure:{7},lastMeasure:{8},lastOccurrence:{9} then getName() should return {1}")
	@MethodSource("metricSnapshotBuilderDatapack")
	public <T> void getName(final MeasureReducer<T> _measureReducer,final String _name,final T _accumulatedSamples,final long _samplingSize,final long _totalHits,final T _maxMeasure,final T _minMeasure,final T _averageMeasure,final T _lastMeasure,final LocalDateTime _lastOccurrence){
		
		Assertions.assertEquals(_name
								,MetricSnapshot.builder(_measureReducer)
													.name(_name)
													.accumulatedSamples(_accumulatedSamples)
													.samplingSize(_samplingSize)
													.totalHits(_totalHits)
													.maxMeasure(_maxMeasure)
													.minMeasure(_minMeasure)
													.averageMeasure(_averageMeasure)
													.lastMeasure(_lastMeasure)
													.lastOccurrence(_lastOccurrence)
												.build()
													.getName());

	}

	@ParameterizedTest(name ="When MetricSnapshot is created with measureReducer:{0},name:{1},accumulatedSamples:{2},samplingSize:{3},totalHits:{4},maxMeasure:{5},minMeasure:{6},averageMeasure:{7},lastMeasure:{8},lastOccurrence:{9} then getAccumulatedSamples() should return {2}")
	@MethodSource("metricSnapshotBuilderDatapack")
	public <T> void getAccumulatedSamples(final MeasureReducer<T> _measureReducer,final String _name,final T _accumulatedSamples,final long _samplingSize,final long _totalHits,final T _maxMeasure,final T _minMeasure,final T _averageMeasure,final T _lastMeasure,final LocalDateTime _lastOccurrence){
		
		Assertions.assertEquals(_accumulatedSamples
								,MetricSnapshot.builder(_measureReducer)
													.name(_name)
													.accumulatedSamples(_accumulatedSamples)
													.samplingSize(_samplingSize)
													.totalHits(_totalHits)
													.maxMeasure(_maxMeasure)
													.minMeasure(_minMeasure)
													.averageMeasure(_averageMeasure)
													.lastMeasure(_lastMeasure)
													.lastOccurrence(_lastOccurrence)
												.build()
													.getAccumulatedSamples());

	}
	@ParameterizedTest(name ="When MetricSnapshot is created with measureReducer:{0},name:{1},accumulatedSamples:{2},samplingSize:{3},totalHits:{4},maxMeasure:{5},minMeasure:{6},averageMeasure:{7},lastMeasure:{8},lastOccurrence:{9} then getFormatedAccumulatedSamples() should return {2}")
	@MethodSource("metricSnapshotBuilderDatapack")
	public <T> void getFormatedAccumulatedSamples(final MeasureReducer<T> _measureReducer,final String _name,final T _accumulatedSamples,final long _samplingSize,final long _totalHits,final T _maxMeasure,final T _minMeasure,final T _averageMeasure,final T _lastMeasure,final LocalDateTime _lastOccurrence){
		
		Assertions.assertEquals(_measureReducer.toString(_accumulatedSamples)
								,MetricSnapshot.builder(_measureReducer)
													.name(_name)
													.accumulatedSamples(_accumulatedSamples)
													.samplingSize(_samplingSize)
													.totalHits(_totalHits)
													.maxMeasure(_maxMeasure)
													.minMeasure(_minMeasure)
													.averageMeasure(_averageMeasure)
													.lastMeasure(_lastMeasure)
													.lastOccurrence(_lastOccurrence)
												.build()
													.getFormatedAccumulatedSamples());
	}

	@ParameterizedTest(name ="When MetricSnapshot is created with measureReducer:{0},name:{1},accumulatedSamples:{2},samplingSize:{3},totalHits:{4},maxMeasure:{5},minMeasure:{6},averageMeasure:{7},lastMeasure:{8},lastOccurrence:{9} then getSamplingSize() should return {3}")
	@MethodSource("metricSnapshotBuilderDatapack")
	public <T> void getSamplingSize(final MeasureReducer<T> _measureReducer,final String _name,final T _accumulatedSamples,final long _samplingSize,final long _totalHits,final T _maxMeasure,final T _minMeasure,final T _averageMeasure,final T _lastMeasure,final LocalDateTime _lastOccurrence){
		
		Assertions.assertEquals(_samplingSize
								,MetricSnapshot.builder(_measureReducer)
													.name(_name)
													.accumulatedSamples(_accumulatedSamples)
													.samplingSize(_samplingSize)
													.totalHits(_totalHits)
													.maxMeasure(_maxMeasure)
													.minMeasure(_minMeasure)
													.averageMeasure(_averageMeasure)
													.lastMeasure(_lastMeasure)
													.lastOccurrence(_lastOccurrence)
												.build()
													.getSamplingSize());

	}
	
	@ParameterizedTest(name ="When MetricSnapshot is created with measureReducer:{0},name:{1},accumulatedSamples:{2},samplingSize:{3},totalHits:{4},maxMeasure:{5},minMeasure:{6},averageMeasure:{7},lastMeasure:{8},lastOccurrence:{9} then getTotalHits() should return {2}")
	@MethodSource("metricSnapshotBuilderDatapack")
	public <T> void getTotalHits(final MeasureReducer<T> _measureReducer,final String _name,final T _accumulatedSamples,final long _samplingSize,final long _totalHits,final T _maxMeasure,final T _minMeasure,final T _averageMeasure,final T _lastMeasure,final LocalDateTime _lastOccurrence){
		
		Assertions.assertEquals(_totalHits
								,MetricSnapshot.builder(_measureReducer)
													.name(_name)
													.accumulatedSamples(_accumulatedSamples)
													.samplingSize(_samplingSize)
													.totalHits(_totalHits)
													.maxMeasure(_maxMeasure)
													.minMeasure(_minMeasure)
													.averageMeasure(_averageMeasure)
													.lastMeasure(_lastMeasure)
													.lastOccurrence(_lastOccurrence)
												.build()
													.getTotalHits());
	}

	@ParameterizedTest(name ="When MetricSnapshot is created with measureReducer:{0},name:{1},accumulatedSamples:{2},samplingSize:{3},totalHits:{4},maxMeasure:{5},minMeasure:{6},averageMeasure:{7},lastMeasure:{8},lastOccurrence:{9} then getMaxMeasure() should return {5}")
	@MethodSource("metricSnapshotBuilderDatapack")
	public <T> void getMaxMeasure(final MeasureReducer<T> _measureReducer,final String _name,final T _accumulatedSamples,final long _samplingSize,final long _totalHits,final T _maxMeasure,final T _minMeasure,final T _averageMeasure,final T _lastMeasure,final LocalDateTime _lastOccurrence){
		
		Assertions.assertEquals(_maxMeasure
								,MetricSnapshot.builder(_measureReducer)
													.name(_name)
													.accumulatedSamples(_accumulatedSamples)
													.samplingSize(_samplingSize)
													.totalHits(_totalHits)
													.maxMeasure(_maxMeasure)
													.minMeasure(_minMeasure)
													.averageMeasure(_averageMeasure)
													.lastMeasure(_lastMeasure)
													.lastOccurrence(_lastOccurrence)
												.build()
													.getMaxMeasure());

	}
	@ParameterizedTest(name ="When MetricSnapshot is created with measureReducer:{0},name:{1},accumulatedSamples:{2},samplingSize:{3},totalHits:{4},maxMeasure:{5},minMeasure:{6},averageMeasure:{7},lastMeasure:{8},lastOccurrence:{9} then getFormatedMaxMeasure() should return {5}")
	@MethodSource("metricSnapshotBuilderDatapack")
	public <T> void getFormatedMaxMeasure(final MeasureReducer<T> _measureReducer,final String _name,final T _accumulatedSamples,final long _samplingSize,final long _totalHits,final T _maxMeasure,final T _minMeasure,final T _averageMeasure,final T _lastMeasure,final LocalDateTime _lastOccurrence){
		
		Assertions.assertEquals(_measureReducer.toString(_maxMeasure)
								,MetricSnapshot.builder(_measureReducer)
													.name(_name)
													.accumulatedSamples(_accumulatedSamples)
													.samplingSize(_samplingSize)
													.totalHits(_totalHits)
													.maxMeasure(_maxMeasure)
													.minMeasure(_minMeasure)
													.averageMeasure(_averageMeasure)
													.lastMeasure(_lastMeasure)
													.lastOccurrence(_lastOccurrence)
												.build()
													.getFormatedMaxMeasure());
	}

	@ParameterizedTest(name ="When MetricSnapshot is created with measureReducer:{0},name:{1},accumulatedSamples:{2},samplingSize:{3},totalHits:{4},maxMeasure:{5},minMeasure:{6},averageMeasure:{7},lastMeasure:{8},lastOccurrence:{9} then getMinMeasure() should return {6}")
	@MethodSource("metricSnapshotBuilderDatapack")
	public <T> void getMinMeasure(final MeasureReducer<T> _measureReducer,final String _name,final T _accumulatedSamples,final long _samplingSize,final long _totalHits,final T _maxMeasure,final T _minMeasure,final T _averageMeasure,final T _lastMeasure,final LocalDateTime _lastOccurrence){
		
		Assertions.assertEquals(_minMeasure
								,MetricSnapshot.builder(_measureReducer)
													.name(_name)
													.accumulatedSamples(_accumulatedSamples)
													.samplingSize(_samplingSize)
													.totalHits(_totalHits)
													.maxMeasure(_maxMeasure)
													.minMeasure(_minMeasure)
													.averageMeasure(_averageMeasure)
													.lastMeasure(_lastMeasure)
													.lastOccurrence(_lastOccurrence)
												.build()
													.getMinMeasure());

	}
	@ParameterizedTest(name ="When MetricSnapshot is created with measureReducer:{0},name:{1},accumulatedSamples:{2},samplingSize:{3},totalHits:{4},maxMeasure:{5},minMeasure:{6},averageMeasure:{7},lastMeasure:{8},lastOccurrence:{9} then getFormatedMinMeasure() should return {6}")
	@MethodSource("metricSnapshotBuilderDatapack")
	public <T> void getFormatedMinMeasure(final MeasureReducer<T> _measureReducer,final String _name,final T _accumulatedSamples,final long _samplingSize,final long _totalHits,final T _maxMeasure,final T _minMeasure,final T _averageMeasure,final T _lastMeasure,final LocalDateTime _lastOccurrence){
		
		Assertions.assertEquals(_measureReducer.toString(_minMeasure)
								,MetricSnapshot.builder(_measureReducer)
													.name(_name)
													.accumulatedSamples(_accumulatedSamples)
													.samplingSize(_samplingSize)
													.totalHits(_totalHits)
													.maxMeasure(_maxMeasure)
													.minMeasure(_minMeasure)
													.averageMeasure(_averageMeasure)
													.lastMeasure(_lastMeasure)
													.lastOccurrence(_lastOccurrence)
												.build()
													.getFormatedMinMeasure());
	}
	
	@ParameterizedTest(name ="When MetricSnapshot is created with measureReducer:{0},name:{1},accumulatedSamples:{2},samplingSize:{3},totalHits:{4},maxMeasure:{5},minMeasure:{6},averageMeasure:{7},lastMeasure:{8},lastOccurrence:{9} then getAverageMeasure() should return {7}")
	@MethodSource("metricSnapshotBuilderDatapack")
	public <T> void getAverageMeasure(final MeasureReducer<T> _measureReducer,final String _name,final T _accumulatedSamples,final long _samplingSize,final long _totalHits,final T _maxMeasure,final T _minMeasure,final T _averageMeasure,final T _lastMeasure,final LocalDateTime _lastOccurrence){
		
		Assertions.assertEquals(_averageMeasure
								,MetricSnapshot.builder(_measureReducer)
													.name(_name)
													.accumulatedSamples(_accumulatedSamples)
													.samplingSize(_samplingSize)
													.totalHits(_totalHits)
													.maxMeasure(_maxMeasure)
													.minMeasure(_minMeasure)
													.averageMeasure(_averageMeasure)
													.lastMeasure(_lastMeasure)
													.lastOccurrence(_lastOccurrence)
												.build()
													.getAverageMeasure());

	}
	@ParameterizedTest(name ="When MetricSnapshot is created with measureReducer:{0},name:{1},accumulatedSamples:{2},samplingSize:{3},totalHits:{4},maxMeasure:{5},minMeasure:{6},averageMeasure:{7},lastMeasure:{8},lastOccurrence:{9} then getFormatedAverageMeasure() should return {7}")
	@MethodSource("metricSnapshotBuilderDatapack")
	public <T> void getFormatedAverageMeasure(final MeasureReducer<T> _measureReducer,final String _name,final T _accumulatedSamples,final long _samplingSize,final long _totalHits,final T _maxMeasure,final T _minMeasure,final T _averageMeasure,final T _lastMeasure,final LocalDateTime _lastOccurrence){
		
		Assertions.assertEquals(_measureReducer.toString(_averageMeasure)
								,MetricSnapshot.builder(_measureReducer)
													.name(_name)
													.accumulatedSamples(_accumulatedSamples)
													.samplingSize(_samplingSize)
													.totalHits(_totalHits)
													.maxMeasure(_maxMeasure)
													.minMeasure(_minMeasure)
													.averageMeasure(_averageMeasure)
													.lastMeasure(_lastMeasure)
													.lastOccurrence(_lastOccurrence)
												.build()
													.getFormatedAverageMeasure());
	}
	
	@ParameterizedTest(name ="When MetricSnapshot is created with measureReducer:{0},name:{1},accumulatedSamples:{2},samplingSize:{3},totalHits:{4},maxMeasure:{5},minMeasure:{6},averageMeasure:{7},lastMeasure:{8},lastOccurrence:{9} then getLastMeasure() should return {8}")
	@MethodSource("metricSnapshotBuilderDatapack")
	public <T> void getLastMeasure(final MeasureReducer<T> _measureReducer,final String _name,final T _accumulatedSamples,final long _samplingSize,final long _totalHits,final T _maxMeasure,final T _minMeasure,final T _averageMeasure,final T _lastMeasure,final LocalDateTime _lastOccurrence){
		
		Assertions.assertEquals(_lastMeasure
								,MetricSnapshot.builder(_measureReducer)
													.name(_name)
													.accumulatedSamples(_accumulatedSamples)
													.samplingSize(_samplingSize)
													.totalHits(_totalHits)
													.maxMeasure(_maxMeasure)
													.minMeasure(_minMeasure)
													.averageMeasure(_averageMeasure)
													.lastMeasure(_lastMeasure)
													.lastOccurrence(_lastOccurrence)
												.build()
													.getLastMeasure());

	}
	@ParameterizedTest(name ="When MetricSnapshot is created with measureReducer:{0},name:{1},accumulatedSamples:{2},samplingSize:{3},totalHits:{4},maxMeasure:{5},minMeasure:{6},averageMeasure:{7},lastMeasure:{8},lastOccurrence:{9} then getFormatedLastMeasure() should return {8}")
	@MethodSource("metricSnapshotBuilderDatapack")
	public <T> void getFormatedLastMeasure(final MeasureReducer<T> _measureReducer,final String _name,final T _accumulatedSamples,final long _samplingSize,final long _totalHits,final T _maxMeasure,final T _minMeasure,final T _averageMeasure,final T _lastMeasure,final LocalDateTime _lastOccurrence){
		
		Assertions.assertEquals(_measureReducer.toString(_lastMeasure)
								,MetricSnapshot.builder(_measureReducer)
													.name(_name)
													.accumulatedSamples(_accumulatedSamples)
													.samplingSize(_samplingSize)
													.totalHits(_totalHits)
													.maxMeasure(_maxMeasure)
													.minMeasure(_minMeasure)
													.averageMeasure(_averageMeasure)
													.lastMeasure(_lastMeasure)
													.lastOccurrence(_lastOccurrence)
												.build()
													.getFormatedLastMeasure());
	}

	@ParameterizedTest(name ="When MetricSnapshot is created with measureReducer:{0},name:{1},accumulatedSamples:{2},samplingSize:{3},totalHits:{4},maxMeasure:{5},minMeasure:{6},averageMeasure:{7},lastMeasure:{8},lastOccurrence:{9} then getLastOccurrence() should return {9}")
	@MethodSource("metricSnapshotBuilderDatapack")
	public <T> void getLastOccurrence(final MeasureReducer<T> _measureReducer,final String _name,final T _accumulatedSamples,final long _samplingSize,final long _totalHits,final T _maxMeasure,final T _minMeasure,final T _averageMeasure,final T _lastMeasure,final LocalDateTime _lastOccurrence){
		
		Assertions.assertEquals(_lastOccurrence
								,MetricSnapshot.builder(_measureReducer)
													.name(_name)
													.accumulatedSamples(_accumulatedSamples)
													.samplingSize(_samplingSize)
													.totalHits(_totalHits)
													.maxMeasure(_maxMeasure)
													.minMeasure(_minMeasure)
													.averageMeasure(_averageMeasure)
													.lastMeasure(_lastMeasure)
													.lastOccurrence(_lastOccurrence)
												.build()
													.getLastOccurrence());

	}
	static Stream<Arguments> metricSnapshotBuilderToStringDatapack() {
	    return metricSnapshotBuilderDatapack()
					.map(Arguments::get)
					.map(Arrays::asList)
					.map(ArrayList::new)
					.filter(list -> list.add(SimpleFormat.format("MetricSnapshot[measureReducer={}, name={}, accumulatedSamples={}, samplingSize={}, totalHits={}, maxMeasure={}, minMeasure={}, averageMeasure={}, lastMeasure={}, lastOccurrence={}"
																,list.get(0),list.get(1),list.get(2),list.get(3),list.get(4),list.get(5),list.get(6),list.get(7),list.get(8),list.get(9))))
					.map(ArrayList::toArray)
					.map(Arguments::of);
	}

	@ParameterizedTest(name ="When MetricSnapshot is created with measureReducer:{0},name:{1},accumulatedSamples:{2},samplingSize:{3},totalHits:{4},maxMeasure:{5},minMeasure:{6},averageMeasure:{7},lastMeasure:{8},lastOccurrence:{9} then toString() should return {10}")
	@MethodSource("metricSnapshotBuilderToStringDatapack")
	public <T> void toString(final MeasureReducer<T> _measureReducer,final String _name,final T _accumulatedSamples,final long _samplingSize,final long _totalHits,final T _maxMeasure,final T _minMeasure,final T _averageMeasure,final T _lastMeasure,final LocalDateTime _lastOccurrence,final String _expected){
		
		Assertions.assertEquals(_expected
								,MetricSnapshot.builder(_measureReducer)
													.name(_name)
													.accumulatedSamples(_accumulatedSamples)
													.samplingSize(_samplingSize)
													.totalHits(_totalHits)
													.maxMeasure(_maxMeasure)
													.minMeasure(_minMeasure)
													.averageMeasure(_averageMeasure)
													.lastMeasure(_lastMeasure)
													.lastOccurrence(_lastOccurrence)
												.build()
													.toString());

	}

	@ParameterizedTest(name ="Two distinct instances from measureReducer:{0},name:{1},accumulatedSamples:{2},samplingSize:{3},totalHits:{4},maxMeasure:{5},minMeasure:{6},averageMeasure:{7},lastMeasure:{8},lastOccurrence:{9} should retrieve the same hashcode")
	@MethodSource("metricSnapshotBuilderDatapack")
	public <T> void hashcode(final MeasureReducer<T> _measureReducer,final String _name,final T _accumulatedSamples,final long _samplingSize,final long _totalHits,final T _maxMeasure,final T _minMeasure,final T _averageMeasure,final T _lastMeasure,final LocalDateTime _lastOccurrence){
		
		MetricSnapshot base=MetricSnapshot.builder(_measureReducer)
												.name(_name)
												.accumulatedSamples(_accumulatedSamples)
												.samplingSize(_samplingSize)
												.totalHits(_totalHits)
												.maxMeasure(_maxMeasure)
												.minMeasure(_minMeasure)
												.averageMeasure(_averageMeasure)
												.lastMeasure(_lastMeasure)
												.lastOccurrence(_lastOccurrence)
											.build();
		Assertions.assertEquals(base.hashCode()
									,MetricSnapshot.builder(_measureReducer)
													.name(_name)
													.accumulatedSamples(_accumulatedSamples)
													.samplingSize(_samplingSize)
													.totalHits(_totalHits)
													.maxMeasure(_maxMeasure)
													.minMeasure(_minMeasure)
													.averageMeasure(_averageMeasure)
													.lastMeasure(_lastMeasure)
													.lastOccurrence(_lastOccurrence)
												.build()
													.hashCode());
		Assertions.assertNotEquals(base.hashCode()
									,MetricSnapshot.builder(_measureReducer)
													.name("af")
													.accumulatedSamples(_accumulatedSamples)
													.samplingSize(_samplingSize)
													.totalHits(_totalHits)
													.maxMeasure(_maxMeasure)
													.minMeasure(_minMeasure)
													.averageMeasure(_averageMeasure)
													.lastMeasure(_lastMeasure)
													.lastOccurrence(_lastOccurrence)
												.build()
													.hashCode());
		Assertions.assertNotEquals(base.hashCode()
									,MetricSnapshot.builder(_measureReducer)
													.name(_name)
													.accumulatedSamples(1000)
													.samplingSize(_samplingSize)
													.totalHits(_totalHits)
													.maxMeasure(_maxMeasure)
													.minMeasure(_minMeasure)
													.averageMeasure(_averageMeasure)
													.lastMeasure(_lastMeasure)
													.lastOccurrence(_lastOccurrence)
												.build()
													.hashCode());
		Assertions.assertNotEquals(base.hashCode()
									,MetricSnapshot.builder(_measureReducer)
													.name(_name)
													.accumulatedSamples(_accumulatedSamples)
													.samplingSize(29)
													.totalHits(_totalHits)
													.maxMeasure(_maxMeasure)
													.minMeasure(_minMeasure)
													.averageMeasure(_averageMeasure)
													.lastMeasure(_lastMeasure)
													.lastOccurrence(_lastOccurrence)
												.build()
													.hashCode());
		Assertions.assertNotEquals(base.hashCode()
									,MetricSnapshot.builder(_measureReducer)
													.name(_name)
													.accumulatedSamples(_accumulatedSamples)
													.samplingSize(_samplingSize)
													.totalHits(34)
													.maxMeasure(_maxMeasure)
													.minMeasure(_minMeasure)
													.averageMeasure(_averageMeasure)
													.lastMeasure(_lastMeasure)
													.lastOccurrence(_lastOccurrence)
												.build()
													.hashCode());
		Assertions.assertNotEquals(base.hashCode()
									,MetricSnapshot.builder(_measureReducer)
													.name(_name)
													.accumulatedSamples(_accumulatedSamples)
													.samplingSize(_samplingSize)
													.totalHits(_totalHits)
													.maxMeasure(_minMeasure)
													.minMeasure(_minMeasure)
													.averageMeasure(_averageMeasure)
													.lastMeasure(_lastMeasure)
													.lastOccurrence(_lastOccurrence)
												.build()
													.hashCode());
		Assertions.assertNotEquals(base.hashCode()
									,MetricSnapshot.builder(_measureReducer)
													.name(_name)
													.accumulatedSamples(_accumulatedSamples)
													.samplingSize(_samplingSize)
													.totalHits(_totalHits)
													.maxMeasure(_maxMeasure)
													.minMeasure(_maxMeasure)
													.averageMeasure(_averageMeasure)
													.lastMeasure(_lastMeasure)
													.lastOccurrence(_lastOccurrence)
												.build()
													.hashCode());
		Assertions.assertNotEquals(base.hashCode()
									,MetricSnapshot.builder(_measureReducer)
													.name(_name)
													.accumulatedSamples(_accumulatedSamples)
													.samplingSize(_samplingSize)
													.totalHits(_totalHits)
													.maxMeasure(_maxMeasure)
													.minMeasure(_minMeasure)
													.averageMeasure(_minMeasure)
													.lastMeasure(_lastMeasure)
													.lastOccurrence(_lastOccurrence)
												.build()
													.hashCode());
		Assertions.assertNotEquals(base.hashCode()
									,MetricSnapshot.builder(_measureReducer)
													.name(_name)
													.accumulatedSamples(_accumulatedSamples)
													.samplingSize(_samplingSize)
													.totalHits(_totalHits)
													.maxMeasure(_maxMeasure)
													.minMeasure(_minMeasure)
													.averageMeasure(_averageMeasure)
													.lastMeasure(_averageMeasure)
													.lastOccurrence(_lastOccurrence)
												.build()
													.hashCode());
		Assertions.assertNotEquals(base.hashCode()
									,MetricSnapshot.builder(_measureReducer)
													.name(_name)
													.accumulatedSamples(_accumulatedSamples)
													.samplingSize(_samplingSize)
													.totalHits(_totalHits)
													.maxMeasure(_maxMeasure)
													.minMeasure(_minMeasure)
													.averageMeasure(_averageMeasure)
													.lastMeasure(_lastMeasure)
													.lastOccurrence(LocalDateTime.now())
												.build()
													.hashCode());

	}

	@ParameterizedTest(name ="Two distinct instances from measureReducer:{0},name:{1},accumulatedSamples:{2},samplingSize:{3},totalHits:{4},maxMeasure:{5},minMeasure:{6},averageMeasure:{7},lastMeasure:{8},lastOccurrence:{9} should be equals()")
	@MethodSource("metricSnapshotBuilderDatapack")
	@SuppressWarnings("IncompatibleEquals")
	public <T> void equals(final MeasureReducer<T> _measureReducer,final String _name,final T _accumulatedSamples,final long _samplingSize,final long _totalHits,final T _maxMeasure,final T _minMeasure,final T _averageMeasure,final T _lastMeasure,final LocalDateTime _lastOccurrence){
		
		MetricSnapshot base=MetricSnapshot.builder(_measureReducer)
												.name(_name)
												.accumulatedSamples(_accumulatedSamples)
												.samplingSize(_samplingSize)
												.totalHits(_totalHits)
												.maxMeasure(_maxMeasure)
												.minMeasure(_minMeasure)
												.averageMeasure(_averageMeasure)
												.lastMeasure(_lastMeasure)
												.lastOccurrence(_lastOccurrence)
											.build();
		
		Assertions.assertTrue(base.equals(base));
		Assertions.assertFalse(base.equals((MetricSnapshot)null));
		Assertions.assertFalse(base.equals("afdgf"));
		Assertions.assertTrue(base.equals(MetricSnapshot.builder(_measureReducer)
															.name(_name)
															.accumulatedSamples(_accumulatedSamples)
															.samplingSize(_samplingSize)
															.totalHits(_totalHits)
															.maxMeasure(_maxMeasure)
															.minMeasure(_minMeasure)
															.averageMeasure(_averageMeasure)
															.lastMeasure(_lastMeasure)
															.lastOccurrence(_lastOccurrence)
														.build()));
		Assertions.assertFalse(base.equals(MetricSnapshot.builder(_measureReducer)
															.name("other")
															.accumulatedSamples(_accumulatedSamples)
															.samplingSize(_samplingSize)
															.totalHits(_totalHits)
															.maxMeasure(_maxMeasure)
															.minMeasure(_minMeasure)
															.averageMeasure(_averageMeasure)
															.lastMeasure(_lastMeasure)
															.lastOccurrence(_lastOccurrence)
														.build()));
		Assertions.assertFalse(base.equals(MetricSnapshot.builder(_measureReducer)
															.name(_name)
															.accumulatedSamples(10)
															.samplingSize(_samplingSize)
															.totalHits(_totalHits)
															.maxMeasure(_maxMeasure)
															.minMeasure(_minMeasure)
															.averageMeasure(_averageMeasure)
															.lastMeasure(_lastMeasure)
															.lastOccurrence(_lastOccurrence)
														.build()));
		Assertions.assertFalse(base.equals(MetricSnapshot.builder(_measureReducer)
															.name(_name)
															.accumulatedSamples(_accumulatedSamples)
															.samplingSize(3)
															.totalHits(_totalHits)
															.maxMeasure(_maxMeasure)
															.minMeasure(_minMeasure)
															.averageMeasure(_averageMeasure)
															.lastMeasure(_lastMeasure)
															.lastOccurrence(_lastOccurrence)
														.build()));
		Assertions.assertFalse(base.equals(MetricSnapshot.builder(_measureReducer)
															.name(_name)
															.accumulatedSamples(_accumulatedSamples)
															.samplingSize(_samplingSize)
															.totalHits(3)
															.maxMeasure(_maxMeasure)
															.minMeasure(_minMeasure)
															.averageMeasure(_averageMeasure)
															.lastMeasure(_lastMeasure)
															.lastOccurrence(_lastOccurrence)
														.build()));
		Assertions.assertFalse(base.equals(MetricSnapshot.builder(_measureReducer)
															.name(_name)
															.accumulatedSamples(_accumulatedSamples)
															.samplingSize(_samplingSize)
															.totalHits(_totalHits)
															.maxMeasure(_minMeasure)
															.minMeasure(_minMeasure)
															.averageMeasure(_averageMeasure)
															.lastMeasure(_lastMeasure)
															.lastOccurrence(_lastOccurrence)
														.build()));
		Assertions.assertFalse(base.equals(MetricSnapshot.builder(_measureReducer)
															.name(_name)
															.accumulatedSamples(_accumulatedSamples)
															.samplingSize(_samplingSize)
															.totalHits(_totalHits)
															.maxMeasure(_maxMeasure)
															.minMeasure(_maxMeasure)
															.averageMeasure(_averageMeasure)
															.lastMeasure(_lastMeasure)
															.lastOccurrence(_lastOccurrence)
														.build()));
		Assertions.assertFalse(base.equals(MetricSnapshot.builder(_measureReducer)
															.name(_name)
															.accumulatedSamples(_accumulatedSamples)
															.samplingSize(_samplingSize)
															.totalHits(_totalHits)
															.maxMeasure(_maxMeasure)
															.minMeasure(_minMeasure)
															.averageMeasure(_minMeasure)
															.lastMeasure(_lastMeasure)
															.lastOccurrence(_lastOccurrence)
														.build()));
		Assertions.assertFalse(base.equals(MetricSnapshot.builder(_measureReducer)
															.name(_name)
															.accumulatedSamples(_accumulatedSamples)
															.samplingSize(_samplingSize)
															.totalHits(_totalHits)
															.maxMeasure(_maxMeasure)
															.minMeasure(_minMeasure)
															.averageMeasure(_averageMeasure)
															.lastMeasure(_averageMeasure)
															.lastOccurrence(_lastOccurrence)
														.build()));
		Assertions.assertFalse(base.equals(MetricSnapshot.builder(_measureReducer)
															.name(_name)
															.accumulatedSamples(_accumulatedSamples)
															.samplingSize(_samplingSize)
															.totalHits(_totalHits)
															.maxMeasure(_maxMeasure)
															.minMeasure(_minMeasure)
															.averageMeasure(_averageMeasure)
															.lastMeasure(_lastMeasure)
															.lastOccurrence(LocalDateTime.now())
														.build()));
	}

	@Test()
	@DisplayName("Name comparison should return ordered by name asc")
	public void equals(){
		
		Assertions.assertEquals(0,MetricSnapshot.compareNames(MetricSnapshot.builder(MeasureReducers.DOUBLE.get(Double.class))
																						.name("my-name")
																					.build()
																	, MetricSnapshot.builder(MeasureReducers.DOUBLE.get(Double.class))
																						.name("my-name")
																					.build()));
		Assertions.assertEquals(-1,MetricSnapshot.compareNames(MetricSnapshot.builder(MeasureReducers.DOUBLE.get(Double.class))
																						.name("my-name1")
																					.build()
																	, null));
		Assertions.assertEquals(-1,MetricSnapshot.compareNames(MetricSnapshot.builder(MeasureReducers.DOUBLE.get(Double.class))
																						.name("my-name1")
																					.build()
																	, MetricSnapshot.builder(MeasureReducers.DOUBLE.get(Double.class))
																					.build()));
		Assertions.assertEquals(-1,MetricSnapshot.compareNames(MetricSnapshot.builder(MeasureReducers.DOUBLE.get(Double.class))
																						.name("my-name1")
																					.build()
																	, MetricSnapshot.builder(MeasureReducers.DOUBLE.get(Double.class))
																						.name("my-name2")
																					.build()));
		Assertions.assertEquals(1,MetricSnapshot.compareNames(null
																	, MetricSnapshot.builder(MeasureReducers.DOUBLE.get(Double.class))
																						.name("my-name2")
																					.build()));
		Assertions.assertEquals(1,MetricSnapshot.compareNames(MetricSnapshot.builder(MeasureReducers.DOUBLE.get(Double.class))
																					.build()
																	, MetricSnapshot.builder(MeasureReducers.DOUBLE.get(Double.class))
																						.name("my-name1")
																					.build()));
		Assertions.assertEquals(1,MetricSnapshot.compareNames(MetricSnapshot.builder(MeasureReducers.DOUBLE.get(Double.class))
																						.name("my-name2")
																					.build()
																	, MetricSnapshot.builder(MeasureReducers.DOUBLE.get(Double.class))
																						.name("my-name1")
																					.build()));
	}
}
