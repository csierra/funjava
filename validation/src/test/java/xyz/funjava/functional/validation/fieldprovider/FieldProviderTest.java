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

import xyz.funjava.functional.higherkinded.monoid.Monoid;
import xyz.funjava.functional.validation.fieldprovider.FieldProvider.Adaptor;
import xyz.funjava.functional.validation.Fail;
import xyz.funjava.functional.validation.Validation;
import xyz.funjava.functional.validation.Validation.Failure;
import xyz.funjava.functional.validation.Validator;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static xyz.funjava.functional.validation.fieldprovider.FieldProvider.Adaptor.focus;
import static xyz.funjava.functional.validation.fieldprovider.FieldProvider.mandatory;
import static xyz.funjava.functional.validation.fieldprovider.FieldProvider.safeCast;
import static xyz.funjava.functional.validation.Composer.compose;
import static xyz.funjava.functional.validation.Validation.apply;
import static xyz.funjava.functional.validation.Validation.just;
import static xyz.funjava.functional.validation.Validator.predicate;

/**
 * @author Carlos Sierra Andrés
 */
public class FieldProviderTest {

    static class MapFieldProvider implements FieldProvider {

        private Map<String, Object> _map;

        public MapFieldProvider(Map<String, Object> map) {
            _map = map;
        }

        @Override
        public <T> Optional<T> get(String name) {
            return Optional.ofNullable((T)_map.get(name));
        }

        @Override
        public Validator<Object, FieldProvider, Fail> safeFieldProvider() {
            return FieldProvider.safeCast(Map.class).map(MapFieldProvider::new);
        }

    }

    @Test
    public void testFieldProvider() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("test", "testValueLongerThan10");

        MapFieldProvider fieldProvider = new MapFieldProvider(map);

        Validator<String, String, Fail> longerThan10 =
            predicate(
                s -> s.length() > 10, s -> new Fail("must be longer than 10"));

        Adaptor<Fail> adaptor = fieldProvider.getAdaptor(FieldFail::new);

        Validation<String, FieldFail> validation = adaptor.safeGet(
            "test", compose(mandatory(), safeCast(String.class), longerThan10));

