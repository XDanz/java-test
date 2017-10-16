package test;

import com.terraware.ArrayUtil;

import java.util.Arrays;

public class Test {

    public void test ( String arg[]) {
        ArrayUtil s = new ArrayUtil();
        System.out.println ( " Hello!" );
        int[] arr1 = {10,34,2,56,7,67,88,42};
        int[] arr2 = s.doInsertionSort(arr1);
        System.out.println("arr2 = " + Arrays.toString(arr2));
    }



}