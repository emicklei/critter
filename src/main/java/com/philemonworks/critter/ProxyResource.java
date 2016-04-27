package com.philemonworks.critter;

import java.io.IOException;
import java.net.URI;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.philemonworks.critter.proto.Definitions;
import com.philemonworks.critter.proto.DefinitionsPerRule;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.rendersnake.HtmlCanvas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import com.philemonworks.critter.action.Forward;
import com.philemonworks.critter.action.Respond;
import com.philemonworks.critter.action.ResponseBody;
import com.philemonworks.critter.action.ResponseHeader;
import com.philemonworks.critter.rule.Rule;
import com.philemonworks.critter.rule.RuleContext;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.spi.container.ContainerRequest;

@Path("/")
@Consumes("*/*")
public class ProxyResource {
    private static final Logger LOG = LoggerFactory.getLogger(ProxyResource.class);

    @Inject
    private TrafficManager trafficManager;
    @Inject
    private HttpClient httpClient;
    @Inject
    private DefinitionsPerRule protoDefinitions;

    @GET
    public Response handleGet(@Context HttpContext httpContext) {
        return this.performActionsFor(httpContext, new HttpGet());
    }

    @POST
    public Response handlePost(@Context HttpContext httpContext) {
        return this.performActionsFor(httpContext, new HttpPost());
    }

    @PUT
    public Response handlePut(@Context HttpContext httpContext) {
        return this.performActionsFor(httpContext, new HttpPut());
    }

    @DELETE
    public Response handleDelete(@Context HttpContext httpContext) {
        return this.performActionsFor(httpContext, new HttpDelete());
    }

    private Response performActionsFor(HttpContext httpContext, HttpRequestBase requestBase) {
        try {
            ContainerRequest containerRequest = (ContainerRequest) httpContext.getRequest();
            URI forwardUri = (URI) containerRequest.getProperties().get(ProxyFilter.UNPROXIED_URI);
            if (forwardUri != null) LOG.debug(requestBase.getMethod() + " " + forwardUri);

            Monitor proxyMon = MonitorFactory.start(forwardUri == null ? "/" : forwardUri.getHost());
            try {
                RuleContext ruleContext = new RuleContext();
                ruleContext.recordingDao = trafficManager.recordingDao;
                ruleContext.httpClient = httpClient;
                ruleContext.httpContext = httpContext;
                ruleContext.forwardMethod = requestBase;
                ruleContext.forwardURI = forwardUri;
                ruleContext.protoDefinitions = this.protoDefinitions;

                if (null == forwardUri) {
                    this.handleEmptyDestination(ruleContext);
                } else {
                    detectAndApplyRule(ruleContext);
                }
                return ruleContext.proxyResponse;
            } finally {
                proxyMon.stop();
            }
        } catch (Exception ex) {
            LOG.error("performActionsFor failed", ex);
            return Response.serverError().entity(ex.toString()).build();
        }
    }

    private void detectAndApplyRule(RuleContext ruleContext) {
        Rule rule = null;
        Monitor mon = MonitorFactory.start("--critter.detect");
        try {
            rule = this.trafficManager.detectRule(ruleContext);
        } finally {
            mon.stop();
        }
        if (null == rule) {
            mon = MonitorFactory.start("--critter.passthrough");
            try {
                new Forward().perform(ruleContext);
                new Respond().perform(ruleContext);
            } finally {
                mon.stop();
            }
        } else {
            mon = MonitorFactory.start("--critter.perform." + rule.id);
            try {
                this.trafficManager.performRule(rule, ruleContext);
            } finally {
                mon.stop();
            }
        }
    }

    private void handleEmptyDestination(RuleContext ruleContext) {
        new ResponseHeader()
                .with("Content-Type", "text/html")
                .perform(ruleContext);
        HtmlCanvas canvas = new HtmlCanvas();
        try {
            canvas.html().body()
                    .p().write("You have reached the endpoint of the")
                    .h2().content("Critter Http Proxy Server.")
                    .br()
                    .write("If you are looking for the Admin console then use a different port.")
                    ._p()
                    .p().content("Have a nice day")
                    ._body()._html();
        } catch (IOException ex) {
        } //eat it
        new ResponseBody()
                .withBody(canvas.toHtml())
                .perform(ruleContext);
        new Respond()
                .perform(ruleContext);
    }
}
