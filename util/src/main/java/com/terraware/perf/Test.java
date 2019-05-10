package com.terraware.perf;

import java.util.ArrayList;
import java.util.List;

public class Test {
    List<String> data;

    public Test(int tlrMask) {
        data = createData(10000000);
    }

//    String test() {
//        for (int i = 0; i < data.size(); i++) {
//            String s = data.get(i); //take out n consume, fair with foreach
//        }
//        return s
//    }

    public static void main(String[] arg) {


    }

    private static List<String> createData(int N) {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            data.add("Number : " + i);
        }
        return data;
    }
}
