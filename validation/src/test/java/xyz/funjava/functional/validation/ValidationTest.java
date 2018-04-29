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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

import static xyz.funjava.functional.validation.Validation.apply;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Carlos Sierra Andrés
 */
public class ValidationTest {

    @Test
    public void testSuccess() {
        Validation<String, Fail> validation = new Success<>("test");

        assertTrue(validation.isSuccess());

        try {
            validation.failures();

            fail("Invocation of failures() in Success must throw " +
                "IllegalStateException");
        }
        catch (IllegalStateException ise) {
        }

        assertEquals("test", validation.get());

        assertEquals("test", validation.mapFailures(x -> x).get());

        assertEquals("test2", validation.map(s -> s + "2").get());

        assertEquals(
            "test-other",
            validation.flatMap(s -> new Success<>(s + "-other")).get());

        assertEquals(
            new Success<>("test-other2"),
            apply(
                String::concat,
                new Success<String, Fail>("test"),
                new Success<>("-other2")));
    }

    @Test
    public void testFaiure() {
        Validation<String, Fail> validation = new Failure<>(
            new Fail(Collections.singleton("error")));

        assertFalse(validation.isSuccess());

        assertEquals(
            new Fail(Collections.singleton("error")), validation.failures());

        try {
            validation.get();

            fail("Invocation of get on Failure must throw " +
                "IllegalStateException");
        }
        catch (IllegalStateException ise) {
        }

        assertEquals(validation, validation.map(x -> x + "append"));

        Failure<String, Fail> failure = new Failure<>(
            new Fail(Collections.singleton("otherError")));

        assertEquals(validation, validation.flatMap(x -> failure));

        assertEquals(
            new Failure<>(
                new Fail(new HashSet<>(Arrays.asList("error", "otherError")))),
            apply(String::concat, validation, failure));

        assertEquals(
            new Failure<>(
                new Fail(
                    new HashSet<>(Arrays.asList("error2", "otherError2")))),
            apply(String::concat, validation, failure).mapFailures(
                f -> new Fail(
                    f._failures.stream().map(s -> s + "2").collect(
                        Collectors.toSet()))));
    }

    @Test
    public void testApply() {
        assertEquals(
            new Success<>(new Data("test", 1)),
            apply(
                Data::new,
                new Success<String, Fail>("test"), new Success<>(1)));

        assertEquals(
            new Failure<>(new Fail("error")),
            apply(
                Data::new, new Success<>("test"),
                new Failure<>(new Fail("error"))));

        assertEquals(
            new Failure<>(new Fail("error")),
            apply(
                Data::new, new Failure<>(new Fail("error")),
                new Success<>(10)));

        assertEquals(
            new Failure<>(new Fail("error", "error2")),
            apply(
                Data::new, new Failure<>(new Fail("error")),
                new Failure<>(new Fail("error2"))));
    }

    class Data {
        private final String a;
        private final Integer b;

        public Data(String a, Integer b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Data data = (Data) o;

            if (!a.equals(data.a)) return false;
            return b.equals(data.b);
        }

        @Override
        public int hashCode() {
            int result = a.hashCode();
            result = 31 * result + b.hashCode();
            return result;
        }
    }
}
