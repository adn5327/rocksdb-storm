package com.company;
import com.company.metric;
import org.rocksdb.RocksDB;
import org.rocksdb.Options;
import org.rocksdb.RocksDBException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws java.io.IOException{

        // Load Rocks
        RocksDB.loadLibrary();
        Options options = new Options().setCreateIfMissing(true);
        RocksDB db = null;
        try {
            db = RocksDB.open(options, "./db");
        } catch (RocksDBException e) {
            System.out.println(e);
        }


        // File IO
        try (BufferedReader br = new BufferedReader(new FileReader("./logs/metrics_sample.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line); // put into rocks here
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }


        // Test metric object
        //metric thing = new metric("2016-11-03 15:45:05,238 74395    1478205905\tlocalhost:6703\t  8:exclaim1   \t__emit-count           \t{default=960}");
        //System.out.println(thing.datestamp);

    }
}
