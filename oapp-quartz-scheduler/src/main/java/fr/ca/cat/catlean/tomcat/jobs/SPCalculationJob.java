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
import fr.ca.cat.catlean.tomcat.api.model.DbJoblet;
import fr.ca.cat.catlean.tomcat.core.annotation.Schedulable;
import fr.ca.cat.catlean.tomcat.dao.JobDao;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author lefebvreme
 * @version 0.0.1
 * @since 28-02-2016
 */
@Schedulable(frequency = 1,
        timeUnit = TimeUnit.MINUTES,
        jobName = SPCalculationJob.JOB_NAME,
        jobGroup = SPCalculationJob.JOB_GROUP,
        triggerName = SPCalculationJob.DEFAULT_TRIGGER_NAME,
        triggerGroup = SPCalculationJob.DEFAULT_TRIGGER_GROUP)
public class SPCalculationJob implements QuartzJobAdapter {

    private static final Logger log = Logger.getLogger(SPCalculationJob.class.getName());

    public static final String JOB_NAME = "sPCalculation";
    public static final String JOB_GROUP = "calculation";

    public static final String DEFAULT_TRIGGER_NAME = "sPCalculationTrigger";
    public static final String DEFAULT_TRIGGER_GROUP = "calculation";

    @Autowired
    private JobDao jobDao;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Executing a calcutation");
        final JobDataMap jobDataMap = context.getMergedJobDataMap();
        final DbJoblet joblet = new DbJoblet();
        joblet.setJobId(jobDataMap.getInt("jobId"));
        joblet.setServerName(jobDataMap.getString("serverName"));
        joblet.setSlotId(jobDataMap.getInt("slotId"));
        joblet.setTsStart(jobDataMap.getLong("tsStart"));
        joblet.setTsEnd(jobDataMap.getLong("tsEnd"));
        joblet.setUuid((UUID)(jobDataMap.get("uuid")));
        try {
            log.fine(String.format("Sending joblet to the database [%s]", joblet.describe()));
            jobDao.callProcedure(joblet);
            log.fine(String.format("Joblet execution complete [%s]", joblet.describe()));
        } catch (SQLException e) {
            log.severe(String.format("An error occurred during processing with message [%s]", e.getMessage()));
            throw new JobExecutionException(e);
        }
    }
}
