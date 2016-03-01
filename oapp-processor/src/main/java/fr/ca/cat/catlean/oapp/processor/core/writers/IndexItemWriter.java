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
package fr.ca.cat.catlean.oapp.processor.core.writers;

import fr.ca.cat.catlean.oapp.processor.core.indexer.IndexProcessingException;
import fr.ca.cat.catlean.oapp.processor.core.indexer.IndexableLogEvent;
import fr.ca.cat.catlean.oapp.processor.core.indexer.Indexer;
import fr.ca.cat.catlean.oapp.processor.domain.LogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 *
 * @author lefebvreme
 * @since 26-02-2016
 * @version 0.0.1
 */
public class IndexItemWriter implements ItemWriter<IndexableLogEvent> {

    private static Logger log = LoggerFactory.getLogger(IndexItemWriter.class);

    @Autowired
    private Indexer indexer;

    /**
     * Walk through the list and index each item.
     *
     * @param list          the list of item to index
     * @throws Exception    leave an exception if one or more item has been failed to index
     */
    @Override
    public void write(List<? extends IndexableLogEvent> list) throws Exception {
        log.info("Starting batch indexation process");
        final LogEvent firstItem = null;
        final IndexProcessingException lastError[] = {null};
        list.forEach((item) -> {
            try {
                indexer.indexElement(item);
            } catch(IndexProcessingException ipe) {
                log.error("Indexing process failed, send to retry");
                lastError[0] = ipe;
            }
        });
        if(null != lastError[0]) {
            markRetry(lastError[0]);
        }
    }

    /**
     * Mark an {@code item} to retry in the batching process
     * <p>Depending of the implementation / infrastructure, marking an element as
     * retry may differ. With Spring Batch, we can use a standalone Job processor
     * or indirect the process to third party job processor like Quartz.
     * This implementation delegate the choice to the framework as it is specified
     * in the configuration (retry x time). The only thing we wanna do here is leaving
     * an exception to let the framework mark the item for us.</p>
     *
     */
    private void markRetry(Throwable e) throws Exception {
        throw new Exception(e.getMessage(), e);
    }
}