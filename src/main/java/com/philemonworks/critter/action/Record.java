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
        RecordingInput requestRecording = new RecordingInput();
        requestRecording.accepttype = context.httpContext.getRequest().getHeaderValue("Accept");
        requestRecording.contenttype = context.httpContext.getRequest().getHeaderValue("Content-Type");
        if (requestRecording.hasTextualRequestContent()) {
            requestRecording.requestcontent = context.httpContext.getRequest().getEntity(String.class);    
        }
        RecordingInput responseRecording = new RecordingInput();
        requestRecording.contenttype = (String)context.forwardResponse.getMetadata().getFirst("Content-Type");
        if (responseRecording.hasTextualRequestContent()) {
            responseRecording.responsecontent = (String)context.forwardResponse.getEntity(); 
        }
    }
}
