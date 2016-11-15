package com.company;

import java.lang.String;
import java.lang.StringBuilder;

// 2016-11-03 15:45:05,238 74395    1478205905	localhost:6703	  8:exclaim1   	__emit-count           	{default=960}
public class Metric {

    public String datestamp = null;
    private String timestamp = null;
    private String jobid = null;
    private String epochTS = null;
    private String port = null;
    private String componentName = null;
    private String metricName = null;

    public String getValue() {
        return value;
    }

    private String value = null;

    public Metric(String stamp) {
        String[] elements = stamp.split("\\s+");
        this.datestamp = elements[0];
        this.timestamp = elements[1];
        this.jobid = elements[2];
        this.epochTS = elements[3];
        this.port = elements[4];
        this.componentName = elements[5];
        this.metricName = elements[6];
        this.value = elements[7];

    }

    public String serialize()
    {
        StringBuilder x = new StringBuilder();
        x.append(this.jobid);
        x.append('|');
        x.append(this.epochTS);
        return String.valueOf(x);
    }
}
