package com.philemonworks.critter.condition;

import com.philemonworks.critter.rule.RuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by emicklei on 25/02/16.
 */
public class ProtobufPath implements Condition {
    private static final Logger LOG = LoggerFactory.getLogger(XPath.class);

    // all required message definitions from .proto
    String proto;

    // addresses to fetch the .proto content ; comma separated list
    String protoURLs;

    // name of the message
    String protoMessageName;

    // dotted path
    String expression;

    // regular expression to match the string representation of the value.
    String matches;

    @Override
    public boolean test(RuleContext ctx) {
        String contentType = ctx.httpContext.getRequest().getHeaderValue("Content-Type");
        if (!"application/octet-stream".equals(contentType)) {
            return false;
        }
        //ctx.httpContext.getRequest()
        return false;
    }

    @Override
    public String explain() {
        return null;
    }
}
