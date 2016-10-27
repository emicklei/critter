package com.philemonworks.critter.condition;

import com.philemonworks.critter.rule.RuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class RequestBody implements Condition {
    private static final Logger LOG = LoggerFactory.getLogger(RequestBody.class);

    public String matches;

    @Override
    public String explain() {
        return "the request matches matches [" + matches + "]";
    }

    @Override
    public boolean test(RuleContext ctx) {
        String payload = new String(ctx.getRequestEntityContent(), Charset.forName("UTF-8"));
        boolean ok = payload.matches(matches);

        if (ctx.rule.tracing) {
            LOG.info("rule={} requestbody={} matches={} test={}", ctx.rule.id, payload, matches, ok);
        }
        return ok;
    }
}
