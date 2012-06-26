package com.philemonworks.critter.condition;

import com.philemonworks.critter.action.RuleIngredient;
import com.philemonworks.critter.rule.RuleContext;

public class Header extends RuleIngredient implements Condition {

    public String name, matches;
    
    @Override
    public boolean test(RuleContext ctx) {
        String value = ctx.httpContext.getRequest().getHeaderValue(this.name);
        return value != null && value.matches(matches);
    }
	@Override
	public String explain() {
		return "request http header matches "+matches;
	}    
}
