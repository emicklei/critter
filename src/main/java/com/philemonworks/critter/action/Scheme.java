package com.philemonworks.critter.action;

import java.net.URI;
import java.net.URISyntaxException;

import com.philemonworks.critter.rule.RuleContext;
import com.sun.jersey.spi.container.ContainerRequest;

public class Scheme extends RuleIngredient implements Action {

    String name;

    @Override
    public String explain() {
        return "changes the schema of the request URL";
    }

    @Override
    public void perform(RuleContext context) {
        ContainerRequest containerRequest = (ContainerRequest) context.httpContext.getRequest();
        URI baseUri = containerRequest.getBaseUri();
        URI requestUri = null;
        try {
            requestUri = new URI(name, baseUri.getUserInfo(), baseUri.getHost(), baseUri.getPort(), baseUri.getPath(), baseUri.getQuery(), baseUri.getFragment());
        } catch (URISyntaxException e) {
            // TODO record this
            return;
        }
        containerRequest.setUris(requestUri, requestUri);
    }
}
