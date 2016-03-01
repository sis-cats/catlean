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
package fr.ca.cat.catlean.tomcat.conf.spring;

import fr.ca.cat.catlean.tomcat.conf.SchedulerConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lefebvreme
 * @since 28-02-2016
 * @version 0.0.1
 */
public class DefaultSchedulerConfiguration implements SchedulerConfiguration {

    private List<String> groups;
    private List<String> jobKeys;
    private List<String> triggerKeys;

    public DefaultSchedulerConfiguration() {
        this(new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
    }

    public DefaultSchedulerConfiguration(List<String> groups, List<String> jobKeys, List<String> triggerKeys) {
        this.groups = groups;
        this.jobKeys = jobKeys;
        this.triggerKeys = triggerKeys;
    }

    @Override
    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    @Override
    public List<String> getJobKeys() {
        return jobKeys;
    }

    public void setJobKeys(List<String> jobKeys) {
        this.jobKeys = jobKeys;
    }

    @Override
    public List<String> getTriggerKeys() {
        return triggerKeys;
    }

    public void setTriggerKeys(List<String> triggerKeys) {
        this.triggerKeys = triggerKeys;
    }
}
