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
package org.bytemechanics.metrics.crawler.internal;

import org.bytemechanics.metrics.crawler.MetricsService;
import org.bytemechanics.metrics.crawler.impl.DefaultMetricsServiceImpl;

/**
 * Default singleton container for the metrics service. Used to store the metric service if no external singleton container exist.
 * @author afarre
 * @since 1.0.0
 */
public class MetricsServiceSingleton {
	
	private static MetricsServiceSingleton instance;

	private final MetricsService metricsService;
	
	private MetricsServiceSingleton(){
		this.metricsService=new DefaultMetricsServiceImpl();
	}

	/**
	 * Returns always the same instance of this metric service singleton creating new one if it's the first time
	 * @return always the same instance of this metric service singleton
	 */
	public static final MetricsServiceSingleton getInstance(){
		
		MetricsServiceSingleton reply;
		
		if((reply=MetricsServiceSingleton.instance)==null){
			synchronized(MetricsServiceSingleton.class){
				if((reply=MetricsServiceSingleton.instance)==null){
					reply=new MetricsServiceSingleton();
					MetricsServiceSingleton.instance=reply;
				}
			}
		}
		
		return reply;
	}

	/**
	 * Returns always the same metric service not null instance
	 * @return always the same metric service not null instance
	 */
	public MetricsService getMetricsService() {
		return metricsService;
	}
}
