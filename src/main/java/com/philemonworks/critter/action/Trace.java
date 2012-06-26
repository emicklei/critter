package com.philemonworks.critter.action;

import javax.ws.rs.core.Response;

import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.philemonworks.critter.rule.RuleContext;

public class Trace extends RuleIngredient implements Action { 
    private static final Logger LOG = LoggerFactory.getLogger(Trace.class);
    
    @Override
    public void perform(RuleContext context) {        
        // detect request or response to trace
        if (context.forwardResponse == null) {
            this.log(context.forwardMethod);
        } else {
            this.log(context.httpContext.getResponse().getResponse());
        }
    }
    private void log(HttpRequestBase request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getRequestLine().toString());
        LOG.trace(sb.toString());
    }
    private void log(Response response) {
        StringBuilder sb = new StringBuilder();
        sb.append(response.getStatus());
        LOG.trace(sb.toString());
    }
	@Override
	public String explain() {
		return "trace the incoming request or outgoing response";
	}    
}
