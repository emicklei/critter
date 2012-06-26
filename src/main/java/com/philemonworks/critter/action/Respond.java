package com.philemonworks.critter.action;

import com.philemonworks.critter.rule.RuleContext;

public class Respond extends RuleIngredient implements Action { 

	@Override
	public void perform(RuleContext context) {		
	    context.proxyResponse = context.forwardResponse;
	}
	@Override
	public String explain() {
		return "send back the response (new or from host)";
	}	
}
