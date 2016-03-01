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
package fr.ca.cat.catlean.tomcat.conf.spring;

import fr.ca.cat.catlean.tomcat.workers.scheduling.RetryPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 *
 * @author lefebvreme
 * @since 29-02-2016
 * @version 0.0.1
 */
@Configuration
@PropertySource("classpath:policies/retryPolycies.properties")
public class PolicyConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    public RetryPolicy retryPolicy() {
        RetryPolicy policy = new RetryPolicy();
        policy.setNbRetry(Integer.parseInt(environment.getProperty("policy.nbRetry")));
        return policy;
    }
}
