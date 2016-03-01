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
package fr.ca.cat.catlean.tomcat.api.adapters.scheduler;

import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 *
 * @author lefebvreme
 * @since 28-02-2016
 * @version 0.0.1
 */
public interface SchedulerAdapter {

    /**
     *
     * @return
     * @throws SchedulerException
     * @throws Exception
     */
    List<JobKey> getJobKeys() throws Exception;

    /**
     *
     * @return
     * @throws Exception
     */
    List<TriggerKey> getTriggerKeys() throws Exception;



    /**
     * Check if the processing is possible and if the processor is ready.
     *
     * @return  {@code true} if the processing is available
     *          {@code false} if tne processing isn't available
     */
    boolean isProcessingAvailable();

    default void foreachJobs(Consumer<JobKey> action) throws Exception {
        Objects.requireNonNull(action, "An action is required to process");
        getJobKeys().forEach(action::accept);
    }
}
