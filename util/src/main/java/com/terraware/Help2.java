package com.terraware;

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

        Elem[] ea = elems(a);
        Elem[] eb = elems(b);

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

        for (Integer i : unResolvedIndexes) {
            String w;
            if ((w = findWord(ea)) != null || (w = findWord(eb)) != null) {
                out[i] = w;
            } else {
                out = new String[0];
            }
        }


        return out;
    }

    public static Set<Integer> resolve(Set<Integer> unresolvedSet, Elem[] ea, Elem[] eb, String[] out) {
        Set<Integer> unresolved = new HashSet<>();
        for (Integer i : unresolvedSet) {
            int index = i;
            String str = eval(ea[index], eb[index]);

            if (str != null) {
                out[index] = str;
            } else if (ea[index].isPlaceHolder() && eb[index].isPlaceHolder()) {
                unresolved.add(index);
            } else {
                return null;
            }
        }
        return unresolved;

    }

    private static String findWord(Elem[] elems) {
        for (Elem elem : elems) {
            if (!elem.isPlaceHolder())
                return elem.getValue();
        }
        return null;
    }

    private static String eval(final Elem a, final Elem b) {
        String val = null;

        if (a.getValue() == null && b.getValue() == null) {
            return null;
        }

        if (a.getValue() == null && (b.getValue() != null)) {
            a.addValue(b.getValue());
            val = b.getValue();
        }

        if (b.getValue() == null && (a.getValue() != null)) {
            b.addValue(a.getValue());
            val = a.getValue();
        }

        if (a.getValue().equals(b.getValue())) {
            val = a.getValue();
        }
        return val;

    }

    public static Elem[] elems(String[] a) {
        Elem[] ea = new Elem[a.length];
        Map<String, Elem> ma = new HashMap<>();
        for (int i = 0; i < a.length; i++) {
            String s = a[i];
            Elem e;
            if (isPlaceHolder(s)) {
                if (ma.containsKey(s)) {
                    e = ma.get(s);
                } else {
                    e = new Elem(null, true);
                    ma.put(s, e);
                }
            } else {
                e = new Elem(s, false);
            }
            ea[i] = e;
        }
        return ea;

    }

    public static boolean isPlaceHolder(String s) {
        return s.startsWith("<") && s.endsWith(">");
    }
}
