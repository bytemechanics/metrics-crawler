/*
 * Copyright 2020 Byte Mechanics.
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
package org.bytemechanics.metrics.crawler.internal;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bytemechanics.metrics.crawler.MeasureReducer;
import org.bytemechanics.metrics.crawler.beans.MetricSnapshot;
import org.bytemechanics.metrics.crawler.exceptions.IncorrectMeasureType;
import org.bytemechanics.metrics.crawler.exceptions.IncorrectSamplingSize;
import org.bytemechanics.metrics.crawler.internal.commons.collections.FastDropLastQueue;
import org.bytemechanics.metrics.crawler.internal.commons.string.SimpleFormat;
import org.bytemechanics.metrics.test.internal.ArgumentsUtils;
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
public class MetricTest {
	
	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> MetricTest >>>> setup");
		try(InputStream inputStream = MetricTest.class.getResourceAsStream("/logging.properties")){
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

	
	static Stream<Arguments> metricBuilderDatapack() {
	    return Stream.of(
					Arguments.of("a",1,MeasureReducers.DURATION.get(Duration.class),Duration.class),
					Arguments.of("c",1000,MeasureReducers.LONG.get(Long.class),Long.class),
					Arguments.of("fdsf",1000000,MeasureReducers.DOUBLE.get(Double.class),Double.class)
				);
	}
	@ParameterizedTest(name ="When Metric is created with _name:{0},_samplingSize:{1},_reducer:{2} of type {3} the getName() returns {0}")
	@MethodSource("metricBuilderDatapack")
	@SuppressWarnings("unchecked")
	public <T> void getName(final String _name,final int _samplingSize,final MeasureReducer _reducer,final Class<T> _type){

		final Metric instance=new Metric(_name,_samplingSize,_reducer);
		
		Assertions.assertNotNull(instance);
		Assertions.assertEquals(_name,instance.getName());
	}
	@ParameterizedTest(name ="When Metric is created with _name:{0},_samplingSize:{1},_reducer:{2} of type {3} the getHits() returns 0")
	@MethodSource("metricBuilderDatapack")
	@SuppressWarnings("unchecked")
	public <T> void getHits(final String _name,final int _samplingSize,final MeasureReducer _reducer,final Class<T> _type){

		final Metric instance=new Metric(_name,_samplingSize,_reducer);
		
		Assertions.assertNotNull(instance);
		Assertions.assertEquals(0,instance.getHits());
	}
	@ParameterizedTest(name ="When Metric is created with _name:{0},_samplingSize:{1},_reducer:{2} of type {3} the getMeasures() returns empty queue")
	@MethodSource("metricBuilderDatapack")
	@SuppressWarnings("unchecked")
	public <T> void getEmptyMeasures(final String _name,final int _samplingSize,final MeasureReducer _reducer,final Class<T> _type){

		final Metric instance=new Metric(_name,_samplingSize,_reducer);
		
		Assertions.assertNotNull(instance);
		Assertions.assertTrue(instance.getMeasures().isEmpty());
	}
	@ParameterizedTest(name ="When Metric is created with _name:{0},_samplingSize:{1},_reducer:{2} of type {3} the getReducer() returns {2}")
	@MethodSource("metricBuilderDatapack")
	@SuppressWarnings("unchecked")
	public <T> void getReducer(final String _name,final int _samplingSize,final MeasureReducer _reducer,final Class<T> _type){

		final Metric instance=new Metric(_name,_samplingSize,_reducer);
		
		Assertions.assertNotNull(instance);
		Assertions.assertEquals(_reducer,instance.getReducer());
	}

	static Stream<Arguments> metricStringBuilderDatapack() {
		return ArgumentsUtils.compose(metricBuilderDatapack()
										,arguments -> SimpleFormat.format("Metric[name={}, hits={}, measures={}, reducer={}]"
																			,(String)arguments[0]
																			,0
																			, new FastDropLastQueue<>((int)arguments[1])
																			, arguments[2]));
	}
	@ParameterizedTest(name ="When Metric is created with _name:{0},_samplingSize:{1},_reducer:{2} of type {3} the toString() returns {4}")
	@MethodSource("metricStringBuilderDatapack")
	@SuppressWarnings("unchecked")
	public <T> void toString(final String _name,final int _samplingSize,final MeasureReducer _reducer,final Class<T> _type,final String _expected){

		final Metric instance=new Metric(_name,_samplingSize,_reducer);
		
		Assertions.assertNotNull(instance);
		Assertions.assertEquals(_expected,instance.toString());
	}

	static Stream<Arguments> metricFailureBuilderDatapack() {
	    return Stream.of(
					Arguments.of(null, 1, MeasureReducers.DURATION.get(Duration.class), new NullPointerException("Name can not be null to create a Metric")),
					Arguments.of("ab", -1, null	, new NullPointerException(SimpleFormat.format("Metric {} reducer can not be null to create a Metric","ab"))),
					Arguments.of("a", -100, MeasureReducers.LONG.get(Long.class), new IncorrectSamplingSize("a", -100)),
					Arguments.of("a", -1, MeasureReducers.DURATION.get(Duration.class), new IncorrectSamplingSize("a", -1)),
					Arguments.of("c", 0, MeasureReducers.DOUBLE.get(Double.class), new IncorrectSamplingSize("c", 0))
				);
	}
	@ParameterizedTest(name ="Try to create with Metric with _name:{0},_samplingSize:{1},_reducer:{2} should raise {3}")
	@MethodSource("metricFailureBuilderDatapack")
	@SuppressWarnings({"ThrowableResultIgnored","unchecked"})
	public <T> void contructorNullControl(final String _name,final int _samplingSize,final MeasureReducer _reducer,final Exception _expected){

		Assertions.assertThrows(_expected.getClass()
								,() -> new Metric(_name,_samplingSize,_reducer)
								,_expected.toString());
	}
	@SuppressWarnings("unchecked")
	static Stream<Arguments> metricComparisonBuilderDatapack() {
	    return Stream.of(
					Arguments.of(new Metric("c",1,MeasureReducers.DURATION.get(Duration.class)),new Metric("c",1,MeasureReducers.DURATION.get(Duration.class)),true),
					Arguments.of(new Metric("c",2,MeasureReducers.DURATION.get(Duration.class)),new Metric("c",1,MeasureReducers.DURATION.get(Duration.class)),true),
					Arguments.of(new Metric("c",1,MeasureReducers.DURATION.get(Duration.class)),new Metric("c",2,MeasureReducers.DURATION.get(Duration.class)),true),
					Arguments.of(new Metric("1",1,MeasureReducers.DURATION.get(Duration.class)),new Metric("c",1,MeasureReducers.DURATION.get(Duration.class)),false),
					Arguments.of(new Metric("c",1,MeasureReducers.DURATION.get(Duration.class)),new Metric("1",1,MeasureReducers.DURATION.get(Duration.class)),false),
					Arguments.of(new Metric("c",1,MeasureReducers.DOUBLE.get(Double.class)),new Metric("c",1,MeasureReducers.DURATION.get(Duration.class)),false),
					Arguments.of(new Metric("c",1,MeasureReducers.DOUBLE.get(Double.class)),new Metric("c",1,MeasureReducers.LONG.get(Long.class)),false),
					Arguments.of(new Metric("c",1,MeasureReducers.DURATION.get(Duration.class)),new Metric("c",1,MeasureReducers.LONG.get(Long.class)),false)
				);
	}
	@ParameterizedTest(name ="When Metric {0} is compared with {1} with equals() the result should be {2}")
	@MethodSource("metricComparisonBuilderDatapack")
	public <T> void equals(final Metric _metric1,final Metric _metric2,final boolean _expected){

		Assertions.assertEquals(true,_metric1.equals(_metric1));
		Assertions.assertEquals(_expected,_metric1.equals(_metric2));
		Assertions.assertEquals(_expected,_metric2.equals(_metric1));
		Assertions.assertEquals(true,_metric2.equals(_metric2));
	}
	@ParameterizedTest(name ="When Metric {0} is compared with {1} with hashcode() the result should be {2}")
	@MethodSource("metricComparisonBuilderDatapack")
	public <T> void hashcode(final Metric _metric1,final Metric _metric2,final boolean _expected){

		Assertions.assertEquals(true,_metric1.hashCode()==_metric1.hashCode());
		Assertions.assertEquals(_expected,_metric1.hashCode()==_metric2.hashCode());
		Assertions.assertEquals(_expected,_metric2.hashCode()==_metric1.hashCode());
		Assertions.assertEquals(true,_metric2.hashCode()==_metric2.hashCode());
	}

	@SuppressWarnings("unchecked")
	static Stream<Arguments> metricAddMeasureBuilderDatapack() {
	    return Stream.of(
					Arguments.of(new Metric("c",1,MeasureReducers.DURATION.get(Duration.class)),LocalDateTime.now(),Duration.ofSeconds(10)),
					Arguments.of(new Metric("c",1,MeasureReducers.DOUBLE.get(Double.class)),LocalDateTime.now(),1.1d),
					Arguments.of(new Metric("c",1,MeasureReducers.LONG.get(long.class)),LocalDateTime.now(),3l)
				);
	}
	@ParameterizedTest(name ="When Metric {0} is called with addMeasure(_time:{1},_measure:{2}) to string must reflect the new value")
	@MethodSource("metricAddMeasureBuilderDatapack")
	@SuppressWarnings("unchecked")
	public <T> void addMeasure(final Metric _metric,final LocalDateTime _time,final T _measure){

		_metric.addMeasure(_time,_measure);
		
		Assertions.assertEquals(SimpleFormat.format("Metric[name={}, hits={}, measures={}, reducer={}]"
													, "c"
													, 1
													, Stream.of(new Measure(_time,_measure,_metric.getReducer())).collect(Collectors.toList())
													,_metric.getReducer())
								,_metric.toString());
	}

	@SuppressWarnings("unchecked")
	static Stream<Arguments> metricIncorrectMeasureBuilderDatapack() {
	    return Stream.of(
					Arguments.of(new Metric("c",1,MeasureReducers.DURATION.get(Duration.class)),null,null,NullPointerException.class),
					Arguments.of(new Metric("c",1,MeasureReducers.DURATION.get(Duration.class)),null,Duration.ofSeconds(10),NullPointerException.class),
					Arguments.of(new Metric("c",1,MeasureReducers.DURATION.get(Duration.class)),LocalDateTime.now(),null,NullPointerException.class),
					Arguments.of(new Metric("c",1,MeasureReducers.DURATION.get(Duration.class)),LocalDateTime.now(),3l,IncorrectMeasureType.class),
					Arguments.of(new Metric("c",1,MeasureReducers.DURATION.get(Duration.class)),LocalDateTime.now(),1.1d,IncorrectMeasureType.class),	
					Arguments.of(new Metric("c",1,MeasureReducers.DOUBLE.get(Double.class)),LocalDateTime.now(),3l,IncorrectMeasureType.class),						
					Arguments.of(new Metric("c",1,MeasureReducers.DOUBLE.get(Double.class)),LocalDateTime.now(),Duration.ofSeconds(10),IncorrectMeasureType.class),
					Arguments.of(new Metric("c",1,MeasureReducers.LONG.get(Long.class)),LocalDateTime.now(),1.1d,IncorrectMeasureType.class),
					Arguments.of(new Metric("c",1,MeasureReducers.LONG.get(Long.class)),LocalDateTime.now(),Duration.ofSeconds(10),IncorrectMeasureType.class)
				);
	}
	@ParameterizedTest(name ="When call addMeasure({1},{2}) to metric {0} a {3} exception must be thrown")
	@MethodSource("metricIncorrectMeasureBuilderDatapack")
	@SuppressWarnings({"ThrowableResultIgnored", "unchecked"})
	public <T> void addWrongMeasure(final Metric _metric,final LocalDateTime _time, final T _measure,final Class<? extends Exception> _exception){

		Assertions.assertThrows(_exception, () -> _metric.addMeasure(_time, _measure));
	}

	@SuppressWarnings("unchecked")
	static Stream<Arguments> metricGetMeasureBuilderDatapack() {
		final Metric persistentMetric=new Metric("c",2,MeasureReducers.DOUBLE.get(Double.class));
	    return Stream.of(
					Arguments.of(persistentMetric,LocalDateTime.of(1,1,1,1,1),1.0d,1,Stream.of(new Measure(LocalDateTime.of(1,1,1,1,1),1.0d,MeasureReducers.DOUBLE.get(Double.class))).collect(Collectors.toList())),
					Arguments.of(persistentMetric,LocalDateTime.of(2,2,2,2,2),2.0d,2,Stream.of(new Measure(LocalDateTime.of(1,1,1,1,1),1.0d,MeasureReducers.DOUBLE.get(Double.class)),new Measure(LocalDateTime.of(2,2,2,2,2),2.0d,MeasureReducers.DOUBLE.get(Double.class))).collect(Collectors.toList())),
					Arguments.of(persistentMetric,LocalDateTime.of(3,3,3,3,3),3.0d,3,Stream.of(new Measure(LocalDateTime.of(2,2,2,2,2),2.0d,MeasureReducers.DOUBLE.get(Double.class)),new Measure(LocalDateTime.of(3,3,3,3,3),3.0d,MeasureReducers.DOUBLE.get(Double.class))).collect(Collectors.toList())),
					Arguments.of(persistentMetric,LocalDateTime.of(4,4,4,4,4),4.0d,4,Stream.of(new Measure(LocalDateTime.of(3,3,3,3,3),3.0d,MeasureReducers.DOUBLE.get(Double.class)),new Measure(LocalDateTime.of(4,4,4,4,4),4.0d,MeasureReducers.DOUBLE.get(Double.class))).collect(Collectors.toList())),
					Arguments.of(persistentMetric,LocalDateTime.of(5,5,5,5,5),5.0d,5,Stream.of(new Measure(LocalDateTime.of(4,4,4,4,4),4.0d,MeasureReducers.DOUBLE.get(Double.class)),new Measure(LocalDateTime.of(5,5,5,5,5),5.0d,MeasureReducers.DOUBLE.get(Double.class))).collect(Collectors.toList()))
				);
	}

	@ParameterizedTest(name ="When Metric metric with 2 sampling size is called with addMeasure(_time:{0},_measure:{1}) {2} times getMeasures() should return {3}")
	@MethodSource("metricGetMeasureBuilderDatapack")
	@SuppressWarnings("unchecked")
	public <T> void getMeasures(final Metric _persistentMetric,final LocalDateTime _time, final T _measure,final int _times,final List<Measure> _expected){

		_persistentMetric.addMeasure(_time,_measure);
			
		Assertions.assertEquals(_expected,_persistentMetric.getMeasures());
	}

	@Test
	@DisplayName("Double metric should convert correctly to snapshot")
	@SuppressWarnings("unchecked")
	public <T> void doubleToSnapshot(){

		Metric metric=new Metric("mNAme",3,MeasureReducers.DOUBLE.get(Double.class));
		metric.addMeasure(LocalDateTime.of(1,1,1,1,1),1.0d);
		metric.addMeasure(LocalDateTime.of(2,2,2,2,2),2.0d);
		metric.addMeasure(LocalDateTime.of(3,3,3,3,3),3.0d);
		metric.addMeasure(LocalDateTime.of(4,4,4,4,4),4.0d);
		metric.addMeasure(LocalDateTime.of(5,5,5,5,5),5.0d);
		MetricSnapshot expected=MetricSnapshot.builder(MeasureReducers.DOUBLE.get(Double.class))
												.name("mNAme")
												.samplingSize(3)
												.accumulatedSamples(12.0d)
												.totalHits(5)
												.maxMeasure(5.0d)
												.minMeasure(3.0d)
												.averageMeasure(4.0d)
												.lastMeasure(5.0d)
												.lastOccurrence(LocalDateTime.of(5,5,5,5,5))
											.build();

		Assertions.assertEquals(expected,metric.toSnapshot());
	}

	@Test
	@DisplayName("Long metric should convert correctly to snapshot")
	@SuppressWarnings("unchecked")
	public <T> void longToSnapshot(){

		Metric metric=new Metric("mNAme",3,MeasureReducers.LONG.get(Long.class));
		metric.addMeasure(LocalDateTime.of(1,1,1,1,1),1l);
		metric.addMeasure(LocalDateTime.of(2,2,2,2,2),2l);
		metric.addMeasure(LocalDateTime.of(3,3,3,3,3),3l);
		metric.addMeasure(LocalDateTime.of(4,4,4,4,4),4l);
		metric.addMeasure(LocalDateTime.of(5,5,5,5,5),5l);
		MetricSnapshot expected=MetricSnapshot.builder(MeasureReducers.LONG.get(Long.class))
											.name("mNAme")
											.samplingSize(3)
											.accumulatedSamples(12l)
											.totalHits(5l)
											.maxMeasure(5l)
											.minMeasure(3l)
											.averageMeasure(4l)
											.lastMeasure(5l)
											.lastOccurrence(LocalDateTime.of(5,5,5,5,5))
										.build();

		Assertions.assertEquals(expected,metric.toSnapshot());
	}

	@Test
	@DisplayName("Duration metric should convert correctly to snapshot")
	@SuppressWarnings("unchecked")
	public <T> void durationToSnapshot(){

		Metric metric=new Metric("mNAme",3,MeasureReducers.DURATION.get(Duration.class));
		metric.addMeasure(LocalDateTime.of(1,1,1,1,1),Duration.ofSeconds(1));
		metric.addMeasure(LocalDateTime.of(2,2,2,2,2),Duration.ofSeconds(2));
		metric.addMeasure(LocalDateTime.of(3,3,3,3,3),Duration.ofSeconds(3));
		metric.addMeasure(LocalDateTime.of(4,4,4,4,4),Duration.ofSeconds(4));
		metric.addMeasure(LocalDateTime.of(5,5,5,5,5),Duration.ofSeconds(5));
		MetricSnapshot expected=MetricSnapshot.builder(MeasureReducers.DURATION.get(Duration.class))
													.name("mNAme")
													.samplingSize(3)
													.accumulatedSamples(Duration.ofSeconds(12))
													.totalHits(5l)
													.maxMeasure(Duration.ofSeconds(5))
													.minMeasure(Duration.ofSeconds(3))
													.averageMeasure(Duration.ofSeconds(4))
													.lastMeasure(Duration.ofSeconds(5))
													.lastOccurrence(LocalDateTime.of(5,5,5,5,5))
												.build();

		Assertions.assertEquals(expected,metric.toSnapshot());
	}
}
