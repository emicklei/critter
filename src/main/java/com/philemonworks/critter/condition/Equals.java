package com.philemonworks.critter.condition;

import com.philemonworks.critter.action.RuleIngredient;
import com.philemonworks.critter.rule.RuleContext;

public class Equals extends RuleIngredient implements Condition {

	public String parameter,value;
	
	@Override
	public boolean test(RuleContext ctx) {
		String currentValue = ctx.parameters.get(this.parameter);
		if (currentValue == null) 
		    return false;
		else 
		    return value.equals(currentValue);
	}
	@Override
	public String explain() {
		return "value or parameter ["+parameter+"] equals ["+value + "]";
	}	
}
