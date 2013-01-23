package com.philemonworks.critter;

import java.io.IOException;
import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

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
        ContainerRequest containerRequest = (ContainerRequest) httpContext.getRequest();
        URI forwardUri = (URI)containerRequest.getProperties().get(ProxyFilter.UNPROXIED_URI);
        if (forwardUri != null) LOG.trace(requestBase.getMethod() + " " + forwardUri);
        Monitor proxyMon = MonitorFactory.start(forwardUri == null ? "/" : forwardUri.getHost());
        
        RuleContext ruleContext = new RuleContext();
        ruleContext.recordingDao = trafficManager.recordingDao;
        ruleContext.httpClient = httpClient;
        ruleContext.httpContext = httpContext;
        ruleContext.forwardMethod = requestBase;
        ruleContext.forwardURI = forwardUri;

        if (null == forwardUri) {
            this.handleEmptyDestination(ruleContext);
        } else {        
            detectAndApplyRule(ruleContext);
        }        
        proxyMon.stop();        
        return ruleContext.proxyResponse;
    }

    private void detectAndApplyRule(RuleContext ruleContext) {
        Rule rule = this.trafficManager.detectRule(ruleContext);
        if (null == rule) {
            LOG.trace("No rule detected");
            Monitor mon = MonitorFactory.start("--critter.passthrough");
            new Forward().perform(ruleContext);
            new Respond().perform(ruleContext);
            mon.stop();
        } else {
            LOG.trace("Apply rule {}", rule.id);
            Monitor mon = MonitorFactory.start("--critter.rule."+rule.id);
            this.trafficManager.performRule(rule, ruleContext);
            mon.stop();
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
                    .write("If you are looking for the Admin console then use a different port (probably 8899).")
                ._p()
                .p().content("Have a nice day")
                ._body()._html();            
        } catch (IOException ex) {} //eat it
        new ResponseBody()
            .withBody(canvas.toHtml())
            .perform(ruleContext);
        new Respond()
            .perform(ruleContext);
    }
}