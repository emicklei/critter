package com.philemonworks.critter.action;

import com.philemonworks.critter.rule.RuleContext;
import com.philemonworks.critter.ui.fixed.RecordingInput;

public class Record implements Action {

    @Override
    public String explain() {
        return "create (or overwrite) a new disabled recording rule";
    }

    @Override
    public void perform(RuleContext context) {
        RecordingInput input = new RecordingInput();
        input.accepttype = context.httpContext.getRequest().getHeaderValue("Accept");
        input.contenttype = context.httpContext.getRequest().getHeaderValue("Content-Type");
        if (input.hasTextualRequestContent()) {
            input.requestcontent = context.httpContext.getRequest().getEntity(String.class);    
        }
        if (input.hasTextualRequestContent()) {
            input.responsecontent = context.httpContext.getRequest().getEntity(String.class);
        }
        
        
    }

}
