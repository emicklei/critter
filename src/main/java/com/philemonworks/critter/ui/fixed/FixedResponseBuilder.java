package com.philemonworks.critter.ui.fixed;

import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.core.HttpHeaders;

import com.philemonworks.critter.action.Respond;
import com.philemonworks.critter.action.ResponseBody;
import com.philemonworks.critter.condition.Host;
import com.philemonworks.critter.condition.Method;
import com.philemonworks.critter.condition.Path;
import com.philemonworks.critter.condition.RequestBody;
import com.philemonworks.critter.condition.RequestHeader;
import com.philemonworks.critter.rule.Rule;

public class FixedResponseBuilder {
    
    public Rule buildRuleFrom(RecordingInput input) {
        Rule r = new Rule();
        
        if (input.hasId()) {
            r.id = input.id;
        }
        if (input.hasMethod()) {
            Method m = new Method();
            m.matches = input.method;
            r.getConditions().add(m);
        }
        if (input.hasUrl()) {
            Host h = new Host();
            Path p = new Path();
            URL url;
            try {
                url = new URL(input.url);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(e);
            }
            h.matches = url.getHost();
            p.matches = url.getPath();
            r.getConditions().add(h);
            r.getConditions().add(p);
        }
        if (input.hasContenttype() && input.hasRequestContent()) {
            RequestHeader h = new RequestHeader();
            h.name = HttpHeaders.CONTENT_TYPE;
            h.matches = input.contenttype;
            r.getConditions().add(h);
        }
        if (input.hasAccepttype()) {
            RequestHeader h = new RequestHeader();
            h.name = HttpHeaders.ACCEPT;
            h.matches = input.accepttype;
            r.getConditions().add(h);
        } 
        if (input.hasResponsecontent()) {
            ResponseBody rb = new ResponseBody();
            rb.body = input.responsecontent;
            r.getActions().add(rb);
        }                
        if (input.hasRequestContent()) {
            RequestBody rb = new RequestBody();
            rb.body = input.requestcontent;
            r.getConditions().add(rb);
        }
        r.getActions().add(new Respond());
        return r;
    }
}
