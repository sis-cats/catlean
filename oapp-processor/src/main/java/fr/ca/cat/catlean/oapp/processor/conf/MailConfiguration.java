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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 *
 * @author lefebvreme
 * @since 26-02-2016
 * @version 0.0.1
 */
@Configuration
@PropertySource("classpath:fr/ca/cat/catlean/oapp/processor/mail.properties")
public class MailConfiguration {

    @Autowired
    private Environment environment;

    /**
     * The Java Mail sender.
     * It's not generally expected for mail sending to work in embedded mode.
     * Since this mail sender is always invoked asynchronously, this won't cause problems for the developer.
     */
    @Bean
    public JavaMailSender mailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setDefaultEncoding(environment.getProperty("mail.encoding.default"));
        mailSender.setHost(environment.getProperty("mail.smtp.host"));
        mailSender.setPort(Integer.valueOf(environment.getProperty("mail.port")));
        mailSender.setProtocol(environment.getProperty("mail.protocol"));
        mailSender.setUsername(environment.getProperty("mail.username"));
        mailSender.setPassword(environment.getProperty("mail.password"));

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", environment.getProperty("mail.smtp.auth", Boolean.class, true));
        mailSender.setJavaMailProperties(properties);

        return mailSender;
    }

}
