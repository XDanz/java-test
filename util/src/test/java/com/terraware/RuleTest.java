package com.terraware;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

public class RuleTest {

    @ClassRule
    public static ClassRule1 classRule1 = new ClassRule1();

    @Before
    public void setUp() throws Exception {
//        System.out.println("TestSetup = " );
    }

    @Rule
    public MethodRule1 methodRule1 = new MethodRule1();

    @Test
    public void name1() {
        System.out.println("testname1 ");
    }

    @Test
    public void name2() {

        int moa = 10;
        int lea;
        boolean santEllerFalskt;


        for (int i = 0; i < 10; i++) {
            if (moa < 100) {
                System.out.println(" Moa är mindre än hundra");
            } else {
                System.out.println(" Moa är inte mindre än hundra");
            }
            moa = moa + 10;
        }

    }
}
