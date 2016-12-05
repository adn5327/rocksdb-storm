/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.company;

import java.util.HashMap;
import java.util.List;

/**
 * This interface defines the methods for preparing, storing and querying metrics
 *
 * @author Austin Chung <achung13@illinois.edu>
 * @author Abhishek Deep Nigam <adn5327@gmail.com>
 * @author Naren Dasan <naren@narendasan.com>
 */

public interface MetricStore {

    /**
     * Create RocksDB instance
     * using the configurations provided via the config map
     * @param config Storm config map
     */
    void prepare(HashMap config);

    /**
     * Stores metrics in the store
     * @param metric Metric to store
     */
    void insert(Metric metric);

    /**
     * Scans all metrics in the store
     * @return List<String> metrics in store
     */
    List<String> scan();

    /**
     * Scans all metrics with prefix in the store
     * @param prefix prefix to query in store
     * @return List<String> metrics in store
     */
    List<String> scan(String prefix);

    /**
     * Scans all metrics with settings in the store
     * @param settings map of settings to search by
     * @return List<String> metrics in store
     */
    List<String> scan(HashMap settings);

    /**
     * Validates storm configuration for Metrics Store
     * @param config Storm config to specify which store type, location of store and creation policy
     * @throws MetricException if there is a missing required configuration or if the store does not exist but
     * the config specifies not to create the store
     */
    void validateConfig(HashMap config) throws MetricException;

    //void remove();

}
