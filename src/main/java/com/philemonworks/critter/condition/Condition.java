package com.philemonworks.critter.condition;

import com.philemonworks.critter.rule.RuleContext;

public interface Condition extends Explainable {
	boolean test(RuleContext ctx);
}
