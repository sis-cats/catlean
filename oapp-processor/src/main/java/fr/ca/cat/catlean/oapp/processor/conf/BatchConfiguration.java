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

import fr.ca.cat.catlean.oapp.processor.core.JobCompletionNotificationListener;
import fr.ca.cat.catlean.oapp.processor.core.OappConsolidationItemProcessor;
import fr.ca.cat.catlean.oapp.processor.core.hash.Hash;
import fr.ca.cat.catlean.oapp.processor.core.hash.HashFactory;
import fr.ca.cat.catlean.oapp.processor.core.hash.NoSuchTypeException;
import fr.ca.cat.catlean.oapp.processor.core.hash.NullHash;
import fr.ca.cat.catlean.oapp.processor.domain.OappConsolidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 *
 * @author lefebvreme
 * @since 24-02-2016
 * @version 0.0.1
 */
@Configuration
@EnableBatchProcessing
@Import(DatabaseConfiguration.class)
public class BatchConfiguration {

    private static Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;


    @Bean
    public JobExecutionListener listener() {
        return new JobCompletionNotificationListener(new JdbcTemplate(dataSource));
    }

    @Bean
    public OappConsolidationItemProcessor processor() {
        return new OappConsolidationItemProcessor();
    }

    @Bean
    public FlatFileItemReader<OappConsolidation> reader() {
        logger.debug("Creating the reader");
        FlatFileItemReader<OappConsolidation> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("fr/ca/cat/catlean/oapp/processor/samples/sample-data.csv"));
        logger.trace("Ressource has been set");
        reader.setLineMapper(new DefaultLineMapper<OappConsolidation>(){{
            setLineTokenizer(new DelimitedLineTokenizer(){{
                setNames(new String[] {"serverName", "tsStart", "tsEnd", "slotId", "jobId"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<OappConsolidation>(){{
                setTargetType(OappConsolidation.class);
            }});
        }});
        logger.debug("Reader creation completed");
        return reader;
    }

    @Bean
    public JdbcBatchItemWriter<OappConsolidation> writer() {
        logger.debug("Creating the writer bean");
        JdbcBatchItemWriter<OappConsolidation> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO consolidation (server_name, ts_start, ts_end, slot_id, job_id) " +
                "VALUES(:serverName, :tsStart, :tsEnd, :slotId, :jobId)");
        writer.setDataSource(dataSource);
        logger.debug("Writer bean creation completed");
        return writer;
    }

    @Bean
    public Job importConsolidation() {
        return jobBuilderFactory.get("importConsolidation")
                //.incrementer(new RunIdIncrementer())
                .listener(listener())
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step  step1() {
        return stepBuilderFactory.get("step1")
                .<OappConsolidation, OappConsolidation>chunk(1)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Hash hash() {
        try {
            return HashFactory.createHash(HashFactory.HashType.STANDARD);
        } catch (NoSuchTypeException e) {
            return new NullHash();
        }
    }
}
