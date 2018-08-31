package com.terraware;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class ClassRule1 implements TestRule {
    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                System.out.println(" START CLASS RULE");
                base.evaluate();
                System.out.println(" END CLASS RULE");

            }
        };
    }
}
