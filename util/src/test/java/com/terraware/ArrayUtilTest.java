package com.terraware;

import com.google.common.collect.Ordering;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.list.TreeList;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

import static org.junit.Assert.assertTrue;

/**
 * @author Daniel Terranova <mailto:daniel.terranova@so4it.com>
 * @since 0.8.0
 */
public class ArrayUtilTest {

    @Test
    public void testJoinJoinsMultipleArraysInTheCorrectOrder() {
        Integer[] array1 = new Integer[] {0, 1, 2};
        Integer[] array2 = new Integer[] {3, 4, 5, 6};
        Integer[] array3 = new Integer[] {7, 8, 9};

        Integer[] result = ArrayUtil.join(Integer.class, array1, array2, array3);

        Assert.assertNotNull(result);
        Assert.assertEquals(10, result.length);

        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(new Integer(i), result[i]);
        }
    }

    @Test
    public void testJoinCanHandleSingleNullInput() {
        Integer[] array1 = new Integer[] {0, 1, 2};
        Integer[] array2 = new Integer[] {3, 4, 5, 6};

        Integer[] result = ArrayUtil.join(Integer.class, array1, null, array2);

        Assert.assertNotNull(result);
        Assert.assertEquals(array1.length + array2.length, result.length);
    }

    @Test
    public void testJoinReturnsEmptyArrayWhenAllInputIsNull() {
        Integer[] result = ArrayUtil.join(Integer.class, null, null);

        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testCase() {
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

//    @Test
//    public void testInvertions() {
//        int[] input = { 4, 2, 9, 6, 23, 12, 34, 35, 1 };
//
//        for (Pair<Integer, Integer> pair : ArrayUtil.doInvertions(input)) {
//            System.out.println("pair = " + pair);
//        }
//
//    }

    @Test
    public void name() {
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
    public void testNavigableSet() {
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
    public void testDuplicateNumber() {
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
    public  void testFibs() {
        System.out.println("ArrayUtil.fib(10) = " + Arrays.toString(ArrayUtil.fib(10)));
    }

    @Test
    public void testRotate_five_step() {
        int[] arr = {1,2,3,4,5};
        ArrayUtil.rotate(arr,5);
        assertArrayElementEquals(new int[] { 1,2,3,4,5 }, arr);
    }

    @Test
    public void testRotate_() {


        System.out.println(Arrays.toString(CardTrickUtils.generate(4)));
        System.out.println(Arrays.toString(CardTrickUtils.generate(5)));
        System.out.println(Arrays.toString(CardTrickUtils.generate(6)));
        //assertArrayElementEquals(new int[] { 1,2,3,4,5 }, arr);
    }

    public static  int[] add2BeginningOfArray(int[] elements, int element)
    {
        int[] newArray = Arrays.copyOf(elements, elements.length + 1);
        newArray[0] = element;
        System.arraycopy(elements, 0, newArray, 1, elements.length);

        return newArray;
    }

    @Test
    public void testRotate_one_step() {
        int[] arr = { 1, 2, 3, 4, 5};
        ArrayUtil.rotate(arr,1);
        assertArrayElementEquals(new int[] {5,1,2,3,4}, arr);
    }

    @Test
    public void testRotate_two_step() {
        int[] arr = { 1,2,3,4, 5};
        ArrayUtil.rotate(arr,2);
        assertArrayElementEquals(new int[] {4,5,1,2,3}, arr);
    }

    @Test
    public void test2() {
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
    public void namess() {
        int[] arr = { 1 ,1};
        Arrays.sort(arr);
        for (int i = 0; i < arr.length-1; i++) {
            if (arr[i]-arr[i+1] == 0)
                System.out.println("i = " + arr[i]);
        }
    }

    @Test
    public void namezz() {
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


    @Test
    public void generateData() {

        List<String> data = null;
        try {
            data = Files.lines(Paths.get("/home/daniel/wlist")).collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> cpy = new ArrayList<>(data);
        Map<Character, List<String>> collect = cpy.stream().collect(Collectors.groupingBy(s -> s.charAt(0),
                TreeMap::new,
                new Collector<String, TreeSet<String>, List<String>>() {
                    @Override
                    public Supplier<TreeSet<String>> supplier() {
                        return TreeSet::new;
                    }

                    @Override
                    public BiConsumer<TreeSet<String>, String> accumulator() {
                        return TreeSet::add;
                    }

                    @Override
                    public BinaryOperator<TreeSet<String>> combiner() {
                        return (strings, strings2) -> {
                            strings.addAll(strings2);
                            return strings;
                        };
                    }

                    @Override
                    public Function<TreeSet<String>, List<String>> finisher() {
                        return ArrayList::new;
                    }

                    @Override
                    public Set<Characteristics> characteristics() {
                        return EnumSet.of(Characteristics.UNORDERED);
                    }
                }));

        List<String> collect1 = collect.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        assertTrue(Ordering.natural().isOrdered(collect1));


    }

    @Test
    public void plainOld() {
        List<String> data = null;
        try {
            data = Files.lines(Paths.get("/home/daniel/wlist")).collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }



        Map<Character, SortedSet<String>> map = new TreeMap<>();

            for (String s : data) {
                char c = s.charAt(0);
                map.compute(c, (character, strings) -> {
                    SortedSet<String> list = new TreeSet<>();
                    if(strings != null) {
                        strings.add(s);
                        list = strings;
                    }
                    return list;
                });
            }
            Map<Character, List<String>> map2 = new TreeMap<>();

        for (Map.Entry<Character, SortedSet<String>> entry : map.entrySet()) {
            map2.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }


    }

    private Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private void assertArrayElementEquals(int[] expected, int[] actual) {
        if (expected.length != actual.length)
            Assert.fail(String.format("length differs: expected: %s, acutal",expected.length, actual.length ));

        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i])
                Assert.fail(String.format("element differs: expected: %s, acutal",expected[i], actual[i] ));
        }
    }
}
