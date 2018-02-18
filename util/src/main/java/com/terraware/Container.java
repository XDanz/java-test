package com.terraware;

import java.util.ArrayList;
import java.util.List;

public class Container<T> {
    private T value;
    private List<T> children;

    Container(T value) {
        this.value = value;
        children = new ArrayList<>();
    }

    Container addChild(T pChild) {
        children.add(pChild);
        return this;
    }

    public T getParent() {
        return value;
    }

    @Override
    public String toString() {
        return "Container{" +
                "value='" + value + '\'' +
                ", children=" + children +
                '}';
    }
}
