package com.philemonworks.critter.condition;

import java.net.URI;
import java.net.URISyntaxException;


import com.philemonworks.critter.ProxyFilter;
import com.philemonworks.critter.action.Action;
import com.philemonworks.critter.action.RuleIngredient;
import com.philemonworks.critter.rule.RuleContext;
import com.sun.jersey.spi.container.ContainerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Host extends RuleIngredient implements Condition, Action {
    private static final Logger LOG = LoggerFactory.getLogger(Host.class);

    public String matches;
    public String value;

    @Override
    public boolean test(RuleContext ctx) {
        String host = ctx.forwardURI.getHost();
        boolean ok = matches.matches(host);
        if (ctx.rule.tracing) {
            LOG.info("rule={} host={} matches={} test={}", ctx.rule.id, host, matches, ok);
        }
        return ok;
    }

    @Override
    public String explain() {
        return matches != null
                ? "host name matches [" + matches + "]"
                : "change the host to [" + this.value + "]";
    }

    @Override
    public void perform(RuleContext context) {
        ContainerRequest containerRequest = (ContainerRequest) context.httpContext.getRequest();
        URI forwardUri = (URI) containerRequest.getProperties().get(ProxyFilter.UNPROXIED_URI);
        try {
            context.forwardURI = new URI(
                    forwardUri.getScheme(),
                    forwardUri.getUserInfo(),
                    this.value,
                    forwardUri.getPort(),
                    forwardUri.getPath(),
                    forwardUri.getQuery(),
                    forwardUri.getFragment());
            if (context.rule.tracing) {
                LOG.info("rule={} uri={}", context.rule.id, context.forwardURI.toString());
            }
        } catch (URISyntaxException e) {
            LOG.error("perform failed", e);
        }
    }
}
