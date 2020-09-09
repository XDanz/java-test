//package com.terraware;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.StringJoiner;

public class Help2 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int nCases = sc.nextInt();
        sc.nextLine();
        for (int i = 0; i < nCases; i++) {
            String firstLine = sc.nextLine();
            String secondLine = sc.nextLine();

            String[] a = firstLine.split(" ");
            String[] b = secondLine.split(" ");
            String[] output = help3(a, b);

            if (output.length == 0) {
                System.out.println("-");
            } else {
                for (int j = 0; j < output.length; j++) {
                    System.out.print(output[j]);
                    if (j == (output.length - 1)) {
                        System.out.println();
                    } else {
                        System.out.print(" ");
                    }
                }
            }
        }

    }

    static class Elem {
        private String value;
        private final boolean isPlaceHolder;

        public Elem(final String value, final boolean isPlaceHolder) {
            this.value = value;
            this.isPlaceHolder = isPlaceHolder;
        }

        public String getValue() {
            return value;
        }

        public boolean isPlaceHolder() {
            return isPlaceHolder;
        }

        public void addValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Elem.class.getSimpleName() + "[", "]")
                .add("value='" + value + "'")
                .add("isPlaceHolder=" + isPlaceHolder)
                .toString();
        }
    }

    public static String[] help3(String[] a, String[] b) {
        if (a.length != b.length) {
            return new String[0];
        }

        Elem[] ea = createElements(a);
        Elem[] eb = createElements(b);

        if (ea == null || eb == null) {
            return new String[0];
        }

        String[] out = new String[ea.length];

        Set<Integer> unResolvedIndexes = new HashSet<>();
        for (int i = 0; i < out.length; i++) {
            unResolvedIndexes.add(i);
        }

        boolean stop = false;
        while (!stop) {
            Set<Integer> unresolvedTmp = resolve(unResolvedIndexes, ea, eb, out);
            if (unresolvedTmp == null) {
                out = new String[0];
                return out;
            } else if (unresolvedTmp.size() < unResolvedIndexes.size()) {
                unResolvedIndexes = unresolvedTmp;
            } else {
                stop = true;
            }
        }

        for (Integer index : unResolvedIndexes) {
            out[index] = "brzzan";
        }

        return out;
    }

    public static Set<Integer> resolve(Set<Integer> unresolvedSet, Elem[] ea, Elem[] eb, String[] out) {
        Set<Integer> unresolved = new HashSet<>();
        for (Integer i : unresolvedSet) {
            int index = i;
            String str = eval(ea[index], eb[index]);
            // if str is null it means 2 things
            // Either both are placeholders and could be determined later
            // so add to set. Or
            // both are non placeholders and string don't match so abort directly!
            if (str != null) {
                out[index] = str;
            } else if (ea[index].isPlaceHolder() && eb[index].isPlaceHolder()) {
                if (ea[index].getValue() == null && eb[index].getValue() == null) {
                    unresolved.add(index);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
        return unresolved;

    }

    /**
     * Determine a String by comparing a and b.
     * If a is placeholder and b is placeholder both
     * without value then return null.
     * <p>
     * If either a or b is a placeholder then set
     * the value of the non-placeholder to the placeholder
     * and return the value.
     * <p>
     * if none of the values is a placeholder then
     * compare and if match return the matched value or else
     * return null.
     *
     * @param a - Elem
     * @param b - Elem
     * @return null or string
     */
    public static String eval(final Elem a, final Elem b) {
        String val = null;

        // Placeholder a and b nothing could be determined
        // return immediately
        if (a.getValue() == null && b.getValue() == null) {
            return null;
        }

        if (a.getValue() == null && (b.getValue() != null)) {
            // Placeholder a is set to b value
            a.addValue(b.getValue());
            val = b.getValue();
        } else if (b.getValue() == null && (a.getValue() != null)) {
            // Placeholder b is set to a value
            b.addValue(a.getValue());
            val = a.getValue();
        } else if (a.getValue().equals(b.getValue())) {
            // values are the same return value
            val = a.getValue();
        }

        return val;

    }

    public static Elem[] createElements(String[] a) {
        Elem[] ea = new Elem[a.length];
        Map<String, Elem> ma = new HashMap<>();
        for (int i = 0; i < a.length; i++) {
            String s = a[i];

            if (!validate(a)) {
                return null;
            }

            Elem e;
            if (isPlaceHolder(s)) {
                if (!validatePlaceHolder(s))
                    return null;

                if (ma.containsKey(s)) {
                    e = ma.get(s);
                } else {
                    e = new Elem(null, true);
                    ma.put(s, e);
                }
            } else {
                if (!validateWord(s)) {
                    return null;
                }
                e = new Elem(s, false);
            }
            ea[i] = e;
        }
        return ea;
    }

    private static boolean validateWord(final String s) {
        return s.length() <= 16;
    }

    private static boolean validatePlaceHolder(final String s) {
        return validateChars(s);
    }

    private static boolean validate(final String[] a) {
        int sz = 0;
        for (String s : a) {
            if ((sz + s.length()) > 100) {
                return false;
            }
            sz += s.length();
        }
        return true;
    }

    private static boolean validateChars(final String s) {
        for (char c : s.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isPlaceHolder(String s) {
        return s.startsWith("<") && s.endsWith(">");
    }
}
