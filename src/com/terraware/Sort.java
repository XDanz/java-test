package com.terraware;

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
}
