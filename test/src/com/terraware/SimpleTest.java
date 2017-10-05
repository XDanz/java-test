package com.terraware;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.NavigableSet;
import java.util.TreeSet;

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

    @Test
    public void testInsertSort() {
        int[] input = { 4, 2, 9, 6, 23, 12, 34, 0, 1 };

        System.out.println(Arrays.toString(Sort.doInsertionSort(input)));
    }

    @Test
    public void testRecursiveMax() {
        int[] input = { 4, 2, 9, 6, 23, 12, 34, 35, 1 };

        System.out.println("Max=" + Sort.max(input));
    }

    @Test
    void testNavigableSet() {
        LocalDate _2017_12_1 = LocalDate.of(2017, 12, 1);
        LocalDate _2018_12_2 = LocalDate.of(2018, 12, 2);
        LocalDate _2019_12_3 = LocalDate.of(2019, 12, 3);
        LocalDate _2020_12_3 = LocalDate.of(2020, 12, 3);
        NavigableSet<Date> original = new TreeSet<>();
        original.add(toDate(_2017_12_1));
        //original.add(toDate(_2018_12_2));
        original.add(toDate(_2019_12_3));
        original.add(toDate(_2020_12_3));
        System.out.println("original = " + original.floor(toDate(LocalDate.of(2018, 12, 2))));

    }

    Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
