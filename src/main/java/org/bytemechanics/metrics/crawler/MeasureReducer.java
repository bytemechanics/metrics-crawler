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
package org.bytemechanics.metrics.crawler;

import java.util.Optional;
import org.bytemechanics.metrics.crawler.internal.Measure;

/**
 * Adapter to create a reducer for a particular type of measure
 * @author afarre
 * @param <T> type of measure reducer
 * @see Measure
 * @since 1.0.0
 */
public interface MeasureReducer<T> {

	/**
	 * Retrieve the type which this reducer can reduce
	 * @return reducer accepted type
	 */
	public Class getType();
	
	/**
	 * Retrieve the reducer type identity
	 * @return reducer type identity
	 */
	public T identity();

	/**
	 * Retrieve the result of _val1 accumulated to _val2 
	 * @param _val1 first value
	 * @param _val2 second value
	 * @return Optional with the accumulated value
	 */
	public Optional<T> accumulate(final T _val1,final T _val2);

	/**
	 * Retrieve the maximum value between _val1 and _val2 
	 * @param _val1 first value
	 * @param _val2 second value
	 * @return Optional with the maximum value
	 */
	public Optional<T> max(final T _val1,final T _val2);

	/**
	 * Retrieve the minimum value between _val1 and _val2 
	 * @param _val1 first value
	 * @param _val2 second value
	 * @return Optional with the minimum value
	 */
	public Optional<T> min(final T _val1,final T _val2);

	/**
	 * Retrieve the division between the _value and the _hits
	 * @param _val value to divide
	 * @param _hits dividend
	 * @return Optional with the result of the division
	 */
	public Optional<T> average(final T _val,final long _hits);

	/**
	 * Generate the string representation for the given value
	 * @param _val value to divide
	 * @return String representing the given value
	 */
	public String toString(final T _val);
}
