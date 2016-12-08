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
package org.apache.storm.metrics2;

import java.lang.String;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

import org.rocksdb.RocksDB;
import org.rocksdb.Options;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;

/**
 * This class implements the Storm Metrics Store Interface using RocksDB as a store
 * It contains preparing, insertion and scan methods to store and query metrics
 *
 * @author Austin Chung <achung13@illinois.edu>
 * @author Abhishek Deep Nigam <adn5327@gmail.com>
 * @author Naren Dasan <naren@narendasan.com>
 */

public class RocksDBConnector implements MetricStore {

    private RocksDB db;

    /**
     * Implements the prepare method of the Metric Store, create RocksDB instance
     * using the configurations provided via the config map
     * @param config Storm config map
     */
    @Override
    public void prepare(Map config) {

        try {
            validateConfig(config);
        } catch(MetricException e) {
            System.out.println(e);
        }

        RocksDB.loadLibrary();
        // the Options class contains a set of configurable DB options
        // that determines the behavior of a database.
        //Utils.getString
        boolean createIfMissing = Boolean.parseBoolean(config.get("storm.metrics2.store.rocksdb.create_if_missing").toString());
        Options options = new Options().setCreateIfMissing(createIfMissing);

        this.db = null;
        try {
            // a factory method that returns a RocksDB instance
            String path = config.get("storm.metrics2.store.rocksdb.location").toString();
            this.db = RocksDB.open(options, path);
            // do something
        } catch (RocksDBException e) {
            System.out.println(e);
        }
    }

    /**
     * Implements the insert method of the Metric Store, stores metrics in the store
     * @param m Metric to store
     */
    @Override
    public void insert(Metric m) {
        try {
            this.db.put(m.serialize().getBytes(), m.getValue().getBytes());
        }
        catch(RocksDBException e) {
            System.out.println("oh no!");
        }
    }

    /**
     * Implements scan method of the Metrics Store, scans all metrics in the store
     * @return List<String> metrics in store
     */
    @Override
    public List<String> scan() {
        List<String> result = new ArrayList<String>();
        RocksIterator iterator = this.db.newIterator();
        for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
            String key = new String(iterator.key());
            result.add(String.format("%s", new String(iterator.key())));
        }
        return result;
    }

    /**
     * Implements scan method of the Metrics Store, scans all metrics with prefix in the store
     * @param prefix prefix to query in store
     * @return List<String> metrics in store
     */
    @Override
    public List<String> scan(String prefix) {
        List<String> result = new ArrayList<String>();
        RocksIterator iterator = this.db.newIterator();
        for (iterator.seek(prefix.getBytes()); iterator.isValid(); iterator.next()) {
            String key = new String(iterator.key());
            if (key.startsWith(prefix)) {
                result.add(String.format("%s", new String(iterator.key())));
            }
        }
        return result;
    }

    /**
     * Implements scan method of the Metrics Store, scans all metrics with settings in the store
     * @param settings map of settings to search by
     * @return List<String> metrics in store
     */
    @Override
    public List<String> scan(HashMap settings) {
        List<String> result = new ArrayList<String>();
        RocksIterator iterator = this.db.newIterator();
        for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
            String key = new String(iterator.key());

            Metric possible_key = new Metric(key);

            if(settings.containsKey("compId") && !possible_key.getCompId().equals(settings.get("compId"))) {
                continue;
            } else if(settings.containsKey("metric") && !possible_key.getMetricName().equals(settings.get("compId"))) {
                continue;
            } else if(settings.containsKey("topoId") && !possible_key.getTopoId().equals(settings.get("compId"))) {
                continue;
            } else if(settings.containsKey("timeStart") && possible_key.getTimeStamp() <= Long.parseLong(settings.get("timeStart").toString())) {
                continue;
            } else if(settings.containsKey("timeEnd") && possible_key.getTimeStamp() >= Long.parseLong(settings.get("timeEnd").toString())) {
                continue;
            } else {
                result.add(String.format("%s", new String(iterator.value())));
            }

        }
        return result;
    }

    /**
     * Implements configuration validation of Metrics Store, validates storm configuration for Metrics Store
     * @param config Storm config to specify which store type, location of store and creation policy
     * @throws MetricException if there is a missing required configuration or if the store does not exist but
     * the config specifies not to create the store
     */
    private void validateConfig(Map config) throws MetricException {
        if (config.get("storm.metrics2.store.connector_class") != "org.apache.storm.metrics2.RocksDBConnector") {
            throw new MetricException("Not a configuration for the RockDB Connector");
        }

        if (!(config.containsKey("storm.metrics2.store.rocksdb.location"))) {
            throw new MetricException("Not a vaild RocksDB configuration - Missing store location");
        }

        if (!(config.containsKey("storm.metrics2.store.rocksdb.create_if_missing"))) {
            throw new MetricException("Not a vaild RocksDB configuration - Does not specify creation policy");
        }

        String createIfMissing = config.get("storm.metrics2.store.rocksdb.create_if_missing").toString();
        if (!Boolean.parseBoolean(createIfMissing)) {
            String storePath = config.get("storm.metrics2.store.rocksdb.location").toString();
            if (!(new File(storePath).exists())) {
                throw new MetricException("Configuration specifies not to create a store but no store currently exists");
            }
        }
        return;
    }

    private void remove() {

    }

}
