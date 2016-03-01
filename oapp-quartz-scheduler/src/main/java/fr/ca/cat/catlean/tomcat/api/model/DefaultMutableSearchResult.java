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

import org.quartz.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lefebvreme
 * @since 28-02-2016
 * @version 0.0.1
 */
public class DefaultMutableSearchResult implements MutableJobSearchResult {

    Map<String, List<Job>> jobs;
    Map<String, List<JobKey>> jobKeys;
    Map<String, List<Trigger>> triggers;
    Map<String, List<TriggerKey>> triggerJeyss;

    public DefaultMutableSearchResult() {
        this(new HashMap<>(), new HashMap<>());
    }

    public DefaultMutableSearchResult(Map<String, List<Trigger>> triggers, Map<String, List<Job>> jobs) {
        this.triggers = triggers;
        this.jobs = jobs;
        checkInit();
    }

    protected void checkInit() {
        if(!jobs.containsKey(DEFAULT_JOB)) {
            jobs.put(DEFAULT_JOB, new ArrayList<>());
        }
        if (!triggers.containsKey(DEFAULT_TRIGGER)) {
            triggers.put(DEFAULT_TRIGGER, new ArrayList<>());
        }
    }


    public MutableJobSearchResult addJobKeyForGroup(String groupName, Job job) {
        if(!jobs.containsKey(groupName)) {
            jobs.put(groupName, new ArrayList<>());
        }
        jobs.get(groupName).add(job);
        return this;
    }

    @Override
    public MutableJobSearchResult addJobForGroup(String groupName, JobDetail job) {
        return null;
    }

    @Override
    public MutableJobSearchResult addTriggerForGroup(String groupName, Trigger trigger) {
        if (!triggers.containsKey(groupName)) {
            triggers.put(groupName, new ArrayList<>());
        }
        triggers.get(groupName).add(trigger);
        return this;
    }

    @Override
    public MutableJobSearchResult addJobKeyForGroup(String groupName, JobKey jobKey) {
        return null;
    }

    @Override
    public MutableJobSearchResult addTriggerKeyForGroup(String groupName, TriggerKey triggerKey) {
        return null;
    }

    @Override
    public List<Job> listJob() {
        final List<Job> allJobs = new ArrayList<>();
        jobs.forEach((group, items) -> allJobs.addAll(items));
        return allJobs;
    }

    @Override
    public List<JobKey> listJobKeys() {
        return null;
    }

    @Override
    public List<Job> listJobByGroup(String groupName) {
        final List<Job> groupJobs = new ArrayList<>();
        if(jobs.containsKey(groupName)) {
            groupJobs.addAll(jobs.get(groupName));
        }
        return groupJobs;
    }

    @Override
    public List<JobKey> listJobKeysByGroup(String groupName) {
        return null;
    }

    @Override
    public List<Trigger> listTrigger() {
        final List<Trigger> allTriggers = new ArrayList<>();
        triggers.forEach((gp, items) -> allTriggers.addAll(items));
        return allTriggers;
    }

    @Override
    public List<TriggerKey> listTriggerKeys() {
        return null;
    }

    @Override
    public List<Trigger> listTriggerByGroup(String groupName) {
        final List<Trigger> items = new ArrayList<>();
        if (triggers.containsKey(groupName)) {
            items.addAll(triggers.get(groupName));
        }
        return items;
    }

    @Override
    public List<TriggerKey> listTriggerKeysByGroup(String groupName) {
        return null;
    }
}
