package test;

import com.terraware.Simple;

import java.util.Arrays;

public class Test {

    public void test ( String arg[]) {
        Simple s = new Simple();
        System.out.println ( " Hello!" );
        int[] arr1 = {10,34,2,56,7,67,88,42};
        int[] arr2 = s.doInsertionSort(arr1);
        System.out.println("arr2 = " + Arrays.toString(arr2));
    }



}