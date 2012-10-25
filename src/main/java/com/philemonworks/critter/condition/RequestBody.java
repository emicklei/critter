package com.philemonworks.critter.condition;

import com.philemonworks.critter.rule.RuleContext;

public class RequestBody implements Condition {

    public String body;
    
    @Override
    public String explain() {
        return "the request body matches [" + body + "]";
    }

    @Override
    public boolean test(RuleContext ctx) {
        // Depending on content type, use different match strategy
        String contentType = ctx.httpContext.getRequest().getHeaderValue("Content-Type");
        if ("application/xml".equals(contentType.toLowerCase())) {
            // Parse Xml
        } else if ("application/json".equals(contentType.toLowerCase())) {
            // Parse Json
        } else if ("application/octet-stream".equals(contentType.toLowerCase())) {
            // Use HEX compare
        }
        return false;
    }
}
