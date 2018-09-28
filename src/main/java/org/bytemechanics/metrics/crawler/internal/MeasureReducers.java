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
import java.util.Optional;
import java.util.function.Supplier;
import org.bytemechanics.metrics.crawler.MeasureReducer;

/**
 *
 * @author afarre
 * @since 1.0.0
 */
public enum MeasureReducers {
	
	DURATION(new MeasureReducer<Duration>(){
				@Override
				public Class getType() {
					return Duration.class;
				}
				@Override
				public final Duration identity() {
					return Duration.ZERO;
				}
				@Override
				public final Optional<Duration> accumulate(final Duration _val1,final Duration _val2) {
					return apply(Duration.class,_val1,_val2,() -> _val1.plus(_val2));
				}
				@Override
				public final Optional<Duration> max(final Duration _val1,final Duration _val2) {
					return apply(Duration.class,_val1,_val2,() -> ((_val1.compareTo(_val2)>0)? _val1 : _val2));
				}
				@Override
				public final Optional<Duration> min(final Duration _val1,final Duration _val2) {
					return apply(Duration.class,_val1,_val2,() -> ((_val1.compareTo(_val2)>0)? _val2 : _val1));
				}
				@Override
				public final Optional<Duration> average(final Duration _val,final long _hits){
					return Optional.ofNullable(_val)
										.map(val -> val.dividedBy(_hits));
				}
				@Override
				public final String toString(final Duration _val) {
					if(_val==null)
						return "null";
					long seconds = _val.getSeconds();
					long absSeconds = Math.abs(seconds);
					String positive = String.format("%d:%02d:%02d",absSeconds / 3600,(absSeconds % 3600) / 60,absSeconds % 60);
					return seconds < 0 ? "-" + positive : positive;
				}
				@Override
				public String toString() {
					return "MeasureReducers.Duration";
				}				
			}),
	LONG(new MeasureReducer<Long>(){
				@Override
				public Class getType() {
					return Long.class;
				}
				@Override
				public final Long identity() {
					return 0l;
				}
				@Override
				public final Optional<Long> accumulate(final Long _val1,final Long _val2) {
					return apply(Long.class,_val1,_val2,() -> _val1+_val2);
				}
				@Override
				public final Optional<Long> max(final Long _val1,final  Long _val2) {
					return apply(Long.class,_val1,_val2,() -> ((_val1.compareTo(_val2)>0)? _val1 : _val2));
				}
				@Override
				public final Optional<Long> min(final Long _val1,final  Long _val2) {
					return apply(Long.class,_val1,_val2,() -> ((_val1.compareTo(_val2)>0)? _val2 : _val1));
				}
				@Override
				public final Optional<Long> average(final Long _val,final long _hits){
					return Optional.ofNullable(_val)
										.map(val -> val/_hits);
				}
				@Override
				public final String toString(final Long _val) {
					if(_val==null)
						return "null";
					return NumberFormat.getNumberInstance().format(_val);
				}
				@Override
				public String toString() {
					return "MeasureReducers.Long";
				}				
			}),
	DOUBLE(new MeasureReducer<Double>(){
				@Override
				public Class getType() {
					return Double.class;
				}
				@Override
				public final Double identity() {
					return 0.0d;
				}
				@Override
				public final Optional<Double> accumulate(final Double _val1,final  Double _val2) {
					return apply(Double.class,_val1,_val2,() -> _val1+_val2);
				}
				@Override
				public final Optional<Double> max(final Double _val1,final  Double _val2) {
					return apply(Double.class,_val1,_val2,() -> ((_val1.compareTo(_val2)>0)? _val1 : _val2));
				}
				@Override
				public final Optional<Double> min(final Double _val1,final  Double _val2) {
					return apply(Double.class,_val1,_val2,() -> ((_val1.compareTo(_val2)>0)? _val2 : _val1));
				}
				@Override
				public final Optional<Double> average(final Double _val,final long _hits){
					return Optional.ofNullable(_val)
										.map(val -> val/_hits);
				}
				@Override
				public final String toString(final Double _val) {
					if(_val==null)
						return "null";
					return NumberFormat.getNumberInstance().format(_val);
				}
				@Override
				public String toString() {
					return "MeasureReducers.Double";
				}				
			}),
	;
	
	private final MeasureReducer reducer;
	
	MeasureReducers(final MeasureReducer _reducer){
		this.reducer=_reducer;
	}
	
	protected static <T> Optional<T> apply(final Class<T> _class,final T _val1,final T _val2,final Supplier<T> _supplier){
		if(_val1==null){
			if(_val2==null){
				return Optional.empty();
			}else{
				return Optional.of(_val2);
			}
		}else{
			if(_val2==null){
				return Optional.of(_val1);
			}else{
				return Optional.of(_supplier.get());
			}
		}		
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
