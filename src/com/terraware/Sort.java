package com.terraware;

import javafx.util.Pair;

import java.util.*;

/**
 * @author Daniel Terranova <mailto:daniel.terranova@so4it.com>
 * @since 0.8.0
 */
public class Sort {

    public Sort() {}

    public static int[] doInsertionSort(int[] input) {
        for (int i = 1; i < input.length; i++) {
            for (int j = i ; j > 0 ; j--) {
                if (input[j] < input[j-1]) {
                    iswap(j,j-1,input);
                }
            }
        }
        return input;
    }

    static int[] doBubbleSort(int[] arr) {
        int n = arr.length;
        int k;
        for (int m = n; m >= 0; m--) {
            for (int i = 0; i < n - 1; i++) {
                k = i + 1;
                if (arr[i] > arr[k]) {
                    iswap(i, k, arr);
                }
            }
        }
        return arr;
    }

    static List<Pair<Integer,Integer>> doInvertions(int[] arr) {
        List<Pair<Integer,Integer>> pairs  = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            for (int j = i+1; j < arr.length; j++) {
                if (arr[i] > arr[j])
                    pairs.add(new Pair<>(arr[i], arr[j]));

            }
        }
        return pairs;
    }

    static int[] fib(int count) {
        int[] fibs = new int[count];
        fibs[0] = 0;
        fibs[1] = 1;
        for (int i = 2; i < count; i++) {
            fibs[i] = fibs[i-1] + fibs[i-2];
        }
        return fibs;
    }

    static boolean validate(String longString, String word) {
        SortedSet<Integer> pos = new TreeSet<>();
        for (char c : word.toCharArray()) {
            if(!validate(longString, c, pos))
                return false;
        }
        System.out.println("pos = " + pos);
        return true;
    }

    private static boolean validate(String longString, char c, Set<Integer> pos) {
        char[] chars = longString.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (!pos.contains(i) && c == chars[i]) {
                pos.add(i);
                return true;
            }
        }
        return false;
    }

    private static int max(int[] arr, int offset, int currentMax) {
        if (offset == arr.length - 1)
            return currentMax;
        else {
            int max;
            if (currentMax < arr[offset]) {
                max = arr[offset];
            } else {
                max = currentMax;
            }
            return max(arr, ++offset, max);
        }
    }




    static int max(int[] arr) {
        return max(arr, 0, Integer.MIN_VALUE);
    }

    private static void iswap(int i, int j, int[] a) {
        int temp;
        temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    static void rotate(int[] arr, int pos) {
        int r = arr[0];
        for ( ;pos > 0; pos-- ) {

            for (int i = 1; i < arr.length+1; i++) {
                int t = r;
                int index = i % arr.length;
                r = arr[index];
                System.out.println("r=" + r);
                System.out.println("arr[" + index + "]=" + t);
                arr[index] = t;
            }
            r = arr[0];
            System.out.println("****** ");
        }
    }

    static int wordCount(String w) {
        char[] arr = w.toCharArray();
        boolean b = false;
        int c = 0;
        for (int i = 0; i < arr.length; i++) {
            if(arr[i] == ' ') {
                if (b) {
                  c++;
                  b = false;
                }
            } else {
                b = true;
                if (i == arr.length - 1)
                    c++;
            }
        }
        return c;
    }
}
