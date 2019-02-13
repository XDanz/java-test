package com.terraware;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableSet;

public final class SetUtil {

    private SetUtil() {
    }

    /**
     * Converts an array to a set. An empty set will be returned if the array is null. If array contains duplicates
     * only the last duplicate in the array will remain.
     *
     * @param <T> .
     * @param array array that will be converted to a set
     * @return array converted to a set. Will never return null.
     */
    public static <T> Set<T> toSet(T[] array) {
        if (array == null) {
            return new HashSet<>(0);
        }
        return Arrays.stream(array).collect(Collectors.toSet());
    }

    public static <T> Set<T> toSet(Iterator<T> iterator) {
        if (iterator == null) {
            return new HashSet<>(0);
        }
        HashSet<T> set = new HashSet<>();
        iterator.forEachRemaining(set::add);
        return set;
    }

    public static <T> Set<T> of(T... vals) {
        HashSet<T> set = new HashSet<>();
        for (T tVal : vals) {
            set.add(tVal);
        }
return set;
    }

    /**
     * Converts an array to a immutable set. This is the same as <code>immutableSet(toSet(array))</code>.
     *
     * @param array array that will be converted to a immutable set
     * @return array converted to a immutable set. Will never return null.
     */
    public static <T> Set<T> toImmutableSet(T[] array) {
        return immutableSet(toSet(array));
    }

    /**
     * Creates a copy of given set and wraps the copy in a unmodifiable set.
     * Changes made to parameter set after this method will not be reflected in
     * returned set.
     *
     * @param <T> .
     * @param set original set
     * @return copy of original set wrapped in a unmodifiable set, if set is null an empty set is returned.
     */
    public static <T> Set<T> immutableSet(Set<T> set) {
        if (set == null) {
            return unmodifiableSet(new HashSet<>(0));
        } else {
            return unmodifiableSet(new HashSet<>(set));
        }
    }

    /**
     * Converts an iterable to a set. An empty set will be returned if the iterable is null.
     *
     * @param <T> .
     * @param iterable iterable that will be converted to a set.
     *
     * @return iterable converted to a set. Will never return null.
     */
    public static <T> Set<T> toSet(Iterable<T> iterable) {
        if (iterable == null)
            return new HashSet<>();

        return toSet(iterable.iterator());
    }

    public static <T> Set<T> copy(Set<T> entities) {
        if (HashSet.class.isInstance(entities)) {
            return new HashSet<>(entities);
        } else if (TreeSet.class.isInstance(entities)) {
            return new TreeSet<>(entities);
        }
        throw new RuntimeException(String.format("Copying a set of type %s not implemented", entities.getClass().getSimpleName()));
    }

    public static <T> Set<T> cloneAndAdd(Set<T> entities, T entity) {
        Set<T> copiedEntities = cloneAndRemove(entities, entity);
        copiedEntities.add(entity);
        return copiedEntities;
    }

    public static <T> Set<T> cloneAndRemove(Set<T> entities, T entity) {
        Set<T> copiedEntities = copy(entities);
        copiedEntities.remove(entity);
        return copiedEntities;
    }
}
