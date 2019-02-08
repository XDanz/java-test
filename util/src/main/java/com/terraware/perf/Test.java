package com.terraware.perf;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class Test {

    public static void main(String[] arg) {
        double pMaximumPackagePrice = 92233720368547.75807;
        BigDecimal bigDecimal = BigDecimal.valueOf(9223372036854775807L,5);
        System.out.println("bigDecimal = " + bigDecimal);


//
//        // Double precision issue
//        double d1 = 0.01;
//        System.out.println("d1 = " + d1);
//
//        double d2 = 33.33;
//        System.out.println("d2 = " + d2);
//
//        double d3 = d2 + d1;
//        System.out.println("d3 = d2+d1 = " + d3);
//
//        double d4 = 33.34;
//        System.out.println("d4 = " + d4);
//
//        // Let's work for BigDecimal
//        BigDecimal bd1 = new BigDecimal(0.01);
//        System.out.println("bd1 = " + bd1.doubleValue());
//
//        BigDecimal bd2 = new BigDecimal(33.33);
//        System.out.println("bd2 = " + bd2.doubleValue());
//
//        BigDecimal bd3 = new BigDecimal(33.34);
//        System.out.println("bd3 = " + bd3.doubleValue());
//
//        BigDecimal bd4 = new BigDecimal(33.33 + 0.01);
//        System.out.println("bd4 = " + bd4.doubleValue());
//
//        BigDecimal bd5 = new BigDecimal(String.format("%.2f", 33.33 + 0.01));
//        System.out.println("bd5 = " + bd5.doubleValue());
//
//        // Provide values in String in BigDecimal for better precision
//        // if you already have value in double like shown below
//        double d11 = 0.1 + 0.1 + 0.1;
//        System.out.println("d11 = " + d11);
//
//        BigDecimal bd11 = new BigDecimal(String.valueOf(d11)); // You already lost the precision
//        System.out.println("bd11 = " + bd11.doubleValue());
//
//        /* Convert to string as per your needed precision like 2 for
//           example when you might have lost the precision  */
//        BigDecimal bd12 = new BigDecimal(String.format("%.2f", d11));
//        System.out.println("bd12 = " + bd12);
//
//        // Or make sure to do that before addition in double
//        double d21 = 0.1;
//        BigDecimal bd21 = new BigDecimal(String.valueOf(d21));
//        BigDecimal bd22 = bd21.add(bd21).add(bd21);
//        System.out.println("bd21 = " + bd21.doubleValue() + ", bd22 = " + bd22.doubleValue());
    }
}
