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

/**
 *
 * @author lefebvreme
 * @since 26-02-2016
 * @version 0.0.1
 */
class IntuitiveHash implements Hash {

    @Override
    public long hash(String str) {
        byte[] bytes = str.getBytes();
        long hash = 0;
        byte b;
        int i = 0;
        while(i < bytes.length) {
            b = bytes[i++];
            hash += b;
        }
        return hash;
    }
}
