package com.philemonworks.critter.condition;

import java.net.URI;
import java.net.URISyntaxException;

import com.philemonworks.critter.ProxyFilter;
import com.philemonworks.critter.action.Action;
import com.philemonworks.critter.action.RuleIngredient;
import com.philemonworks.critter.rule.RuleContext;
import com.sun.jersey.spi.container.ContainerRequest;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

public class Port extends RuleIngredient implements Condition, Action {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(Port.class);

    public String matches = null;
    public int value;

    @Override
    public boolean test(RuleContext ctx) {
        String port = Integer.toString(ctx.forwardURI.getPort());
        boolean ok = port.matches(matches);
        if (ctx.rule.tracing) {
            LOG.info("rule={} port={} matches={} test={}", ctx.rule.id, port, matches, ok);
        }
        return ok;
    }

    @Override
    public String explain() {
        return matches != null
                ? "port number matches [" + matches + "]"
                : "change the port to [" + value + "]";
    }

    @Override
    public void perform(RuleContext context) {
        ContainerRequest containerRequest = (ContainerRequest) context.httpContext.getRequest();
        URI forwardUri = (URI) containerRequest.getProperties().get(ProxyFilter.UNPROXIED_URI);
        try {
            context.forwardURI = new URI(
                    forwardUri.getScheme(),
                    forwardUri.getUserInfo(),
                    forwardUri.getHost(),
                    this.value,
                    forwardUri.getPath(),
                    forwardUri.getQuery(),
                    forwardUri.getFragment());
        } catch (URISyntaxException e) {
            LOG.error("perform failed", e);
        }
    }
}
