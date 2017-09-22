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

package xyz.funjava.functional.validation;

import xyz.funjava.functional.monoid.Monoid;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Carlos Sierra Andrés
 */
public class Fail implements Monoid<Fail> {

    final Set<String> _failures;

    public Fail(Set<String> failures) {
        _failures = failures;
    }

    public Fail(String... failures) {
        _failures = new HashSet<>(Arrays.asList(failures));
    }

    public Fail prependAll(String prefix) {
        return new Fail(_failures.stream().map(s -> prefix + s).collect(
            Collectors.toSet()));
    }

    public Fail(String error) {
        _failures = Collections.singleton(error);
    }

    @Override
    public Fail mappend(Monoid<Fail> other) {
        Set<String> otherFailures = ((Fail) other)._failures;

        Stream<String> stream = Stream.concat(
            _failures.stream(), otherFailures.stream());

        return new Fail(stream.collect(Collectors.toSet()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fail fail = (Fail) o;

        return _failures.equals(fail._failures);
    }

    @Override
    public int hashCode() {
        return _failures.hashCode();
    }

    @Override
    public String toString() {
        return "Fail{" +
            "_failures=" + _failures +
            '}';
    }
}
