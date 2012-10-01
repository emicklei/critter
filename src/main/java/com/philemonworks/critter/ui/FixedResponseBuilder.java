package com.philemonworks.critter.ui;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.ws.rs.core.HttpHeaders;

import com.philemonworks.critter.action.Action;
import com.philemonworks.critter.action.ResponseBody;
import com.philemonworks.critter.condition.Condition;
import com.philemonworks.critter.condition.Header;
import com.philemonworks.critter.condition.Host;
import com.philemonworks.critter.condition.Method;
import com.philemonworks.critter.condition.Path;
import com.philemonworks.critter.condition.RequestBody;
import com.philemonworks.critter.rule.Rule;

public class FixedResponseBuilder {
    
    public Rule buildRuleFrom(RecordingInput input) {
        Rule r = new Rule();
        r.actions = new ArrayList<Action>();
        r.conditions = new ArrayList<Condition>();
        
        if (input.hasId()) {
            r.id = input.id;
        }
        if (input.hasMethod()) {
            Method m = new Method();
            m.matches = input.method;
            r.conditions.add(m);
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
            r.conditions.add(h);
            r.conditions.add(p);
        }
        if (input.hasContenttype() && input.hasRequestContent()) {
            Header h = new Header();
            h.name = HttpHeaders.CONTENT_TYPE;
            h.matches = input.contenttype;
            r.conditions.add(h);
        }
        if (input.hasAccepttype()) {
            Header h = new Header();
            h.name = HttpHeaders.ACCEPT;
            h.matches = input.accepttype;
            r.conditions.add(h);
        } 
        if (input.hasResponsecontent()) {
            ResponseBody rb = new ResponseBody();
            rb.body = input.responsecontent;
            r.actions.add(rb);
        }                
        if (input.hasRequestContent()) {
            RequestBody rb = new RequestBody();
            rb.body = input.requestcontent;
            r.conditions.add(rb);
        }
        return r;
    }
}
