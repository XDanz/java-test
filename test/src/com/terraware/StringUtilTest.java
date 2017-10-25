package com.terraware;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Daniel Terranova <mailto:daniel.terranova@so4it.com>
 * @since 0.8.0
 */
public class StringUtilTest {

    @Test
    void testWC() {
        System.out.println("WC = " + StringUtil.wordCount("manchester united is also known as red devil"));
    }

    @Test
    void testValidate() {
        assertTrue(StringUtil.validate("allanballan", "banan"));
        assertTrue(StringUtil.validate("allanballan", "llll"));
    }

    @Test
    void testReverseString() {
        String s = "Daniel";
        Assertions.assertEquals("leinaD", StringUtil.reverseString(s));
    }

    @Test
    void name() {
        ConcurrentSkipListMap<String, String> map = new ConcurrentSkipListMap<>();
        map.put("Daniel", "Terranova");
        map.put("Lena", "Andersson");
        map.put("Moa", "Andersson");
        map.put("Lea", "Andersson");

        for (Map.Entry<String, String> entry : map.entrySet()) {

            System.out.println("entry = " + entry);
            if ("Daniel".equals(entry.getKey())) {
                System.out.println("(B)size = " + map.size());
                map.remove(entry.getKey());
                System.out.println("(A)size = " + map.size());
            }

        }
        System.out.println("(A2)size = " + map.size());
        System.out.println(" -- After -- ");
        for (Map.Entry<String, String> entry : map.entrySet()) {

            System.out.println("entry = " + entry);
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {

            map.remove(entry.getKey());
            System.out.println("\tsize = " + map.size());

        }

    }
}
