package com.terraware;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author Daniel Terranova <mailto:daniel.terranova@so4it.com>
 * @since 0.8.0
 */
class SimpleTest {


    @Test
    public void dummy2() {

        int price = 6;
        switch (price) {
            case 2: System.out.println("It is: 2");
            case 5: System.out.println("It is: 5");
            case 9: System.out.println("It is: 9");
            default: System.out.println("It is: default");
        }
    }

    @Test
    public void testBubbleSort() {
            int[] input = { 4, 2, 9, 6, 23, 12, 34, 0, 1 };

        System.out.println(Arrays.toString(Sort.doBubbleSort(input)));
    }


}
