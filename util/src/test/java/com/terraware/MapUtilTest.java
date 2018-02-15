package com.terraware;

import com.sun.corba.se.impl.presentation.rmi.IDLTypeException;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Daniel Terranova <mailto:daniel.terranova@so4it.com>
 * @since 0.8.0
 */
public class MapUtilTest {

    @Test
    public void testSortAscByValue() {
        Map<Integer,String> map = new HashMap<>();
        map.put(1,"Xaniel");
        map.put(2, "Daniel");
        map.put(3, "Anna");
        Map<Integer, String> sortAscByValue = MapUtil.sortAscByValue(map);

        // A,D,X
        Integer expectedKey = map.size();
        for (Map.Entry<Integer, String> entry : sortAscByValue.entrySet()) {
            Assert.assertEquals(expectedKey--, entry.getKey());
        }
    }

    @Test
    public void testSortDescByValue() {
        Map<Integer,String> map = new HashMap<>();
        map.put(1,"Xaniel");
        map.put(2, "Daniel");
        map.put(3, "Anna");
        Map<Integer, String> sortAscByValue = MapUtil.sortDescByValue(map);

        // A,D,X
        Integer expectedKey = 1;
        for (Map.Entry<Integer, String> entry : sortAscByValue.entrySet()) {
            Assert.assertEquals(expectedKey++, entry.getKey());
        }
    }

    @Test
    public void testSortDescByValue2() {
        Map<Integer,String> map = new HashMap<>();
        map.put(1,"Xaniel");
        map.put(2, "Daniel");
        map.put(3, "Anna");

        map.keySet().stream().sorted(Comparator.reverseOrder()).forEach(System.out::println);
    }

    class Container {
        String value;
        List<String> children;
        Container(String value) {
            this.value = value;
            children = new ArrayList<>();
        }

        Container addChild(String pChild) {
            children.add(pChild);
            return this;
        }

        @Override
        public String toString() {
            return "Container{" +
                    "value='" + value + '\'' +
                    ", children=" + children +
                    '}';
        }
    }

    @Test
    public void test() {
        Map<Integer,Container> map = new HashMap<>();
        map.put(1, new Container("1aniel"));
        map.put(2, new Container("Xaniel"));
        map.put(3, new Container("Daniel"));
        map.put(4, new Container("Anna"));

        Container container1 = map.values().stream()
                .reduce((container, container2) -> container.addChild(container2.value))
                .orElseThrow(IllegalArgumentException::new);
        System.out.println("container1 = " + container1);

        Collector.of(ArrayDeque::new,
                ArrayDeque::addFirst,
                (d1, d2) -> { d2.addAll(d1); return d2; });
    }

    @Test
    public void test2() {
        Map<String,List<Container>> map = new HashMap<>();
        map.put("aa", Arrays.asList(new Container("AAtree"),new Container("AAtwo"),new Container("AAone") ));
        map.put("bb", Arrays.asList(new Container("BBtree"),new Container("BBtwo"),new Container("BBone") ));
        map.put("cc", Arrays.asList(new Container("CCtree"),new Container("CCtwo"),new Container("CCone") ));
        map.put("dd", Arrays.asList(new Container("DDtree"),new Container("DDtwo"),new Container("DDone") ));


        Function<Map.Entry<String,List<Container>>, Map.Entry<String,List<Container>>> f = o -> { Collections.reverse(o.getValue()); return o;};

        List<Container> collect = map.entrySet().stream().map(f)
                .map(o -> o.getValue().stream().reduce((container, container2) -> container.addChild(container2.value)))
                .filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
        System.out.println("collect = " + collect);

    }
}
