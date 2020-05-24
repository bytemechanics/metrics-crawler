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
import java.util.logging.LogManager;
import java.util.logging.Logger;
import mockit.Expectations;
import mockit.Mocked;
import org.bytemechanics.metrics.crawler.MetricsService;
import org.bytemechanics.metrics.crawler.internal.MetricsServiceSingleton;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 * @author afarre
 */
public class AbstractSensorTest {

	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> AbstractSensorTest >>>> setup");
		try(InputStream inputStream = AbstractSensorTest.class.getResourceAsStream("/logging.properties")){
			LogManager.getLogManager().readConfiguration(inputStream);
		}catch (final IOException e){
			Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
			Logger.getAnonymousLogger().severe(e.getMessage());
		}
	}
	@AfterAll
	public static void cleanup(){
		System.out.println(">>>>> AbstractSensorTest >>>> cleanup");
		AbstractSensor.registerMetricsServiceSupplier(() -> MetricsServiceSingleton.getInstance().getMetricsService());
	}

	@BeforeEach
    void beforeEachTest(final TestInfo testInfo) {
        System.out.println(">>>>> "+this.getClass().getSimpleName()+" >>>> "+testInfo.getTestMethod().map(Method::getName).orElse("Unkown")+""+testInfo.getTags().toString()+" >>>> "+testInfo.getDisplayName());
		AbstractSensor.registerMetricsServiceSupplier(() -> MetricsServiceSingleton.getInstance().getMetricsService());
    }
	
	
	@Test
	@DisplayName("When call getMetricService() without register any metric service supplier the instance must be always the same DefaultMetricsServiceImpl from the automatic singleton")
	@SuppressWarnings("ThrowableResultIgnored")
	public void getMetricServiceDefault(){

		DoubleSensor sensor1=DoubleSensor.get("name1");
		LongSensor sensor2=LongSensor.get("name2");
		DurationSensor sensor3=DurationSensor.get("name3");
		DoubleSensor sensor4=DoubleSensor.get("name4");
		LongSensor sensor5=LongSensor.get("name5");
		DurationSensor sensor6=DurationSensor.get("name6");
		
		Assertions.assertAll("metric-service",
					()-> Assertions.assertSame(sensor1.getMetricService(), sensor2.getMetricService()),
					()-> Assertions.assertSame(sensor1.getMetricService(), sensor3.getMetricService()),
					()-> Assertions.assertSame(sensor1.getMetricService(), sensor4.getMetricService()),
					()-> Assertions.assertSame(sensor1.getMetricService(), sensor5.getMetricService()),
					()-> Assertions.assertSame(sensor1.getMetricService(), sensor6.getMetricService()));
			
	}
	@Test
	@DisplayName("When call getMetricService() without having registered another supplier then the instance retrieved should be the supplied one")
	@SuppressWarnings("ThrowableResultIgnored")
	public void getMetricServiceSupplied(@Mocked MetricsService _metricService){
		
		new Expectations() {{
			_metricService.buildMetricName("name4"); result="my-name4";
			_metricService.buildMetricName("name5"); result="my-name5";
			_metricService.buildMetricName("name6"); result="my-name6";
		}};
					
		DoubleSensor sensor1=DoubleSensor.get("name1");
		LongSensor sensor2=LongSensor.get("name2");
		DurationSensor sensor3=DurationSensor.get("name3");
		AbstractSensor.registerMetricsServiceSupplier(() -> _metricService);
		DoubleSensor sensor4=DoubleSensor.get("name4");
		LongSensor sensor5=LongSensor.get("name5");
		DurationSensor sensor6=DurationSensor.get("name6");
				
		Assertions.assertAll("metric-service",
					()-> Assertions.assertNotSame(_metricService, sensor1.getMetricService()),
					()-> Assertions.assertSame(sensor1.getMetricService(), sensor2.getMetricService()),
					()-> Assertions.assertSame(sensor1.getMetricService(), sensor3.getMetricService()),
					()-> Assertions.assertSame(_metricService, sensor4.getMetricService()),
					()-> Assertions.assertSame(_metricService, sensor5.getMetricService()),
					()-> Assertions.assertSame(_metricService, sensor6.getMetricService()));
			
	}
}
