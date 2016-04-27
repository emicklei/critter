package com.philemonworks.critter.condition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.philemonworks.critter.action.RuleIngredient;
import com.philemonworks.critter.rule.RuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Path extends RuleIngredient implements Condition {
    private static final Logger LOG = LoggerFactory.getLogger(Host.class);

    public String matches;

    @Override
    public boolean test(RuleContext ctx) {
        Pattern p = Pattern.compile(this.matches);
        Matcher m = p.matcher(ctx.forwardURI.getPath());
        boolean ok = false;
        if (m.matches()) {
            // take any group values
            for (int g = 0; g <= m.groupCount(); g++) {
                ctx.parameters.put("path." + g, m.group(g));
            }
            ok = true;
        }
        if (ctx.rule.tracing) {
            LOG.info("rule={} path={} matches={} test={}", ctx.rule.id, ctx.forwardURI.getPath(), matches, ok);
        }
        return ok;
    }

    @Override
    public String explain() {
        return "url path matches [" + matches + "]";
    }
}
