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
package fr.ca.cat.catlean.tomcat.service.processing;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.listeners.TriggerListenerSupport;

import java.util.Date;

import static org.quartz.DateBuilder.evenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @author lefebvreme
 * @version 0.0.1
 * @since 26-02-2016
 */
public class SimpleQuarzJobProcessing {

    private static Logger log = LogManager.getLogger(SimpleQuarzJobProcessing.class.getName());

    /**
     * Internal job processor
     */
    public static class SimpleJob implements Job {

        private Logger log = Logger.getLogger(SimpleJob.class.getName());

        public SimpleJob() {
            super();
            log.info("Creating the job");
        }

        public void execute(JobExecutionContext context) {
            log.info("Starting job processing");
        }
    }

    public static class SimpleTriggerListener extends TriggerListenerSupport {

        private static Logger log = LogManager.getLogger(SimpleTriggerListener.class.getName());

        private int cpt = 0;

        @Override
        public String getName() {
            return getClass().getName();
        }

        @Override
        public void triggerFired(Trigger trigger, JobExecutionContext context) {
            log.info(String.format("Fire a trigger [%s] for job [%s] jobDataMap [%s] with context [%s]",
                    trigger.getKey().getName(),
                    trigger.getJobKey().getName(),
                    context.getMergedJobDataMap(),
                    context.getFireInstanceId()));
        }

        @Override
        public void triggerComplete(Trigger trigger, JobExecutionContext context,
                                    Trigger.CompletedExecutionInstruction triggerInstructionCode) {
            log.info(String.format("Complete a trigger [%s] for job [%s] trigger mode [%s] with context [%s]",
                    trigger.getKey().getName(),
                    trigger.getJobKey().getName(),
                    triggerInstructionCode.name(),
                    context.getFireInstanceId()));
        }

        @Override
        public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
            log.info("Vetoed an execution");
            int i = (++cpt) % 2;
            log.debug(String.format("Vetoed : %d", i));
            return i == 0;
        }

        @Override
        public void triggerMisfired(Trigger trigger) {
            log.info("Misfire a trigger");
        }
    }

    public static void main(String... argv) throws SchedulerException, InterruptedException {
        log.info("Starting job processing");
        log.info("Creating factory instance");
        final SchedulerFactory factory = new StdSchedulerFactory();
        final SimpleTriggerListener listener = new SimpleTriggerListener();
        log.info("Fetching the scheduler");
        Scheduler scheduler = factory.getScheduler();
        log.info("Adding a listener trigger");
        scheduler.getListenerManager().addTriggerListener(listener);
        log.info("Creating a job");
        JobDetail job = newJob(SimpleJob.class)
                .withIdentity("job", "group1")
                .build();
        log.info("Creating a trigger");
        Date runDate = evenSecondDate(new Date());
        Trigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .startAt(runDate)
                .build();
        log.info("Scheduling the job");
        scheduler.scheduleJob(job, trigger);
        scheduler.start();
        int round = 10;
        while(true) {
            Thread.sleep(2 * 1000L);
            scheduler.scheduleJob(job, trigger);
            if(--round <= 0) {
                break;
            }
        }

        scheduler.shutdown();
    }
}