        Assert.assertEquals(just("testValueLongerThan10"), validation);
    }

    @Test
    public void testFieldProviderWhenNull() {
        HashMap<String, Object> map = new HashMap<>();

        MapFieldProvider fieldProvider = new MapFieldProvider(map);

        Validator<String, String, Fail> longerThan10 =
            predicate(
                s -> s.length() > 10, s -> new Fail("must be longer than 10"));

        Adaptor<Fail> adaptor = fieldProvider.getAdaptor(FieldFail::new);

        Validation<String, FieldFail> validation =
            adaptor.safeGet(
                "test",
                compose(mandatory(), safeCast(String.class), longerThan10));

        Assert.assertEquals(
            new Failure<String, FieldFail>(
                new FieldFail("test", "should not be empty")), validation);
    }

    @Test
    public void testFieldProviderWhenFailure() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("test", "testValue");

        MapFieldProvider fieldProvider = new MapFieldProvider(map);

        Validator<String, String, Fail> longerThan10 =
            predicate(
                s -> s.length() > 10, s -> new Fail("must be longer than 10"));

        Adaptor<Fail> adaptor = fieldProvider.getAdaptor(FieldFail::new);

        Validation<String, FieldFail> validation =
            adaptor.safeGet(
                "test",
                compose(mandatory(), safeCast(String.class), longerThan10));

        Assert.assertEquals(
            new Failure<String, FieldFail>(
                new FieldFail("test", "must be longer than 10")),
            validation);
    }

    @Test
    public void testFieldProviderWhenNested() {
        HashMap<String, Object> map = new HashMap<>();

        HashMap<String, Object> nested = new HashMap<>();

        map.put("nested", nested);

        nested.put("test", "testValueLongerThan10");

        FieldProvider fieldProvider = new MapFieldProvider(map);

        Validator<String, String, Fail> longerThan10 =
            predicate(
                s -> s.length() > 10, s -> new Fail("must be longer than 10"));

        Adaptor<Fail> adaptor = fieldProvider.getAdaptor(FieldFail::new);

        Assert.assertEquals(
            just("testValueLongerThan10"),
            adaptor.getAdaptor("nested").flatMap(nestedAdaptor ->
                nestedAdaptor.safeGet(
                    "test",
                    compose(
                        mandatory(), safeCast(String.class), longerThan10))));
    }

    @Test
    public void testFieldProviderWhenNestedIsNull() {
        HashMap<String, Object> map = new HashMap<>();

        FieldProvider fieldProvider = new MapFieldProvider(map);

        Validator<String, String, Fail> longerThan10 =
            predicate(
                s -> s.length() > 10, s -> new Fail("must be longer than 10"));

        Adaptor<Fail> adaptor = fieldProvider.getAdaptor(FieldFail::new);

        Validation<String, FieldFail> validation =
            adaptor.getAdaptor("nested").flatMap(nestedAdaptor ->
                nestedAdaptor.safeGet(
                    "test",
                    compose(
                        mandatory(), safeCast(String.class), longerThan10)));

        Assert.assertEquals(
            new Failure<>(
                new FieldFail("nested", "should not be empty")), validation);
    }

    @Test
    public void testFieldProviderWhenNestedhasWrongType() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("nested", "wrong");

        FieldProvider fieldProvider = new MapFieldProvider(map);

        Validator<String, String, Fail> longerThan10 =
            predicate(
                s -> s.length() > 10, s -> new Fail("must be longer than 10"));

        Adaptor<Fail> adaptor = fieldProvider.getAdaptor(FieldFail::new);

        Validation<String, FieldFail> validation =
            adaptor.getAdaptor("nested").flatMap(nestedAdaptor ->
                nestedAdaptor.safeGet(
                    "test",
                    compose(
                        mandatory(), safeCast(String.class), longerThan10)));

        Assert.assertEquals(
            new Failure<>(
                new FieldFail(
                    "nested", "can't be casted to interface java.util.Map")),
            validation);
    }

    @Test
    public void testFailureFieldProviderWhenDeepNested() {
        HashMap<String, Object> map = new HashMap<>();

        HashMap<Object, Object> nestedMap2 = new HashMap<>();

        nestedMap2.put("test", "test");

        HashMap<Object, Object> nestedMap = new HashMap<>();

        nestedMap.put("nested2", nestedMap2);

        map.put("nested", nestedMap);

        FieldProvider fieldProvider = new MapFieldProvider(map);

        Validator<String, String, Fail> longerThan10 =
            predicate(
                s -> s.length() > 10, s -> new Fail("must be longer than 10"));

        Adaptor<Fail> adaptor = fieldProvider.getAdaptor(FieldFail::new);

        Validation<Adaptor<Fail>, FieldFail> nested2 = focus(
            adaptor, "nested", "nested2");

        Validation<String, FieldFail> validation =
            Adaptor.safeGet(
                nested2, "test",
                compose(mandatory(), safeCast(String.class), longerThan10));

        Assert.assertEquals(
            new Failure<String, FieldFail>(
                new FieldFail("nested",
                    new FieldFail(
                    "nested2", new FieldFail("test", "must be longer than 10")))),
            validation);
    }

    @Test
    public void testFieldProviderWhenDeepNested() {
        HashMap<String, Object> map = new HashMap<>();

        HashMap<Object, Object> nestedMap2 = new HashMap<>();

        nestedMap2.put("test", "stringlongerthan10");

        HashMap<Object, Object> nestedMap = new HashMap<>();

        nestedMap.put("nested2", nestedMap2);

        map.put("nested", nestedMap);

        FieldProvider fieldProvider = new MapFieldProvider(map);

        Validator<String, String, Fail> longerThan10 =
            predicate(
                s -> s.length() > 10, s -> new Fail("must be longer than 10"));

        Adaptor<Fail> adaptor = fieldProvider.getAdaptor(FieldFail::new);

        Validation<Adaptor<Fail>, FieldFail> nested = focus(
            adaptor, "nested", "nested2");

        Validation<String, FieldFail> validation =
            Adaptor.safeGet(
                nested, "test",
                compose(mandatory(), safeCast(String.class), longerThan10));

        Assert.assertEquals(just("stringlongerthan10"), validation);
    }

    @Test
    public void testFailureFieldProviderWhenProgressiveNesting() {
        HashMap<String, Object> map = new HashMap<>();

        HashMap<Object, Object> nestedMap2 = new HashMap<>();

        nestedMap2.put("test", "test");

        HashMap<Object, Object> nestedMap = new HashMap<>();

        nestedMap.put("nested2", nestedMap2);

        map.put("nested", nestedMap);

        FieldProvider fieldProvider = new MapFieldProvider(map);

        Validator<String, String, Fail> longerThan10 =
            predicate(
                s -> s.length() > 10, s -> new Fail("must be longer than 10"));

        Adaptor<Fail> adaptor = fieldProvider.getAdaptor(FieldFail::new);

        Validation<Adaptor<Fail>, FieldFail> nested = focus(
            adaptor, "nested");

        Validation<Adaptor<Fail>, FieldFail> nested2 = focus(
            adaptor, "nested", "nested2");

        Validation<String, FieldFail> validation = Adaptor.safeGet(
            nested, "test",
            compose(mandatory(), safeCast(String.class), longerThan10));

        Validation<String, FieldFail> validation2 = Adaptor.safeGet(
            nested2, "test",
            compose(mandatory(), safeCast(String.class), longerThan10));

        Assert.assertEquals(
            new Failure<String, FieldFail>(
                new FieldFail("nested",
                        new FieldFail(
                            new HashMap<String, Monoid>(){{
                            put("test", new Fail("should not be empty"));
                            put("nested2", new FieldFail("test", "must be longer than 10"));
                        }}))),
            apply(String::concat, validation, validation2));
    }


}
