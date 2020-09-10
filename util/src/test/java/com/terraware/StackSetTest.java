package com.terraware;

import org.junit.jupiter.api.Test;

import static com.terraware.StackSet.OP;
import static org.assertj.core.api.Assertions.assertThat;

public class StackSetTest {

    @Test
    void provided_test_case0() {
        StackSet stackSet = new StackSet();
        OP[] ops = OP.of(
            OP.PUSH,
            OP.DUP,
            OP.ADD,
            OP.PUSH,
            OP.ADD,
            OP.DUP,
            OP.ADD,
            OP.DUP,
            OP.UNION
        );

        int[] out = stackSet.processOps(ops);
        assertThat(out).contains(0, 0, 1, 0, 1, 1, 2, 2, 2);
    }

    @Test
    void provided_test_case1() {
        StackSet stackSet = new StackSet();
        OP[] ops = OP.of(
            OP.PUSH,
            OP.PUSH,
            OP.ADD,
            OP.PUSH,
            OP.INTERSECT
        );

        int[] out = stackSet.processOps(ops);
        assertThat(out).contains(0, 0, 1, 0, 0);
    }


}