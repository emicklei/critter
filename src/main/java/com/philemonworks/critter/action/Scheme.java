package com.philemonworks.critter.action;

import java.net.URI;
import java.net.URISyntaxException;

import com.philemonworks.critter.ProxyFilter;
import com.philemonworks.critter.rule.RuleContext;
import com.sun.jersey.spi.container.ContainerRequest;

public class Scheme extends RuleIngredient implements Action {

    String name;

    @Override
    public String explain() {
        return "change the scheme of the request URL to ["+name+"]";
    }

    @Override
    public void perform(RuleContext context) {
        ContainerRequest containerRequest = (ContainerRequest) context.httpContext.getRequest();
        URI forwardUri = (URI)containerRequest.getProperties().get(ProxyFilter.UNPROXIED_URI);
        try {
            forwardUri = new URI(name, forwardUri.getUserInfo(), forwardUri.getHost(), forwardUri.getPort(), forwardUri.getPath(), forwardUri.getQuery(), forwardUri.getFragment());
        } catch (URISyntaxException e) {
            // TODO record this
            return;
        }
        containerRequest.getProperties().put(ProxyFilter.UNPROXIED_URI, forwardUri);
    }
}
