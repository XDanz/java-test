package com.terraware;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

import static java.lang.System.arraycopy;
import static java.util.Arrays.asList;

public class StackSet {
    final private Deque<EmptySet> deque = new ArrayDeque<>();

    enum OP {
        PUSH,
        DUP,
        ADD,
        UNION,
        INTERSECT;
        public static OP[] of(OP ...ops) {
            int len = ops.length;
            OP[] o = new OP[len];
            arraycopy(ops, 0, o, 0, len);
            return o;
        }

    }

    public static class EmptySet {
        private final Set<EmptySet> set;

        private EmptySet() {
            this.set = new LinkedHashSet<>();
        }

        public static EmptySet of() {
            return new EmptySet();
        }

        public static EmptySet of(EmptySet s) {
            return new EmptySet(s);
        }

        public static EmptySet of(EmptySet... s) {
            EmptySet emptySet = new EmptySet();
            Set<EmptySet> backedSet = emptySet.getSet();
            backedSet.addAll(asList(s));
            return emptySet;
        }

        public EmptySet duplicate() {
            EmptySet e = EmptySet.of();
            e.getSet().addAll(getSet());
            return e;
        }

        public int cardinality() {
            return set.size();
        }

        public EmptySet(EmptySet emptySet) {
            this.set = new LinkedHashSet<>();
            set.add(emptySet);
        }

        public void add(EmptySet s) {
            set.add(s);
        }

        public Set<EmptySet> getSet() {
            return set;
        }

        public EmptySet intersect(EmptySet emptySet) {
            EmptySet clone = EmptySet.of();
            clone.getSet().addAll(set);
            clone.getSet().retainAll(emptySet.getSet());
            return clone;
        }

        EmptySet union(EmptySet providedSet) {
            EmptySet union = new EmptySet();
            union.getSet().addAll(set);
            union.getSet().addAll(providedSet.getSet());
            return union;
        }


        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final EmptySet set1 = (EmptySet) o;
            return Objects.equals(set, set1.set);
        }

        @Override
        public int hashCode() {
            return Objects.hash(set);
        }

        /**
         * Returns a string representation of this collection.  The string
         * representation consists of a list of the collection's elements in the
         * order they are returned by its iterator, enclosed in square brackets
         * ({@code "{}]"}).  Adjacent elements are separated by the characters
         * {@code ", "} (comma and space).  Elements are converted to strings as
         * by {@link String#valueOf(Object)}.
         *
         * @return a string representation of this collection
         */
        public String toString() {
            Iterator<EmptySet> it = set.iterator();
            if (!it.hasNext())
                return "{}";

            StringBuilder sb = new StringBuilder();
            sb.append('{');
            for (; ; ) {
                EmptySet e = it.next();
                sb.append(e == this ? "(this Collection)" : e);
                if (!it.hasNext())
                    return sb.append('}').toString();
                sb.append(',').append(' ');
            }
        }
    }

    public int[] processOps(OP[] ops) {
        int[] card = new int[ops.length];
        for (int i = 0; i < ops.length; i++) {
            card[i] = processOp(ops[i]);
        }
        return card;

    }

    private int processOp(final OP op) {
        switch (op) {
            case ADD: {
                EmptySet top = deque.pop();
                EmptySet next = deque.pop();
                next.add(top);
                deque.push(next);
                return next.cardinality();
            }
            case PUSH: {
                EmptySet e = EmptySet.of();
                deque.push(e);
                return e.cardinality();
            }
            case UNION: {
                EmptySet top = deque.pop();
                EmptySet next = deque.pop();
                EmptySet union = top.union(next);
                deque.push(union);
                return union.cardinality();
            }
            case INTERSECT: {
                EmptySet top = deque.pop();
                EmptySet next = deque.pop();
                EmptySet intersect = top.intersect(next);
                deque.push(intersect);
                return intersect.cardinality();
            }
            case DUP: {
                EmptySet top = deque.pop();
                EmptySet dup = top.duplicate();
                deque.push(top);
                deque.push(dup);
                return top.cardinality();
            }
            default:
                return 0;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int nCases = sc.nextInt();
        for (int i = 0; i < nCases; i++) {
            int nOps = sc.nextInt();
            StackSet stackSet =
                new StackSet();
            OP[] ops = new OP[nOps];
            for (int j = 0; j < nOps; j++) {
                String op = sc.next();
                ops[j] = OP.valueOf(op);
            }

            int[] output = stackSet.processOps(ops);

            for (final int k : output) {
                System.out.println(k);
            }
            System.out.println("***");
        }
    }

}
