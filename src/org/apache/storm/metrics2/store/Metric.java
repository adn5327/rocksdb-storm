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

package org.apache.storm.metrics2.store;

import java.lang.String;
import java.lang.StringBuilder;
import java.util.HashMap;
import java.util.List;

public class Metric {

    private String metricName;
    private String topoId;
    private String host;
    private int port;
    private String compId;
    private Long timestamp;
    private String value;
    private String executor;
    private String dimensions;
    private String stream;
    private static String[] prefixOrder = {StringKeywords.topoId, StringKeywords.metricName, StringKeywords.time,
                                    StringKeywords.component, StringKeywords.executor, StringKeywords.host,
                                    StringKeywords.port, StringKeywords.port, StringKeywords.stream};

    public String getValue()
    {
        return value;
    }

    public Metric(String metric, Long TS, String compId, String topoId, String value)
    {
        this.metricName = metric;
        this.timestamp = TS;
        this.compId = compId;
        this.topoId = topoId;
        this.value = value;
    }

    public Metric(String str)
    {
        deserialize(str);
    }

    public String getCompId() { return this.compId; }

    public Long getTimeStamp() { return this.timestamp; }

    public String getTopoId() { return this.topoId; }

    public String getMetricName() { return this.metricName; }

    public String serialize()
    {
        StringBuilder x = new StringBuilder();
        x.append(this.topoId);
        x.append("|");
        x.append(this.metricName);
        x.append("|");
        x.append(this.timestamp);
        x.append("|");
        x.append(this.compId);
        x.append("|");
        x.append(this.executor);
        x.append("|");
        x.append(this.host);
        x.append("|");
        x.append(this.port);
        x.append("|");
        x.append(this.stream);

        return String.valueOf(x);
    }

    public static String createPrefix(HashMap<String, Object> settings){
        StringBuilder x = new StringBuilder();
        for(String each: prefixOrder){
            Object cur = settings.get(each);
            if(cur != null){
                x.append(cur.toString());
                x.append("|");
                settings.remove(each);
            }
            else{
                break;
            }
        }

        if(x.length() == 0) {
            return null;
        }
        else
        {
            x.deleteCharAt(x.length()-1);
            return x.toString();
        }
    }

    public void deserialize(String str)
    {

        String[] elements = str.split("\\|");
        this.metricName = elements[1];
        this.timestamp = Long.parseLong(elements[2]);
        this.compId = elements[3];
        this.topoId = elements[0];

    }
}
