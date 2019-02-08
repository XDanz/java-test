package com.terraware.perf;

public class ThreadTest2 {

    static int x = 0, y = 0;
    static int a = 0, b = 0;

    public static void main(String[] arg) throws InterruptedException {

            Thread t0 = new Thread(() -> {
                a = 1;
                x = b;
            });


            Thread t1 = new Thread(() -> {
                b = 1;
                y = a;
            });
            t0.start(); t1.start();
            t0.join(); t1.join();
            System.out.println("(x = " + x + ",y= " + y + ")");
    }
}
