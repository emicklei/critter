package com.philemonworks.critter.condition;

import com.philemonworks.critter.action.RuleIngredient;
import com.philemonworks.critter.rule.RuleContext;

public class Not extends RuleIngredient implements Condition {
    public Condition condition;

    @Override
    public String explain() {
        return "not(" + condition.explain() + ")";
    }

    @Override
    public boolean test(RuleContext ctx) {
        return !condition.test(ctx);
    }
}