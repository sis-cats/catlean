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
package fr.ca.cat.catlean.oapp.processor.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 *
 * @author lefebvreme
 * @since 24-02-2016
 * @version 0.0.1
 */
@Configuration
@PropertySource("classpath:fr/ca/cat/catlean/oapp/processor/datasource.properties")
public class DatabaseConfiguration {

    private static Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    @Autowired
    private Environment environment;

    @Bean
    public DataSource dataSource() throws SQLException {
        log.info("Creating the datasource bean");
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("ds.driver"));
        dataSource.setUrl(environment.getProperty("ds.url"));
        dataSource.setUsername(environment.getProperty("ds.username"));
        dataSource.setPassword(environment.getProperty("ds.password"));
        return dataSource;
    }
}
