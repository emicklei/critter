package com.philemonworks.critter.action;

import java.net.URI;
import java.net.URISyntaxException;

import com.philemonworks.critter.rule.RuleContext;

public class Scheme extends RuleIngredient implements Action {

    String name;

    @Override
    public String explain() {
        return "change the scheme of the request URL to ["+name+"]";
    }

    @Override
    public void perform(RuleContext context) {
        URI forwardUri = context.forwardURI;
        try {
            forwardUri = new URI(name, 
                    forwardUri.getUserInfo(), 
                    forwardUri.getHost(), 
                    forwardUri.getPort(), 
                    forwardUri.getPath(), 
                    forwardUri.getQuery(), 
                    forwardUri.getFragment());
            context.forwardURI = forwardUri;
        } catch (URISyntaxException e) {
            // TODO record this
            return;
        }
        
    }
}
