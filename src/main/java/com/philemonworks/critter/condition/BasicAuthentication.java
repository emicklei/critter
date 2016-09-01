package com.philemonworks.critter.condition;

import java.nio.charset.Charset;

import com.philemonworks.critter.action.RuleIngredient;
import com.philemonworks.critter.rule.RuleContext;
import com.thoughtworks.xstream.core.util.Base64Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicAuthentication extends RuleIngredient implements Condition {
    private static final Logger LOG = LoggerFactory.getLogger(BasicAuthentication.class);

    public String username, password;

    @Override
    public boolean test(RuleContext ctx) {
        String actual = ctx.httpContext.getRequest().getHeaderValue("Authorization");
        String expected = this.computeBasicAuthenticationHeaderValue(username, password);
        boolean ok = actual.matches(expected);
        if (ctx.rule.tracing) {
            LOG.info("rule={} actual={} expected={} test={}", ctx.rule.id, actual, expected, ok);
        }
        return ok;
    }

    public String computeBasicAuthenticationHeaderValue(String username2, String password2) {
        String toEncode = username2 + ":" + password2;
        return
                "Basic " +
                        new Base64Encoder().encode(toEncode.getBytes(Charset.forName("utf-8")));
    }

    @Override
    public String explain() {
        return "basic authenticated with [" + username + "] and [" + password + "]";
    }
}
