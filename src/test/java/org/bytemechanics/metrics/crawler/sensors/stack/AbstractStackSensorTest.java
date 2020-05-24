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
package org.bytemechanics.metrics.crawler.sensors.stack;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import mockit.Expectations;
import mockit.Mocked;
import org.bytemechanics.metrics.crawler.MetricsService;
import org.bytemechanics.metrics.crawler.internal.MetricsServiceSingleton;
import org.bytemechanics.metrics.crawler.sensors.*;
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
public class AbstractStackSensorTest {

	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> AbstractStackSensorTest >>>> setup");
		try(InputStream inputStream = AbstractStackSensorTest.class.getResourceAsStream("/logging.properties")){
			LogManager.getLogManager().readConfiguration(inputStream);
		}catch (final IOException e){
			Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
			Logger.getAnonymousLogger().severe(e.getMessage());
		}
	}
	@AfterAll
	public static void cleanup(){
		System.out.println(">>>>> AbstractStackSensorTest >>>> cleanup");
		AbstractSensor.registerMetricsServiceSupplier(() -> MetricsServiceSingleton.getInstance().getMetricsService());
		AbstractStackSensor.CURRENT_NAME.set(null);
	}

	@BeforeEach
    void beforeEachTest(final TestInfo testInfo) {
        System.out.println(">>>>> "+this.getClass().getSimpleName()+" >>>> "+testInfo.getTestMethod().map(Method::getName).orElse("Unkown")+""+testInfo.getTags().toString()+" >>>> "+testInfo.getDisplayName());
		AbstractSensor.registerMetricsServiceSupplier(() -> MetricsServiceSingleton.getInstance().getMetricsService());
		AbstractStackSensor.CURRENT_NAME.set(null);
    }
	
	
	@Test
	@DisplayName("When call getMetricService() without register any metric service supplier the instance must be always the same DefaultMetricsServiceImpl from the automatic singleton")
	@SuppressWarnings("ThrowableResultIgnored")
	public void getMetricServiceDefault(){

		DoubleStackSensor sensor1=DoubleStackSensor.get("name1");
		LongStackSensor sensor2=LongStackSensor.get("name2");
		DurationStackSensor sensor3=DurationStackSensor.get("name3");
		DoubleStackSensor sensor4=DoubleStackSensor.get("name4");
		LongStackSensor sensor5=LongStackSensor.get("name5");
		DurationStackSensor sensor6=DurationStackSensor.get("name6");
		
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
					
		DoubleStackSensor sensor1=DoubleStackSensor.get("name1");
		LongStackSensor sensor2=LongStackSensor.get("name2");
		DurationStackSensor sensor3=DurationStackSensor.get("name3");
		AbstractSensor.registerMetricsServiceSupplier(() -> _metricService);
		DoubleStackSensor sensor4=DoubleStackSensor.get("name4");
		LongStackSensor sensor5=LongStackSensor.get("name5");
		DurationStackSensor sensor6=DurationStackSensor.get("name6");
				
		Assertions.assertAll("metric-service",
					()-> Assertions.assertNotSame(_metricService, sensor1.getMetricService()),
					()-> Assertions.assertSame(sensor1.getMetricService(), sensor2.getMetricService()),
					()-> Assertions.assertSame(sensor1.getMetricService(), sensor3.getMetricService()),
					()-> Assertions.assertSame(_metricService, sensor4.getMetricService()),
					()-> Assertions.assertSame(_metricService, sensor5.getMetricService()),
					()-> Assertions.assertSame(_metricService, sensor6.getMetricService()));
			
	}

	@Test
	@DisplayName("When anidate more than one sensor stack then the names must be accumulated")
	@SuppressWarnings("ThrowableResultIgnored")
	public void getSensorAnidatedName(){

		DoubleStackSensor sensor1=DoubleStackSensor.get("name{}",1);
		DoubleStackSensor sensor2=DoubleStackSensor.get("name{}",2);
		DoubleStackSensor sensor3=DoubleStackSensor.get("name{}",3);
		sensor3.close();
		DoubleStackSensor sensor4=DoubleStackSensor.get("name{}",4);
		sensor4.close();
		sensor2.close();
		DoubleStackSensor sensor5=DoubleStackSensor.get("name{}",5);
		sensor5.close();
		sensor1.close();
		DoubleStackSensor sensor6=DoubleStackSensor.get("name{}",6);
		DoubleStackSensor sensor7=DoubleStackSensor.get("name{}",7);
		DoubleStackSensor sensor8=DoubleStackSensor.get("name{}",8);
		sensor7.close();
		DoubleStackSensor sensor9=DoubleStackSensor.get("name{}",9);

		Assertions.assertAll("metric-service",
					()-> Assertions.assertEquals("name1", sensor1.getName()),
					()-> Assertions.assertEquals("name1.name2", sensor2.getName()),
					()-> Assertions.assertEquals("name1.name2.name3", sensor3.getName()),
					()-> Assertions.assertEquals("name1.name2.name4", sensor4.getName()),
					()-> Assertions.assertEquals("name1.name5", sensor5.getName()),
					()-> Assertions.assertEquals("name6", sensor6.getName()),
					()-> Assertions.assertEquals("name6.name7", sensor7.getName()),
					()-> Assertions.assertEquals("name6.name7.name8", sensor8.getName()),
					()-> Assertions.assertEquals("name6.name9", sensor9.getName()));
	}

	@Test
	@DisplayName("When anidate more than one sensor stack then the names must be accumulated")
	@SuppressWarnings("ThrowableResultIgnored")
	public void getSensorAnidatedMultithreadedName(){

		AtomicReference<DoubleStackSensor> sensor7=new AtomicReference<>();
				
		System.out.println("Main thread: "+Thread.currentThread().getId());
		DoubleStackSensor sensor1=DoubleStackSensor.get("name{}",1);
		DoubleStackSensor sensor2=DoubleStackSensor.get("name{}",2);
		DoubleStackSensor sensor3=DoubleStackSensor.get("name{}",3);
		sensor3.close();
		DoubleStackSensor sensor4=DoubleStackSensor.get("name{}",4);
		sensor4.close();
		sensor2.close();
		DoubleStackSensor sensor5=DoubleStackSensor.get("name{}",5);
		sensor5.close();
		sensor1.close();
		DoubleStackSensor sensor6=DoubleStackSensor.get("name{}",6);
		FutureTask<String> futureTask = new FutureTask<>(new Runnable() {
																			@Override
																			public void run() {
																				  System.out.println("Internal Thread: "+Thread.currentThread().getId());
																				  sensor7.set(DoubleStackSensor.get("name{}",7));
																				  sensor7.get().close();

																			}
																		}, "The result");
		futureTask.run();
		
		DoubleStackSensor sensor8=DoubleStackSensor.get("name{}",8);
		DoubleStackSensor sensor9=DoubleStackSensor.get("name{}",9);

		Assertions.assertAll("metric-service",
					()-> Assertions.assertEquals("name1", sensor1.getName()),
					()-> Assertions.assertEquals("name1.name2", sensor2.getName()),
					()-> Assertions.assertEquals("name1.name2.name3", sensor3.getName()),
					()-> Assertions.assertEquals("name1.name2.name4", sensor4.getName()),
					()-> Assertions.assertEquals("name1.name5", sensor5.getName()),
					()-> Assertions.assertEquals("name6", sensor6.getName()),
					()-> Assertions.assertEquals("name6.name7", sensor7.get().getName()),
					()-> Assertions.assertEquals("name6.name8", sensor8.getName()),
					()-> Assertions.assertEquals("name6.name8.name9", sensor9.getName()));
	}
}
