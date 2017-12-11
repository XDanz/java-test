package com.terraware;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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
}
