package com.terraware;

import java.util.Objects;

public class AttributeId {

    public static final AttributeId BRAND = new AttributeId("brand");

    private final String id;

    private AttributeId(final String id) {
        this.id = Objects.requireNonNull(id);
    }

    public static AttributeId of(final String id) {
        return new AttributeId(id);
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
        final AttributeId attributeId = (AttributeId) o;
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
