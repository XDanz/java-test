package com.terraware;


public class Test {

    static class A {};
    private static class B extends A {};
    interface I {};

    public static void main(String[] args) {

        A a = new A();
        B b = new B();

        //
        // instanceof
        //

//        if ( B instanceof A ) {} // Illegal
//        if ( B instanceof a ) {} // Illegal
//        if ( b instanceof a ) {} // Illegal
//        if ( b instanceof A ) {} // OK
//        if ( b instanceof I ) {} // OK
//
//        if ( null instanceof A ) {} // OK
//        if ( b instanceof null ) {} // Illegal
//
//        if ( b instanceof b.getClass() ) {} // Illegal
//
//        if ( b instanceof double ) {} // Illegal
//        if ( b instanceof Double ) {} // Illegal
//        if ( b instanceof double.class ) {} // Illegal
//        if ( b instanceof Double.class ) {} // Illegal

        //
        // isAssignableFrom()
        //

        // All OK 
        if ( A.class.isAssignableFrom(B.class) ) {
            System.out.println("A.class.isAssignableFrom(B.class)");
        }

        if ( A.class.isInstance(B.class) ) {
            System.out.println("A.class.isAssignableFrom(B.class)");
        }

        if ( b.getClass().isInstance(A.class) ) {
            System.out.println("b.getClass().isInstance(A.class)");
        }

        if ( a.getClass().isInstance(A.class) ) {
            System.out.println("a.getClass().isInstance(A.class)");
        }

        if ( A.class.isInstance(b) ) {
            System.out.println("A.class.isInstance(b)");
        }


        if ( A.class.isAssignableFrom(b.getClass()) ) {
            System.out.println("A.class.isAssignableFrom(b.getClass()");
        }

//        if ( A.class.isInstance(b) ) {
//            System.out.println("A.class.isInstance(b)");
//        }
//
//
//        if ( A.class.isAssignableFrom(b.getClass()) ) {
//            System.out.println("A.class.isAssignableFrom(b.getClass()");
//        }
//        if ( a.getClass().isAssignableFrom(B.class) ) {}
//        if ( a.getClass().isAssignableFrom(b.getClass()) ) {}
//
//        // All OK
//        if ( double.class.isAssignableFrom(double.class) ) {}
//        if ( Double.class.isAssignableFrom(Double.class) ) {}
//        if ( double.class.isAssignableFrom(B.class) ) {}
//        if ( Double.class.isAssignableFrom(b.getClass()) ) {}
//
//        // All OK
//        if ( I.class.isAssignableFrom(double.class) ) {}
//        if ( I.class.isAssignableFrom(Double.class) ) {}
//
//        Class c = null;

//        // Throws NullPointerException at runtime
//        if ( A.class.isAssignableFrom(c) ) {}
//
//        //
//        // isInstance()
//        //
//
//        b = null;
//
//        // All OK
//        if ( A.class.isInstance(b) ) {}
//        if ( a.getClass().isInstance(b) ) {}
//        if ( double.class.isInstance(b) ) {}
//        if ( Double.class.isInstance(b) ) {}
//        if ( I.class.isInstance(b) ) {}
//
//         // Throws NullPointerException at runtime
//         if ( c.isInstance(a) ) {}

    }

}