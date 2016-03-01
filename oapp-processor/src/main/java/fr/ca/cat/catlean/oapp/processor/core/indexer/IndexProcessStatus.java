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

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author lefebvreme
 * @since 26-02-2016
 * @version 0.0.1
 */
public class IndexProcessStatus {

    public static final String INDEX_TYPE = "indexType";
    public static final String INDEX_NAME = "indexName";
    public static final String INDEX_ID = "indexId";

    private Map<String, String> properties;
    private long version;
    private boolean created;
    private DateTime responseTick;
    private long duration;
    private TimeUnit timeUnit;

    public IndexProcessStatus() {
        this(new HashMap<>());
    }

    public IndexProcessStatus(Map<String, String> properties) {
        this.properties = properties;
    }

    public IndexProcessStatus put(final String key, final String value) {
        properties.put(key, value);
        return this;
    }

    public String getValue(final String key) {
        return properties.get(key);
    }

    public boolean contains(final String key) {
        return properties.containsKey(key);
    }

    public IndexProcessStatus remove(final String key) {
        properties.remove(key);
        return this;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public IndexProcessStatus setProperties(Map<String, String> properties) {
        this.properties = properties;
        return this;
    }

    public long getVersion() {
        return version;
    }

    public IndexProcessStatus setVersion(long version) {
        this.version = version;
        return this;
    }

    public boolean isCreated() {
        return created;
    }

    public IndexProcessStatus setCreated(boolean created) {
        this.created = created;
        return this;
    }

    public DateTime getResponseTick() {
        return responseTick;
    }

    public IndexProcessStatus setResponseTick(DateTime responseTick) {
        this.responseTick = responseTick;
        return this;
    }

    public long getDuration() {
        return duration;
    }

    public IndexProcessStatus setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public IndexProcessStatus setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
        return this;
    }
}
