# Metrics Crawler
[![Latest version](https://maven-badges.herokuapp.com/maven-central/org.bytemechanics/metrics-crawler/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.bytemechanics/metrics-crawler/badge.svg)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=org.bytemechanics%3Ametrics-crawler&metric=alert_status)](https://sonarcloud.io/dashboard/index/org.bytemechanics%3Ametrics-crawler)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=org.bytemechanics%3Ametrics-crawler&metric=coverage)](https://sonarcloud.io/dashboard/index/org.bytemechanics%3Ametrics-crawler)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A little library with zero dependencies to crawl metrics and store during a certain window. The library provides an utility to monitor certain code segments and measure with minimum performance some
estatistics:
* Number of measures taken
* Last measure
* Maximum measure
* Minimum measure
* Average measure
* Last measure occurrence
And this measures can be:
* Double
* Long
* Duration (elapsed time)
All sensors implements Autocloseable in order to reduce boilerplate when taking measures.
The library allows create metric names manually or tracking your sensor stack if you are using imperative programming (As the library uses ThreadLocal to stack the sensors if you use multithreading you can have unexpected results)

## Motivation
When you have performance problems in production environment and don't know where look, the only way is to have measures directly from real, using sensors allows to identify the bottlenecks in production

## Quick start
(Please read our [Javadoc] (javadoc/index.html) for further information)
1. First of all include the Jar file in your compile and execution classpath.

**Maven**
```xml
	<dependency>
		<groupId>org.bytemechanics</groupId>
		<artifactId>metrics-crawler</artifactId>
		<version>X.X.X</version>
	</dependency>
```
**Graddle**
```groovy
dependencies {
    compile 'org.bytemechanics:metrics-crawler:X.X.X'
}
```
1.1. Optionally register external MetricService supplier at application startup, take in account that expects this supplier always return the same instance (only necessary if you want to use a singleton distinct from the default one)
```Java
AbstractSensor.registerMetricsServiceSupplier([your supplier]);
```
2. Start measuring 
2.1. Option1: With manual naming
```Java
import org.bytemechanics.metrics.crawler.sensors.DoubleSensor;
import org.bytemechanics.metrics.crawler.sensors.LongSensor;
import org.bytemechanics.metrics.crawler.sensors.DurationSensor;
(...)
try(DoubleSensor sensor1=DoubleSensor.get("myName")){
	sensor1=2.0d;
	(...)
}
(...)
try(LongSensor sensor1=LongSensor.get("my{}Name{}","long",1)){
	sensor1=2l;
	(...)
	try(LongSensor sensor2=LongSensor.get("my{}Name{}.my{}Name{}","long",1,"long",2)){
		sensor2=5l;
		(...)
	}
}
(...)
try(DurationSensor sensor1=DurationSensor.get("{}myName{}","duration","sensor")){
	(...)
}
```
2.2. Option2: With stack naming (the same result)
```Java
import org.bytemechanics.metrics.crawler.sensors.stack.DoubleStackSensor;
import org.bytemechanics.metrics.crawler.sensors.stack.LongStackSensor;
import org.bytemechanics.metrics.crawler.sensors.stack.DurationStackSensor;
(...)
try(DoubleStackSensor sensor1=DoubleStackSensor.get("myName")){
	sensor1=2.0d;
	(...)
}
(...)
try(LongStackSensor sensor1=LongStackSensor.get("my{}Name{}","long",1)){
	sensor1=2l;
	(...)
	try(LongStackSensor sensor2=LongStackSensor.get("my{}Name{}","long",2)){
		sensor2=5l;
		(...)
	}
}
(...)
try(DurationStackSensor sensor1=DurationStackSensor.get("{}myName{}","duration","sensor")){
	(...)
}
```



