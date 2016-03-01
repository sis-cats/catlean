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
package fr.ca.cat.catlean.tomcat.api.model;

import fr.ca.cat.catlean.tomcat.api.Describable;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.UUID;

/**
 * @author lefebvreme
 * @version 0.0.1
 * @since 28-02-2016
 */
public class DbJoblet implements Serializable, Describable, Cloneable {

    private UUID uuid;
    private String jobName;
    private String serverName;
    private int jobId;
    private int slotId;
    private long tsStart;
    private long tsEnd;

    public DbJoblet() {
        uuid = UUID.randomUUID();
    }

    public DbJoblet(String jobName, String serverName, int jobId, int slotId, long tsStart, long tsEnd) {
        this();
        this.jobName = jobName;
        this.serverName = serverName;
        this.jobId = jobId;
        this.slotId = slotId;
        this.tsStart = tsStart;
        this.tsEnd = tsEnd;
    }

    public DbJoblet(UUID uuid, String jobName, String serverName, int jobId, int slotId, long tsStart, long tsEnd) {
        this.uuid = uuid;
        this.jobName = jobName;
        this.serverName = serverName;
        this.jobId = jobId;
        this.slotId = slotId;
        this.tsStart = tsStart;
        this.tsEnd = tsEnd;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public long getTsStart() {
        return tsStart;
    }

    public void setTsStart(long tsStart) {
        this.tsStart = tsStart;
    }

    public long getTsEnd() {
        return tsEnd;
    }

    public void setTsEnd(long tsEnd) {
        this.tsEnd = tsEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DbJoblet dbJoblet = (DbJoblet) o;

        return uuid.equals(dbJoblet.uuid);

    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    public String describe() {
        StringBuilder sb = new StringBuilder();
        try {
            // I'm still lazy,
            // TODO : reimplement this for performance improvement using the plain field name - value :)
            Field[] fields = getClass().getDeclaredFields();
            for (final Field f : fields) {
                if (sb.length() > 0) {
                    sb.append(" - ");
                }
                sb.append(f.getName())
                        .append(':')
                        .append(f.get(this));
            }
        } catch (IllegalAccessException e) {
            // nothing to do right now.
        }
        return sb.toString();
    }
}
