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
package fr.ca.cat.catlean.tomcat.api.core;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 *
 * @author lefebvreme
 * @since 28-02-2016
 * @version 0.0.1
 */
public interface SearchQuery {

    List<String> getGroups();
    List<String> getJobKeys();
    List<String> getTriggerKeys();

    default boolean containsGroup(final String groupName) {
        return getGroups().contains(groupName);
    }

    default boolean containsJobKey(final String jobKey) {
        return getJobKeys().contains(jobKey);
    }

    default boolean containsTriggerKey(final String triggerKey) {
        return getTriggerKeys().contains(triggerKey);
    }

    default SearchQuery addGroup(String groupName) {
        if(!containsGroup(groupName)) {
            getGroups().add(groupName);
        }
        return this;
    }

    default SearchQuery addJobKey(String jobKeyName) {
        if (!containsJobKey(jobKeyName)) {
            getJobKeys().add(jobKeyName);
        }
        return this;
    }
    default SearchQuery addTriggerKey(String triggerKey) {
        if (!containsTriggerKey(triggerKey)) {
            getTriggerKeys().add(triggerKey);
        }
        return this;
    }

    default void foreachGroup(Consumer<String> action) {
        Objects.requireNonNull(action, "Action should be defied");
        getGroups().forEach(action::accept);
    }

    default void foreachJobName(Consumer<String> action) {
        Objects.requireNonNull(action, "Action should be defied");
        getJobKeys().forEach(action::accept);
    }

    default void foreachTrigger(Consumer<String> action) {
        Objects.requireNonNull(action, "Action should be defied");
        getTriggerKeys().forEach(action::accept);
    }
}
