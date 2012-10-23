package com.philemonworks.critter.condition;

import java.net.URI;
import java.net.URISyntaxException;

import com.philemonworks.critter.ProxyFilter;
import com.philemonworks.critter.action.Action;
import com.philemonworks.critter.action.RuleIngredient;
import com.philemonworks.critter.rule.RuleContext;
import com.sun.jersey.spi.container.ContainerRequest;

public class Port extends RuleIngredient implements Condition , Action{

    public String matches = null;
    public int value;
    
    @Override
    public boolean test(RuleContext ctx) {      
        return Integer.toString(ctx.httpContext.getRequest().getBaseUri().getPort())
                .matches(matches);
    }

	@Override
	public String explain() {
		return matches != null ? "port number matches "+matches : "change the port to "+value;
	}

    @Override
    public void perform(RuleContext context) {
        ContainerRequest containerRequest = (ContainerRequest) context.httpContext.getRequest();
        URI forwardUri = (URI)containerRequest.getProperties().get(ProxyFilter.UNPROXIED_URI);
        try {
            forwardUri = new URI(forwardUri.getScheme(), forwardUri.getUserInfo(), forwardUri.getHost(), this.value, forwardUri.getPath(), forwardUri.getQuery(), forwardUri.getFragment());
        } catch (URISyntaxException e) {
            // TODO record this
            return;
        }
        containerRequest.getProperties().put(ProxyFilter.UNPROXIED_URI, forwardUri);
    }
}
