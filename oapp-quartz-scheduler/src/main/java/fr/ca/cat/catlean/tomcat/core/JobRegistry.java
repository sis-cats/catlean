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
package fr.ca.cat.catlean.tomcat.core;

import fr.ca.cat.catlean.tomcat.api.adapters.scheduler.JobAdapter;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 *
 * @author lefebvreme
 * @since 29-02-2016
 * @version 0.0.1
 */
public interface JobRegistry<T extends JobAdapter> {

    void addJob(T job);

    void removeJob(T job);

    boolean containsJob(T job);

    List<T> getJobs();

    default Stream<T> stream() {
        return getJobs().stream();
    }
    /**
     *
     * @param action    action to apply to all jobs
     */
    default void foreach(Consumer<T> action) {
        Objects.requireNonNull(action);
        for (final T job : getJobs()) {
            action.accept(job);
        }
    }
}
