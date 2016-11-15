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
                //System.out.println(line); // put into rocks here
                connector.insert(new Metric(line));
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            e.printStackTrace();
        }


        connector.aggregate("74395", "74398", connector);

    }
}
