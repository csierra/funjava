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

import xyz.funjava.functional.Function2;
import xyz.funjava.functional.higherkinded.monoid.Monoid;
import xyz.funjava.functional.validation.Fail;
import xyz.funjava.functional.validation.Validation;
import xyz.funjava.functional.validation.Validation.Failure;
import xyz.funjava.functional.validation.Validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Supplier;

import static xyz.funjava.functional.validation.Validation.just;

/**
 * @author Carlos Sierra Andrés
 */
public interface FieldProvider {

    <T> Optional<T> get(String name);

    Validator<Object, FieldProvider, Fail> safeFieldProvider();

    static <T> SafeCast<T, Fail> safeCast(Class<T> clazz) {
        return input -> {
            try {
                return new Validation.Success<>(clazz.cast(input));
            }
            catch (ClassCastException cce) {
                return new Failure<>(
                    new Fail("can't be casted to " + clazz));
            }
        };
    }

    public static <T> Validator<Optional<T>, T, Fail> mandatory() {
        return input -> {
            if (input.isPresent()) {
                return just(input.get());
            }
            else {
                return new Failure<>(new Fail("should not be empty"));
            }
        };
    }

    public static <T> Validator<Optional<T>, Optional<T>, Fail> withDefault() {
        return Validation::just;
    }

    public static <T> Validator<Optional<T>, T, Fail> withDefault(
        Supplier<T> def) {
        return input -> {
            if (input.isPresent()) {
                return just(input.get());
            }
            else {
                return just(def.get());
            }
        };
    }

    public static <T, R> Validator<Optional<T>, Optional<R>, Fail> ifPresent(
        Validator<T, R, Fail> validator) {

        return input -> {
            if (input.isPresent()) {
                return validator.validate(input.get()).map(
                    Optional::ofNullable);
            }
            else {
                return just(Optional.empty());
            }
        };

    }

    interface Adaptor<F extends Monoid<F>> {
        <T, R> Validation<R, FieldFail> safeGet(
            String fieldName, Validator<Optional<T>, R, F> validator);

        Validation<Adaptor<F>, FieldFail> getAdaptor(String fieldName);

        static <T, R, F extends Monoid<F>>
        Validation<R, FieldFail> safeGet(
            Adaptor<F> adaptor, String fieldName,
            Validator <Optional<T>, R, F> validator) {

            return adaptor.safeGet(fieldName, validator);
        }

        static <T, R, F extends Monoid<F>>
        Validation<R, FieldFail> safeGet(
            Validation<Adaptor<F>, FieldFail> adaptor, String fieldName,
            Validator<Optional<T>, R, F> validator) {

            return adaptor.flatMap(a -> a.safeGet(fieldName, validator));
        }

        static <F extends Monoid<F>>
        Validation<Adaptor<F>, FieldFail> focus(
            Adaptor<F> adaptor, String ... fields) {

            if (fields.length == 0) {
                return just(adaptor);
            }

            Validation<Adaptor<F>, FieldFail> current = adaptor.getAdaptor(
                fields[0]);

            if (fields.length == 1) {
                return current;
            }

            for (int i = 1; i < fields.length; i++) {
                String field = fields[i];

                current = current.flatMap(a -> a.getAdaptor(field));

            }

            return current;
        }
    }

    default <F extends Monoid<F>> Adaptor<F> getAdaptor(
        Function2<String, F, FieldFail> map) {

        return getAdaptor(map, Collections.emptyList());
    }

    default <F extends Monoid<F>> Adaptor<F> getAdaptor(
        Function2<String, F, FieldFail> map,
        Collection<String> stack) {

        return new Adaptor<F>() {

            @Override
            public <T, R> Validation<R, FieldFail> safeGet(
                String fieldName, Validator<Optional<T>, R, F> validator) {

                return validator.validate(get(fieldName)).mapFailures(
                    map.curried().apply(fieldName)).mapFailures(f -> {

                    for (String s : stack) {
                        f = new FieldFail(s, f);
                    }

                    return f;
                });
            }

            @Override
            public Validation<Adaptor<F>, FieldFail> getAdaptor(
                String fieldName) {

                ArrayList<String> objects = new ArrayList<>(stack);

                objects.add(0, fieldName);

                return mandatory().
                    compose(safeFieldProvider()).
                    validate(get(fieldName)).
                    map(fp -> fp.getAdaptor(map, objects)).mapFailures(
                    f -> new FieldFail(fieldName, f));
            }

        };
    }
}
