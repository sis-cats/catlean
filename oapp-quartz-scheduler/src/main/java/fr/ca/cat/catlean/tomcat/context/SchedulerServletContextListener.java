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

import fr.ca.cat.catlean.tomcat.conf.SchedulerConfiguration;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.logging.Logger;

/**
 * <p>This is the entry point of the job scheduler. Since we have to start / stop / clean / restart / ...
 * of the scheduler processing we want to ensure his working's consistency.
 * To do that, we are working on two layer provided by the container.
 * <ul><ol>The first one is the {@code Servlet} container layer (meanings on the server starting / stopping).
 * 		In this case, we have to check the state of the scheduler. It has to be started and the job stack has to
 * 		be clean.
 * 		What does "clean" means ?<br>
 * 		Clean is equivalent that no job should be marked has "pending" or on "error".
 * 		<ul><li>If one or more are in this
 * 				state, the scheduling process implementation will try to repair the fault. See {@see RepairingProcessor}
 * 				for this case.</li>
 * 			<li>If some slots has been forget and if they are still "processable", the worker will try to process these.
 * 			({@see ForgettedProcessor}).</li>
 * 			<li>If some jobs aborted. they can be rescheduled according to our retrying policy ({@see fr.ca.cat.catlean.tomcat.workers.scheduling.RetryPolicy}). Jobs
 * 			that exceed the number of processing attempts will be forget.Database will reflect the state of the job and
 * 			some reporting can be made easily.</li></ul></ol>
 * 	<ol>The second one is the {@code Servlet} lifecycle (meanings web request layer). This layer provide a way to do some action
 * 		at each request. For now, we'll doing some little check but since we are working with the scheduler itself, it's
 * 		not always relevant.
 * 	</ol>
 * </ul>
 * This is the basic implementation of a service.
 * This implementation don't use the quartzInitializerListener. If we can setup this listener in
 * our context, we can retrieve {@code SchedulerFactory} like this:
 * <code>StdSchedulerFactory factory = (StdSchedulerFactory) ctx
 *              .getAttribute(QuartzInitializerListener.QUARTZ_FACTORY_KEY);</code></p>
 *
 * @author lefebvreme
 * @since 26-02-2016
 * @version 0.0.1
 */
@SuppressWarnings("unused")
public class SchedulerServletContextListener implements ServletContextListener {

    private Logger logger = Logger.getLogger(SchedulerServletContextListener.class.getName());

    /**
     * Remind the servlet context to process event out of the listeners' method withou passing it
     * as parameters
     */
    private ServletContext servletContext;

    /**
     * TODO : see if we have to relocate this int the {@link SchedulableApplicationContext}
     */
    @Autowired
    private SchedulerConfiguration schedulerConfiguration;

    /**
     * The main reference to the entry point.
     * This handle the most of part of our implementation. This act as a facade.
     */
    @Autowired
    private SchedulableApplicationContext schedulableApplicationContext;

    /**
     * Constructor of the listener.
     * Construction occurred during tomcat's application startup. The container instanciate
     * the listener. It shouldn't be instanciate  by another way.
     */
    @SuppressWarnings("unused")
    public SchedulerServletContextListener() {
        log("Instanciating a [%s] class", SchedulerServletContextListener.class.getName());
    }

    /**
     * Listener triggered after context initialization.
     *
     * In this phase, we will setup the context. If necessary, some missed job will be rescheduled.
     *
     * @param servletContextEvent   the event triggered.
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContext = servletContextEvent.getServletContext();
        log("Initializing servlet context");
        try {
            /*
             * Simply boot the context if necessary and setup it.
             * If something goes wrong, we should provide a fallback method
             * to create a fault behavior.
             * In normal situation, everything should happens correctly.
             */
            schedulableApplicationContext.boot().setupContext();
        } catch (Exception e) {
            // TODO : make a fallback behavior
            logger.severe(String.format("Unable to cleanup scheduler correctly. Reason [%s]", e.getMessage()));
        }
    }

    /**
     * This listener listen about context destroy.
     *
     * @param servletContextEvent   the servletContextEvent fired by the container
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        servletContext = servletContextEvent.getServletContext();
        log("Destroying the context of the application");
        try {
            schedulableApplicationContext.tearDownContext();
        } catch (Exception e) {
            logger.severe(String.format("Unable to cleanup scheduler correctly. Reason [%s]", e.getMessage()));
        }
    }

    /**
     * Utility method to log information.
     *
     * @param message   the message to log
     * @param params    the optional list of parameters to use to create the message ({@link java.lang.String#format})
     */
    private void log(final String message, Object... params) {
        log(null, message, params);
    }

    /**
     * Utility method to log information
     *
     * @param e         the exception to log
     * @param message   the message to log
     * @param params    the optional list of parameters to use to create the message ({@link java.lang.String#format})
     */
    private void log(Throwable e, final String message, Object... params) {
        final String msg = String.format(message, params);
        if(null != servletContext) {
            if(null != e) {
                servletContext.log(msg, e);
            } else {
                servletContext.log(msg);
            }
        } else {
            logger.info(msg);
            if(null != e) {
                e.printStackTrace(System.err);
            }
        }
    }

    /**
     * Log an error message on the servlet world.
     * This log the message on the default log file and push the full stack trace on the
     * stderr output.
     *
     * @param message   the message to log (will go in log files)
     * @param th        the exception to log to stderr
     */
    private void error(String message, Throwable th) {
        if(null != servletContext) {
            servletContext.log(message);
        } else {
            System.out.println(message);
        }
        th.printStackTrace(System.err);
    }
}
