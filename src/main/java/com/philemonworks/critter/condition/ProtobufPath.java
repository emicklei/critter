package com.philemonworks.critter.condition;

import com.philemonworks.critter.proto.Inspector;
import com.philemonworks.critter.rule.RuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

/**
 * Created by emicklei on 25/02/16.
 */
public class ProtobufPath implements Condition {
    private static final Logger LOG = LoggerFactory.getLogger(XPath.class);

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
            LOG.debug("got {} want {}", contentType, "application/octet-stream");
            return false;
        }
        byte[] data = ctx.httpContext.getRequest().getEntity(byte[].class);

        // see if we need to decode it
        String acceptEncoding = ctx.httpContext.getRequest().getHeaderValue("Content-Type");
        if (acceptEncoding.length() > 0) {
            if (!"base64".equals(acceptEncoding)) {
                LOG.debug("got {} want {}", acceptEncoding, "base64");
                return false;
            }
            data = Base64.getDecoder().decode(data);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("data base64:{}, data size:{}", new String(Base64.getEncoder().encode(data)), data.length);
        }
        Inspector inspector = ctx.protoDefinitions.getDefinitions(ctx.rule.id).newInspector(this.messageName);
        if (!inspector.read(data)) {
            LOG.debug("unable to read protobuf message data");
            return false;
        }
        String value = inspector.path(this.expression);
        LOG.debug("inspector for {} on {} returns {}", this.expression, this.messageName, value);
        return value.matches(this.matches);
    }

    @Override
    public String explain() {
        return "the protobuf path [" + expression + "] matches [" + matches + "] from a [" + this.messageName + "]";
    }
}
