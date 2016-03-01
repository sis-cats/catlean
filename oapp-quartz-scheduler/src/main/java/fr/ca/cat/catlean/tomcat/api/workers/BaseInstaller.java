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
package fr.ca.cat.catlean.tomcat.api.workers;

import fr.ca.cat.catlean.tomcat.api.core.SearchEngine;
import fr.ca.cat.catlean.tomcat.api.model.JobSearchResult;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author lefebvreme
 * @version 0.0.1
 * @since 28-02-2016
 */
public abstract class BaseInstaller<T> implements Installer<T> {

    private static Logger log = Logger.getLogger(BaseInstaller.class.getName());

    @Autowired(required = false)
    protected Scheduler scheduler;

    @Autowired()
    @Qualifier("quartzSearchEngine")
    protected SearchEngine searchEngine;

    protected JobSearchResult searchResult;

    /**
     * list of errors occurred during a process
     */
    protected final List<Exception> errors = new ArrayList<>();

    protected Installer<T> next;

    @PostConstruct
    public void setUp() {

    }

    @PreDestroy
    public void tearDown() {

    }

    @Override
    public Installer<T> reset() {
        log.fine("resetting search results");
        searchResult = null;
        return this;
    }

    @Override
    protected void finalize() throws Throwable {
        reset();
        super.finalize();
    }

    @Override
    public Installer<T> resetErrors() {
        errors.clear();
        return this;
    }

    public Installer<T> setErrors(List<Exception> errors) {
        this.errors.addAll(errors);
        return this;
    }

    @Override
    public List<Exception> getLasatErrors() {
        return errors;
    }

    public Installer<T> setNext(Installer<T> installer) {
        this.next = installer;
        return this;
    }

    public boolean hasNext() {
        return next != null;
    }

    public Installer<T> next() {
        return next;
    }
}
