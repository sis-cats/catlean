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
package fr.ca.cat.catlean.oapp.processor.core.indexer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import fr.ca.cat.catlean.oapp.processor.domain.LogEvent;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

/**
 *
 * @author lefebvreme
 * @since 26-02-2016
 * @version 0.0.1
 */
public class EsIndexer implements Indexer {

    private static Logger log = LoggerFactory.getLogger(EsIndexer.class);

    interface IndexSearchResponse {

        /**
         * Check if an object exists inside an index according to the response send
         * back by the remote index system.
         *
         * @return  {@code true} if the {@code item} exists in the index
         *          {@code false} if the {@code item} doesn't exists in the index
         */
        boolean exists();

        /**
         * Return the plain index response.
         *
         * @return  the index response
         */
        SearchResponse indexResponse();

        /**
         * Obtain the plain {@code event} that has been search in the index
         * @return  the event searched in the index
         */
        LogEvent event();
    }

    /**
     * The client indexer
     */
    private Client client;
    private ObjectMapper mapper;
    private String indexName;
    private String indexType;

    /**
     * Default constructor
     *
     * @param clusterName   the name of the cluster
     */
    public EsIndexer(final String clusterName) {
        mapper = new ObjectMapper();
        Node node = nodeBuilder().clusterName(clusterName).node();
        Client client = node.client();
    }

    @Override
    public Indexer setIdexName(final String indexName) {
        this.indexName = indexName;
        return this;
    }

    @Override
    public Indexer setIndexType(final String indexType) {
        this.indexType = indexType;
        return this;
    }

    /**
     * Indexing an {@code item}.
     *
     * @param item      the item to index
     * @return          a representation of the response
     * @throws IndexProcessingException
     *              may occurred in something goea wrong during serialization
     *              or during indexing transaction.
     */
    @Override
    public IndexProcessStatus indexElement(final LogEvent item) throws IndexProcessingException {
        log.info("Starting Indexing process for item [{}]", item);
        try {
            final Stopwatch watch = Stopwatch.createStarted();
            /*
             * The essence of this part is to avoid to store an element more than once/
             * To ensure unicity, we're making a footprint of the element (inside an IndexableLogEvent).
             * a simple update. Otherwise, we simply made a simple insertion.
             * ElasticSearch allow to update document
             */
            final IndexableLogEvent event = new IndexableLogEvent();
            item.cloneTo(event);
            final TimeUnit tu = TimeUnit.MILLISECONDS;
            final String id = event.getFootPrint();
            byte[] indexItem = mapper.writeValueAsBytes(event);
            IndexSearchResponse searchResponse = searchElement(event);
            log.trace("Creating the index request in index [{}-{}] for document [{}]", indexName, indexType, id);
            IndexRequest request = new IndexRequest(indexName, indexType, id)
                    .source(indexItem);
            /*
             * We have to take care about concurrency. Somebody may have drop out
             * the item between the time of our search and the time of our deletion request.
             * To avoid conflict, we just wrap the process inside a try catch and report problem
             * in log if necessary.
             * This implementation should be review to go in production.
             * I'm lazy, but the best solution is to write a specific implementation
             * that reads the LogEvent fields like:
             * .source(jsonBuilder()
             *      .startObject()
             *      .field("key1", "val1")
             *      .field("key2", "val2")
             *   .endObject());
             */
            log.trace("Creating the update request in index [{}-{}] for document [{}]", indexName, indexType, id);
            UpdateRequest updateRequest = new UpdateRequest(indexName, indexType, id)
                    .source(indexItem).upsert(request);
            log.trace("Processing the request in index [{}-{}] for document [{}]", indexName, indexType, id);
            UpdateResponse response = client.update(updateRequest).get();
            long duration = watch.elapsed(tu);
            log.debug("Request proccessed in {} ms. index [{}-{}] for document [{}]", Long.toString(duration), indexName, indexType, id);
            IndexProcessStatus result = new IndexProcessStatus();
            result.put(IndexProcessStatus.INDEX_NAME, response.getIndex())
                    .put(IndexProcessStatus.INDEX_TYPE, response.getType())
                    .put(IndexProcessStatus.INDEX_ID, response.getId())
                    .setVersion(response.getVersion())
                    .setCreated(response.isCreated())
                    .setDuration(duration)
                    .setTimeUnit(tu);
            watch.reset();
            log.info("Indexing process complete for item [{}] in [{} ms]", item, duration);
            return result;
        } catch (Exception e) {
            log.error("Error occurred when processing [{}] with message [{}]", item, e.getMessage());
            throw new IndexProcessingException(item, e.getMessage(), e);
        }
    }

    /**
     * Search an item from the index
     *
     * @param event     the event to search
     * @return          the search response wrapped into an home made POJO
     */
    private IndexSearchResponse searchElement(IndexableLogEvent event) {
        /*
         * Make a simple search an return anonymous instance may don't fit with our
         * needs. It should be review by peer since I'm not sure about the best way of
         * working.
         */
        final SearchResponse response = client.prepareSearch(indexName, indexType)
                // TODO : check setTypes method to be sure to set the correct / relevant values
                .setTypes(indexName, indexType)
                // TODO : Search type may be optimized for our specific case
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("footprint", event.getFootPrint()))
                // SEE : for now we don't make any filter except result counter threshold
                // .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))
                .setFrom(0).setSize(60).setExplain(true)
                .execute()
                .actionGet();

        return new IndexSearchResponse() {
            @Override
            public boolean exists() {
                return !response.isContextEmpty();
            }

            @Override
            public
            SearchResponse indexResponse() {
                return response;
            }

            @Override
            public LogEvent event() {
                return event;
            }
        };
    }
}
