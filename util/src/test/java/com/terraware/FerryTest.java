package com.terraware;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.Assert.assertArrayEquals;

public class FerryTest {

    @ParameterizedTest
    @MethodSource("provideCapacity")
    void cargo_test(int cap, int[] expected) {
        FerryProblem.Evt[] evts = new FerryProblem.Evt[]{
            new FerryProblem.Evt(5, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(6, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(7, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(8, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(9, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(10, FerryProblem.Dir.RIGHT),
        };
        int[] output = FerryProblem.trans2(evts, cap, 10);
        assertArrayEquals(expected, output);
    }

    private static Stream<Arguments> provideCapacity() {
        return Stream.of(
            Arguments.of(1, new int[]{25, 45, 65, 85, 105,125}),
            Arguments.of(2, new int[]{25, 25, 45, 45, 65, 65}),
            Arguments.of(3, new int[]{25, 25, 25, 45, 45, 45}),
            Arguments.of(4, new int[]{25, 25, 25, 25, 45, 45}),
            Arguments.of(5, new int[]{25, 25, 25, 25, 25, 45}),
            Arguments.of(6, new int[]{25, 25, 25, 25, 25, 25})
        );
    }

    @ParameterizedTest
    @MethodSource("provideCapacity2")
    void cargo_test2(int cap, int[] expected) {
        FerryProblem.Evt[] evts = new FerryProblem.Evt[]{
            new FerryProblem.Evt(5, FerryProblem.Dir.LEFT),
            new FerryProblem.Evt(6, FerryProblem.Dir.LEFT),
            new FerryProblem.Evt(7, FerryProblem.Dir.LEFT),
            new FerryProblem.Evt(8, FerryProblem.Dir.LEFT),
            new FerryProblem.Evt(9, FerryProblem.Dir.LEFT),
            new FerryProblem.Evt(10, FerryProblem.Dir.LEFT),
        };
        int[] output = FerryProblem.trans2(evts, cap, 10);
        assertArrayEquals(expected, output);
    }

    private static Stream<Arguments> provideCapacity2() {
        return Stream.of(
            Arguments.of(1, new int[]{15, 35, 55, 75, 95, 115}),
            Arguments.of(2, new int[]{15, 35, 35, 55, 55, 75}),
            Arguments.of(3, new int[]{15, 35, 35, 35, 55, 55}),
            Arguments.of(4, new int[]{15, 35, 35, 35, 35, 55}),
            Arguments.of(5, new int[]{15, 35, 35, 35, 35, 35}),
            Arguments.of(6, new int[]{15, 35, 35, 35, 35, 35})
        );
    }

    @Test
    void test_simple__0() {
        FerryProblem.Evt[] evts = new FerryProblem.Evt[] {
            new FerryProblem.Evt(5, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(6, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(7, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(8, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(9, FerryProblem.Dir.RIGHT),
        };
        int[] output = FerryProblem.trans2(evts, 1, 10);
        int[] exp = {25,45,65,85,105};
        assertArrayEquals(exp, output);
    }

    @Test
    void test_simple_0() {
        FerryProblem.Evt[] evts = new FerryProblem.Evt[]
            {
            new FerryProblem.Evt(0, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(40, FerryProblem.Dir.LEFT),
        };
        int[] output = FerryProblem.trans2(evts, 2, 10);
        int[] exp = {20, 50};
        assertArrayEquals(exp, output);
    }

    @Test
    void test_simple_1() {
        FerryProblem.Evt[] evts = new FerryProblem.Evt[]{
            new FerryProblem.Evt(0, FerryProblem.Dir.LEFT),
            new FerryProblem.Evt(40, FerryProblem.Dir.RIGHT),
        };
        int[] output = FerryProblem.trans2(evts, 2, 10);
        int[] exp = {10, 50};
        assertArrayEquals(exp, output);
    }

    @Test
    void test_simple_2() {
        FerryProblem.Evt[] evts = new FerryProblem.Evt[]{
            new FerryProblem.Evt(0, FerryProblem.Dir.LEFT),
            new FerryProblem.Evt(10, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(15, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(20, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(25, FerryProblem.Dir.RIGHT),
        };
        int[] output = FerryProblem.trans2(evts, 2, 10);
        int[] exp = {10, 20, 40, 40, 60};
        assertArrayEquals(exp, output);
    }

    @Test
    public void test_ferry_case0() {
        FerryProblem.Evt[] evts = new FerryProblem.Evt[]{
            new FerryProblem.Evt(10, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(25, FerryProblem.Dir.LEFT),
            new FerryProblem.Evt(40, FerryProblem.Dir.LEFT),
        };
        int[] output = FerryProblem.trans2(evts, 2, 10);
        int[] exp = {30, 40, 60};
        assertArrayEquals(exp, output);
    }

    @Test
    void test0() {
        FerryProblem.Evt[] evts = new FerryProblem.Evt[]{
            new FerryProblem.Evt(10, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(10, FerryProblem.Dir.LEFT)
        };
        int[] output = FerryProblem.trans2(evts, 2, 10);
        int[] exp = {30, 20};
        assertArrayEquals(exp, output);
    }

    @Test
    void test1() {
        FerryProblem.Evt[] evts = new FerryProblem.Evt[]{
            new FerryProblem.Evt(10, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(10, FerryProblem.Dir.LEFT),
            new FerryProblem.Evt(10, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(10, FerryProblem.Dir.LEFT)
        };
        int[] output = FerryProblem.trans2(evts, 2, 10);
        int[] exp = {30, 20, 30, 20};
        assertArrayEquals(exp, output);
    }

    @Test
    public void test_ferry_case1() {
        FerryProblem.Evt[] evts = new FerryProblem.Evt[]{
            new FerryProblem.Evt(0, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(10, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(25, FerryProblem.Dir.LEFT),
            new FerryProblem.Evt(40, FerryProblem.Dir.LEFT),
        };
        int[] output = FerryProblem.trans2(evts, 2, 10);
        int[] exp = {20, 20, 35, 60};
        assertArrayEquals(exp, output);
    }

    @Test
    public void test_ferry_case11() {
        FerryProblem.Evt[] evts = new FerryProblem.Evt[]{
            new FerryProblem.Evt(0, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(10, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(25, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(40, FerryProblem.Dir.RIGHT),
        };
        int[] output = FerryProblem.trans2(evts, 2, 10);
        int[] exp = {20, 20, 45, 65};
        assertArrayEquals(exp, output);
    }

    @Test
    public void test_ferry_case2() {
        FerryProblem.Evt[] evts = new FerryProblem.Evt[]{
            new FerryProblem.Evt(0, FerryProblem.Dir.LEFT),
            new FerryProblem.Evt(10, FerryProblem.Dir.LEFT),
            new FerryProblem.Evt(20, FerryProblem.Dir.LEFT),
            new FerryProblem.Evt(30, FerryProblem.Dir.LEFT),
            new FerryProblem.Evt(40, FerryProblem.Dir.LEFT),
            new FerryProblem.Evt(50, FerryProblem.Dir.LEFT),
            new FerryProblem.Evt(60, FerryProblem.Dir.LEFT),
            new FerryProblem.Evt(70, FerryProblem.Dir.LEFT),
            new FerryProblem.Evt(80, FerryProblem.Dir.LEFT),
            new FerryProblem.Evt(90, FerryProblem.Dir.LEFT),
        };
        int[] output = FerryProblem.trans2(evts, 2, 10);
        int[] exp = {10, 30, 30, 50, 50, 70, 70, 90, 90, 110};
        assertArrayEquals(exp, output);
    }

    @Test
    public void test_ferry_case3() {
        FerryProblem.Evt[] evts = new FerryProblem.Evt[]{
            new FerryProblem.Evt(0, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(0, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(10, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(40, FerryProblem.Dir.LEFT),

        };
        int[] output = FerryProblem.trans2(evts, 2, 10);
        int[] exp = {20, 20, 40, 50};
        assertArrayEquals(exp, output);
    }

    @Test
    public void test_ferry_case4() {
        FerryProblem.Evt[] evts = new FerryProblem.Evt[]{
            new FerryProblem.Evt(0, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(0, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(10, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(40, FerryProblem.Dir.LEFT),

        };
        int[] output = FerryProblem.trans2(evts, 3, 10);
        int[] exp = {20, 20, 20, 50};
        assertArrayEquals(exp, output);
    }

    @Test
    public void test_ferry_case5() {
        FerryProblem.Evt[] evts = new FerryProblem.Evt[]{
            new FerryProblem.Evt(0, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(0, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(0, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(40, FerryProblem.Dir.LEFT),

        };
        int[] output = FerryProblem.trans2(evts, 2, 10);
        int[] exp = {20, 20, 40, 50};
        assertArrayEquals(exp, output);
    }

    @Test
    public void test_ferry_case6() {
        FerryProblem.Evt[] evts = new FerryProblem.Evt[]{
            new FerryProblem.Evt(0, FerryProblem.Dir.LEFT),
            new FerryProblem.Evt(10, FerryProblem.Dir.LEFT),
            new FerryProblem.Evt(20, FerryProblem.Dir.LEFT),
            new FerryProblem.Evt(30, FerryProblem.Dir.LEFT),

        };
        int[] output = FerryProblem.trans2(evts, 1, 10);
        int[] exp = {10, 30, 50, 70};
        assertArrayEquals(exp, output);
    }

    @Test
    public void test_ferry_case7() {
        FerryProblem.Evt[] evts = new FerryProblem.Evt[]{
            new FerryProblem.Evt(0, FerryProblem.Dir.LEFT),
            new FerryProblem.Evt(10, FerryProblem.Dir.LEFT),
            new FerryProblem.Evt(20, FerryProblem.Dir.LEFT),
            new FerryProblem.Evt(30, FerryProblem.Dir.LEFT),

        };
        int[] output = FerryProblem.trans2(evts, 3, 10);
        int[] exp = {10, 30, 30, 50};
        assertArrayEquals(exp, output);

    }

    @Test
    public void test_ferry_case8() {
        FerryProblem.Evt[] evts = new FerryProblem.Evt[]{
            new FerryProblem.Evt(10, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(50, FerryProblem.Dir.RIGHT),
            new FerryProblem.Evt(70, FerryProblem.Dir.RIGHT)
        };
        int[] output = FerryProblem.trans2(evts, 3, 10);
        int[] exp = {30, 70, 90};
        assertArrayEquals(exp, output);

    }
}
