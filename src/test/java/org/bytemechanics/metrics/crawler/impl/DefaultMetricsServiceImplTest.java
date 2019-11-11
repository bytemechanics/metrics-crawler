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
package org.bytemechanics.metrics.crawler.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bytemechanics.metrics.crawler.MeasureReducer;
import org.bytemechanics.metrics.crawler.MetricsService;
import org.bytemechanics.metrics.crawler.beans.MetricSnapshot;
import org.bytemechanics.metrics.crawler.exceptions.IncorrectMeasureType;
import org.bytemechanics.metrics.crawler.internal.MeasureReducers;
import org.bytemechanics.metrics.crawler.internal.commons.functional.LambdaUnchecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 *
 * @author afarre
 */
public class DefaultMetricsServiceImplTest {

	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> DefaultMetricsServiceImplTest >>>> setup");
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

	@ParameterizedTest(name = "Create DefaultMetricsServiceImpl without parameters should instance a metrics service with {0} sampling size")
	@ValueSource(ints = {DefaultMetricsServiceImpl.DEFAULT_SAMPLING_SIZE})
	public void getDefaultSamplingSize(final int _defaultSamplingSize){
		
		MetricsService metricsService=new DefaultMetricsServiceImpl();
		Assertions.assertNotNull(metricsService);
		Assertions.assertEquals(_defaultSamplingSize,metricsService.getSamplingSize());
	}	

