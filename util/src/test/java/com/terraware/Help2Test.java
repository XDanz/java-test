package com.terraware;

import java.util.stream.Stream;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static com.terraware.FerryProblem.getNextArrivalTime;
import static org.assertj.core.api.Assertions.assertThat;

public class Help2Test extends TestCase {

    @Test
    public void test0() {
        String[] a = {"how", "now", "brown", "<animal>"};
        String[] b = {"<foo>", "now", "<color>", "cow"};
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{"how", "now", "brown", "cow"});
    }

    @Test
    public void test1() {
        String[] a = {"<how>", "now", "brown", "<animal>"};
        String[] b = {"<foo>", "now", "<color>", "cow"};
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{"now", "now", "brown", "cow"});
    }

    @Test
    public void test2() {
        String[] a = {"<how>", "now", "brown", "<animal>"};
        String[] b = {"foo", "now", "<color>", "cow"};
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{"foo", "now", "brown", "cow"});
    }

    @Test
    public void test3() {
        String[] a = {"who", "are", "you"};
        String[] b = {"<a>", "<b>", "<a>"};
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{});
    }

    @Test
    public void test4() {
        String[] a = {"<a>", "b"};
        String[] b = {"c", "<a>"};
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{"c", "b"});
    }

    @Test
    public void test5() {
        String[] a = {"twinkle", "twinkle", "little", "<x>"};
        String[] b = {"<a>", "<a>", "little", "star"};
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{"twinkle", "twinkle", "little", "star"});
    }

    @Test
    public void test6() {
        String[] b = {"twinkle", "twinkle", "little", "<x>"};
        String[] a = {"<a>", "<a>", "little", "star"};
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{"twinkle", "twinkle", "little", "star"});
    }

    @Test
    public void test7() {
        String[] b = {"twinkle", "twinkle", "little", "<x>"};
        String[] a = {"<a>", "<b>", "little", "star"};
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{"twinkle", "twinkle", "little", "star"});
    }

    @Test
    public void test8() {
        String[] b = {"twinkle", "twinkle", "little"};
        String[] a = {"<a>", "<b>", "little", "star"};
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{});
    }

    @Test
    public void test9() {
        String[] b = {"<x0>", "b", "<x1>", "d"};
        String[] a = {"a", "<x0>", "c", "<x1>"};
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{"a", "b", "c", "d"});
    }

    @Test
    public void test10() {
        String[] b = {"to", "be", "or", "not", "to", "be"};
        String[] a = {"<foo>", "be", "<bar>", "not", "<foo>", "<baf>"};
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{"to", "be", "or", "not", "to", "be"});
    }

    @Test
    public void test11() {
        String[] b = {"<a>", "<a>", "b"};
        String[] a = {"a", "c", "b"};
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{});
    }

    @Test
    public void test12() {
        String[] b = {"who", "are", "you"};
        String[] a = {"<a>", "<c>", "<b>"};
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{"who", "are", "you"});
    }

    @Test
    public void test13() {
        String[] b = {"a",    "a" , "a"  ,"b"};
        String[] a = {"<x>", "<x>", "<x>","<y>"};
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{"a", "a", "a","b"});
    }

    @Test
    public void test14() {
        String[] b = {"a", "<x>", "a",   "b"  };
        String[] a = {"a", "<x>", "<x>", "<y>"};
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{"a", "a", "a", "b"});
    }

    @Test
    public void test15() {
        String[] b = {"a", "<x>", "a",   "b"  };
        String[] a = {"a", "<x>", "<b>", "<y>"};
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{"a", "a", "a", "b"});
    }

    @Test
    public void test16() {
        String[] b = {"a", "<y>", "a", "b"  };
        String[] a = {"a", "<x>", "<x>", "b"};
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{"a", "a", "a", "b"});
    }

    @Test
    public void test17() {
        String[] b = { "<z>", "<y>", "<z>", "<v>"  };
        String[] a = { "a"  , "<x>", "<x>",  "b"   };
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{"a", "a", "a", "b"});
    }

    @Test
    public void test18() {
        String[] b = { "a",   "<v>",  "c",   "a",  "d"  ,"e","<v>","<u>"  };
        String[] a = { "<x>",  "b",  "<y>",  "<x>","<z>","e","<w>",  "c"   };
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{"a", "b", "c", "a","d","e","b","c"});
    }

    @Test
    public void test19() {
        String[] b = { "a",    "<x>",    "b",    "<y>", "c"  };
        String[] a = { "<y>",  "<x>",    "b",    "<y>","<z>"   };
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{"a", "b", "b", "a","c"});
    }

    @Test
    public void test20() {
        String[] b = { "a",    "<x>",    "b",    "<x>", "c"  };
        String[] a = { "<y>",  "<x>",    "b",    "<y>","<z>"   };
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{"a", "a", "b", "a","c"});
    }

    @Test
    public void test21() {
        String[] b = { "a",    "<x>",    "b",    "<x>", "c"  };
        String[] a = { "<y>",  "<x>",    "<z>",    "<y>","<z>"   };
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{});
    }

    @ParameterizedTest
    @MethodSource("provider")
    void get_next_arrivalTime(String[] pattern, String[] expected) {
        String[] b = { "a", "<x>", "b", "<x>", "c"  };
        String[] r = Help2.help3(a, b);

        int res = Help2.help3( );
        assertThat(res).isEqualTo(expected);
    }

    private static Stream<Arguments> provider() {
        return Stream.of(
            Arguments.of(0, 5),
            Arguments.of(1, 5),
            Arguments.of(2, 5),
            Arguments.of(3, 5),
            Arguments.of(3, 5),
            Arguments.of(4, 6),
            Arguments.of(5, 7),
            Arguments.of(6, 8),
            Arguments.of(7, 8),
            Arguments.of(8, 9),
            Arguments.of(9, 10),
            Arguments.of(10, -1));
    }
}