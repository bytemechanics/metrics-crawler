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

import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Supplier;
import org.bytemechanics.metrics.crawler.MeasureReducer;

/**
 *
 * @author afarre
 */
public enum MeasureReducers {
	
	DURATION(new MeasureReducer<Duration>(){
				@Override
				public final Duration identity() {
					return Duration.ZERO;
				}
				@Override
				public final Optional<Duration> accumulate(final Duration _val1,final Duration _val2) {
						return Optional.ofNullable(_val1)
									.map(val1 -> val1.plus(_val2));
				}
				@Override
				public final Optional<Duration> max(final Duration _val1,final Duration _val2) {
					return Optional.ofNullable(_val1)
									.map(val1 -> (val1.compareTo(_val2)>0)? val1 : _val2);
				}
				@Override
				public final Optional<Duration> min(final Duration _val1,final Duration _val2) {
					return Optional.ofNullable(_val1)
									.map(val1 -> (val1.compareTo(_val2)>0)? (Duration)_val2 : val1);
				}
				@Override
				public final Optional<Duration> average(final Duration _val,final long _hits){
					return Optional.ofNullable(_val)
										.map(val -> val.dividedBy(_hits));
				}
				@Override
				public final String toString(final Duration _val) {
					final LocalTime t = LocalTime.MIDNIGHT.plus((Duration)_val);
					return DateTimeFormatter.ofPattern("HH:mm:ss").format(t);
				}
			}),
	LONG(new MeasureReducer<Long>(){
				@Override
				public final Long identity() {
					return 0l;
				}
				@Override
				public final Optional<Long> accumulate(final Long _val1,final Long _val2) {
					return Optional.ofNullable(_val1)
									.map(val1 -> val1+_val2);
				}
				@Override
				public final Optional<Long> max(final Long _val1,final  Long _val2) {
					return Optional.ofNullable(_val1)
									.map(val1 -> (val1.compareTo(_val2)>0)? val1 : _val2);
				}
				@Override
				public final Optional<Long> min(final Long _val1,final  Long _val2) {
					return Optional.ofNullable(_val1)
									.map(val1 -> (val1.compareTo(_val2)>0)? _val2 : val1);
				}
				@Override
				public final Optional<Long> average(final Long _val,final long _hits){
					return Optional.ofNullable(_val)
										.map(val -> val/_hits);
				}
				@Override
				public final String toString(final Long _val) {
					return NumberFormat.getNumberInstance().format(_val);
				}
			}),
	DOUBLE(new MeasureReducer<Double>(){
				@Override
				public final Double identity() {
					return 0.0d;
				}
				@Override
				public final Optional<Double> accumulate(final Double _val1,final  Double _val2) {
					return Optional.ofNullable(_val1)
									.map(val1 -> val1+_val2);
				}
				@Override
				public final Optional<Double> max(final Double _val1,final  Double _val2) {
					return Optional.ofNullable(_val1)
									.map(val1 -> (val1.compareTo(_val2)>0)? val1 : _val2);
				}
				@Override
				public final Optional<Double> min(final Double _val1,final  Double _val2) {
					return Optional.ofNullable(_val1)
									.map(val1 -> (val1.compareTo(_val2)>0)? _val2 : val1);
				}
				@Override
				public final Optional<Double> average(final Double _val,final long _hits){
					return Optional.ofNullable(_val)
										.map(val -> val/_hits);
				}
				@Override
				public final String toString(final Double _val) {
					return NumberFormat.getNumberInstance().format(_val);
				}
			}),
	;
	
	private final MeasureReducer reducer;
	
	MeasureReducers(final MeasureReducer _reducer){
		this.reducer=_reducer;
	}
	
	
	public <T> T identity(final Class<T> _class){
		return (T)this.reducer.identity();
	}
	public <T> MeasureReducer<T> get(final Class<T> _class){
		return this.reducer;
	}
	public <T> Supplier<MeasureReducer<T>> supplier(final Class<T> _class){
		return () -> this.reducer;
	}
}
