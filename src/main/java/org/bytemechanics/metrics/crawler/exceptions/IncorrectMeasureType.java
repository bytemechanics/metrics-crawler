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
 * Exception thrown when try to register a measure into a metric with a distinct type of the previous ones
 * @author afarre
 * @since 1.0.0
 */
public class IncorrectMeasureType extends RuntimeException{

	private final String metricName;
	private final Class originalType;
	private final Class wrongType;
	
	/**
	 * Sampling size incorrect exception
	 * @param _metricName metric name that has incorrect sampling size
	 * @param _originalType Original and correct measure type
	 * @param _wrongType New and wrong measure type
	 */
	public IncorrectMeasureType(final String _metricName,final Class _originalType,final Class _wrongType) {
		super(SimpleFormat.format("Metric {} can not register {} measures, because has been created with type {}", _metricName,_wrongType,_originalType));
		this.metricName=_metricName;
		this.originalType=_originalType;
		this.wrongType=_wrongType;
	}

	/**
	 * Metric name that can has incorrect samplingSize
	 * @return metric name
	 */
	public String getMetricName() {
		return metricName;
	}

	/**
	 * Original and correct measure type
	 * @return original class type
	 */
	public Class getOriginalType() {
		return originalType;
	}

	/**
	 * New and wrong measure type
	 * @return wrong class type
	 */
	public Class getWrongType() {
		return wrongType;
	}

	/** @see Object#hashCode()  */
	@Override
	public int hashCode() {
		int hash = 5;
		hash = 59 * hash + Objects.hashCode(this.metricName);
		hash = 59 * hash + Objects.hashCode(this.originalType);
		hash = 59 * hash + Objects.hashCode(this.wrongType);
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
		final IncorrectMeasureType other = (IncorrectMeasureType) obj;
		if (!Objects.equals(this.metricName, other.metricName)) {
			return false;
		}
		if (!Objects.equals(this.originalType, other.originalType)) {
			return false;
		}
		if (!Objects.equals(this.wrongType, other.wrongType)) {
			return false;
		}
		return true;
	}
	

}
