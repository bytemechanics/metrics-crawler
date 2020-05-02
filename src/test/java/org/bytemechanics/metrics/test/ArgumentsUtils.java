/*
 * Copyright 2020 Byte Mechanics.
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
package org.bytemechanics.metrics.test;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

/**
 * @author afarre
 */
public class ArgumentsUtils {

	public static final Stream<Arguments> compose(final Stream<Arguments> _supplier,final Function<Object[],Object> _composer) {
		return _supplier.map(args -> ArgumentsUtils.calculated(args, _composer));
	}
	
	public static final Arguments calculated(final Arguments _arguments,final Function<Object[],Object> _provider){
		return Optional.ofNullable(_arguments)
						.map(Arguments::get)
						.map(_provider::apply)
						.map(val -> ArgumentsUtils.aggregate(_arguments, val))
						.orElse(_arguments);
	}
	public static final Arguments aggregate(final Arguments _arguments,final Object _value){
		
		return Optional.ofNullable(_arguments)
						.map(Arguments::get)
						.map(Stream::of)
						.map(stream -> Stream.concat(stream,Stream.of(_value)))
						.map(stream -> stream.collect(Collectors.toList()))
						.map(List::toArray)
						.map(Arguments::of)
						.orElse(_arguments);
	}
}
