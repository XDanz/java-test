package com.terraware;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Daniel Terranova <mailto:daniel.terranova@so4it.com>
 * @since 0.8.0
 */
public class StringUtil {


    static boolean validate(String longString, String word) {
        SortedSet<Integer> pos = new TreeSet<>();
        for (char c : word.toCharArray()) {
            if(!validate(longString, c, pos))
                return false;
        }
        System.out.println("pos = " + pos);
        return true;
    }

    private static boolean validate(String longString, char c, Set<Integer> pos) {
        char[] chars = longString.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (!pos.contains(i) && c == chars[i]) {
                pos.add(i);
                return true;
            }
        }
        return false;
    }

    public static String reverseString(String str) {
        String reverse = "";
        if (str.length() == 1) {
            return str;
        } else {
            reverse += str.charAt(str.length()-1)
                    + reverseString(str.substring(0,str.length()-1));
            return reverse;
        }
    }

    static int wordCount(String w) {
        char[] arr = w.toCharArray();
        boolean b = false;
        int c = 0;
        for (int i = 0; i < arr.length; i++) {
            if(arr[i] == ' ') {
                if (b) {
                    c++;
                    b = false;
                }
            } else {
                b = true;
                if (i == arr.length - 1)
                    c++;
            }
        }
        return c;
    }
}
