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

/**
 *
 * @author afarre
 * @param <T>
 */
public interface MeasureReducer<T> {
	
	public T identity();
	public Optional<T> accumulate(final T _val1,final T _val2);
	public Optional<T> max(final T _val1,final T _val2);
	public Optional<T> min(final T _val1,final T _val2);
	public Optional<T> average(final T _val,final long _hits);
	public String toString(final T _val);
}
