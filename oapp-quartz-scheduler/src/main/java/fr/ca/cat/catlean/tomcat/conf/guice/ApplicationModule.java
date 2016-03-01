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
package fr.ca.cat.catlean.tomcat.conf.guice;

import com.google.inject.Binder;
import com.google.inject.Module;
import fr.ca.cat.catlean.tomcat.api.conf.ContextConfiguration;
import fr.ca.cat.catlean.tomcat.conf.LocalContextConfiguration;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author lefebvreme
 * @since 28-02-2016
 * @version 0.0.1
 */
public class ApplicationModule implements Module {

    private SchedulerFactory factory = new StdSchedulerFactory();

    @Override
    public void configure(Binder binder) {
        binder.bind(ContextConfiguration.class).to(LocalContextConfiguration.class);
        binder.bind(SchedulerFactory.class).toInstance(factory);
    }
}
