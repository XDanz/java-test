package com.terraware;

import junit.extensions.TestSetup;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;

public class RuleTest {

    @ClassRule
    public static ClassRule1 classRule1 = new ClassRule1();

    @Before
    public void setUp() throws Exception {
        System.out.println("TestSetup = " );
    }

    @Rule
    public MethodRule1 methodRule1 = new MethodRule1();

    @Test
    public void name1() {
        System.out.println("testname1 ");
    }

    @Test
    public void name2() {
        System.out.println("testname2 ");
    }
}
