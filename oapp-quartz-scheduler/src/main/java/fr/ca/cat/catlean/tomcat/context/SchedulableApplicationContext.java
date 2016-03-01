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

import fr.ca.cat.catlean.tomcat.api.workers.WorkerException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import javax.servlet.ServletContext;

/**
 *
 * @author lefebvreme
 * @since 29-02-2016
 * @version 0.0.1
 */
public interface SchedulableApplicationContext {

    /**
     * Fetch the application scheduler.
     *
     * @return the application global scheduler.
     */
    Scheduler getScheduler();

    /**
     * Boot the application context
     *
     * @return
     */
    SchedulableApplicationContext boot() throws WorkerException;

    /**
     * Shutdown the application context
     *
     * @return
     */
    int shutdown();

    /**
     * Check if the application context is started.
     *
     * @return	true if the application context is started
     * 			false if the application context isn't started
     */
    boolean isStarted();

    /**
     * Check if the context is shutdown.
     *
     * @return
     */
    boolean isShutdown();

    /**
     * Check if the context is in standby mode
     *
     * @return
     */
    boolean isInStandbyMode();

    /**
     * Define the servlet context of the application.
     *
     * @param sc
     */
    void setServletContext(ServletContext sc);

    /**
     * Fetch the servlet context actually define as current context in the application.
     *
     * @return
     */
    ServletContext getServletContext();

    /**
     * Place the scheduler in standby mode
     *
     * @param inStandBy		indicates the mode
     * 						true indicates that the standby mode will be activated
     * 						false indicates that the standby mode will be deactivated
     * @return	the instance itself
     * @throws 	SchedulerException	throw an exception if something goes wrong on the scheduler layer
     */
    SchedulableApplicationContext setStandByMode(boolean inStandBy) throws SchedulerException;

    /**
     *
     * Setup the application context.
     *
     * @return              the status of the starting
     * @throws Exception    may leaved if something goes wrong
     */
    int setupContext() throws Exception;

    /**
     * Tear down the context.
     * @return
     */
    SchedulableApplicationContext tearDownContext(boolean forceStart) throws ContextException;

    /**
     * Tear down the context.
     * @return
     */
    default SchedulableApplicationContext tearDownContext() throws ContextException {
        return tearDownContext(false);
    }
}
