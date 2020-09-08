package com.terraware;


import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

public class Help2EvalTest {

    @ParameterizedTest
    @MethodSource("provider")
    void test_elem(Help2.Elem a, Help2.Elem b, String expected, String expectedVala, String expectedValb) {
        String o = Help2.eval(a, b);
        assertThat(o).isEqualTo(expected);
        assertThat(a.getValue()).isEqualTo(expectedVala);
        assertThat(b.getValue()).isEqualTo(expectedValb);
    }

    private static Stream<Arguments> provider() {
        return Stream.of(
            Arguments.of(
                new Help2.Elem(null, true), new Help2.Elem(null, true), null, null, null),
            Arguments.of(
                new Help2.Elem("b", false), new Help2.Elem("a", false), null, "b", "a"),
            Arguments.of(
                new Help2.Elem("a", false), new Help2.Elem("b", false), null, "a", "b"),
            Arguments.of(
                new Help2.Elem("a", false), new Help2.Elem("a", false), "a", "a", "a"),
            Arguments.of(new Help2.Elem("a", true), new Help2.Elem("a", true), "a", "a", "a"),
            Arguments.of(new Help2.Elem("a", true), new Help2.Elem(null, true), "a", "a", "a"),
            Arguments.of(new Help2.Elem(null, true), new Help2.Elem("a", false), "a", "a", "a"),
            Arguments.of(new Help2.Elem("b", false), new Help2.Elem(null, false), "b", "b", "b")
        );
    }

    @Test
    void resolve_test1() {
        Help2.Elem[] elemsa = new Help2.Elem[]{
            new Help2.Elem(null, true),
            new Help2.Elem(null, true)
        };

        Help2.Elem[] elemsb = new Help2.Elem[]{
            new Help2.Elem("a", false),
            new Help2.Elem(null, true)
        };

        String[] out = new String[2];

        Set<Integer> unresolved = Help2.resolve(Set.of(0, 1), elemsa, elemsb, out);

        assertThat(out).isEqualTo(new String[]{"a", null});
        assertThat(unresolved).isEqualTo(Set.of(1));
    }

    @Test
    void resolve_test2() {
        Help2.Elem[] elemsa = new Help2.Elem[]{
            new Help2.Elem("a", false),
            new Help2.Elem(null, true)
        };

        Help2.Elem[] elemsb = new Help2.Elem[]{
            new Help2.Elem("a", false),
            new Help2.Elem(null, true)
        };

        String[] out = new String[2];

        Set<Integer> unresolved = Help2.resolve(Set.of(0, 1), elemsa, elemsb, out);

        assertThat(out).isEqualTo(new String[]{"a", null});
        assertThat(unresolved).isEqualTo(Set.of(1));
    }

    @Test
    void resolve_test3() {
        Help2.Elem[] elemsa = new Help2.Elem[]{
            new Help2.Elem("b", false),
            new Help2.Elem(null, true)
        };

        Help2.Elem[] elemsb = new Help2.Elem[]{
            new Help2.Elem("a", false),
            new Help2.Elem(null, true)
        };

        String[] out = new String[2];

        Set<Integer> unresolved = Help2.resolve(Set.of(0, 1), elemsa, elemsb, out);

        assertThat(out).isEqualTo(new String[]{null, null});
        assertThat(unresolved).isEqualTo(null);
    }

    @Test
    void resolve_test4() {
        Help2.Elem[] elemsa = new Help2.Elem[]{
            new Help2.Elem("b", false),
            new Help2.Elem(null, true)
        };

        Help2.Elem[] elemsb = new Help2.Elem[]{
            new Help2.Elem("a", false),
            new Help2.Elem(null, true)
        };

        String[] out = new String[2];

        Set<Integer> unresolved = Help2.resolve(Set.of(1), elemsa, elemsb, out);

        assertThat(out).isEqualTo(new String[]{null, null});
        assertThat(unresolved).isEqualTo(Set.of(1));
    }
}