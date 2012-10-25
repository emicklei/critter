package com.philemonworks.critter.condition;

import com.philemonworks.critter.action.RuleIngredient;
import com.philemonworks.critter.rule.RuleContext;

public class Path extends RuleIngredient implements Condition {
	public String matches;
	
	@Override
	public boolean test(RuleContext ctx) {		
		return ctx.forwardURI.getPath().matches(matches);
	}
	@Override
	public String explain() {
		return "url path matches <strong>"+matches+"</strong>";
	}	
}