	static Stream<Arguments> accumulatedSamplesDatapack() {
	    return Stream.of(
					Arguments.of(MeasureReducers.DOUBLE.get(Double.class),Stream.of(2.0d).collect(Collectors.toList()),1,2.0d),
					Arguments.of(MeasureReducers.DOUBLE.get(Double.class),Stream.of(2.0d,3.0d).collect(Collectors.toList()),2,5.0d),
					Arguments.of(MeasureReducers.DOUBLE.get(Double.class),Stream.of(2.0d,3.0d,4.0d).collect(Collectors.toList()),3,9.0d),
					Arguments.of(MeasureReducers.DOUBLE.get(Double.class),Stream.of(2.0d,3.0d,4.0d).collect(Collectors.toList()),1,4.0d),
					Arguments.of(MeasureReducers.DOUBLE.get(Double.class),Stream.of(2.0d,3.0d,4.0d).collect(Collectors.toList()),2,7.0d),
					Arguments.of(MeasureReducers.LONG.get(Long.class),Stream.of(2l).collect(Collectors.toList()),1,2l),
					Arguments.of(MeasureReducers.LONG.get(Long.class),Stream.of(2l,3l).collect(Collectors.toList()),2,5l),
					Arguments.of(MeasureReducers.LONG.get(Long.class),Stream.of(2l,3l,4l).collect(Collectors.toList()),3,9l),
					Arguments.of(MeasureReducers.LONG.get(Long.class),Stream.of(2l,3l,4l).collect(Collectors.toList()),1,4l),
					Arguments.of(MeasureReducers.LONG.get(Long.class),Stream.of(2l,3l,4l).collect(Collectors.toList()),2,7l),
					Arguments.of(MeasureReducers.DURATION.get(Duration.class),Stream.of(Duration.ofDays(2)).collect(Collectors.toList()),1,Duration.ofDays(2)),
					Arguments.of(MeasureReducers.DURATION.get(Duration.class),Stream.of(Duration.ofDays(2),Duration.ofDays(3)).collect(Collectors.toList()),2,Duration.ofDays(5)),
					Arguments.of(MeasureReducers.DURATION.get(Duration.class),Stream.of(Duration.ofDays(2),Duration.ofDays(3),Duration.ofDays(4)).collect(Collectors.toList())	,3,Duration.ofDays(9)),
					Arguments.of(MeasureReducers.DURATION.get(Duration.class),Stream.of(Duration.ofDays(2),Duration.ofDays(3),Duration.ofDays(4)).collect(Collectors.toList())	,1,Duration.ofDays(4)),
					Arguments.of(MeasureReducers.DURATION.get(Duration.class),Stream.of(Duration.ofDays(2),Duration.ofDays(3),Duration.ofDays(4)).collect(Collectors.toList())	,2,Duration.ofDays(7))
				)
				.map(Arguments::get)
				.map(args -> Arguments.of(args[0],args[1],((List)args[1]).size(),args[2],args[3]));
	}

	
	@ParameterizedTest(name = "Retrieve a {0} metric with a numberOfMeasures ({2}) and sampling size ({3}) should return the reduced snapshot with {4} accumulated")
	@MethodSource("accumulatedSamplesDatapack")
	public <T> void getAccumulatedSamples(final MeasureReducer<T> type,final List<T> measures,final int numberOfMeasures,final int samplingSize,final T accumulated){
		
		MetricsService metricsService=new DefaultMetricsServiceImpl(samplingSize);
		metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),1.0d,MeasureReducers.DOUBLE.get(Double.class),1);
		metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),5.0d,MeasureReducers.DOUBLE.get(Double.class),3);
		
		for(T measure:measures){
			metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),measure,type,2);
		}
		Optional<MetricSnapshot> optional=metricsService.getMetric("myMeasure{}",2);
			
		Assertions.assertTrue(optional.isPresent());
		Assertions.assertEquals(accumulated,optional.get().getAccumulatedSamples());
	}	

	@Test
	@DisplayName("Retrieve an unnexistent metric should return an empty optional")
	public void getMetric_unexpected(){
		
		MetricsService metricsService=new DefaultMetricsServiceImpl();
		metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),22.0d,MeasureReducers.DOUBLE.get(Double.class),1);
		
		Optional<MetricSnapshot> optional=metricsService.getMetric("myMeasure{}",2);
			
		Assertions.assertFalse(optional.isPresent());
	}

	@Test
	@DisplayName("Retrieve all metrics should return a list of metricSnapshot ordered by name")
	@SuppressWarnings("AssertEqualsBetweenInconvertibleTypes")
	public void getMetrics(){
		
		MetricsService metricsService=new DefaultMetricsServiceImpl(4);
		metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),1.0d,MeasureReducers.DOUBLE.get(Double.class),1);
		metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),1l,MeasureReducers.LONG.get(Long.class),3);
		metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),1.0d,MeasureReducers.DOUBLE.get(Double.class),1);
		metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),1.0d,MeasureReducers.DOUBLE.get(Double.class),1);
		metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),1.0d,MeasureReducers.DOUBLE.get(Double.class),1);
		metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),1.0d,MeasureReducers.DOUBLE.get(Double.class),1);
		metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),Duration.ofDays(1),MeasureReducers.DURATION.get(Duration.class),"02");
		metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),Duration.ofDays(1),MeasureReducers.DURATION.get(Duration.class),"02");
		metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),Duration.ofDays(1),MeasureReducers.DURATION.get(Duration.class),"02");
		
		List<MetricSnapshot> actualList=metricsService.getMetrics();
		
		Assertions.assertEquals(3,actualList.size());
		Assertions.assertEquals("myMeasure02",actualList.get(0).getName());
		Assertions.assertEquals(Duration.ofDays(3),actualList.get(0).getAccumulatedSamples());
		Assertions.assertEquals("myMeasure1",actualList.get(1).getName());
		Assertions.assertEquals(4.0d,actualList.get(1).getAccumulatedSamples());
		Assertions.assertEquals("myMeasure3",actualList.get(2).getName());
		Assertions.assertEquals(1l,actualList.get(2).getAccumulatedSamples());
	}

	static Stream<Arguments> distinctMeasuresDatapack() {
	    return Stream.of(
					Arguments.of(MeasureReducers.DOUBLE.get(Double.class),Stream.of(2.0d,3.0d,4.0d).collect(Collectors.toList()),MeasureReducers.DURATION.get(Duration.class),Duration.ofDays(2)),
					Arguments.of(MeasureReducers.DOUBLE.get(Double.class),Stream.of(2.0d,3.0d,4.0d).collect(Collectors.toList()),MeasureReducers.LONG.get(Long.class),2l),
					Arguments.of(MeasureReducers.LONG.get(Long.class),Stream.of(2l,3l,4l).collect(Collectors.toList())	,MeasureReducers.DOUBLE.get(Double.class),2.0d),
					Arguments.of(MeasureReducers.LONG.get(Long.class),Stream.of(2l,3l,4l).collect(Collectors.toList())	,MeasureReducers.DURATION.get(Duration.class),Duration.ofDays(2)),
					Arguments.of(MeasureReducers.DURATION.get(Duration.class),Stream.of(Duration.ofDays(2),Duration.ofDays(3),Duration.ofDays(4)).collect(Collectors.toList()),MeasureReducers.DOUBLE.get(Double.class)	,2l),
					Arguments.of(MeasureReducers.DURATION.get(Duration.class),Stream.of(Duration.ofDays(2),Duration.ofDays(3),Duration.ofDays(4)).collect(Collectors.toList()),MeasureReducers.LONG.get(Long.class),2.0d)
				);
	}

	
	@ParameterizedTest(name = "Register a {2} measure on a metric started with {0} will raise an exception")
	@MethodSource("distinctMeasuresDatapack")
	@SuppressWarnings("ThrowableResultIgnored")
	public <T1,T2> void getAccumulatedSamples(final MeasureReducer<T1> originalType,final List<T1> originalMeasures,final MeasureReducer<T2> newType,final T2 newMeasure){
		
		DefaultMetricsServiceImpl metricsService=new DefaultMetricsServiceImpl(2);
		for(T1 measure:originalMeasures){
			metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),measure,originalType,2);
		}
		
		Assertions.assertThrows(IncorrectMeasureType.class
								,() -> metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),newMeasure,newType,2));
	}	

	
	@Test
	@DisplayName("Call clear should remove all current metrics")
	public void clear(){
		
		MetricsService metricsService=new DefaultMetricsServiceImpl(4);
		metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),1.0d,MeasureReducers.DOUBLE.get(Double.class),1);
		metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),1l,MeasureReducers.LONG.get(Long.class),3);
		metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),1.0d,MeasureReducers.DOUBLE.get(Double.class),1);
		metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),1.0d,MeasureReducers.DOUBLE.get(Double.class),1);
		metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),1.0d,MeasureReducers.DOUBLE.get(Double.class),1);
		metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),1.0d,MeasureReducers.DOUBLE.get(Double.class),1);
		metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),Duration.ofDays(1),MeasureReducers.DURATION.get(Duration.class),"02");
		metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),Duration.ofDays(1),MeasureReducers.DURATION.get(Duration.class),"02");
		metricsService.registerMeasure("myMeasure{}",LocalDateTime.now(),Duration.ofDays(1),MeasureReducers.DURATION.get(Duration.class),"02");
		
		metricsService.clear();
		List<MetricSnapshot> actualList=metricsService.getMetrics();
		
		Assertions.assertEquals(0,actualList.size());
	}
}
