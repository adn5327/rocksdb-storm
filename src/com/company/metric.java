package com.company;

// 2016-11-03 15:45:05,238 74395    1478205905	localhost:6703	  8:exclaim1   	__emit-count           	{default=960}
public class metric {

    public String datestamp = null;
    private String timestamp = null;
    private String jobid = null;
    private String somethingID = null;
    private String port = null;
    private String componentName = null;
    private String metricName = null;

    private String value = null;

    // topo1|hb1|0|c1|s1|h1|p1,50
    public metric(String stamp) {
        String[] elements = stamp.split("\\s+");
        this.datestamp = elements[0];
        this.timestamp = elements[1];
        this.jobid = elements[2];
        this.somethingID = elements[3];
        this.port = elements[4];
        this.componentName = elements[5];
        this.metricName = elements[6];
        this.value = elements[7];

    }
}
