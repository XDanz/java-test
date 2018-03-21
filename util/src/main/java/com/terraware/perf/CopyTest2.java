package com.terraware.perf;

public class CopyTest2 {

    static int[][] copy2dArray(int[][] from) {
        int[][] copy = new int[5][3];
        for (int i = 0; i < 5; i++) {
            System.arraycopy(from[i], 0, copy[i],0, from[i].length);
        }
        return copy;
    }

    public static void main(String[] arg) {
        int num = Integer.parseInt(arg[0]);
        int[][] copy =
                new int[][] {
                        { 1,2,3 },
                        { 1,2,3 },
                        { 1,2,3 },
                        { 1,2,3 },
                        { 1,2,3 },
                };
        for (int i = 0; i < num; i++) {
            long start = System.nanoTime();
            copy2dArray(copy);
            long diff = System.nanoTime() - start;
            System.out.println(diff + " ns");
        }
    }

}
