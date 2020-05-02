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
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.bytemechanics.metrics.crawler.MeasureReducer;
import org.bytemechanics.metrics.crawler.beans.MetricSnapshot;
import org.bytemechanics.metrics.crawler.internal.commons.functional.LambdaUnchecker;
import org.bytemechanics.metrics.crawler.internal.commons.string.SimpleFormat;
import org.bytemechanics.metrics.test.ArgumentsUtils;
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
public class MeasureTest {
	
	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> MeasureTest >>>> setup");
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

	
	@Test
	@DisplayName("When try to create null _timestamp measure should raise NullPointerException")
	@SuppressWarnings("ThrowableResultIgnored")
	public void constructor_null_timestamp(){

		Assertions.assertThrows(NullPointerException.class
								,() -> new Measure(null,1.0d,MeasureReducers.DOUBLE.get(MeasureReducer.class))
								,"Can not create null _timestamp measure");
	}
	@Test
	@DisplayName("When try to create null _value measure should raise NullPointerException")
	@SuppressWarnings("ThrowableResultIgnored")
	public void constructor_null_value(){

		Assertions.assertThrows(NullPointerException.class
								,() -> new Measure(LocalDateTime.now(),null,MeasureReducers.DOUBLE.get(MeasureReducer.class))
								,"Can not create null _value measure");
	}
	@Test
	@DisplayName("When try to create null _reducer measure should raise NullPointerException")
	@SuppressWarnings("ThrowableResultIgnored")
	public void constructor_null_reducer(){

		Assertions.assertThrows(NullPointerException.class
								,() -> new Measure(LocalDateTime.now(),1.0d,null)
								,"Can not create null _reducer measure");
	}

		
	static Stream<Arguments> measureBuilderDatapack() {
	    return Stream.of(
					Arguments.of(LocalDateTime.now(),Duration.ofSeconds(10),MeasureReducers.DURATION.get(MeasureReducer.class),Duration.class),
					Arguments.of(LocalDateTime.now(),10l,MeasureReducers.LONG.get(MeasureReducer.class),Long.class),	
					Arguments.of(LocalDateTime.now(),10.2d,MeasureReducers.DOUBLE.get(MeasureReducer.class),Double.class)
				);
	}

	@ParameterizedTest(name ="When Measure is created with _time:{0},_measure:{1},_reducer:{2} of type {3} the getTimestamp() returns {0}")
	@MethodSource("measureBuilderDatapack")
	public <T> void getTimestamp(final LocalDateTime _time,final T _measure,final MeasureReducer _reducer,final Class<T> _type){

		final Measure instance=new Measure(_time,_measure,_reducer);
		
		Assertions.assertNotNull(instance);
		Assertions.assertEquals(_time,instance.getTimestamp());
	}
	@ParameterizedTest(name ="When Measure is created with _time:{0},_measure:{1},_reducer:{2} of type {3} the getValue() returns {1}")
	@MethodSource("measureBuilderDatapack")
	public <T> void getValue(final LocalDateTime _time,final T _measure,final MeasureReducer _reducer,final Class<T> _type){

		final Measure instance=new Measure(_time,_measure,_reducer);
		
		Assertions.assertNotNull(instance);
		Assertions.assertEquals(_measure,instance.getValue());
	}
	@ParameterizedTest(name ="Two measures of _time:{0},_measure:{1},_reducer:{2} of type {3} must return the same hashcode()")
	@MethodSource("measureBuilderDatapack")
	public <T> void hashcode(final LocalDateTime _time,final T _measure,final MeasureReducer _reducer,final Class<T> _type){

		final Measure instance1=new Measure(_time,_measure,_reducer);
		final Measure instance2=new Measure(_time,_measure,_reducer);
		
		Assertions.assertEquals(instance1.hashCode(),instance2.hashCode());
	}
	@ParameterizedTest(name ="Two measures of_time:{0},_measure:{1},_reducer:{2} of type {3} must be equals()")
	@MethodSource("measureBuilderDatapack")
	public <T> void equals(final LocalDateTime _time,final T _measure,final MeasureReducer _reducer,final Class<T> _type){

		final Measure instance1=new Measure(_time,_measure,_reducer);
		final Measure instance2=new Measure(_time,_measure,_reducer);
		
		Assertions.assertTrue(instance1.equals(instance1));
		Assertions.assertTrue(instance1.equals(instance2));
		Assertions.assertTrue(instance2.equals(instance1));
		Assertions.assertTrue(instance2.equals(instance2));
	}

	static Stream<Arguments> measureStringBuilderDatapack() {
		return ArgumentsUtils.compose(measureBuilderDatapack(),arguments -> SimpleFormat.format("Measure[timestamp={}, measureType={}, measure={},reducer={}]",
																				Optional.ofNullable((LocalDateTime)arguments[0])
																						.map(val -> (LocalDateTime)val)
																						.map(val -> val.format(DateTimeFormatter.ISO_DATE_TIME))
																						.orElse("null"),
																				Optional.ofNullable(arguments[1])
																						.map(val -> val.getClass())
																						.map(val -> val.toString()).orElse("null")
																				,arguments[1]
																				,arguments[2]));
	}
	@ParameterizedTest(name ="When Measure is created with _time:{0},_measure:{1},_reducer:{2} of type {3} the toString() should return {4}")
	@MethodSource("measureStringBuilderDatapack")
	public <T> void toString(final LocalDateTime _time,final T _measure,final MeasureReducer _reducer,final Class<T> _type,final String _expected){

		final Measure instance=new Measure(_time,_measure,_reducer);
		
		Assertions.assertNotNull(instance);
		Assertions.assertEquals(_expected,instance.toString());
	}

	static Stream<Arguments> measureSnapshotBuilderDatapack() {
		return ArgumentsUtils.compose(measureBuilderDatapack(),arguments -> MetricSnapshot.builder((MeasureReducer)arguments[2])
																								.samplingSize(1)
																								.accumulatedSamples(arguments[1])
																								.lastOccurrence((LocalDateTime)arguments[0])
																							.build());
	}
	@ParameterizedTest(name ="When Measure is created with _time:{0},_measure:{1},_reducer:{2} of type {3} the toMetricSnapshot() should return {4}")
	@MethodSource("measureSnapshotBuilderDatapack")
	public <T> void toMetricSnapshot(final LocalDateTime _time,final T _measure,final MeasureReducer _reducer,final Class<T> _type,final MetricSnapshot _expected){

		final Measure instance=new Measure(_time,_measure,_reducer);
		
		Assertions.assertNotNull(instance);
		final MetricSnapshot actual=instance.toMetricSnapshot();
		Assertions.assertEquals(_expected.getSamplingSize(),actual.getSamplingSize());
		Assertions.assertEquals(_expected.getAccumulatedSamples(),actual.getAccumulatedSamples());
		Assertions.assertEquals(_expected.getLastOccurrence(),actual.getLastOccurrence());
	}
}
