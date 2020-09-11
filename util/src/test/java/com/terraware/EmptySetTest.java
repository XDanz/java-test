package com.terraware;


import com.terraware.StackSet.EmptySet;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static com.terraware.StackSet.EmptySet.of;
import static org.assertj.core.api.Assertions.assertThat;

public class EmptySetTest {

    @ParameterizedTest
    @MethodSource("provider")
    void test_cardinality(EmptySet set, int expected) {
        assertThat(set.cardinality()).isEqualTo(expected);
    }

    private static Stream<Arguments> provider() {
        return Stream.of(
            Arguments.of(of(), 0),
            Arguments.of(of(of()), 1),
            Arguments.of(of(of(), of()), 2),
            Arguments.of(of(of(), of(), of()), 3),
            Arguments.of(of(of(), of(), of(), of()), 4));
    }

    @Test
    void test_Equals() {
        assertThat(of()).isEqualTo(of());
        assertThat(of(of())).isEqualTo(of(of()));
        assertThat(of(of(), of())).isNotEqualTo(of(of()));
    }

    @Test
    void test_simple_add() {
        EmptySet e0 = of();
        e0.add(of());
        e0.add(of());
        assertThat(e0).isEqualTo(of(of(), of()));
    }

    @Test
    void test_simple_add1() {
        EmptySet e0 = of();
        e0.add(of(of()));
        e0.add(of(of()));
        assertThat(e0).isEqualTo(of(of(of()), of(of())));
    }

    @Test
    void test_union() {
        EmptySet A = of();
        A.add(of());
        A.add(of(of()));
        System.out.println(A);
        EmptySet B = of();
        B.add(of());
        B.add(of(of(of())));
        System.out.println(B);

        EmptySet union = A.union(B);
        System.out.println("union = " + union);
        assertThat(union.getList()).containsExactlyInAnyOrder(of(), of(of(of())), of(of()));
    }

    @Test
    void test_intersect() {
        EmptySet A = of();
        A.add(of());
        A.add(of(of()));

        EmptySet B = of();
        B.add(of());
        B.add(of(of(of())));

        EmptySet intersect = A.intersect(B);
        System.out.println("intersect = " + intersect);
    }

    @ParameterizedTest
    @MethodSource("provider2")
    void test_intersect(EmptySet a, EmptySet b, EmptySet intersect) {
        assertThat(a.intersect(b)).isEqualTo(intersect);
    }

    private static Stream<Arguments> provider2() {
        return Stream.of(
            Arguments.of(
                of(of(), of(of()), of(of(of()))), //a
                of(of(), of(of())),      //b
                of(of(), of(of()))      //expected
                ,
                of(of(), of(of())),
                of(of(), of()),
                of(of())
            )
        );
    }

    @Test
    void test_add() {
        EmptySet A = of();
        A.add(of());
        A.add(of(of()));

        EmptySet B = of();
        B.add(of());
        B.add(of(of(of())));

        B.add(A);

        EmptySet emptySet = of(of(), of(of(of())), of(of(), of(of())));
        assertThat(B).isEqualTo(emptySet);
    }

    @Test
    void test_add1() {
        EmptySet a = of();
        EmptySet b = of();
        a.add(b);

        System.out.println("a = " + a);
        assertThat(a.cardinality()).isEqualTo(1);

    }
}