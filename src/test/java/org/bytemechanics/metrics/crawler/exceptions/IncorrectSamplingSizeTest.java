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
package org.bytemechanics.metrics.crawler.exceptions;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 *
 * @author afarre
 */
public class IncorrectSamplingSizeTest {
	
	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> IncorrectSamplingSizeTest >>>> setup");
		try(InputStream inputStream = IncorrectSamplingSizeTest.class.getResourceAsStream("/logging.properties")){
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
	
	
	static Stream<Arguments> exceptionDatapack() {
	    return Stream.of(
					Arguments.of("ada",2),
					Arguments.of("ada1",3),
					Arguments.of("ada2",0),
					Arguments.of("ada3",5),
					Arguments.of("ada4",6),
					Arguments.of(null,6)
				);
	}

	@ParameterizedTest(name ="When IncorrectSamplingSize is instantiated with metricName:{0} and samplingSize:{1} then getMetricName() must return {0}")
	@MethodSource("exceptionDatapack")
	@SuppressWarnings({"ThrowableInstanceNotThrown", "ThrowableInstanceNeverThrown"})
	public void getName(final String metricName,final int samplingSize){
		
		IncorrectSamplingSize exception=new IncorrectSamplingSize(metricName,samplingSize);
		Assertions.assertNotNull(exception);
		Assertions.assertEquals(metricName, exception.getMetricName());
	}
	@ParameterizedTest(name ="When IncorrectSamplingSize is instantiated with metricName:{0} and samplingSize:{1} then getSamplingSize() must return {0}")
	@MethodSource("exceptionDatapack")
	@SuppressWarnings({"ThrowableInstanceNotThrown", "ThrowableInstanceNeverThrown"})
	public void getSamplingSize(final String metricName,final int samplingSize){
		
		IncorrectSamplingSize exception=new IncorrectSamplingSize(metricName,samplingSize);
		Assertions.assertNotNull(exception);
		Assertions.assertEquals(samplingSize, exception.getSamplingSize());
	}

	@ParameterizedTest(name ="Two instances of IncorrectSamplingSize must have same hashcode() if has the same metricName:{0} and samplingSize:{1}")
	@MethodSource("exceptionDatapack")
	@SuppressWarnings({"ThrowableInstanceNotThrown", "ThrowableInstanceNeverThrown"})
	public void hashCode(final String metricName,final int samplingSize){
		
		IncorrectSamplingSize base=new IncorrectSamplingSize(metricName,samplingSize);
		IncorrectSamplingSize second=new IncorrectSamplingSize(metricName,samplingSize);
		Assertions.assertEquals(base.hashCode(),second.hashCode());
		second=new IncorrectSamplingSize("myName",samplingSize);
		Assertions.assertNotEquals(base.hashCode(),second.hashCode());
		second=new IncorrectSamplingSize(metricName,100);
		Assertions.assertNotEquals(base.hashCode(),second.hashCode());
	}
	@ParameterizedTest(name ="Two instances of IncorrectSamplingSize must be equals() if has the same metricName:{0} and samplingSize:{1}")
	@MethodSource("exceptionDatapack")
	@SuppressWarnings({"ThrowableInstanceNotThrown", "ThrowableInstanceNeverThrown", "IncompatibleEquals"})
	public void equals(final String metricName,final int samplingSize){
		
		IncorrectSamplingSize base=new IncorrectSamplingSize(metricName,samplingSize);
		Assertions.assertTrue(base.equals(base));
		IncorrectSamplingSize second=new IncorrectSamplingSize(metricName,samplingSize);
		Assertions.assertTrue(base.equals(second));
		Assertions.assertFalse(base.equals((IncorrectSamplingSize)null));
		Assertions.assertFalse(base.equals(String.class));
		second=new IncorrectSamplingSize("myName",samplingSize);
		Assertions.assertFalse(base.equals(second));
		second=new IncorrectSamplingSize(metricName,100);
		Assertions.assertFalse(base.equals(second));
	}

}
