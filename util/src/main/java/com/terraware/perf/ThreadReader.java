package com.terraware.perf;

import java.util.Map;

public class ThreadReader extends Thread {

    private Map<Integer, String> map;
    private int startKey;
    private int endKey;

    ThreadReader(Map<Integer, String> map, int startKey, int endKey) {
        this.map = map;
        this.startKey = startKey;
        this.endKey = endKey;
    }

    @Override
    public void run() {
        while (startKey <= endKey) {
            if (map.containsKey(startKey)) {
                String s = map.get(startKey);
                if (s == null)
                    throw new RuntimeException("key:" + startKey + " has null as value!");

            }
            startKey++;
        }

    }
}
