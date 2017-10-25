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
class ArrayUtilTest {


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

        System.out.println(Arrays.toString(ArrayUtil.doBubbleSort(input)));
    }

    @Test
    public void testInsertSort() {
        int[] input = { 4, 2, 9, 6, 23, 12, 34, 0, 1 };

        System.out.println(Arrays.toString(ArrayUtil.doInsertionSort(input)));
    }

    @Test
    public void testRecursiveMax() {
        int[] input = { 4, 2, 9, 6, 23, 12, 34, 35, 1 };

        System.out.println("Max=" + ArrayUtil.max(input));
    }

    @Test
    public void testInvertions() {
        int[] input = { 4, 2, 9, 6, 23, 12, 34, 35, 1 };

        for (Pair<Integer, Integer> pair : ArrayUtil.doInvertions(input)) {
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
    void testDuplicateNumber() {
        List<Integer> numbers = new ArrayList<Integer>();
        for(int i=1;i<10;i++){
            numbers.add(i);
        }
        //add duplicate number into the list
        numbers.add(1);
        System.out.println("Duplicate Number: "+findDuplicateNumber(numbers));
    }

    public static int findDuplicateNumber(List<Integer> numbers){

        int highestNumber = numbers.size() - 1;
        int total = getSum(numbers);
        int duplicate = total - (highestNumber*(highestNumber+1)/2);
        return duplicate;
    }

    public static int getSum(List<Integer> numbers){

        int sum = 0;
        for(int num:numbers){
            sum += num;
        }
        return sum;
    }

    @Test
    void testFibs() {
        System.out.println("ArrayUtil.fib(10) = " + Arrays.toString(ArrayUtil.fib(10)));
    }

    @Test
    void testRotate_five_step() {
        int[] arr = {1,2,3,4,5};
        ArrayUtil.rotate(arr,5);
        assertArrayElementEquals(new int[] { 1,2,3,4,5 }, arr);
    }

    @Test
    void testRotate_one_step() {
        int[] arr = { 1, 2, 3, 4, 5};
        ArrayUtil.rotate(arr,1);
        assertArrayElementEquals(new int[] {5,1,2,3,4}, arr);
    }

    @Test
    void testRotate_two_step() {
        int[] arr = { 1,2,3,4, 5};
        ArrayUtil.rotate(arr,2);
        assertArrayElementEquals(new int[] {4,5,1,2,3}, arr);
    }

    @Test
    void test2() {
        int[] arr = { 1, 1, 3, 3, 3};

        HashMap<Integer,Integer> map  = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            if (map.containsKey(arr[i]))
                map.replace(arr[i], map.get(arr[i])+1);
            else
                map.put(arr[i], 1);
        }
        for (Map.Entry<Integer, Integer> e : map.entrySet()) {
            if (e.getValue() >= 2 )
                System.out.println("dup = " + e.getKey() );
        }
    }

    @Test
    void namess() {
        int[] arr = { 1 ,1};
        Arrays.sort(arr);
        for (int i = 0; i < arr.length-1; i++) {
            if (arr[i]-arr[i+1] == 0)
                System.out.println("i = " + arr[i]);
        }
    }

    @Test
    void namezz() {
        int[] arr = { 8, 1, 1, 3, 3, 3, 3, 2, 2, 4, 8};
        Set<Integer> set = new HashSet<>();
        for (int i : arr) {
            if (!set.contains(i))
                set.add(i);
            else {
                System.out.println("dup:" + i);
                set.remove(i);
            }
        }
        System.out.println("set = " + set);
    }


    Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private void assertArrayElementEquals(int[] expected, int[] actual) {
        if (expected.length != actual.length)
            Assertions.fail(String.format("length differs: expected: %s, acutal",expected.length, actual.length ));

        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i])
                Assertions.fail(String.format("element differs: expected: %s, acutal",expected[i], actual[i] ));
        }
    }
}
