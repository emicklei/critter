package com.philemonworks.critter.action;

import javax.ws.rs.core.Response;

import com.philemonworks.critter.rule.RuleContext;

public class ResponseBody extends StatusCode implements Action { 

    public String body;
    
    @Override
    public void perform(RuleContext context) {
        if (context.forwardResponse != null) {
            context.forwardResponse = Response
                    .fromResponse(context.forwardResponse)
                    .status(this.code)
                    .entity(body).build();
            return;
        } else {
            context.forwardResponse = Response.ok().entity(body).build();
        }            
    }
	@Override
	public String explain() {
		return "replace the response body with ["+body.substring(0,Math.min(4,body.length())).replace("\n","")+"...]";
	}    
}
