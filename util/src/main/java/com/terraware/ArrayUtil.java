package com.terraware;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author Daniel Terranova <mailto:daniel.terranova@so4it.com>
 * @since 0.8.0
 */
public class ArrayUtil {

    /**
     * Joins multiple object arrays of the same type into one array of the specified type.
     * Arrays can be null. If all arrays are null an empty array is returned.
     *
     * @param <T>
     * @param clazz the type of the array
     * @param arrays the arrays to join, can contain <code>null</code>
     * @return the joined array
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] join(Class<?> clazz, T[] ... arrays) {

        // calculate size of target array
        int size = 0;
        for (T[] array : arrays) {
            if (array != null) {
                size += array.length;
            }
        }

        // Its i safe here!
        T[] result = (T[]) Array.newInstance(clazz, size);

        int i = 0;
        for (T[] array : arrays) {
            if (array != null) {
                System.arraycopy(array, 0, result, i, array.length);
                i += array.length;
            }
        }
        return result;
    }

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
                    pairs.add(ImmutablePair.of(arr[i], arr[j]));

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
                arr[index] = t;
            }
            r = arr[0];
        }
    }

    static int[][] copy2dArray(int[][] from) {
        int[][] copy = new int[from.length][from[0].length];
        for (int i = 0; i < 5; i++) {
            System.arraycopy(from[i], 0, copy[i],0, from[i].length);
        }
        return copy;
    }

}
