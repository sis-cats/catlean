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
package fr.ca.cat.catlean.tomcat.adapters.scheduler;


import fr.ca.cat.catlean.tomcat.api.adapters.scheduler.SpringBatchJobAdapter;
import org.springframework.batch.core.*;

/**
 * Unused for now.
 *
 * @author lefebvreme
 * @version 0.0.1
 * @since 28-02-2016
 */
public class SpringJobAdapter implements SpringBatchJobAdapter {

    class DefaultIncrementer implements JobParametersIncrementer {

        @Override
        public JobParameters getNext(JobParameters parameters) {
            return parameters;
        }
    }

    class DefaultJobParametersValidator implements JobParametersValidator {

        @Override
        public void validate(JobParameters parameters) throws JobParametersInvalidException {
            // nothing to do
        }
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isRestartable() {
        return false;
    }

    @Override
    public void execute(JobExecution jobExecution) {

    }

    private JobParametersIncrementer incrementer = new DefaultIncrementer();
    private JobParametersValidator validator = new DefaultJobParametersValidator();

    @Override
    public JobParametersIncrementer getJobParametersIncrementer() {
        return incrementer;
    }

    @Override
    public JobParametersValidator getJobParametersValidator() {
        return validator;
    }
}
