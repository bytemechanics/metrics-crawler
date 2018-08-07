# Metrics Crawler
[![Latest version](https://maven-badges.herokuapp.com/maven-central/org.bytemechanics/metrics-crawler/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.bytemechanics/metrics-crawler/badge.svg)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=org.bytemechanics%3Ametrics-crawler&metric=alert_status)](https://sonarcloud.io/dashboard/index/org.bytemechanics%3Ametrics-crawler)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=org.bytemechanics%3Ametrics-crawler&metric=coverage)](https://sonarcloud.io/dashboard/index/org.bytemechanics%3Ametrics-crawler)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A little library with zero dependencies to crawl metrics and store during a certain window. The library provides an utility to monitor certain code segments and measure with minimum performance some
estatistics:
* Number of executions
* Total time spent during a certain period of time
* Average execution time
* Maximum execution time
* Minimum execution time

## Motivation
When you have performance problems and don't know where look the cause its usually to track calls in order to identify the bottlenecks in production environment

## Quick start

1. First of all include the Jar file in your compile and execution classpath.

**Maven**
```Maven
	<dependency>
		<groupId>org.bytemechanics</groupId>
		<artifactId>metrics-crawler</artifactId>
		<version>X.X.X</version>
	</dependency>
```
**Graddle**
```Gradle
dependencies {
    compile 'org.bytemechanics:metrics-crawler:X.X.X'
}
```



