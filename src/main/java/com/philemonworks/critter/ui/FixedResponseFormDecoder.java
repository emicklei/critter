package com.philemonworks.critter.ui;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang3.StringUtils;

import com.philemonworks.critter.action.Action;
import com.philemonworks.critter.action.ResponseBody;
import com.philemonworks.critter.condition.Condition;
import com.philemonworks.critter.condition.Header;
import com.philemonworks.critter.condition.Host;
import com.philemonworks.critter.condition.Method;
import com.philemonworks.critter.condition.Path;
import com.philemonworks.critter.condition.RequestBody;
import com.philemonworks.critter.rule.Rule;

public class FixedResponseFormDecoder {
    
    public Rule buildRuleFrom(Properties props) {
        Rule r = new Rule();
        r.actions = new ArrayList<Action>();
        r.conditions = new ArrayList<Condition>();
        
        boolean hasRequestContent = props.containsKey("requestcontent") && !StringUtils.isEmpty(props.getProperty("requestcontent"));
        
        if (props.containsKey("id")) {
            r.id = props.getProperty("id");
        }
        if (props.containsKey("method")) {
            Method m = new Method();
            m.matches = props.getProperty("method");
            r.conditions.add(m);
        }
        if (props.containsKey("url")) {
            Host h = new Host();
            Path p = new Path();
            URL url;
            try {
                url = new URL(props.getProperty("url"));
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(e);
            }
            h.matches = url.getHost();
            p.matches = url.getPath();
            r.conditions.add(h);
            r.conditions.add(p);
        }
        if (props.containsKey("contenttype") && hasRequestContent) {
            Header h = new Header();
            h.name = HttpHeaders.CONTENT_TYPE;
            h.matches = props.getProperty("contenttype");
            r.conditions.add(h);
        }
        if (props.containsKey("accepttype")) {
            Header h = new Header();
            h.name = HttpHeaders.ACCEPT;
            h.matches = props.getProperty("accepttype");
            r.conditions.add(h);
        } 
        if (props.containsKey("responsecontent")) {
            ResponseBody rb = new ResponseBody();
            rb.body = props.getProperty("responsecontent");
            r.actions.add(rb);
        }                
        if (hasRequestContent) {
            RequestBody rb = new RequestBody();
            rb.body = props.getProperty("requestcontent");
            r.conditions.add(rb);
        }
        return r;
    }
    
    public Properties decode(String input) {
        Properties props = new Properties();
        String[] tokens = new String[]{"critter_id","critter_method","critter_url","critter_contenttype","critter_accepttype","critter_responsecontent","critter_requestcontent"};
        for (String each : tokens) {
            int begin = input.indexOf(each);
            if (begin != -1) {
                int end = input.indexOf("critter",begin+1);
                int slash = each.indexOf('_');
                String key = each.substring(slash+1);
                String value = "";
                if (begin + each.length() + 1 < input.length()) {
                  value = input.substring(begin + each.length() + 1, end == -1 ? input.length() - 1 : end - 1);
                }
                props.put(key, value);
            }
        }
        return props;
    }
}
