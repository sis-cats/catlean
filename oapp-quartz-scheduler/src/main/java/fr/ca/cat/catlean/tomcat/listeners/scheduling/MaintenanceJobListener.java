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
package fr.ca.cat.catlean.tomcat.listeners.scheduling;

import fr.ca.cat.catlean.tomcat.api.adapters.scheduler.AppJobListener;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;

import java.util.logging.Logger;

/**
 * Each times a job will be / was executed, this listener will act.
 *
 * Our in memory model and to avoid extra useless load, we are using a specific context to store last execution of a job,
 * information about watch related information and so on.
 *
 *
 * @author lefebvreme
 * @since 28-02-2016
 * @version 0.0.1
 */
public class MaintenanceJobListener extends JobListenerSupport implements AppJobListener {

    private static final Logger log = Logger.getLogger(MaintenanceJobListener.class.getName());

    private String name;

    public MaintenanceJobListener(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        log.info("Prepare job execution.");
        final JobDetail jobDetail = context.getJobDetail();

    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        log.info("Job has been executeed.");
    }
}
