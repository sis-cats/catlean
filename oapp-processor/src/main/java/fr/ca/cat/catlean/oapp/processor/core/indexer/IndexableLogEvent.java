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

import static org.elasticsearch.common.xcontent.XContentFactory.*;

import fr.ca.cat.catlean.oapp.processor.core.hash.Hash;
import fr.ca.cat.catlean.oapp.processor.domain.LogEvent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

/**
 * Add meta information relevant for the index.
 * Mainly add a unique identifier to make later process easier. The footprint calculation is empiric
 * and use an hash function which has a good enough divergence but stay light a approximative in terms of collision.
 * Using a digest should be better but cost a little bit more.
 *
 * TODO : Implementation uses reflection to be faster and because the object change during the definition of the context
 *          it may be review to validate the incidence on the performance side. If the impact is too heavy, we have to
 *          make a fresh implementation that don't use reflection.
 * Note: this kind of reflection is a little bit friendly  that the ObjectMapper since it is a lot less generic.
 *
 * @author lefebvreme
 * @since 26-02-2016
 * @version 0.0.1
 */
public class IndexableLogEvent extends LogEvent implements IndexedJsonifiable {

    @Autowired
    private Hash hash;

    public IndexableLogEvent() {
        super();
    }

    public IndexableLogEvent(final LogEvent event) throws IllegalAccessException, CloneNotSupportedException, NoSuchAlgorithmException {
        this();
        event.cloneTo(this);
        generateFootPrint();
    }

    protected void generateFootPrint() throws IllegalAccessException, NoSuchAlgorithmException {
        List<Field> fields = Arrays.asList(getClass().getDeclaredFields());
        final StringBuilder sb = new StringBuilder();
        final IllegalAccessException[] last = {null};
        fields.forEach((f) -> {
            try {
                sb.append(f.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                last[0] = e;
            }
        });
        if(last[0] != null) {
            throw last[0];
        }
        footPrint = Long.toString(hash.hash(sb.toString()));
    }

    protected String footPrint;

    public String getFootPrint() {
        return footPrint;
    }

    @Override
    public XContentBuilder buildJSON() throws IOException, IllegalAccessException {
        final XContentBuilder builder = jsonBuilder();
        builder.startObject();
        Field[] fields = getClass().getDeclaredFields();
        for (final Field f : fields) {
            builder.field(f.getName(), f.get(this));
        }
        builder.endObject();
        return builder;
    }
}
