/*
 * Copyright 2017 Byte Mechanics.
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

import java.util.Objects;
import org.bytemechanics.metrics.crawler.internal.commons.string.SimpleFormat;


/**
 * Exception thrown when samplingSize has incorrect value
 * @author afarre
 * @since 1.0.0
 */
public class IncorrectSamplingSize extends RuntimeException{

	private final String metricName;
	private final int samplingSize;
	
	/**
	 * Sampling size incorrect exception
	 * @param _metricName metric name that has incorrect sampling size
	 * @param _samplingSize incorrect sampling size
	 */
	public IncorrectSamplingSize(final String _metricName,final int _samplingSize) {
		super(SimpleFormat.format("Metric {} must have samplingSize {} should be equal or greater than 1", _metricName,_samplingSize));
		this.metricName=_metricName;
		this.samplingSize=_samplingSize;
	}

	/**
	 * Metric name that can has incorrect samplingSize
	 * @return metric name
	 */
	public String getMetricName() {
		return metricName;
	}
	
	/**
	 * Incorrect sampling size
	 * @return incorrect sampling size
	 */
	public int getSamplingSize() {
		return samplingSize;
	}

	/** @see Object#hashCode()  */
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 23 * hash + Objects.hashCode(this.metricName);
		hash = 23 * hash + this.samplingSize;
		return hash;
	}

	/** @see Object#equals(java.lang.Object)   */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final IncorrectSamplingSize other = (IncorrectSamplingSize) obj;
		if (this.samplingSize != other.samplingSize) {
			return false;
		}
		return Objects.equals(this.metricName, other.metricName);
	}
}
