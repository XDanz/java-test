package com.terraware;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

public class Help2Test  {

    @Test
    public void test0() {
        String[] a = {"how", "now", "brown", "<animal>"};
        String[] b = {"<foo>", "now", "<color>", "cow"};
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{"how", "now", "brown", "cow"});
    }

    @Test
    public void test01() {
        String[] a = {"howafskfjkasfjkdjsafkjdkasjfkasfkjasdkfjkasfjdkasjfkjaskf", "now", "brown", "<animal>"};
        String[] b = {"<foo>", "now", "<color>", "cow"};
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{});
    }

    @Test
    public void test02() {
        List<String> al = new ArrayList<>();

        for (int i = 0; i < 110; i++)  {
          al.add("now");
        }

        List<String> bl = new ArrayList<>();
        bl.add("<foo>");

        for (int i = 0; i < 109; i++)  {
          bl.add("now");
        }

        String[] a = al.toArray(new String[0]);
        String[] b = bl.toArray(new String[0]);
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{});
    }

    @Test
    public void test1() {
        String[] a = {"<how>", "now", "brown", "<animal>"};
        String[] b = {"<foo>", "now", "<color>", "cow"};
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{"brzzan", "now", "brown", "cow"});
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
        assertThat(r).isEqualTo(new String[]{"a", "brzzan", "a", "b"});
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
        assertThat(r).isEqualTo(new String[]{"a", "brzzan", "b", "a","c"});
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

    @Test
    public void test22() {
        String[] b = { "<x>",    "<x>",    "<x>",    "<x>", "<x>"  };
        String[] a = { "<y>",  "<y>",    "<y>",      "<y>", "<y>"   };
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{"brzzan","brzzan","brzzan","brzzan","brzzan"});
    }

    @Test
    public void test23() {
        String[] b = { "<x>",    "<x>",    "<x>",    "<x>", "<x>"  };
        String[] a = { "<y>",    "<y>",    "<y>",    "<y>", "<z>"   };
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{"brzzan", "brzzan", "brzzan", "brzzan", "brzzan"});
    }

    @Test
    public void test24() {
        String[] b = {"<x>", "<y>", "<z>", "<u>", "<v>"};
        String[] a = {"<v>", "<x>", "<y>", "<u>", "<z>"};
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{"brzzan", "brzzan", "brzzan", "brzzan", "brzzan"});
    }

     @Test
    public void test25() {
        String[] b = { "<x>",    "<y>",    "<z>",    "<u>", "<v>"  };
        String[] a = { "a",      "<x>",    "<y>",    "<u>", "<z>"   };
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{"a", "brzzan", "brzzan", "brzzan", "brzzan"});
    }

    @Test
    public void test26() {
        String[] b = { "<x>",    "<x>",    "<x>"  };
        String[] a = { "<y>",      "<x>",    "<x>"   };
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{ "brzzan", "brzzan", "brzzan"});
    }

    @Test
    public void test27() {
        String[] b = { "<x>","<y>","<z>","a","b"  };
        String[] a = { "b",  "<x>", "<y>","a","<z>"  };
        String[] r = Help2.help3(a, b);
        assertThat(r).isEqualTo(new String[]{ "b", "brzzan", "brzzan", "a", "b"});
    }

    @ParameterizedTest
    @MethodSource("provider")
    void parma(String[] pattern, String[] expected) {
        String[] b = { "a", "<x>", "b", "<x>", "c"  };

        String[] out = Help2.help3(b, pattern);
        assertThat(out).isEqualTo(expected);

        out = Help2.help3(pattern,b);
        assertThat(out).isEqualTo(expected);
    }

    private static Stream<Arguments> provider() {
        return Stream.of(
            Arguments.of(new String[]{"<y>","<y>","<z>", "a", "c" }, new String[]{"a","a","b","a","c" }),
            Arguments.of(new String[]{"a",  "<y>","b", "<y>","c" }, new String[]{"a","brzzan","b","brzzan","c" }),
            Arguments.of(new String[]{"<y>",  "<z>","<x>", "<z>","<v>" }, new String[]{"a","brzzan","b","brzzan","c" }),
            Arguments.of(new String[]{"<z>",  "<x>","b", "<x>","<x>" }, new String[]{"a","c","b","c","c" }));
    }
}