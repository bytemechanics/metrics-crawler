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
package org.bytemechanics.metrics.crawler.internal;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.Optional;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.bytemechanics.metrics.crawler.MeasureReducer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author afarre
 */
public class MeasureReducersTest {

	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> MeasureReducersTest >>>> setup");
		try(InputStream inputStream = MeasureReducersTest.class.getResourceAsStream("/logging.properties")){
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
	
	
	@ParameterizedTest(name ="Reducer {0} get() and supplier() should return the same instance")
	@EnumSource(MeasureReducers.class)
	public void getAndSupplierSameInstance(final MeasureReducers _reducer){

		MeasureReducer<Object> reducerInstance1=_reducer.get(Object.class);
		MeasureReducer<Object> reducerInstance2=_reducer.supplier(Object.class).get();
		
		Assertions.assertNotNull(reducerInstance1);
		Assertions.assertNotNull(reducerInstance2);
		Assertions.assertTrue(reducerInstance1==reducerInstance2);
	}

	static Stream<Arguments> identityDatapack() {
	    return Stream.of(
			Arguments.of(MeasureReducers.DURATION,Duration.ZERO),
			Arguments.of(MeasureReducers.LONG,0l),
			Arguments.of(MeasureReducers.DOUBLE	,0.0d)
		);
	}
	
	@ParameterizedTest(name ="When call identity() from {0} the result must be {1} and the same of call get().identity()")
	@MethodSource("identityDatapack")
	public void identity(final MeasureReducers _reducer,final Object _identity){

		Object actualIdentity=_reducer.identity(Object.class);
		Object actualIdentity2=_reducer.get(Object.class).identity();
			
		Assertions.assertNotNull(actualIdentity);
		Assertions.assertEquals(_identity, actualIdentity);
		Assertions.assertNotNull(actualIdentity2);
		Assertions.assertEquals(_identity, actualIdentity2);
	}


	static Stream<Arguments> accumulateDatapack() {
	    return Stream.of(
			Arguments.of(MeasureReducers.DURATION,Duration.class,Duration.ofSeconds(10),Duration.ofDays(10),Optional.ofNullable(Duration.parse("P10DT10S"))),
			Arguments.of(MeasureReducers.DURATION,Duration.class,Duration.ofDays(10),Duration.ofDays(1),Optional.ofNullable(Duration.parse("P11D"))),
			Arguments.of(MeasureReducers.DURATION,Duration.class,null,Duration.ofDays(1),Optional.ofNullable(Duration.ofDays(1))),
			Arguments.of(MeasureReducers.DURATION,Duration.class,Duration.ofDays(10),null,Optional.ofNullable(Duration.ofDays(10))),
			Arguments.of(MeasureReducers.DURATION,Duration.class,null,null,Optional.empty()),
			Arguments.of(MeasureReducers.LONG,Long.class,10l,-5l	,Optional.ofNullable(5l)),
			Arguments.of(MeasureReducers.LONG,Long.class,-9l,0l	,Optional.ofNullable(-9l)),
			Arguments.of(MeasureReducers.LONG,Long.class,null,0l	,Optional.ofNullable(0l)),
			Arguments.of(MeasureReducers.LONG,Long.class,-9l,null,Optional.ofNullable(-9l)),
			Arguments.of(MeasureReducers.LONG,Long.class,null,null,Optional.empty()),
			Arguments.of(MeasureReducers.DOUBLE,Double.class,10.2d, 7.5d	,Optional.ofNullable(17.7d)),
			Arguments.of(MeasureReducers.DOUBLE,Double.class,-7.8d, -9.9d	,Optional.ofNullable(-17.7d)),
			Arguments.of(MeasureReducers.DOUBLE,Double.class,null, -9.9d	,Optional.ofNullable(-9.9d)),
			Arguments.of(MeasureReducers.DOUBLE,Double.class,-7.8d, null	,Optional.ofNullable(-7.8d)),
			Arguments.of(MeasureReducers.DOUBLE,Double.class,null, null	,Optional.empty())
		);
	}
	
	@ParameterizedTest(name ="When call accumulate() from {0} over {1} and {2} result must be {3}")
	@MethodSource("accumulateDatapack")
	public <T> void accumulate(final MeasureReducers _reducer,final Class<T> _class,final T _left,final T _right,final Optional<T> _result){

		Optional<T> result=_reducer.get(_class).accumulate(_left,_right);
			
		Assertions.assertNotNull(result);
		Assertions.assertTrue(result.isPresent()==_result.isPresent());
		Assertions.assertEquals(_result, result);
	}

	static Stream<Arguments> maxDatapack() {
	    return Stream.of(
			Arguments.of(MeasureReducers.DURATION,Duration.class, Duration.ofSeconds(10),Duration.ofDays(10),Optional.ofNullable(Duration.ofDays(10))),
			Arguments.of(MeasureReducers.DURATION,Duration.class, Duration.ofSeconds(121),Duration.ofMinutes(2),Optional.ofNullable(Duration.ofSeconds(121))),
			Arguments.of(MeasureReducers.DURATION,Duration.class, null,Duration.ofMinutes(2),Optional.ofNullable(Duration.ofMinutes(2))),
			Arguments.of(MeasureReducers.DURATION,Duration.class, Duration.ofSeconds(121),null,Optional.ofNullable(Duration.ofSeconds(121))),
			Arguments.of(MeasureReducers.DURATION,Duration.class, null,null,Optional.empty()),
			Arguments.of(MeasureReducers.LONG,Long.class,10l,-5l,Optional.ofNullable(10l)),
			Arguments.of(MeasureReducers.LONG,Long.class,-10l,-5l,Optional.ofNullable(-5l)),
			Arguments.of(MeasureReducers.LONG,Long.class,null,-5l,Optional.ofNullable(-5l)),
			Arguments.of(MeasureReducers.LONG,Long.class,-10l,null,Optional.ofNullable(-10l)),
			Arguments.of(MeasureReducers.LONG,Long.class,null,null,Optional.empty()),
			Arguments.of(MeasureReducers.DOUBLE,Double.class,10.2d,7.5d,Optional.ofNullable(10.2d)),
			Arguments.of(MeasureReducers.DOUBLE,Double.class,-10.2d,-7.5d,Optional.ofNullable(-7.5d)),
			Arguments.of(MeasureReducers.DOUBLE,Double.class,null,-7.5d,Optional.ofNullable(-7.5d)),
			Arguments.of(MeasureReducers.DOUBLE,Double.class,-10.2d,null,Optional.ofNullable(-10.2d)),
			Arguments.of(MeasureReducers.DOUBLE,Double.class,null,null,Optional.empty())
		);
	}
	
	@ParameterizedTest(name ="When call max() from {0} over {1} and {2} result must be {3}")
	@MethodSource("maxDatapack")
	public <T> void max(final MeasureReducers _reducer,final Class<T> _class,final T _left,final T _right,final Optional<T> _result){

		Optional<T> result=_reducer.get(_class).max(_left,_right);
			
		Assertions.assertNotNull(result);
		Assertions.assertTrue(result.isPresent()==_result.isPresent());
		Assertions.assertEquals(_result, result);
	}

	static Stream<Arguments> minDatapack() {
	    return Stream.of(
			Arguments.of(MeasureReducers.DURATION,Duration.class,Duration.ofSeconds(10),Duration.ofDays(10),Optional.ofNullable(Duration.ofSeconds(10))),
			Arguments.of(MeasureReducers.DURATION,Duration.class,Duration.ofSeconds(121),Duration.ofMinutes(2),Optional.ofNullable(Duration.ofMinutes(2))),
			Arguments.of(MeasureReducers.DURATION,Duration.class,null,Duration.ofMinutes(2),Optional.ofNullable(Duration.ofMinutes(2))),
			Arguments.of(MeasureReducers.DURATION,Duration.class,Duration.ofSeconds(121),null,Optional.ofNullable(Duration.ofSeconds(121))),
			Arguments.of(MeasureReducers.DURATION,Duration.class,null,null,Optional.empty()),
			Arguments.of(MeasureReducers.LONG,Long.class,10l,-5l,Optional.ofNullable(-5l)),
			Arguments.of(MeasureReducers.LONG,Long.class,-10l,-5l,Optional.ofNullable(-10l)),
			Arguments.of(MeasureReducers.LONG,Long.class,null,-5l,Optional.ofNullable(-5l)),
			Arguments.of(MeasureReducers.LONG,Long.class,-10l,null,Optional.ofNullable(-10l)),
			Arguments.of(MeasureReducers.LONG,Long.class,null,null,Optional.empty()),
			Arguments.of(MeasureReducers.DOUBLE,Double.class,10.2d	,7.5d,Optional.ofNullable(7.5d)),
			Arguments.of(MeasureReducers.DOUBLE,Double.class,-10.2d	,-7.5d,Optional.ofNullable(-10.2d)),
			Arguments.of(MeasureReducers.DOUBLE,Double.class,null	,-7.5d,Optional.ofNullable(-7.5d)),
			Arguments.of(MeasureReducers.DOUBLE,Double.class,-10.2d	,null,Optional.ofNullable(-10.2d)),
			Arguments.of(MeasureReducers.DOUBLE,Double.class,null	,null,Optional.empty())
		);
	}
	
	@ParameterizedTest(name ="When call min() from {0} over {1} and {2} result must be {3}")
	@MethodSource("minDatapack")
	public <T> void min(final MeasureReducers _reducer,final Class<T> _class,final T _left,final T _right,final Optional<T> _result){

		Optional<T> result=_reducer.get(_class).min(_left,_right);
			
		Assertions.assertNotNull(result);
		Assertions.assertTrue(result.isPresent()==_result.isPresent());
		Assertions.assertEquals(_result, result);
	}


	static Stream<Arguments> averageDatapack() {
	    return Stream.of(
			Arguments.of(MeasureReducers.DURATION,Duration.class,Duration.ofSeconds(10),10l,Optional.ofNullable(Duration.ofSeconds(1))),
			Arguments.of(MeasureReducers.DURATION,Duration.class,Duration.ofSeconds(121),2l,Optional.ofNullable(Duration.parse("PT1M0.5S"))),
			Arguments.of(MeasureReducers.DURATION,Duration.class,null,2l,Optional.empty()),
			Arguments.of(MeasureReducers.LONG,Long.class,10l,2l,Optional.ofNullable(5l)),
			Arguments.of(MeasureReducers.LONG,Long.class,-10l,3l,Optional.ofNullable(-3l)),
			Arguments.of(MeasureReducers.LONG,Long.class,null,4l,Optional.empty()),
			Arguments.of(MeasureReducers.DOUBLE,Double.class,10.2d	,5l,Optional.ofNullable(2.04d)),
			Arguments.of(MeasureReducers.DOUBLE,Double.class,-10.2d	,2l,Optional.ofNullable(-5.1d)),
			Arguments.of(MeasureReducers.DOUBLE,Double.class,null	,10l,Optional.empty())
		);
	}
	
	@ParameterizedTest(name ="When call average() from {0} over {1} accumulated and {2} hits result must be {3}")
	@MethodSource("averageDatapack")
	public <T> void average(final MeasureReducers _reducer,final Class<T> _class,final T _accumulated,final long _hits,final Optional<T> _result){

		Optional<T> result=_reducer.get(_class).average(_accumulated,_hits);
			
		Assertions.assertNotNull(result);
		Assertions.assertTrue(result.isPresent()==_result.isPresent());
		Assertions.assertEquals(_result, result);
	}

	static Stream<Arguments> toStringDatapack() {
	    return Stream.of(
			Arguments.of(MeasureReducers.DURATION,Duration.class,Duration.ofDays(10),"240:00:00"),
			Arguments.of(MeasureReducers.DURATION,Duration.class,Duration.ofSeconds(121),"0:02:01"),
			Arguments.of(MeasureReducers.DURATION,Duration.class,null,"null"),
			Arguments.of(MeasureReducers.LONG,Long.class,10l,NumberFormat.getNumberInstance().format(10l)),
			Arguments.of(MeasureReducers.LONG,Long.class,-10l,NumberFormat.getNumberInstance().format(-10l)),
			Arguments.of(MeasureReducers.LONG,Long.class,null,"null"),
			Arguments.of(MeasureReducers.DOUBLE,Double.class,10.2d,NumberFormat.getNumberInstance().format(10.2d)),
			Arguments.of(MeasureReducers.DOUBLE,Double.class,-10.2d,NumberFormat.getNumberInstance().format(-10.2d)),
			Arguments.of(MeasureReducers.DOUBLE,Double.class,null,"null")
		);
	}
	
	@ParameterizedTest(name ="When call average() from {0} over {1} accumulated and {2} hits result must be {3}")
	@MethodSource("toStringDatapack")
	public <T> void toString(final MeasureReducers _reducer,final Class<T> _class,final T _val,final String _result){

		String result=_reducer.get(_class).toString(_val);
			
		Assertions.assertNotNull(result);
		Assertions.assertEquals(_result, result);
	}
}
