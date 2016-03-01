/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.ca.cat.catlean.oapp.processor.core.hash;

import java.util.Objects;

/**
 *
 * @author lefebvreme
 * @since 26-02-2016
 * @version 0.0.1
 */
public class HashFactory {

    /**
     * Supported type of {@link Hash} that the factory can instanciate
     *
     * @author mehdi Lefebvre
     * @version 0.0.1
     * @since 0.0.1
     */
    public static enum HashType {
        FAST,
        INTUITIVE,
        STANDARD
    }

    /**
     * Create a type of {@link Hash} according to specific {@link HashType}.
     *
     * @param type	the type of the {@link Hash} expected
     * @return	the {@link Hash} instance if it exists
     * @throws Exception	if the {@link HashType} doesn't exists
     */
    public static Hash createHash(HashType type) throws NoSuchTypeException {

        Objects.requireNonNull(type, "Type cannot be null");

        switch(type) {
            case FAST :
                return new FastHash();
            case INTUITIVE:
                return new IntuitiveHash();
            case STANDARD:
                return new StdHash();
        }
        // never reached ...
        throw new NoSuchTypeException("Unknow type of hash");
    }
}
