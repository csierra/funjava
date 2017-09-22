/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.funjava.functional.validation.fieldprovider;

import xyz.funjava.functional.monoid.Monoid;
import xyz.funjava.functional.validation.Fail;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Carlos Sierra Andrés
 */
public class FieldFail implements Monoid<FieldFail> {

    final Map<String, Monoid> _failures;

    public FieldFail(String field, Fail fail) {
        this();

        _failures.put(field, fail);
    }

    public FieldFail() {
        _failures = new HashMap<>();
    }

    public FieldFail(String fieldName, FieldFail nested) {
        this();

        _failures.put(fieldName, nested);
    }

    FieldFail(Map<String, Monoid> failures) {
        _failures = failures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldFail fieldFail = (FieldFail) o;

        return _failures.equals(fieldFail._failures);

    }

    @Override
    public int hashCode() {
        return _failures.hashCode();
    }

    public FieldFail(String field, String message) {
        this(field, new Fail(message));
    }

    @Override
    public FieldFail mappend(Monoid<FieldFail> other) {
        Map<String, Monoid> collect = Stream.of(
            ((FieldFail) other)._failures, _failures).
            map(Map::entrySet).
            flatMap(Collection::stream).collect(Collectors.toMap(
            Map.Entry::getKey, Map.Entry::getValue, Monoid::mappend
        ));

        return new FieldFail(collect);
    }

    @Override
    public String toString() {
        return "FieldFail{" +
            "_failures=" + _failures +
            '}';
    }

    public static Function<Fail, FieldFail> fromFail(String fieldName) {
        return (Fail fail) -> new FieldFail(fieldName, fail);
    }
}
