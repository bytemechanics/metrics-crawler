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
package org.bytemechanics.metrics.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Stream;
import mockit.Tested;
import org.bytemechanics.metrics.crawler.impl.DefaultMetricsServiceImpl;
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
public class MetricsServiceTest {

	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> MetricsServiceTest >>>> setup");
		try(InputStream inputStream = MetricsServiceTest.class.getResourceAsStream("/logging.properties")){
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
	
	@Tested
	DefaultMetricsServiceImpl metricsService;
	
	@Test
	@DisplayName("Try to build a null named name should raise NullPointerException")
	public void nullNamedBuildMetric(){
			
		Assertions.assertEquals("Can not create null named sensor metric"
								,Assertions.assertThrows(NullPointerException.class, () -> metricsService.buildMetricName(null)).getMessage());
	}

	static Stream<Arguments> metricNameDatapack() {
	    return Stream.of(
			Arguments.of("{} ada",new Object[]{3,"ds","fdsfdsgs"},"3 ada"),
			Arguments.of("ada1 {} daasf",new Object[]{3,"ds","fdsfdsgs"},"ada1 3 daasf"),
			Arguments.of("ada2 {}",new Object[]{3,"ds","fdsfdsgs"},"ada2 3"),
			Arguments.of("{}ada3",new Object[]{3,"ds","fdsfdsgs"},"3ada3"),
			Arguments.of("ada{}4",new Object[]{3,"ds","fdsfdsgs"},"ada34"),
			Arguments.of("ada{}",new Object[]{3,"ds","fdsfdsgs"},"ada3"),
			Arguments.of("{} ada{}",new Object[]{3,"ds","fdsfdsgs"},"3 adads"),
			Arguments.of("ada1 {} d {}aasf{}{}",new Object[]{3,"ds","fdsfdsgs"},"ada1 3 d dsaasffdsfdsgsnull")
		);
	}
	
	@ParameterizedTest(name ="When any MetricsService try to build buildMetricName with {0} and {2} the result should be {3}")
	@MethodSource("metricNameDatapack")
	public void nullNamedBuildMetric(final String _name,final Object[] _args,final String _result){

		Assertions.assertEquals(_result
								,metricsService.buildMetricName(_name, _args));
	}
}
