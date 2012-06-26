package com.philemonworks.critter.action;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.philemonworks.critter.rule.RuleContext;

public class ResponseHeader extends RuleIngredient implements Action { 

    String remove;
    String add;
    String value;
    
    @Override
    public void perform(RuleContext context) {
        if (context.forwardResponse != null) {
            context.forwardResponse = this.applyTo(Response.fromResponse(context.forwardResponse)).build();                   
            return;
        } else {
            context.forwardResponse = this.applyTo(Response.ok()).build();
        } 
    }

    private ResponseBuilder applyTo(ResponseBuilder responseBuilder) {
        if (remove != null) {
            // If value is null then all current headers of the same name will be removed.
            responseBuilder.header(this.remove, null);
        }
        if (add != null && value != null) {
            responseBuilder.header(this.add, this.value);
        }
        return responseBuilder;
    }
	@Override
	public String explain() {
		if (remove != null)
			return "remove the http header named ["+remove+"]";
		else
			return "add the http header named ["+add+"] with value ["+ value +"]";
	}    
}