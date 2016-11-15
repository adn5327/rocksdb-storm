package com.company;

import java.lang.String;
import java.lang.StringBuilder;

// 2016-11-03 15:45:05,238 74395    1478205905	localhost:6703	  8:exclaim1   	__emit-count           	{default=960}
public class Metric {

    private String metricName;
    private String topoId;
    private String host;
    private int port;
    private String componentName;
    private int compId;
    private int timestamp;
    private String value;
    private String dimensions;

    public String getValue()
    {
        return value;
    }

    public Metric(String metric, int TS, int compId, String topoId, String value)
    {
        this.metricName = metric;
        this.timestamp = TS;
        this.compId = compId;
        this.topoId = topoId;
        this.value = value;
    }

    public String serialize()
    {
        StringBuilder x = new StringBuilder();
        x.append(this.topoId);
        x.append('|');
        x.append(this.compId);
        x.append('|');
        x.append(this.timestamp);
        return String.valueOf(x);
    }
}
