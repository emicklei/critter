package com.philemonworks.critter.action;

import javax.ws.rs.core.Response;

import com.philemonworks.critter.rule.RuleContext;

public class StatusCode extends RuleIngredient implements Action { 
	
	int code = 200;
	
	public void perform(RuleContext context) {
	    if (context.forwardResponse != null) {
	        context.forwardResponse = Response
	                .fromResponse(context.forwardResponse)
	                .status(this.code).build();
	        return;
	    } else {
	        context.forwardResponse = Response.status(code).build();
	    }	         
	}
	@Override
	public String explain() {
		return "set the response status code to ["+code+"]";
	}	
	public StatusCode withCode(int code) { this.code = code; return this;}
}
