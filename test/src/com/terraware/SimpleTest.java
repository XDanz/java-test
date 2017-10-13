package com.terraware;

import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

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
    public void testInvertions() {
        int[] input = { 4, 2, 9, 6, 23, 12, 34, 35, 1 };

        for (Pair<Integer, Integer> pair : Sort.doInvertions(input)) {
            System.out.println("pair = " + pair);
        }

    }

    @Test
    void name() {
        Set<Person> people = new ConcurrentSkipListSet<>();
        people.add(new Person(3, "Moa", "moa"));
        people.add(new Person(1, "Daniel", "a"));
        people.add(new Person(4, "Lena", "lena"));
        people.add(new Person(5, "Lenas", "lenas"));
        people.add(new Person(0, "L", "l"));

        for (Person person : people) {
            System.out.println("person = " + person);
        }

    }

    static class Person implements Comparable<Person> {
        Integer score;

        String name;
        String word;

        public Person(Integer score, String name, String word) {
            this.score = score;
            this.name = name;
            this.word = word;
        }

        @Override
        public int compareTo(Person o) {
            return -1 *score.compareTo(o.score);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return Objects.equals(score, person.score) &&
                    Objects.equals(name, person.name) &&
                    Objects.equals(word, person.word);
        }

        @Override
        public int hashCode() {
            return Objects.hash(score, name, word);
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("Person{");
            sb.append("score=").append(score);
            sb.append(", name='").append(name).append('\'');
            sb.append(", word='").append(word).append('\'');
            sb.append('}');
            return sb.toString();
        }
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

    @Test
    void testFibs() {
        System.out.println("Sort.fib(10) = " + Arrays.toString(Sort.fib(10)));
    }

    @Test
    void testValidate() {
        Assertions.assertTrue(Sort.validate("allanballan", "banan"));
        Assertions.assertTrue(Sort.validate("allanballan", "llll"));
    }

    Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
