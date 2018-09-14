/*
 * Copyright 2018 Byte Mechanics.
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
package org.bytemechanics.metrics.crawler

import org.bytemechanics.metrics.crawler.impl.*
import spock.lang.Specification
import spock.lang.Unroll
import java.util.Optional
import java.util.logging.*
import java.util.stream.*
import spock.lang.Shared

/**
 * @author afarre
 */
class MetricsServiceSpec extends Specification{
	
	def setupSpec(){
		println(">>>>> MetricsServiceSpec >>>> setupSpec")
		final InputStream inputStream = MetricsServiceSpec.class.getResourceAsStream("/logging.properties");
		try{
			LogManager.getLogManager().readConfiguration(inputStream);
		}catch (final IOException e){
			Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
			Logger.getAnonymousLogger().severe(e.getMessage());
		}finally{
			if(inputStream!=null)
				inputStream.close();
		}
	}
	
	@Unroll
	def "When any MetricsService try to build buildMetricName with #name and #args the result should be #result"(){
		println(">>>>> MetricsServiceSpec >>>> When any MetricsService try to build buildMetricName with $name and $args the result should be $result")

		given:
			def metricsService=new DefaultMetricsServiceImpl()
			
		expect:
			metricsService.buildMetricName(name,(Object[])args)==result
			
		where:
			name					| args					| result
			"{} ada"				| [3,"ds","fdsfdsgs"]	| "3 ada"
			"ada1 {} daasf"			| [3,"ds","fdsfdsgs"]	| "ada1 3 daasf"
			"ada2 {}"				| [3,"ds","fdsfdsgs"]	| "ada2 3"
			"{}ada3"				| [3,"ds","fdsfdsgs"]	| "3ada3"
			"ada{}4"				| [3,"ds","fdsfdsgs"]	| "ada34"
			"ada{}"					| [3,"ds","fdsfdsgs"]	| "ada3"
			"{} ada{}"				| [3,"ds","fdsfdsgs"]	| "3 adads"
			"ada1 {} d {}aasf{}{}"	| [3,"ds","fdsfdsgs"]	| "ada1 3 d dsaasffdsfdsgsnull"
	}
}

