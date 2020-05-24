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
import java.math.BigDecimal;
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
public class IncorrectMeasureTypeTest {
	
	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> IncorrectMeasureTypeTest >>>> setup");
		try(InputStream inputStream = IncorrectMeasureTypeTest.class.getResourceAsStream("/logging.properties")){
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
					Arguments.of("ada",String.class,Integer.class),
					Arguments.of("ada1",Integer.class,Double.class),
					Arguments.of("ada2",Double.class,Long.class),
					Arguments.of("ada3",Long.class,null),
					Arguments.of("ada4",null,char.class),
					Arguments.of(null,char.class,String.class)
				);
	}

	@ParameterizedTest(name ="When IncorrectMeasureType is instantiated with metricName:{0},originalType:{1} and wrongType:{2} then getMetricName() must return {0}")
	@MethodSource("exceptionDatapack")
	@SuppressWarnings({"ThrowableInstanceNotThrown", "ThrowableInstanceNeverThrown"})
	public void getName(final String metricName,final Class originalType,final Class wrongType){
		
		IncorrectMeasureType exception=new IncorrectMeasureType(metricName,originalType,wrongType);
		Assertions.assertNotNull(exception);
		Assertions.assertEquals(metricName, exception.getMetricName());
	}
	@ParameterizedTest(name ="When IncorrectMeasureType is instantiated with metricName:{0},originalType:{1} and wrongType:{2} then getOriginalType() must return {1}")
	@MethodSource("exceptionDatapack")
	@SuppressWarnings({"ThrowableInstanceNotThrown", "ThrowableInstanceNeverThrown"})
	public void getOriginalType(final String metricName,final Class originalType,final Class wrongType){
		
		IncorrectMeasureType exception=new IncorrectMeasureType(metricName,originalType,wrongType);
		Assertions.assertNotNull(exception);
		Assertions.assertEquals(originalType, exception.getOriginalType());
	}
	@ParameterizedTest(name ="When IncorrectMeasureType is instantiated with metricName:{0},originalType:{1} and wrongType:{2} then getWrongType() must return {2}")
	@MethodSource("exceptionDatapack")
	@SuppressWarnings({"ThrowableInstanceNotThrown", "ThrowableInstanceNeverThrown"})
	public void getWrongType(final String metricName,final Class originalType,final Class wrongType){
		
		IncorrectMeasureType exception=new IncorrectMeasureType(metricName,originalType,wrongType);
		Assertions.assertNotNull(exception);
		Assertions.assertEquals(wrongType, exception.getWrongType());
	}
	@ParameterizedTest(name ="Two instances of IncorrectMeasureType must have same hashcode() if has the same metricName:{0},originalType:{1} and wrongType:{2}")
	@MethodSource("exceptionDatapack")
	@SuppressWarnings({"ThrowableInstanceNotThrown", "ThrowableInstanceNeverThrown"})
	public void hashCode(final String metricName,final Class originalType,final Class wrongType){
		
		IncorrectMeasureType base=new IncorrectMeasureType(metricName,originalType,wrongType);
		IncorrectMeasureType second=new IncorrectMeasureType(metricName,originalType,wrongType);
		Assertions.assertEquals(base.hashCode(),second.hashCode());
		second=new IncorrectMeasureType("myName",originalType,wrongType);
		Assertions.assertNotEquals(base.hashCode(),second.hashCode());
		second=new IncorrectMeasureType(metricName,BigDecimal.class,wrongType);
		Assertions.assertNotEquals(base.hashCode(),second.hashCode());
		second=new IncorrectMeasureType(metricName,originalType,BigDecimal.class);
		Assertions.assertNotEquals(base.hashCode(),second.hashCode());
	}
	@ParameterizedTest(name ="Two instances of IncorrectMeasureType must be equals() if has the same metricName:{0},originalType:{1} and wrongType:{2}")
	@MethodSource("exceptionDatapack")
	@SuppressWarnings({"ThrowableInstanceNotThrown", "ThrowableInstanceNeverThrown", "IncompatibleEquals"})
	public void equals(final String metricName,final Class originalType,final Class wrongType){
		
		IncorrectMeasureType base=new IncorrectMeasureType(metricName,originalType,wrongType);
		Assertions.assertTrue(base.equals(base));
		IncorrectMeasureType second=new IncorrectMeasureType(metricName,originalType,wrongType);
		Assertions.assertTrue(base.equals(second));
		Assertions.assertFalse(base.equals((IncorrectMeasureType)null));
		Assertions.assertFalse(base.equals(String.class));
		second=new IncorrectMeasureType("myName",originalType,wrongType);
		Assertions.assertFalse(base.equals(second));
		second=new IncorrectMeasureType(metricName,BigDecimal.class,wrongType);
		Assertions.assertFalse(base.equals(second));
		second=new IncorrectMeasureType(metricName,originalType,BigDecimal.class);
		Assertions.assertFalse(base.equals(second));
	}

}
