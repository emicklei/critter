package com.philemonworks.critter.condition;

import com.philemonworks.critter.action.RuleIngredient;
import com.philemonworks.critter.rule.RuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHeader extends RuleIngredient implements Condition {
    private static final Logger LOG = LoggerFactory.getLogger(Host.class);

    public String name, matches;

    @Override
    public boolean test(RuleContext ctx) {
        String value = ctx.httpContext.getRequest().getHeaderValue(this.name);

        boolean ok = value != null && value.matches(matches);
        if (ctx.rule.tracing) {
            LOG.info("rule={} name={} value={} matches={} test={}", ctx.rule.id, name, value, matches, ok);
        }
        return ok;
    }

    @Override
    public String explain() {
        return "request http header [" + name + "] matches [" + matches + "]";
    }
}
