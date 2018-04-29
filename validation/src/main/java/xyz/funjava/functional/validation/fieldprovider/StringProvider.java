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

import xyz.funjava.functional.validation.Fail;
import xyz.funjava.functional.validation.Validator;

import java.util.Optional;

/**
 * @author Carlos Sierra Andrés
 */
public interface StringProvider extends FieldProvider {

    @Override
    Optional<String> get(String name);

    public static <T> Validator<StringProvider, T, FieldFail> adapt(
        String fieldName, Validator<Optional<String>, T, Fail> validator) {

        return validator.adapt(
            sp -> sp.get(fieldName), FieldFail.fromFail(fieldName));
    }

}
