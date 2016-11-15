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

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        RocksConnector connector = new RocksConnector("db.test");

        // File IO
        try (BufferedReader br = new BufferedReader(new FileReader("./logs/metrics_sample.txt"))) {
            String line;
            line = br.readLine();
            while ((line = br.readLine()) != null) {
                //System.out.println(line); // put into rocks here
                String[] elements = line.split("\\s+");
                //    public Metric(String metric, int TS, int compId, String topoId, String value)
                //metric	topoId	host	port	compname	compId	TS	value	dimentions (key=value)
                connector.insert(new Metric(elements[0], Integer.parseInt(elements[6]), Integer.parseInt(elements[5]), elements[1], elements[7]));
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> x = connector.scan("my-test-topology|7");
        for(String each : x)
        {
            System.out.println(each);
        }

    }
}
