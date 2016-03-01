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
package fr.ca.cat.catlean.tomcat.workers.scheduling.installers;

import fr.ca.cat.catlean.tomcat.api.core.DefaultSearchQuery;
import fr.ca.cat.catlean.tomcat.api.core.SearchQuery;
import fr.ca.cat.catlean.tomcat.api.model.JobSearchResult;
import fr.ca.cat.catlean.tomcat.api.workers.BaseInstaller;
import fr.ca.cat.catlean.tomcat.api.workers.Installer;
import fr.ca.cat.catlean.tomcat.api.workers.WorkerException;
import fr.ca.cat.catlean.tomcat.conf.SchedulerConfiguration;
import fr.ca.cat.catlean.tomcat.jobs.AutoCleanupJob;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import java.util.logging.Logger;

import static org.quartz.DateBuilder.tomorrowAt;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 *
 * @author lefebvreme
 * @since 28-02-2016
 * @version 0.0.1
 */
public class DeploymentJobInstaller extends BaseInstaller<SchedulerConfiguration> implements Installer<SchedulerConfiguration> {

    private static Logger log = Logger.getLogger(DeploymentJobInstaller.class.getName());

    /**
     * Default constructor.
     * Leave this one since we may have to do come automated tasks which require
     * the default constructor
     */
    public DeploymentJobInstaller() {

    }

    protected final JobSearchResult search(final SchedulerConfiguration configuration) throws WorkerException {
        log.info("Looking for actual job define in the scheduler");
        final SearchQuery query = new DefaultSearchQuery();
        configuration.getGroups().forEach(query::addGroup);
        configuration.getJobKeys().forEach(query::addJobKey);
        configuration.getTriggerKeys().forEach(query::addTriggerKey);
        return searchEngine.search(query);
    }

    @Override
    public final void install(final SchedulerConfiguration configuration, boolean uninstallBefore) throws WorkerException {
        if(null == searchResult) {
            searchResult = search(configuration);
        }
        if(uninstallBefore) {
            uninstall(configuration);
        }
        JobDetail jobDetail = newJob(AutoCleanupJob.class)
                .withIdentity(AutoCleanupJob.JOB_NAME, AutoCleanupJob.JOB_GROUP)
                .build();
        Trigger trigger = newTrigger()
                .withIdentity(AutoCleanupJob.DEFAULT_TRIGGER_NAME, AutoCleanupJob.DEFAULT_TRIGGER_GROUP)
                .startAt(tomorrowAt(0, 20, 0))
                .withSchedule(
                        simpleSchedule()
                        .withIntervalInHours(24)
                        .repeatForever()
                ).build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.severe(String.format("Unable to schedule autoCleanup job. Reason [%s]", e.getMessage()));
        }

        if(hasNext()) {
            next().setErrors(errors).install(configuration, false);
        }
    }

    @Override
    public final void uninstall(final SchedulerConfiguration configuration) throws WorkerException {
        if(null == searchResult) {
            searchResult = search(configuration);
        }
        searchResult.listJobKeys().forEach(jobKey -> {
            try {
                scheduler.deleteJob(jobKey);
            } catch (SchedulerException e) {
                log.severe(String.format("An error occurred during deleting job. Reason [%s]", e.getMessage()));
                errors.add(e);
            }
        });
        try {
            scheduler.deleteJob(jobKey(AutoCleanupJob.JOB_NAME, AutoCleanupJob.JOB_GROUP));
        } catch (SchedulerException e) {
            log.severe(String.format("Error occurred during unscheduling jobs [%s]", e.getMessage()));
            throw new WorkerException(e);
        }
        if(hasNext()) {
            next().setErrors(errors).uninstall(configuration);
        }
    }
}
