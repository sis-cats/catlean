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
package fr.ca.cat.catlean.tomcat.core.annotation;

import fr.ca.cat.catlean.tomcat.core.BehaviorStrategy;
import fr.ca.cat.catlean.tomcat.core.JobType;
import fr.ca.cat.catlean.tomcat.core.SchedulingStrategy;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author lefebvreme
 * @version 0.0.1
 * @since 29-02-2016
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Schedulable {

    /**
     * Define the frequency of the job running
     *
     * @return the frequency in the specified {@link TimeUnit}
     */
    long frequency() default 60 * 1000;

    /**
     * Timeunit of the frequency. Default milliseconds
     *
     * @return the frequency unit
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * GEt the jobName
     *
     * @return the job name
     */
    String jobName() default "";

    /**
     * get The job's group
     *
     * @return  the job group
     */
    String jobGroup() default "";

    /**
     * THe running strategy
     *
     * @return  the strategy
     */
    SchedulingStrategy strategy() default SchedulingStrategy.ONCE;

    /**
     * Return the name of the trigger to run with this job.
     *
     * @return  the trigger name
     */
    String triggerName() default "";

    /**
     * Return the trigger group
     *
     * @return the trigger group
     */
    String triggerGroup() default "";

    /**
     * Return the type of the job.
     * Maintenance job and reporting job act at the framework level, processing type
     * act at the application level
     *
     * @return the type of the job
     */
    JobType jobType() default JobType.PROCESSING;

    /**
     * Define the processing strategy.
     *
     * @return the processing strategy
     */
    BehaviorStrategy behaviorStrategy() default BehaviorStrategy.AUTO_REPROCESS;
}
