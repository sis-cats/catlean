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
package fr.ca.cat.catlean.tomcat.api.model;

import org.quartz.Job;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import java.util.List;

/**
 *
 * @author lefebvreme
 * @since 28-02-2016
 * @version 0.0.1
 */
public interface JobSearchResult {

    List<Job> listJob();
    List<JobKey> listJobKeys();
    List<Job> listJobByGroup(String groupName);
    List<JobKey> listJobKeysByGroup(String groupName);
    List<Trigger> listTrigger();
    List<TriggerKey> listTriggerKeys();
    List<Trigger> listTriggerByGroup(String groupName);
    List<TriggerKey> listTriggerKeysByGroup(String groupName);

    default boolean hasJobKeys() {
        return listJob().size() > 0;
    }

    default boolean hasJobKeysForGroup(final String groupName) {
        return listJobByGroup(groupName).size() > 0;
    }

    default boolean hasTriggerKeys() {
        return listTrigger().size() > 0;
    }

    default boolean hasTriggerKeysForGroup(final String groupName) {
        return listTriggerByGroup(groupName).size() > 0;
    }
}
