package com.philemonworks.critter.condition;

import com.philemonworks.critter.action.RuleIngredient;
import com.philemonworks.critter.rule.RuleContext;

public class Method extends RuleIngredient implements Condition {
	public String matches;
	
	@Override
	public boolean test(RuleContext ctx) {
		String method = ctx.httpContext.getRequest().getMethod();
		return matches.matches(method);
	}
	@Override
	public String explain() {
		return "http method name matches <strong>"+matches+"</strong>";
	}	
}
