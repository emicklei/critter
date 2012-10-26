package com.philemonworks.critter.condition;

import com.philemonworks.critter.action.RuleIngredient;
import com.philemonworks.critter.rule.RuleContext;

public class RequestHeader extends RuleIngredient implements Condition {

    public String name, matches;
    
    @Override
    public boolean test(RuleContext ctx) {
        String value = ctx.httpContext.getRequest().getHeaderValue(this.name);
        return value != null && value.matches(matches);
    }
	@Override
	public String explain() {
		return "request http header ["+name+"] matches ["+matches+"]";
	}    
}
