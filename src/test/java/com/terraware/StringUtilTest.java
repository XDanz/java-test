package com.terraware;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import static org.junit.Assert.assertEquals;


/**
 * @author Daniel Terranova <mailto:daniel.terranova@so4it.com>
 * @since 0.8.0
 */
public class StringUtilTest {

    @Test
    public void testWC() {
        System.out.println("WC = " + StringUtil.wordCount("manchester united is also known as red devil"));
    }

    @Test
    public void testValidate() {
        Assert.assertTrue(StringUtil.validate("allanballan", "banan"));
        Assert.assertTrue(StringUtil.validate("allanballan", "llll"));
    }

    @Test
    public void testReverseString() {
        String s = "Daniel";
        assertEquals("leinaD", StringUtil.reverseString(s));
    }

    @Test
    public void name() {
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

    @Test
    public void testMoa() {
        String namn = "Moa Andersson";


            System.out.println(namn);


    }
}
