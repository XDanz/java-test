package com.terraware;


import com.terraware.StackSet.EmptySet;
import org.junit.jupiter.api.Test;


import static com.terraware.StackSet.EmptySet.of;
import static org.assertj.core.api.Assertions.assertThat;

public class EmptySetTest {

    @Test
    void test0() {
        EmptySet e0 = of();
        EmptySet e1 = of();
        assertThat(e0).isEqualTo(e1);
    }

    @Test
    void test1() {
        EmptySet e0 = of();
        e0.add(of());
        EmptySet e1 = of();
        assertThat(e0).isNotEqualTo(e1);
        assertThat(e0).isEqualTo(of(of()));
    }

    @Test
    void test_union() {
        EmptySet A = of();
        A.add(of());
        A.add(of(of()));

        EmptySet B = of();
        B.add(of());
        B.add(of(of(of())));

        EmptySet union = A.union(B);
        System.out.println("union = " + union);
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
        System.out.println("union = " + intersect);
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