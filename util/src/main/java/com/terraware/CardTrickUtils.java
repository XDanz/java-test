package com.terraware;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

public class CardTrickUtils {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int nCases = sc.nextInt();
        int[] cases = new int[nCases];
        for (int i = 0; i < nCases; i++) {
            cases[i] = sc.nextInt();
        }

        for (int aCase : cases) {
            int[] output = generate(aCase);
            for (int i : output) {
                System.out.print(i);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    static int[] generate(int n) {
        int[] ints = IntStream.range(1, n).toArray();

        int[] arr = {n};
        for (int i = ints.length - 1; i >= 0; i--) {
            int c = ints[i];
            arr = add2BeginningOfArray(arr, c);
            rotate(arr, c);
        }
        return arr;
    }

    private static int[] add2BeginningOfArray(int[] es, int e) {
        int[] newArray = Arrays.copyOf(es, es.length + 1);
        newArray[0] = e;
        System.arraycopy(es, 0, newArray, 1, es.length);

        return newArray;
    }

    private static void rotate(int[] arr, int pos) {
        int r = arr[0];
        for (; pos > 0; pos--) {
            for (int i = 1; i < arr.length + 1; i++) {
                int t = r;
                int index = i % arr.length;
                r = arr[index];
                arr[index] = t;
            }
            r = arr[0];
        }
    }
}
