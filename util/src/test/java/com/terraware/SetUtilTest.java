package com.terraware;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;


public class SetUtilTest {

    @Test
    public void toSet_Set_ReturnsEmptySetIfArrayIsNull() {
        assertTrue(SetUtil.toSet((Object[]) null).isEmpty());
    }

    @Test
    public void toSet_Set_ReturnsExpectedSet() {
        Set<Integer> set = SetUtil.toSet(new Integer[]{1, 2, 2, 3});
        assertEquals(3, set.size());
        assertTrue(set.contains(1));
        assertTrue(set.contains(2));
        assertTrue(set.contains(3));
    }

    @Test
    public void toSet_Iterable_ReturnsEmptySetIfArrayIsNull() {
        assertTrue(SetUtil.toSet((Iterable<?>) null).isEmpty());
    }

    @Test
    public void toSet_Iterable_ReturnsExpectedSet() {
        Iterable<Integer> iterable = new HashSet<>(Arrays.asList(1, 2, 2, 3));
        Set<Integer> set = SetUtil.toSet(iterable);
        assertEquals(3, set.size());
        assertTrue(set.contains(1));
        assertTrue(set.contains(2));
        assertTrue(set.contains(3));
    }

    @Test
    public void immutableSetIsNotAffectedIfOriginalSetIsChanged() {
        Set<Integer> original = SetUtil.toSet(new Integer[]{1});
        Set<Integer> immutable = SetUtil.immutableSet(original);
        assertEquals(1, immutable.size());
        assertTrue(immutable.contains(1));
        original.add(2);
        assertEquals(1, immutable.size());
    }

    @Test
    public void immutableSetThrowsUnsupportedOperationExceptionWhenCallingModifiableMethods() {
        final Set<Integer> set = SetUtil.immutableSet(SetUtil.toSet(new Integer[]{1}));
        throwsUOE(() -> set.add(1), "add");
        throwsUOE(() -> set.addAll(Collections.<Integer>emptySet()), "addAll");
        throwsUOE(set::clear, "clear");
        throwsUOE(() -> set.remove(1), "remove");
        throwsUOE(() -> set.removeAll(Collections.<Integer>emptySet()), "removeAll");
        throwsUOE(() -> set.retainAll(Collections.<Integer>emptySet()), "retainAll");
    }

    private void throwsUOE(T t, String method) {
        try {
            t.call();
        } catch (UnsupportedOperationException e) {
            return;
        }
        fail(method + " does not throw UnsupportedOperationException");
    }

    private static interface T {
        void call();
    }

}
