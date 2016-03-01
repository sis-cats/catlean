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
package fr.ca.cat.catlean.tomcat.context;

import javax.servlet.ServletContext;

import fr.ca.cat.catlean.tomcat.api.adapters.scheduler.SchedulerAdapter;
import fr.ca.cat.catlean.tomcat.api.workers.WorkerException;
import fr.ca.cat.catlean.tomcat.conf.SchedulerConfiguration;
import fr.ca.cat.catlean.tomcat.workers.scheduling.installers.DeploymentJobInstaller;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * @author lefebvreme
 * @version 0.0.1
 * @since 29-02-2016
 */
public class DefaultSchedulableApplicationContext implements SchedulableApplicationContext {

    private static Logger log = Logger.getLogger(DefaultSchedulableApplicationContext.class.getName());

    public static final int SUCCESS = 0;
    public static final int GENERIC_ERROR = 1;
    public static final int NOT_STARTED_ERROR = 2;
    public static final int ALREADY_STARTED_ERROR = 3;

    @Autowired
    private Scheduler scheduler;

    private ServletContext servletContext;

    @Autowired
    private SchedulerAdapter adapter;

    @Autowired
    private DeploymentJobInstaller deploymentJobInstaller;

    @Autowired
    private SchedulerConfiguration schedulerConfiguration;

    public DefaultSchedulableApplicationContext() {
        log.info("Creating a new instance of the application context");
    }

    /**
     * Instance of the application context
     */
    public DefaultSchedulableApplicationContext(Scheduler scheduler) {
        this();
        this.scheduler = scheduler;
    }

    @Override
    public Scheduler getScheduler() {
        return scheduler;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public SchedulableApplicationContext boot() throws WorkerException {
        log.info("Booting the application context");
        Objects.requireNonNull(scheduler);
        Objects.requireNonNull(servletContext);
        Objects.requireNonNull(adapter);
        Objects.requireNonNull(deploymentJobInstaller);
        Objects.requireNonNull(schedulerConfiguration);
        if (isStarted()) {
            log.warning("The context is already up");
        } else {
            try {
                scheduler.start();
                log.info("Application context successfully started");
            } catch (Exception e) {
                log.severe(String.format("Unable to start the context due to error : [%s]", e.getMessage()));
                throw new WorkerException(e);
            }
        }
        return this;
    }

    @Override
    public int shutdown() {
        log.info("Shuting down the context");
        int result = 0;
        if (!isStarted()) {
            result = NOT_STARTED_ERROR;
        } else {
            try {
                scheduler.shutdown(true);
                log.info("The context has been successfully shutdown");
                result = SUCCESS;
            } catch (Exception e) {
                log.severe(String.format("An error occurrre when the context trying to shutdown with message [%s]", e.getMessage()));
                result = GENERIC_ERROR;
            }
        }
        return result;
    }

    @Override
    public boolean isStarted() {
        log.info("Check if the context is started");
        try {
            return scheduler.isStarted();
        } catch (SchedulerException e) {
            log.severe(String.format("Unable to stop the context due to the following error [{}]", e.getMessage()));
            return false;
        }
    }

    @Override
    public boolean isShutdown() {
        try {
            return scheduler.isShutdown();
        } catch (SchedulerException e) {
            log.severe(String.format("Error occurred when checking about eh shutdown state of the application context with message [%s]", e.getMessage()));
            return false;
        }
    }

    @Override
    public boolean isInStandbyMode() {
        try {
            return scheduler.isInStandbyMode();
        } catch (SchedulerException e) {
            log.severe(String.format("Error occurred when checking about eh standby state of the application context with message [%s]", e.getMessage()));
            return false;
        }
    }

    @Override
    public DefaultSchedulableApplicationContext setStandByMode(boolean inStandBy) throws SchedulerException {
        // TODO : should we check the state of the scheduler ?
        // TODO : should we realy leave an exception ?
        if (inStandBy) {
            log.info("Tryingo to put the scheduler in standby mode");
            scheduler.standby();
        } else {
            log.info("Tryingo to restart the scheduler");
            scheduler.start();
        }
        return this;
    }

    /**
     * When an application is redeploy, we have to drop out all stored since some parts of the
     * Scheduling process are serialized java classes and this, if something change in this class, it may
     * produces bugs.
     *
     * @return
     * @throws Exception
     */
    @Override
    public int setupContext() throws Exception {
        int result = WorkerException.EXIT_FAILURE;
        if (adapter.isProcessingAvailable()) {
            log.info("Processing can start");
            try {
                deploymentJobInstaller.install(schedulerConfiguration, true);
            } catch (WorkerException e) {
                log.severe(String.format("Unable to reset scheduling configuration due to error [%s]", e.getMessage()));
                result = WorkerException.EXIT_SUCCESS;
            }
        } else {
            log.severe("Scheduler isn't initialized, skipping process");
        }
        return result;
    }

    @Override
    public SchedulableApplicationContext tearDownContext(boolean forceStart) throws ContextException {
        boolean haveToShutdown = false;
        if (!adapter.isProcessingAvailable()) {
            throw new ContextException("Context isn't ready");
        }
        if (!isStarted() && !forceStart) {
            log.severe("The context isn't started and shouldn't be started");
        } else {
            log.info("executing the tearDown");
            if(!isStarted()) {
                haveToShutdown = true;
                log.fine("Context isn't started, we will start right now");
                try {
                    boot();
                } catch (WorkerException e) {
                    log.severe(String.format("An error occurred during tearDown process (starting attempt) with message [%s]", e.getMessage()));
                    throw new ContextException(e);
                }
            }

            if(haveToShutdown) {
                shutdown();
            }
        }
        return this;
    }
}
