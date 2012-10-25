package com.philemonworks.critter.action;

import java.io.IOException;

import javax.ws.rs.core.Response;

import org.rendersnake.HtmlCanvas;

import com.philemonworks.critter.rule.RuleContext;

public class ResponseBody implements Action { 

    public String body;
    
    @Override
    public void perform(RuleContext context) {
        if (context.forwardResponse != null) {
            context.forwardResponse = Response
                    .fromResponse(context.forwardResponse)
                    .entity(body).build();
            return;
        } else {
            context.forwardResponse = Response.ok().entity(body).build();
        }            
    }
	@Override
	public String explain() {
	    // Use a canvas to escape HTML/XML.  Explain strings may otherwise contain HTML markup.
	    HtmlCanvas c = new HtmlCanvas();
	    try {
	        String shortBody = body.substring(0,Math.min(24,body.length()));
	        if (body.length() > 24) {
	            shortBody = shortBody.replace("\n","") + "...";
	        }
            c.write(shortBody);            
        } catch (IOException e) {}
		return "replace the response body with ["+c.toHtml()+"]";
	}  
	
	public ResponseBody withBody(String body) { this.body = body; return this; }
}
