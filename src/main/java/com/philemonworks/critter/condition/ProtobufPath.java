package com.philemonworks.critter.condition;

import com.philemonworks.critter.proto.Definitions;
import com.philemonworks.critter.proto.Inspector;
import com.philemonworks.critter.rule.RuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

/**
 * Created by emicklei on 25/02/16.
 */
public class ProtobufPath implements Condition {
    private static final Logger LOG = LoggerFactory.getLogger(ProtobufPath.class);

    // name of the message
    String messageName = "** missing messageName **";

    // dotted path
    String expression = "";

    // regular expression to match the string representation of the value.
    String matches = "";

    @Override
    public boolean test(RuleContext ctx) {
        String contentType = ctx.httpContext.getRequest().getHeaderValue("Content-Type");
        if (!"application/octet-stream".equals(contentType)) {
            if (ctx.rule.tracing) {
                LOG.info("rule={} contentType={} expected={}", ctx.rule.id, contentType, "application/octet-stream");
                return false;
            }
        }
        byte[] data = ctx.getRequestEntityContent();

        // see if we need to decode it
        String contentEncoding = ctx.httpContext.getRequest().getHeaderValue("Content-Encoding");
        if (contentEncoding != null && contentEncoding.length() > 0) {
            if (!"base64".equals(contentEncoding)) {
                if (ctx.rule.tracing) {
                    LOG.info("rule={} acceptEncoding={} expected={}", ctx.rule.id, contentEncoding, "base64");
                }
                return false;
            }
            data = Base64.getDecoder().decode(data);
        }
        if (ctx.rule.tracing) {
            LOG.info("rule={} data-base64={} data-size={}", ctx.rule.id, new String(Base64.getEncoder().encode(data)), data.length);
        }
        Definitions defs = ctx.protoDefinitions.getDefinitions(ctx.rule.id);
        if (defs.isEmpty()) {
            if (ctx.rule.tracing) {
                LOG.info("rule={} missing definitions", ctx.rule.id);
            }
            return false;
        }
        Inspector inspector = defs.newInspector(this.messageName);
        if (!inspector.read(data)) {
            if (ctx.rule.tracing) {
                LOG.info("rule={} error=unable to read protobuf message data (definitions uploaded?)", ctx.rule.id);
            }
            return false;
        }
        String value = inspector.path(this.expression);
        if (ctx.rule.tracing) {
            LOG.debug("rule={} messageName={} expression={} value={}", ctx.rule.id, this.messageName, this.expression, value);
        }
        return value.matches(this.matches);
    }

    @Override
    public String explain() {
        return "the protobuf path [" + expression + "] matches [" + matches + "] from a [" + this.messageName + "]";
    }
}
