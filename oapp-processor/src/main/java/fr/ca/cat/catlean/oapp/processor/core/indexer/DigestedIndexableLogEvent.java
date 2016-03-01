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
package fr.ca.cat.catlean.oapp.processor.core.indexer;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author lefebvreme
 * @since 26-02-2016
 * @version 0.0.1
 */
public class DigestedIndexableLogEvent extends IndexableLogEvent {

    /**
     * Convert a string to hex format.
     * @param bytes
     * @return
     */
    private String toHexString(byte[] bytes) {
        final int MASK = 0xFF;
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < bytes.length; ++i) {
            sb.append(Integer.toHexString(MASK & bytes[i]));
        }
        return sb.toString();
    }

    @Override
    protected void generateFootPrint() throws IllegalAccessException, NoSuchAlgorithmException {
        List<Field> fields = Arrays.asList(getClass().getDeclaredFields());
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        final IllegalAccessException[] last = {null};
        fields.forEach((f) -> {
            try {
                final String val = f.get(this).toString();
                digest.update(val.getBytes(StandardCharsets.UTF_8));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                last[0] = e;
            }
        });
        if(last[0] != null) {
            throw last[0];
        }
        footPrint = toHexString(digest.digest());
    }
}
