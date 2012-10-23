package com.philemonworks.critter.action;

import com.philemonworks.critter.rule.RuleContext;

public class Forward extends RuleIngredient implements Action { 

	@Override
	public void perform(RuleContext context) {
		context.forwardResponse = context.httpClient.forward(context.httpContext, context.forwardMethod);
	}
	@Override
	public String explain() {
		return "forward the request to the remote host";
	}	
}
