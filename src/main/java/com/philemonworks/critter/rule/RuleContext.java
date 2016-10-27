package com.philemonworks.critter.rule;

import com.philemonworks.critter.HttpClient;
import com.philemonworks.critter.dao.RecordingDao;
import com.philemonworks.critter.proto.DefinitionsPerRule;
import com.sun.jersey.api.core.HttpContext;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RuleContext implements ScriptContext {
    private static final Logger LOG = LoggerFactory.getLogger(ScriptContext.class);
    public Rule rule;
    public HttpContext httpContext;
    public HttpClient httpClient;
    public URI forwardURI;
    public HttpRequestBase forwardMethod;
    public Response forwardResponse;
    public Response proxyResponse;
    // this caches the request entity stream contents
    public byte[] requestEntityContent;

    // for access by ProtobufPath
    public DefinitionsPerRule protoDefinitions;

    // for Record action
    public Date requestReceivedDate = new Date();
    public RecordingDao recordingDao;

    public Map<String, String> parameters = new HashMap<String, String>();

    @Override
    public URI getUri() {
        return forwardURI;
    }

    @Override
    public String getMethod() {
        if (httpContext.getRequest() == null) {
            LOG.error("No request available, return <empty> HTTP method");
            return "";
        }
        return httpContext.getRequest().getMethod();
    }

    @Override
    public String getParameter(String name) {
        return parameters.get(name);
    }

    @Override
    public void setResponseBody(int status, String content) {
        if (forwardResponse != null) {
            forwardResponse = Response.fromResponse(forwardResponse).entity(content).build();
        } else {
            forwardResponse = Response.status(status).entity(content).build();
        }
    }

    @Override
    public String getHeader(String name) {
        if (httpContext.getRequest() == null) {
            LOG.error("No request available, return <empty> header value");
            return "";
        }
        return httpContext.getRequest().getHeaderValue(name);
    }

    @Override
    public void log(String what) {
        LOG.debug(what);
    }

    public byte[] getRequestEntityContent() {
        if (null == requestEntityContent) {
            this.requestEntityContent = this.httpContext.getRequest().getEntity(byte[].class);
        }
        return this.requestEntityContent;
    }
}
