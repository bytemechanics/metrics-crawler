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
package org.bytemechanics.metrics.crawler.sensors;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Stream;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.bytemechanics.metrics.crawler.MetricsService;
import org.bytemechanics.metrics.crawler.internal.MeasureReducers;
import org.bytemechanics.metrics.crawler.internal.MetricsServiceSingleton;
import org.junit.jupiter.api.AfterEach;
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
 * @author afarre
 */
public class DoubleSensorTest {

	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> DoubleSensorTest >>>> setup");
		try(InputStream inputStream = DoubleSensorTest.class.getResourceAsStream("/logging.properties")){
			LogManager.getLogManager().readConfiguration(inputStream);
		}catch (final IOException e){
			Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
			Logger.getAnonymousLogger().severe(e.getMessage());
		}
	}
	@BeforeEach
    void beforeEachTest(final TestInfo testInfo) {
        System.out.println(">>>>> "+this.getClass().getSimpleName()+" >>>> "+testInfo.getTestMethod().map(Method::getName).orElse("Unkown")+""+testInfo.getTags().toString()+" >>>> "+testInfo.getDisplayName());
		DoubleSensor.registerMetricsServiceSupplier(() -> MetricsServiceSingleton.getInstance().getMetricsService());
    }
	@AfterEach
	void afterEachTest(){
		DoubleSensor.registerMetricsServiceSupplier(() -> MetricsServiceSingleton.getInstance().getMetricsService());
	}
	
	@Test
	@DisplayName("Create a null name metric sensor should raise NullPointerException")
	@SuppressWarnings("ThrowableResultIgnored")
	public void getMetricServiceDefault(){

		Assertions.assertThrows(NullPointerException.class,
								() -> DoubleSensor.get(null), 
								"Can not create null named sensor metric");
	}	

	static Stream<Arguments> sensorDatapack() {
	    return Stream.of(
			Arguments.of("a", new Object[]{1,2.0d,"string"}, "a",1.0d,2.0d),			
			Arguments.of("{}b{}c{}",new Object[]{},"nullbnullcnull",2.0d,1.0d),
			Arguments.of("{}b{}c{}",new Object[]{1},"1bnullcnull"	,3.0d,null),
			Arguments.of("{}b{}c{}",new Object[]{1,2.0d},"1b2.0cnull",4.0d,2.0d),	
			Arguments.of("{}b{}c{}",new Object[]{1,2.0d,"string"},"1b2.0cstring",5.0d,3.3d),	
			Arguments.of("{}b{}c{}",new Object[]{null,2.0d,"string"},"nullb2.0cstring",null,1.0d),	
			Arguments.of("{}b{}c{}",new Object[]{1,null,"string"},"1bnullcstring",6.0d,1.2d),	
			Arguments.of("{}b{}c{}",new Object[]{1,2.0d,null},"1b2.0cnull",1.7d,7.0d)
		);
	}

	@ParameterizedTest(name ="When create a double sensor with get(_name:{0},_args:{1}) and then call getName() should return {2}")
	@MethodSource("sensorDatapack")
	public void getName(final String _name,final Object[] _args,final String _expected){

		DoubleSensor obj=DoubleSensor.get(_name,_args);
				
		Assertions.assertNotNull(obj);
		Assertions.assertEquals(_expected, obj.getName());
	}
	@ParameterizedTest(name ="When create a double sensor with get(_measure:{3},_name:{0},_args:{1}) and then call getName() should return {2}")
	@MethodSource("sensorDatapack")
	public void getNameWithMeasure(final String _name,final Object[] _args,final String _expected,final Double _measure){

		DoubleSensor obj=DoubleSensor.get(_measure,_name,_args);
				
		Assertions.assertNotNull(obj);
		Assertions.assertEquals(_expected, obj.getName());
	}
	@ParameterizedTest(name ="When create a double sensor with get(_measure:{3},_name:{0},_args:{1}) and then call getMeasure() should return {3}")
	@MethodSource("sensorDatapack")
	public void getMeasure(final String _name,final Object[] _args,final String _expected,final Double _measure){

		DoubleSensor obj=DoubleSensor.get(_measure,_name,_args);
				
		Assertions.assertEquals(_measure, obj.getMeasure());
	}
	@ParameterizedTest(name ="When create a double sensor with get(_measure:{3},_name:{0},_args:{1}) and then call isSkip() should return false")
	@MethodSource("sensorDatapack")
	public void isSkipFalse(final String _name,final Object[] _args,final String _expected,final Double _measure){

		DoubleSensor obj=DoubleSensor.get(_measure,_name,_args);
				
		Assertions.assertFalse( obj.isSkip());
	}
	@ParameterizedTest(name ="When create a double sensor with get(_measure:{3},_name:{0},_args:{1}) and call skip() over it then isSkip() should return true")
	@MethodSource("sensorDatapack")
	public void isSkipTrue(final String _name,final Object[] _args,final String _expected,final Double _measure){

		DoubleSensor obj=DoubleSensor.get(_measure,_name,_args);
		obj.skip();
				
		Assertions.assertTrue( obj.isSkip());
	}
	@ParameterizedTest(name ="When create a double sensor with get(_measure:{3},_name:{0},_args:{1}) and call setMeasure(_measure:{4}) over it then getMeasure() should return {4}")
	@MethodSource("sensorDatapack")
	public void setMeasure(final String _name,final Object[] _args,final String _expected,final Double _measure,final Double _newMeasure){

		DoubleSensor obj=DoubleSensor.get(_measure,_name,_args);
		obj.setMeasure(_newMeasure);
				
		Assertions.assertEquals(_newMeasure, obj.getMeasure());
	}
	static Stream<Arguments> sensorNotNullMeasuresDatapack() {
		return sensorDatapack()
				.filter(args -> args.get()[3]!=null);
	}
	@ParameterizedTest(name ="When close() sensor with name:{0},args:{1} and measure:{3} metric must add to metricservice the registered measure")
	@MethodSource("sensorNotNullMeasuresDatapack")
	public void closeWithMeasure(final String _name,final Object[] _args,final String _expected,final Double _measure,final Double _newMeasure,@Mocked MetricsService _metricService){

		DoubleSensor.registerMetricsServiceSupplier(() -> _metricService);
		
		new Expectations() {{
			_metricService.buildMetricName(_name, _args); result=_expected; times=1;
		}};
		
		DoubleSensor obj=DoubleSensor.get(_measure,_name,_args);
		obj.close();

		new Verifications() {{
			
			_metricService.registerMeasure(_expected, (LocalDateTime)any, _measure, MeasureReducers.DOUBLE.get(Double.class)); times=1;
		}};
	}
	@ParameterizedTest(name ="When close() sensor with name:{0},args:{1} and null measure then no registration should happens")
	@MethodSource("sensorDatapack")
	public void closeWithoutMeasure(final String _name,final Object[] _args,final String _expected,final Double _measure,final Double _newMeasure,@Mocked MetricsService _metricService){

		DoubleSensor.registerMetricsServiceSupplier(() -> _metricService);
		
		new Expectations() {{
			_metricService.buildMetricName(_name, _args); result=_expected; times=1;
		}};
		
		DoubleSensor obj=DoubleSensor.get(_name,_args);
		obj.close();

		new Verifications() {{
			_metricService.registerMeasure(_expected, (LocalDateTime)any, _measure, MeasureReducers.DOUBLE.get(Double.class)); times=0;
		}};
	}
	@ParameterizedTest(name ="When measure() sensor with name:{0},args:{1} and measure:{3} must add to metricservice the registered measure")
	@MethodSource("sensorNotNullMeasuresDatapack")
	public void measure(final String _name,final Object[] _args,final String _expected,final Double _measure,final Double _newMeasure,@Mocked MetricsService _metricService){

		DoubleSensor.registerMetricsServiceSupplier(() -> _metricService);
		
		new Expectations() {{
			_metricService.buildMetricName(_name, _args); result=_expected; times=1;
		}};
		
		DoubleSensor.measure(_measure,_name,_args);
		
		new Verifications() {{
			_metricService.registerMeasure(_expected, (LocalDateTime)any, _measure, MeasureReducers.DOUBLE.get(Double.class)); times=1;
		}};
	}
	@ParameterizedTest(name ="When measure() sensor with name:{0},args:{1} and null measure, no registration in MetricsService should be done")
	@MethodSource("sensorNotNullMeasuresDatapack")
	public void measureNull(final String _name,final Object[] _args,final String _expected,final Double _measure,final Double _newMeasure,@Mocked MetricsService _metricService){

		DoubleSensor.registerMetricsServiceSupplier(() -> _metricService);
		
		new Expectations() {{
			_metricService.buildMetricName(_name, _args); result=_expected; times=1;
		}};
		
		DoubleSensor.measure(null,_name,_args);
		
		new Verifications() {{
			_metricService.registerMeasure(_expected, (LocalDateTime)any, _measure, MeasureReducers.DOUBLE.get(Double.class)); times=0;
		}};
	}
}
