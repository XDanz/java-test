package com.terraware;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapUtil {

    public static <K, V extends Comparable<? super V>> Map<K, V> sortDescByValue( Map<K, V> map ) {
        Map<K, V> result;
        Stream<Map.Entry<K, V>> st = map.entrySet().stream();

        result = st.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));

        return result;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortAscByValue( Map<K, V> map ) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> st = map.entrySet().stream();

        st.sorted(Map.Entry.comparingByValue())
                .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));

        return result;
    }
}
