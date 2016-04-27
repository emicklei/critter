package com.philemonworks.critter.condition;

import com.philemonworks.critter.action.RuleIngredient;
import com.philemonworks.critter.rule.RuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Method extends RuleIngredient implements Condition {
    private static final Logger LOG = LoggerFactory.getLogger(Host.class);

    public String matches;

    @Override
    public boolean test(RuleContext ctx) {
        String method = ctx.httpContext.getRequest().getMethod();
        boolean ok = matches.matches(method);
        if (ctx.rule.tracing) {
            LOG.info("rule={} method={} matches={} test={}", ctx.rule.id, method, matches, ok);
        }
        return ok;
    }

    @Override
    public String explain() {
        return "http method name matches [" + matches + "]";
    }
}
