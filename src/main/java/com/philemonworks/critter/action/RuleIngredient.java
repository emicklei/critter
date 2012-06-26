package com.philemonworks.critter.action;

import com.philemonworks.critter.rule.RuleConverter;

public abstract class RuleIngredient  {
	
    public String toXml() {
        return RuleConverter.toXml(this);
    }
}
