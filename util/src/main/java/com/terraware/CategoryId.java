package com.terraware;

import java.util.Objects;

public class CategoryId {
    public static final CategoryId BRAND = new CategoryId("brand");

    private final String id;

    private CategoryId(final String id) {
        this.id = Objects.requireNonNull(id);
    }

    private CategoryId(final Integer id) {
        this.id = String.valueOf(Objects.requireNonNull(id));
    }

    public static CategoryId of(final String id) {
        return new CategoryId(id);
    }

    public static CategoryId of(final Integer id) {
        return new CategoryId(id);
    }

    public String id() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CategoryId attributeId = (CategoryId) o;
        return Objects.equals(id, attributeId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id;
    }
}
