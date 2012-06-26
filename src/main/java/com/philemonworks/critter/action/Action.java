package com.philemonworks.critter.action;

import com.philemonworks.critter.condition.Explainable;
import com.philemonworks.critter.rule.RuleContext;

public interface Action extends Explainable {
	void perform(RuleContext context);
}
