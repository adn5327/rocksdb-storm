package com.company;

import org.rocksdb.RocksDB;
import org.rocksdb.Options;
import org.rocksdb.RocksDBException;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws RocksDBException{
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
        for (String each : x) {
            //System.out.println(each);
        }

        connector.scan("my-test-topology","1478209814","1478209816");
    }
}
