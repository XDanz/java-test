package com.terraware.perf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreadTest {

    Map<Integer, String> m = new HashMap<>();

    void fill(int startKey, int endKey) {
        while (startKey <= endKey) {
            m.put(startKey, "Value" + startKey);
            startKey++;
        }
    }

    public Map<Integer, String> getMap() {
        return m;
    }

    public static void main(String[] arg) throws InterruptedException {
        int startKey = 1;
        int endKey = 16;
        int numThreads = 10;

        while (true) {
            ThreadTest threadTest = new ThreadTest();
            threadTest.fill(startKey, endKey);
            Map<Integer, String> map = threadTest.getMap();

            List<ThreadReader> tThreads = new ArrayList<>();

            for (int i = 0; i < numThreads; i++) {
                ThreadReader threadReader = new ThreadReader(map, startKey, endKey);
                threadReader.start();
                tThreads.add(threadReader);
            }

            for (ThreadReader tThread : tThreads) {
                tThread.join();
            }
            numThreads++;
            endKey++;
        }
    }
}
