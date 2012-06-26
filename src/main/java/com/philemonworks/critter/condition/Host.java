package com.philemonworks.critter.condition;

import com.philemonworks.critter.action.RuleIngredient;
import com.philemonworks.critter.rule.RuleContext;

public class Host extends RuleIngredient implements Condition {
	public String matches;
	
	@Override
	public boolean test(RuleContext ctx) {
		String host = ctx.httpContext.getRequest().getHeaderValue("Host");
		return matches.matches(host);
	}
	@Override
	public String explain() {
		return "host name matches <strong>"+matches+"</strong>";
	}	
}
