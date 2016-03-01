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
package fr.ca.cat.catlean.tomcat.jobs;

import fr.ca.cat.catlean.tomcat.api.adapters.scheduler.QuartzJobAdapter;
import fr.ca.cat.catlean.tomcat.core.BehaviorStrategy;
import fr.ca.cat.catlean.tomcat.core.JobType;
import fr.ca.cat.catlean.tomcat.core.SchedulingStrategy;
import fr.ca.cat.catlean.tomcat.core.annotation.Schedulable;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author lefebvreme
 * @since 28-02-2016
 * @version 0.0.1
 */
@Schedulable(frequency = 1,
        timeUnit = TimeUnit.HOURS,
        jobName = AutoCleanupJob.JOB_NAME,
        jobGroup = AutoCleanupJob.JOB_GROUP,
        triggerName = AutoCleanupJob.DEFAULT_TRIGGER_NAME,
        triggerGroup = AutoCleanupJob.DEFAULT_TRIGGER_GROUP,
        jobType = JobType.MAINTENANCE,
        strategy = SchedulingStrategy.ININITE,
        behaviorStrategy = BehaviorStrategy.AUTO_REPROCESS)
public class AutoCleanupJob implements QuartzJobAdapter {

    public static final String JOB_NAME = "autoCleanup";
    public static final String JOB_GROUP = "maintainance";

    public static final String DEFAULT_TRIGGER_NAME = "autoCleanupTrigger";
    public static final String DEFAULT_TRIGGER_GROUP = "maintenance";

    @SuppressWarnings("unused")
    private String name;
    @SuppressWarnings("unused")
    private String group;

    public AutoCleanupJob() {
        this(JOB_NAME, JOB_GROUP);
    }

    public AutoCleanupJob(String name, String group) {
        this.name = name;
        this.group = group;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // TODO : Implement me
    }
}
