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

    public static void main(String[] args) {
        RocksConnector connector = new RocksConnector("db.test");

        // File IO
        try (BufferedReader br = new BufferedReader(new FileReader("./logs/metrics_sample.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line); // put into rocks here
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Metric thing = new Metric("2016-11-03 15:45:05,238 74395    1478205905\tlocalhost:6703\t  8:exclaim1   \t__emit-count           \t{default=960}");
        System.out.println(thing.serialize());
        connector.insert(thing);
        List<String> x = connector.scan("74395");

        for(String each : x)
        {
            System.out.println(each);
        }

    }
}
