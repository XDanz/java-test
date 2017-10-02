package com.terraware;

import org.junit.jupiter.api.Test;

/**
 * @author Daniel Terranova <mailto:daniel.terranova@so4it.com>
 * @since 0.8.0
 */
public class SimpleTest {

    @Test
    public void dummy() {
        Simple s = new Simple();
        System.out.println("s = " + s);
    }

    @Test
    public void dummy2() {

        int price = 6;
        switch (price) {
            case 2: System.out.println("It is: 2");
            case 5: System.out.println("It is: 5");
            case 9: System.out.println("It is: 9");
            default: System.out.println("It is: default");
        }
    }



}
