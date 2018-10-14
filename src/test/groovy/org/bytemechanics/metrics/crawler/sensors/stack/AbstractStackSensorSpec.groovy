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

package org.bytemechanics.metrics.crawler.sensors.stack

import spock.lang.Specification
import spock.lang.Unroll
import java.util.Optional
import java.util.logging.*
import java.util.stream.*
import java.time.LocalDateTime
import java.time.Duration
import java.time.format.DateTimeFormatter
import org.bytemechanics.metrics.crawler.beans.MetricSnapshot
import org.bytemechanics.metrics.crawler.impl.DefaultMetricsServiceImpl
import org.bytemechanics.metrics.crawler.internal.commons.string.SimpleFormat
import org.bytemechanics.metrics.crawler.internal.MetricsServiceSingleton

/**
 * @author afarre
 */
class AbstractStackSensorSpec extends Specification{
	
	def setupSpec(){
		println(">>>>> AbstractStackSensorSpec >>>> setupSpec")
		final InputStream inputStream = AbstractStackSensorSpec.class.getResourceAsStream("/logging.properties");
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

	def cleanupSpec(){
		println(">>>>> AbstractStackSensorSpec >>>> cleanupSpec")
		AbstractStackSensor.registerMetricsServiceSupplier({ -> MetricsServiceSingleton.getInstance().getMetricsService()})
	}

	def "When call getMetricService() without register any metric service supplier the instance must be always the same DefaultMetricsServiceImpl from the automatic singleton"() {
		println(">>>>> AbstractStackSensorSpec >>>> When call getMetricService() without register any metric service supplier the instance must be always the same DefaultMetricsServiceImpl from the automatic singleton")
		
		when:
			def sensor1=DoubleStackSensor.get("name1")
			def sensor2=DoubleStackSensor.get("name2")
			def sensor3=DoubleStackSensor.get("name3")
			def sensor4=DoubleStackSensor.get("name4")
			def sensor5=DoubleStackSensor.get("name5")
			def sensor6=DoubleStackSensor.get("name6")
		
		then:
			System.identityHashCode(sensor1.getMetricService())==System.identityHashCode(sensor2.getMetricService())
			System.identityHashCode(sensor1.getMetricService())==System.identityHashCode(sensor3.getMetricService())
			System.identityHashCode(sensor1.getMetricService())==System.identityHashCode(sensor4.getMetricService())
			System.identityHashCode(sensor1.getMetricService())==System.identityHashCode(sensor5.getMetricService())
			System.identityHashCode(sensor1.getMetricService())==System.identityHashCode(sensor6.getMetricService())			
	}

	def "When call getMetricService() without having registered another supplier then the instance retrieved should be the supplied one"() {
		println(">>>>> AbstractStackSensorSpec >>>> When call getMetricService() without having registered another supplier then the instance retrieved should be the supplied one")

		when:
			def metricService=new DefaultMetricsServiceImpl()
			def sensor1=DoubleStackSensor.get("name1")
			def sensor2=DoubleStackSensor.get("name2")
			def sensor3=DoubleStackSensor.get("name3")
			AbstractStackSensor.registerMetricsServiceSupplier({ -> metricService})
			def sensor4=DoubleStackSensor.get("name4")
			def sensor5=DoubleStackSensor.get("name5")
			def sensor6=DoubleStackSensor.get("name6")

		then:
			System.identityHashCode(metricService)!=System.identityHashCode(sensor1.getMetricService())
			System.identityHashCode(metricService)!=System.identityHashCode(sensor2.getMetricService())
			System.identityHashCode(metricService)!=System.identityHashCode(sensor3.getMetricService())			
			System.identityHashCode(sensor1.getMetricService())==System.identityHashCode(sensor2.getMetricService())
			System.identityHashCode(sensor1.getMetricService())==System.identityHashCode(sensor3.getMetricService())			
			System.identityHashCode(metricService)==System.identityHashCode(sensor4.getMetricService())
			System.identityHashCode(metricService)==System.identityHashCode(sensor5.getMetricService())
			System.identityHashCode(metricService)==System.identityHashCode(sensor6.getMetricService())
	}

	def "When anidate more than one sensor stack then the names must be accumulated"() {
		println(">>>>> AbstractStackSensorSpec >>>> When anidate more than one sensor stack then the names must be accumulated")

		given:
			AbstractStackSensor.CURRENT_NAME.set(null)

		when:
			def sensor1=DoubleStackSensor.get("name{}",1)
			def sensor2=DoubleStackSensor.get("name{}",2)
			def sensor3=DoubleStackSensor.get("name{}",3)
			sensor3.close()
			def sensor4=DoubleStackSensor.get("name{}",4)
			sensor4.close()
			sensor2.close()
			def sensor5=DoubleStackSensor.get("name{}",5)
			sensor5.close()
			sensor1.close()
			def sensor6=DoubleStackSensor.get("name{}",6)
			def sensor7=DoubleStackSensor.get("name{}",7)
			def sensor8=DoubleStackSensor.get("name{}",8)
			sensor7.close()
			def sensor9=DoubleStackSensor.get("name{}",9)

		then:
			sensor1.getName()=="name1"
			sensor2.getName()=="name1.name2"
			sensor3.getName()=="name1.name2.name3"
			sensor4.getName()=="name1.name2.name4"
			sensor5.getName()=="name1.name5"
			sensor6.getName()=="name6"
			sensor7.getName()=="name6.name7"
			sensor8.getName()=="name6.name7.name8"
			sensor9.getName()=="name6.name7.name9"
			
		cleanup:
			AbstractStackSensor.CURRENT_NAME.set(null)
	}
}

