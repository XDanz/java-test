package com.terraware;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class MethodRule1 implements MethodRule {

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                System.out.println("Before in rule. ..");
                base.evaluate();
                System.out.println("After in rule. ..");
            }
        };
    }
}
