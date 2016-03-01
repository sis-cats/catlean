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

import fr.ca.cat.catlean.tomcat.api.core.SearchEngine;
import fr.ca.cat.catlean.tomcat.api.core.SearchException;
import fr.ca.cat.catlean.tomcat.api.core.SearchQuery;
import fr.ca.cat.catlean.tomcat.api.model.DefaultMutableSearchResult;
import fr.ca.cat.catlean.tomcat.api.model.JobSearchResult;
import fr.ca.cat.catlean.tomcat.api.model.MutableJobSearchResult;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Logger;

import static org.quartz.impl.matchers.GroupMatcher.groupEquals;

/**
 *
 * @author lefebvreme
 * @since 28-02-2016
 * @version 0.0.1
 */
public class DefaultSearchEngine implements SearchEngine {

    private static Logger log = Logger.getLogger(DefaultSearchEngine.class.getName());

    @Autowired
    private Scheduler scheduler;

    @Override
    public JobSearchResult search(SearchQuery searchQuery) throws SearchException {
        final MutableJobSearchResult searchResult = new DefaultMutableSearchResult();
        searchQuery.foreachGroup(groupName -> {
            searchQuery.foreachJobName(jobName -> {
                try {
                    for(final JobKey jobKey: scheduler.getJobKeys(groupEquals(groupName))) {
                        if(searchQuery.containsJobKey(jobKey.getName())) {
                            searchResult.addJobKeyForGroup(groupName, jobKey);
                            final JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                            searchResult.addJobForGroup(groupName, jobDetail);
                        }
                    }
                } catch (SchedulerException e) {
                    log.severe(String.format("An error occurred during jobKey listing with message [%s]", e.getMessage()));
                }
                try {
                    for (final TriggerKey triggerKey : scheduler.getTriggerKeys(groupEquals(groupName))) {
                        if(searchQuery.containsTriggerKey(triggerKey.getName())) {
                            searchResult.addTriggerKeyForGroup(groupName, triggerKey);
                            final Trigger trigger = scheduler.getTrigger(triggerKey);
                            searchResult.addTriggerForGroup(groupName, trigger);
                        }
                    }
                } catch (SchedulerException e) {
                    log.severe(String.format("An error occurred during triggerKey listing with message [%s]", e.getMessage()));
                }
            });
        });
        return searchResult;
    }
}
