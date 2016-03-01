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
package fr.ca.cat.catlean.tomcat.adapters.scheduler;

import fr.ca.cat.catlean.tomcat.api.adapters.scheduler.SchedulerAdapter;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.quartz.impl.matchers.GroupMatcher.groupEquals;

/**
 *
 * @author lefebvreme
 * @since 28-02-2016
 * @version 0.0.1
 */
public class QuartzSchedulerAdapter implements SchedulerAdapter {

    // TODO : replace logger by tomcat's log4j wrapper.
    private static Logger logger = Logger.getLogger(QuartzSchedulerAdapter.class.getName());

    private List<JobKey> jobKeys;
    private List<TriggerKey> triggerKeys;

    @Autowired
    private SchedulerFactory factory;
    @Autowired
    private Scheduler scheduler;

    @SuppressWarnings("unused")
    public QuartzSchedulerAdapter() {
        // TODO : check thread safetyness
        this(new ArrayList<>(), new ArrayList<>());
    }

    public QuartzSchedulerAdapter(List<JobKey> jobKeys, List<TriggerKey> triggerKeys) {
        this.jobKeys = jobKeys;
        this.triggerKeys = triggerKeys;
    }

    @Override
    public List<JobKey> getJobKeys() throws Exception {
        if(shouldRefreshKeys()) {
            for (final String group : scheduler.getJobGroupNames()) {
                jobKeys.addAll(scheduler.getJobKeys(groupEquals(group)).stream().collect(Collectors.toList()));
            }
        }
        return jobKeys;
    }

    public List<TriggerKey> getTriggerKeys() throws Exception {
        if(shouldRefreshTriggers()) {
            for (final String group : scheduler.getTriggerGroupNames()) {
                triggerKeys.addAll(scheduler.getTriggerKeys(groupEquals(group)).stream().collect(Collectors.toList()));
            }
        }
        return triggerKeys;
    }

    @Override
    public boolean isProcessingAvailable() {
        return null != scheduler;
    }

    private boolean shouldRefreshKeys() {
        // TODO : add time dealing to refresh information.
        // TODO : add cache
        return null == jobKeys;
    }

    private boolean shouldRefreshTriggers() {
        // TODO : add time dealing to refresh information.
        // TODO : add cache
        return null == triggerKeys;
    }
}
