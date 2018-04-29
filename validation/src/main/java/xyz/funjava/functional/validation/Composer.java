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

import xyz.funjava.functional.higherkinded.monoid.Monoid;

/**
 * @author Carlos Sierra Andrés
 */
public interface Composer {

    static <A, B, C, FAIL extends Monoid<FAIL>> Validator<A, C, FAIL> compose(
        Validator<A, B, FAIL> a, Validator<B, C, FAIL> b) {

        return a.compose(b);
    }

    static <A, B, C, D, FAIL extends Monoid<FAIL>> Validator<A, D, FAIL> compose(
        Validator<A, B, FAIL> a, Validator<B, C, FAIL> b, Validator<C, D, FAIL> c) {

        return a.compose(b).compose(c);
    }

    static <A, B, C, D, E, FAIL extends Monoid<FAIL>> Validator<A, E, FAIL> compose(
        Validator<A, B, FAIL> a, Validator<B, C, FAIL> b, Validator<C, D, FAIL> c, Validator<D, E, FAIL> d) {

        return a.compose(b).compose(c).compose(d);
    }

    static <A, B, C, D, E, F, FAIL extends Monoid<FAIL>> Validator<A, F, FAIL> compose(
        Validator<A, B, FAIL> a, Validator<B, C, FAIL> b, Validator<C, D, FAIL> c, Validator<D, E, FAIL> d, Validator<E, F, FAIL> e) {

        return a.compose(b).compose(c).compose(d).compose(e);
    }

}
