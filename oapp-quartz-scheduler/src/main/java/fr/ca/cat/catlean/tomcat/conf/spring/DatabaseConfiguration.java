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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 *
 * @author lefebvreme
 * @since 28-02-2016
 * @version 0.0.1
 */
@Configuration
@EnableAutoConfiguration
@PropertySource("classpath:/com/myco/app.properties")
public class DatabaseConfiguration {

    private static Logger log = Logger.getLogger(DatabaseConfiguration.class.getName());

    @Autowired
    private Environment environment;

    @Bean
    public Connection mysqlConnection() throws SQLException, ClassNotFoundException {
        // environment.getProperty("testbean.name")
        String connectionString = String.format("jdbc:mysql://%s:%s/%s?user=%s&password=%s",
                environment.getProperty("db.hostname"),
                environment.getProperty("db.port"));
        return DriverManager.getConnection(connectionString);
    }

    @Bean(name = "singleDatasource")
    public DataSource dataSource() throws SQLException {
        log.info("Creating the datasource bean");
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("ds.driver"));
        dataSource.setUrl(environment.getProperty("db.url"));
        dataSource.setUsername(environment.getProperty("db.username"));
        dataSource.setPassword(environment.getProperty("db.password"));
        return dataSource;
    }

    @Bean(name = "singleDsConnection")
    public Connection connection() throws SQLException {
        log.info("Getting connection for single datasource");
        final String username = environment.getProperty("db.username");
        final String password = environment.getProperty("db.password");
        return dataSource().getConnection(username, password);
    }

    @Bean
    public InitialContext initialContext() throws NamingException {
        return new InitialContext();
    }

    @Bean(name = "poolableDatasource")
    public DataSource poolableDataSource() throws SQLException, NamingException {
        InitialContext ctx = initialContext();
        return  (DataSource) ctx.lookup(environment.getProperty("db.dsName"));
    }
}
