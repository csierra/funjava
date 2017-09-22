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

import xyz.funjava.functional.validation.Validation.Failure;
import xyz.funjava.functional.validation.Validation.Success;
import org.junit.Test;

import java.util.function.Function;

import static xyz.funjava.functional.validation.Validator.partials;
import static xyz.funjava.functional.validation.Validator.predicate;
import static xyz.funjava.functional.validation.ValidatorTest.Data.adapt;
import static org.junit.Assert.assertEquals;

/**
 * @author Carlos Sierra Andrés
 */
public class ValidatorTest {

    @Test
    public void testValidate() {
        Validator<String, String, Fail> stringValidator = predicate(
            (String s) -> s.length() > 5,
            s -> new Fail("length should be longer than 5"));

        assertEquals(
            new Failure<>(new Fail("length should be longer than 5")),
            stringValidator.validate("hello"));

        assertEquals(
            new Success<>("hello world"),
            stringValidator.validate("hello world"));

        assertEquals(
            new Failure<>(new Fail("length should be longer than 5")),
            stringValidator.validate("hello").map(String::length));

        assertEquals(
            new Success<>(11),
            stringValidator.validate("hello world").map(String::length));

        Validator<Integer, Integer, Fail> intValidator = predicate(
            (Integer i) -> i > 5,
            i -> new Fail("number should be greater than 5"));

        assertEquals(
            new Success<>(11),
            stringValidator.map(String::length).compose(
                intValidator).validate("hello world"));
    }

    @Test
    public void testAdaptAndPartial() {
        Validator<String, String, Fail> notBlankValidator = predicate(
            (String s) -> !s.isEmpty(), s -> new Fail("should not be empty"));

        Validator<Integer, Integer, Fail> positiveValidator = predicate(
            (Integer i) -> i > 0, i -> new Fail("should be positive"));

        Validator<Data, Data, Fail> dataValidator =
            partials(
                adapt(d -> d.name, "name: ", notBlankValidator),
                adapt(d -> d.number, "number: ", positiveValidator)
            );

        assertEquals(
            new Failure<>(
                new Fail(
                    "name: should not be empty",
                    "number: should be positive")),
            dataValidator.validate(new Data("", -1)));

        assertEquals(
            new Success<Data, Fail>(new Data("hello", 1)),
            dataValidator.validate(new Data("hello", 1)));
    }

    static class Data {
        private String name;
        private Integer number;

        public Data(String name, Integer number) {
            this.number = number;
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Data data = (Data) o;

            return name.equals(data.name) && number.equals(data.number);

        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + number.hashCode();
            return result;
        }

        static <T> Validator<Data, Data, Fail> adapt(
            Function<Data, T> adaptor, String prefix,
            Validator<T, ?, Fail> validator) {

            return (Validator<Data, Data, Fail>)validator.adapt(
                adaptor, (Fail f) -> f.prependAll(prefix));
        }
    }

}
