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
package fr.ca.cat.catlean.oapp.processor.domain;

import java.math.BigInteger;

/**
 *
 * @author lefebvreme
 * @since 24-02-2016
 * @version 0.0.1
 */
public class OappConsolidation {

    private String serverName;
    private BigInteger tsStart;
    private BigInteger tsEnd;
    private int slotId;
    private int jobId;

    public OappConsolidation() {
    }

    public OappConsolidation(String serverName, BigInteger tsStart, BigInteger tsEnd, int slotId, int jobId) {
        this.serverName = serverName;
        this.tsStart = tsStart;
        this.tsEnd = tsEnd;
        this.slotId = slotId;
        this.jobId = jobId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public BigInteger getTsStart() {
        return tsStart;
    }

    public void setTsStart(BigInteger tsStart) {
        this.tsStart = tsStart;
    }

    public BigInteger getTsEnd() {
        return tsEnd;
    }

    public void setTsEnd(BigInteger tsEnd) {
        this.tsEnd = tsEnd;
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OappConsolidation that = (OappConsolidation) o;

        if (slotId != that.slotId) return false;
        if (jobId != that.jobId) return false;
        if (!serverName.equals(that.serverName)) return false;
        if (!tsStart.equals(that.tsStart)) return false;
        return tsEnd.equals(that.tsEnd);

    }

    @Override
    public int hashCode() {
        int result = serverName.hashCode();
        result = 31 * result + tsStart.hashCode();
        result = 31 * result + tsEnd.hashCode();
        result = 31 * result + slotId;
        result = 31 * result + jobId;
        return result;
    }
}
