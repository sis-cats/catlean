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

import fr.ca.cat.catlean.tomcat.api.adapters.scheduler.AppJobListener;
import fr.ca.cat.catlean.tomcat.api.adapters.scheduler.AppSchedulerListenerSupport;
import fr.ca.cat.catlean.tomcat.api.adapters.scheduler.AppTriggerListener;
import fr.ca.cat.catlean.tomcat.api.core.SearchEngine;
import fr.ca.cat.catlean.tomcat.conf.SchedulerConfiguration;
import fr.ca.cat.catlean.tomcat.core.DefaultSearchEngine;
import fr.ca.cat.catlean.tomcat.listeners.scheduling.*;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.logging.Logger;

import static org.quartz.impl.matchers.EverythingMatcher.allJobs;
import static org.quartz.impl.matchers.EverythingMatcher.allTriggers;
import static org.quartz.impl.matchers.GroupMatcher.jobGroupEquals;
import static org.quartz.impl.matchers.GroupMatcher.triggerGroupEquals;

/**
 *
 * @author lefebvreme
 * @since 29-02-2016
 * @version 0.0.1
 */
@Configuration
public class SpringAwareSchedulerConfiguration {

    /**
     * TODO : check for a more powerfull logger implementation
     */
    private static Logger log = Logger.getLogger(SchedulerConfiguration.class.getName());

    private SchedulerFactory factory;
    private Scheduler scheduler;

    //private Scheduler

    @Bean
    public SchedulerFactory factory() {
        if(null == factory) {
            factory = new StdSchedulerFactory();
        }
        return factory;
    }

    @Bean
    public Scheduler scheduler() {
        if(null == scheduler) {
            try {
                scheduler = factory().getScheduler();
            } catch (Exception e) {
                log.severe("Unable to create the scheduler");
            }
        }
        return scheduler;
    }

    @Bean
    public AppSchedulerListenerSupport appSchedulerListenerSupport() {
        return new DefaultSchedulerListenerSupport();
    }

    @Bean(name = "allJobListener")
    public AppJobListener allJobListener() {
        return new AllJobListener("allJobListener");
    }

    @Bean(name = "maintenanceJobListener")
    public AppJobListener maintenanceJobListener() {
        return new MaintenanceJobListener("maintenanceJobListener");
    }

    @Bean(name = "reportingJobListener")
    public AppJobListener reportingJobListener() {
        return new ReportingJobListener("reportingJobListener");
    }

    @Bean(name = "allTriggerListener")
    public AppTriggerListener allTriggerListener() {
        return new AllTriggerListener("allTriggerListener");
    }

    @Bean(name = "maintenancelTriggerListener")
    public AppTriggerListener maintenanceTriggerListener() {
        return new MaintenanceTriggerListener("maintenancelTriggerListener");
    }

    @Bean(name = "reportingTriggerListener")
    public AppTriggerListener reportingTriggerListener() {
        return new ReportingTriggerListener("reportingTriggerListener");
    }

    @Bean(name = "globalSchedulerListener")
    public GLobalSchedulerListener gLobalSchedulerListener() {
        return new GLobalSchedulerListener();
    }

    @Bean(name = "quartzSearchEngine")
    public SearchEngine quartzSearchEngine() {
        return new DefaultSearchEngine();
    }

    @Bean
    public SchedulerConfiguration schedulerConfiguration() {
        SchedulerConfiguration configuration = new DefaultSchedulerConfiguration();
        List<String> items = configuration.getGroups();
        items.add("processing");          // groups dedicated to processing jobs
        items.add("calculation");         // groups dedicated to calculation jobs
        items.add("maintenance");         // groups dedicated to maintenance jobs
        items.add("reporting");           // group dedicated to reporting jobs
        items = configuration.getJobKeys();
        // TODO : list all indicators
        items.add("allBusinessIndicators");     // default calcultation job
        items.add("maintenanceJobs");           // default maintainance prcessing jobs
        items.add("teportingJobs");             // default reporting jobs
        items.add("calculationProcessingJob");  // calculation processing jobs
        items = configuration.getTriggerKeys();
        items.add("daily");     // trigger once per day
        items.add("hourly");    // ttigger once per our
        items.add("minutely");  // triggeronce per minutes
        items.add("sloted");    // trigger once each slot duration (parameterized) time
        items.add("special");   // special trigger
        items.add("halfday");   // trigger once per half day
        items.add("morning");   // trigger once each morning
        items.add("evening");   // tihhrt onvr each evening
        return configuration;
    }

    @PostConstruct
    public void afterConstruct() {
        if(null == scheduler()) {
            log.severe("Unable to process the post construct correctly. The system's behavior isn't garanted");
            return;
        }
        // Registering all Listener
        try {
            scheduler.getListenerManager().addSchedulerListener(gLobalSchedulerListener());
            scheduler.getListenerManager().addSchedulerListener(appSchedulerListenerSupport());
            scheduler.getListenerManager().addJobListener(allJobListener(), allJobs ());
            scheduler.getListenerManager().addJobListener(maintenanceJobListener(), jobGroupEquals(AppJobListener.MAINENANCE_JOBS));
            scheduler.getListenerManager().addJobListener(reportingJobListener(), jobGroupEquals(AppJobListener.REPORTING_JOBS));
            scheduler.getListenerManager().addTriggerListener(allTriggerListener(), allTriggers());
            scheduler.getListenerManager().addTriggerListener(maintenanceTriggerListener(), triggerGroupEquals(AppTriggerListener.MAINENANCE_TRIGGERS));
            scheduler.getListenerManager().addTriggerListener(reportingTriggerListener(), triggerGroupEquals(AppTriggerListener.REPORTING_TRIGGERS));
        } catch (SchedulerException e) {
            log.severe(String.format("Unable to register scheduler listener "));
        }
    }
}
