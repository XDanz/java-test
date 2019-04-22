package com.terraware.combination;

import java.util.ArrayList;
import java.util.List;

public class MultiTree<E> {

    protected class MultiTreeNode<T> {
        private ArrayList<T> data;
        private ArrayList<MultiTreeNode<T>> child;

        public MultiTreeNode(int degree) {
            this.data = new ArrayList<T>();
            for (int i = 0; i < degree-1; i++) {
                this.data.add(null);
            }
            this.child = new ArrayList<>();
            for (int i = 0; i < degree; i++) {
                this.child.add(null);
            }
        }

        public T getData(int i) {
            return this.data.get(i);
        }

        public MultiTreeNode<T> getChild(int i) {
            return this.child.get(i);
        }

        public void setData(int i, T newData) {
            this.data.set(i, newData);
        }

        public void setChild(int i, MultiTreeNode<T> newChild) {
            this.child.set(i, newChild);
        }
    }

    protected MultiTreeNode<E> root;
    protected int degree;

    /**
     * Constructs an empty multitree of the specified degree.
     *   @param degree the branching factor for the tree (assumes degree >= 2)
     */
    public MultiTree(int degree) {
        this.root = null;
        this.degree = degree;
    }

    /**
     * Adds a value to the multitree (in a random location).
     *   @param value the value to be added
     */
    public void add(E value) {
        this.root = this.add(this.root, value);
    }
    private MultiTreeNode<E> add(MultiTreeNode<E> current, E value) {
        if (current == null) {
            current = new MultiTreeNode<E>(this.degree);
            current.setData(0,  value);
            return current;
        }

        for (int i = 0; i < this.degree-1; i++) {
            if (current.getData(i) == null) {
                current.setData(i, value);
                return current;
            }

        }

        int randChoice = (int)(Math.random()*this.degree);
        current.setChild(randChoice, this.add(current.getChild(randChoice), value));
        return current;
    }

    /**
     * Converts the multitree into its string representation.
     *   @return the contents of the multitree as a string
     */
    public String toString() {
        if (this.root == null) {
            return "[]";
        }

        String recStr = this.toString(this.root);
        return "[" + recStr.substring(0, recStr.length()-1) + "]";
    }
    private String toString(MultiTreeNode<E> current) {
        if (current ==  null) {
            return "";
        }

        String str = "";
        for (int i = 0; i < this.degree-1; i++) {
            if (current.getChild(i) != null) {
                str += this.toString(current.getChild(i));
            }
            if (current.getData(i) != null) {
                str += current.getData(i) + ",";
            }
        }
        if (current.getChild(this.degree-1) != null) {
            str += this.toString(current.getChild(this.degree-1));
        }
        return str;
    }

    public static void main (String[] args) {

        List<Character> aToZ = new ArrayList<>();
        for (char c = 'a'; c <= 'z'; c++) {
            aToZ.add(c);
        }
        MultiTree<Character> multiTree = new MultiTree<>(aToZ.size()+1);
        for (Character character : aToZ) {
            multiTree.add(character);
        }

    }
}