package com.philemonworks.critter.condition;

import com.philemonworks.critter.action.RuleIngredient;
import com.philemonworks.critter.rule.RuleContext;

public class Port extends RuleIngredient implements Condition {

    public String matches;
    
    @Override
    public boolean test(RuleContext ctx) {      
        return Integer.toString(ctx.httpContext.getRequest().getBaseUri().getPort())
                .matches(matches);
    }

	@Override
	public String explain() {
		return "port number matches "+matches;
	}
}
