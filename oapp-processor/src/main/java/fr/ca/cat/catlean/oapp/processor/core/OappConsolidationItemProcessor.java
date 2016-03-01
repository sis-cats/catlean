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
package fr.ca.cat.catlean.oapp.processor.core;

import fr.ca.cat.catlean.oapp.processor.domain.OappConsolidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 *
 * @author lefebvreme
 * @since 24-02-2016
 * @version 0.0.1
 */
public class OappConsolidationItemProcessor implements ItemProcessor<OappConsolidation, OappConsolidation> {

    private static Logger logger = LoggerFactory.getLogger(OappConsolidationItemProcessor.class);

    @Override
    public OappConsolidation process(OappConsolidation oappConsolidation) throws Exception {
        logger.info("Process mapping for core elements");
        // nothing to do right now
        return oappConsolidation;
    }
}
