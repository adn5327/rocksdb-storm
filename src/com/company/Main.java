package com.company;
import com.company.metric;
import org.rocksdb.RocksDB;
import org.rocksdb.Options;
import org.rocksdb.RocksDBException;

public class Main {

    public static void main(String[] args) {
        RocksDB.loadLibrary();
        // the Options class contains a set of configurable DB options
        // that determines the behavior of a database.
        Options options = new Options().setCreateIfMissing(true);
        RocksDB db = null;
        try {
            // a factory method that returns a RocksDB instance
            db = RocksDB.open(options, "./db");
            // do something
        } catch (RocksDBException e) {
            System.out.println(e);
        }


        metric thing = new metric("2016-11-03 15:45:05,238 74395    1478205905\tlocalhost:6703\t  8:exclaim1   \t__emit-count           \t{default=960}");
        System.out.println(thing.datestamp);


    }
}
