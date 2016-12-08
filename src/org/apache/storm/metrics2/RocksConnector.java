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

import java.util.*;

import org.rocksdb.RocksDB;
import org.rocksdb.Options;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import org.apache.storm.metrics2.StringKeywords;


public class RocksConnector {

    private RocksDB db;
    public RocksConnector(String dbfilename)
    {
        RocksDB.loadLibrary();
        // the Options class contains a set of configurable DB options
        // that determines the behavior of a database.
        Options options = new Options().setCreateIfMissing(true);
        this.db = null;
        try {
            // a factory method that returns a RocksDB instance
            this.db = RocksDB.open(options, dbfilename);
            // do something
        } catch (RocksDBException e) {
            System.out.println(e);
        }
    }

    public void insert(Metric m)
    {
        try
        {
            this.db.put(m.serialize().getBytes(), m.getValue().getBytes());
        }
        catch(RocksDBException e)
        {
            System.out.println("oh no!");
        }
    }

    public void remove()
    {

    }

    public List<String> scan()
    {
        List<String> result = new ArrayList<String>();
        RocksIterator iterator = this.db.newIterator();
        for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
            String key = new String(iterator.key());
            result.add(String.format("%s", new String(iterator.key())));
        }
        return result;
    }

    private List<String> scan(String prefix, HashMap<String, Object> settings)
    {
        List<String> result = new ArrayList<String>();
        RocksIterator iterator = this.db.newIterator();
        for (iterator.seek(prefix.getBytes()); iterator.isValid(); iterator.next()) {
            String key = new String(iterator.key());
            if(!key.startsWith(prefix))
            {
                break;
            }
            Metric possibleKey = new Metric(key);
            if(checkMetric(possibleKey, settings))
            {
                result.add(String.format("%s", new String(iterator.value())));
            }
        }
        return result;
    }

    public List<String> scan(HashMap<String, Object> settings)
    {
        List<String> result = new ArrayList<String>();
        //IF CAN CREATE PREFIX -- USE THAT
        //ELSE DO FULL TABLE SCAN
        String prefix = Metric.createPrefix(settings);
        if(prefix != null){
            return scan(prefix, settings);
        }
        RocksIterator iterator = this.db.newIterator();
        for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
            String key = new String(iterator.key());

            Metric possibleKey = new Metric(key);
            if(checkMetric(possibleKey, settings))
            {
                result.add(String.format("%s", new String(iterator.value())));
            }

        }
        return result;
    }

    public boolean checkMetric(Metric possibleKey, HashMap<String, Object> settings)
    {
        if(settings.containsKey(StringKeywords.component) && !possibleKey.getCompId().equals(settings.get(StringKeywords.component))) {
            return false;
        } else if(settings.containsKey(StringKeywords.metricName) && !possibleKey.getMetricName().equals(settings.get(StringKeywords.metricName))) {
            return false;
        } else if(settings.containsKey(StringKeywords.topoId) && !possibleKey.getTopoId().equals(settings.get(StringKeywords.topoId))) {
            return false;
        } else if(settings.containsKey(StringKeywords.timeStart) && possibleKey.getTimeStamp() <= Long.parseLong(settings.get(StringKeywords.timeStart).toString())) {
            return false;
        } else if(settings.containsKey(StringKeywords.timeEnd) && possibleKey.getTimeStamp() >= Long.parseLong(settings.get(StringKeywords.timeEnd).toString())) {
            return false;
        }
        return true;
    }

}
