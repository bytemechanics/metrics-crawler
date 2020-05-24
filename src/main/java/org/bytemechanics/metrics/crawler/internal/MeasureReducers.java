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
import java.util.function.BiFunction;
import java.util.function.Supplier;
import org.bytemechanics.metrics.crawler.MeasureReducer;

/**
 * An enumeration of the available included Measure reducers
 * @author afarre
 * @since 1.0.0
 */
public enum MeasureReducers {
	
	/**
	 * Duration measure reducer
	 * @see Duration
	 */
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
					return secureApply(_val1,_val2,(v1,v2) -> v1.plus(v2));
				}
				@Override
				public final Optional<Duration> max(final Duration _val1,final Duration _val2) {
					return secureApply(_val1,_val2,(v1,v2) -> ((v1.compareTo(v2)>0)? v1 : v2));
				}
				@Override
				public final Optional<Duration> min(final Duration _val1,final Duration _val2) {
					return secureApply(_val1,_val2,(v1,v2) -> ((v1.compareTo(v2)>0)? v2 : v1));
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
	/**
	 * Long measure reducer
	 * @see Long
	 */
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
					return secureApply(_val1,_val2,Long::sum);
				}
				@Override
				public final Optional<Long> max(final Long _val1,final  Long _val2) {
					return secureApply(_val1,_val2,Math::max);
				}
				@Override
				public final Optional<Long> min(final Long _val1,final  Long _val2) {
					return secureApply(_val1,_val2,Math::min);
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
	/**
	 * Double measure reducer
	 * @see Double
	 */
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
					return secureApply(_val1,_val2,Double::sum);
				}
				@Override
				public final Optional<Double> max(final Double _val1,final  Double _val2) {
					return secureApply(_val1,_val2,Math::max);
				}
				@Override
				public final Optional<Double> min(final Double _val1,final  Double _val2) {
					return secureApply(_val1,_val2,Math::min);
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
	
	/**
	 * Applies to the given function the two given values but never applies if any of them is null
	 * <ul>
	 *	<li>if _val1 and _val2 are null: return Empty optional</li>
	 *	<li>if _val1 or _val2 are null: return an optional of the not null value</li>
	 *	<li>if _val1 and _val2 are not null: return Optional of _function.apply(_val1,_val2)</li>
	 * </ul>
	 * @param <T> type of the values to apply
	 * @param _val1 first value to apply _function
	 * @param _val2 second value to apply _function
	 * @param _function funtion to apply returning the same type
	 * @return Optional.empty (if both values are null), Optional.of(_function.apply(_val1,_val2)) (if both of them are not null) and Optional.of(one of not null _val1 or _val2) (if any of them is null)
	 * @see Optional
	 * @see BiFunction
	 */
	protected static <T> Optional<T> secureApply(final T _val1,final T _val2,final BiFunction<T,T,T> _function){
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
				return Optional.of(_function.apply(_val1, _val2));
			}
		}		
	}
	
	/**
	 * Returns the identity value of the reducer of this enum
	 * @param <T> type of the returning value
	 * @param _class Class to cast the returned value
	 * @return measure reducer identity of this enum cast to _class
	 */
	@SuppressWarnings("unchecked")
	public <T> T identity(final Class<T> _class){
		return (T)this.reducer.identity();
	}
	/**
	 * Returns measure reducer of this enum
	 * @param <T> type of the returning value
	 * @param _class Class to cast the returned value
	 * @return measure reducer of this enum cast to _class
	 */
	@SuppressWarnings("unchecked")
	public <T> MeasureReducer<T> get(final Class<T> _class){
		return (MeasureReducer<T>)this.reducer;
	}
	/**
	 * Returns supplier to get the reducer of this enum
	 * @param <T> type of the returning value
	 * @param _class Class to cast the returned value
	 * @return supplier to get the reducer of this enum cast to _class
	 */
	@SuppressWarnings("unchecked")
	public <T> Supplier<MeasureReducer<T>> supplier(final Class<T> _class){
		return () -> (MeasureReducer<T>)this.reducer;
	}
}
