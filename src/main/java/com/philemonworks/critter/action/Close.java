package com.philemonworks.critter.action;

import java.io.IOException;

import com.philemonworks.critter.rule.RuleContext;

public class Close extends RuleIngredient implements Action { 

    @Override
    public void perform(RuleContext context) {        
        try {
            context.httpContext.getResponse().getOutputStream().close();
        } catch (IOException e) {
            // eat this
        }
    }
	@Override
	public String explain() {
		return "close the input stream of the request";
	}    
}
