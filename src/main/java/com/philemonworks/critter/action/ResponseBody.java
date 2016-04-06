package com.philemonworks.critter.action;

import com.philemonworks.critter.Utils;
import com.philemonworks.critter.rule.RuleContext;
import org.apache.commons.lang3.StringUtils;
import org.rendersnake.HtmlCanvas;

import javax.ws.rs.core.Response;
import java.io.IOException;

public class ResponseBody implements Action {

    public String body;
    public boolean base64;

    @Override
    public void perform(RuleContext context) {
        // compose body
        String content = Utils.applyContextParametersTo(context, body);

        context.forwardResponse = getResponseBuilder(context).entity(content).build();
    }

    private Response.ResponseBuilder getResponseBuilder(RuleContext context) {
        Response.ResponseBuilder responseBuilder = Response.ok();
        if (context.forwardResponse != null) {
            responseBuilder = Response.fromResponse(context.forwardResponse);
        }
        return responseBuilder;
    }

    @Override
    public String explain() {
        // Use a canvas to escape HTML/XML.  Explain strings may otherwise contain HTML markup.
        HtmlCanvas c = new HtmlCanvas();
        try {
            String shortBody = StringUtils.trim(body);
            if (StringUtils.length(body) > 24) {
                shortBody = StringUtils.substring(shortBody, 0, Math.min(24, StringUtils.length(shortBody)));
                shortBody = StringUtils.replace(shortBody, "\n", "") + "...";
            }
            c.write(shortBody);
        } catch (IOException e) {
        }
        return "replace the response body with [" + c.toHtml() + "]";
    }

    public Action withBody(String body) {
        this.body = body;
        return this;
    }

}
