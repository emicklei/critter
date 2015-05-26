package com.philemonworks.critter;

import com.jamonapi.MonitorFactory;
import com.philemonworks.critter.rule.Rule;
import com.philemonworks.critter.rule.RuleConverter;
import com.philemonworks.critter.ui.HelpPage;
import com.philemonworks.critter.ui.HomePage;
import com.philemonworks.critter.ui.RecordingsPage;
import com.philemonworks.critter.ui.SiteLayout;
import com.philemonworks.selfdiagnose.SelfDiagnose;
import com.philemonworks.selfdiagnose.output.DiagnoseRunReporter;
import com.philemonworks.selfdiagnose.output.HTMLReporter;
import org.apache.commons.lang3.StringUtils;
import org.rendershark.core.logging.LoggerManager;
import org.rendershark.http.HttpServer;
import org.rendersnake.HtmlCanvas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;

@Path("/")
public class TrafficResource {
    private static final Logger LOG = LoggerFactory.getLogger(TrafficResource.class);

    @Inject TrafficManager trafficManager;
    @Inject LoggerManager loggerManager;
    @Inject @Named("Proxy") HttpServer proxyServer;
    @Inject @Named("proxy.host") String proxyHost;
    @Inject @Named("traffic.port") String port;

    @GET
    @Produces("text/html")
    public Response home() throws IOException {
        List<Rule> rules;
        try {
            rules = trafficManager.getAllRules();
        } catch (Exception ex) {
            LOG.error("Failed to retrieve rules",ex);
            return Response.serverError().entity(ex.getMessage()).build();
        }
        HtmlCanvas html = new HtmlCanvas();
        html.getPageContext()
            .withObject("rules", rules)
            .withBoolean("proxy.started", this.proxyServer.isStarted());
        html.render(new SiteLayout(new HomePage()));
        return Response.ok().entity(html.toHtml()).build();
    }

    @GET
    @Produces("application/xml")
    @Path("/rules/{id}")
    public Response getRule(@PathParam("id") String id) {
        String xml = null;
        Rule rule = null;
        try {
            rule = this.trafficManager.getRule(id);
            if (null != rule)
                xml = RuleConverter.toXml(rule);
        } catch (Exception ex) {
            LOG.error("Failed to retrieve rule:"+id,ex);
            return Response.serverError().entity(ex.getMessage()).build();
        }
        if (null == rule) {
            return Response.status(404).build();
        } else {
            return Response.ok().entity(xml).build();
        }
    }

    @POST
    @Path("/rules/{id}/enable")
    public Response enableRule(@PathParam("id") String id) {
        Rule rule = null;
        try {
            rule = this.trafficManager.getRule(id);
            if (null != rule) {
                // TODO: Afstemmen Ernest m.b.t. cached rules in MongoDb en enabled/disablen van rules.
                rule.enabled = true;
                trafficManager.addOrReplaceRule(rule);
            }
        } catch (Exception ex) {
            LOG.error("Failed to enable rule:"+id,ex);
            return Response.serverError().entity(ex.getMessage()).build();
        }
        if (null == rule) {
            return Response.status(404).build();
        } else {
            return Response.ok().build();
        }
    }

    @POST
    @Path("/rules/{id}/disable")
    public Response disableRule(@PathParam("id") String id) {
        Rule rule = null;
        try {
            rule = this.trafficManager.getRule(id);
            if (null != rule) {
                rule.enabled = false;
                trafficManager.addOrReplaceRule(rule);
            }
        } catch (Exception ex) {
            LOG.error("Failed to disable rule:"+id,ex);
            return Response.serverError().entity(ex.getMessage()).build();
        }
        if (null == rule) {
            return Response.status(404).build();
        } else {
            return Response.ok().build();
        }
    }

    @DELETE
    @Path("/rules/{id}")
    public Response deleteRule(@PathParam("id") String id) {
        try {
            this.trafficManager.deleteRule(id);
        } catch (Exception ex) {
            LOG.error("Failed to delete rule:"+id,ex);
            return Response.serverError().entity(ex.getMessage()).build();
        }
        return Response.ok().build();
    }

    @GET
    @Produces("application/xml")
    @Path("/rules")
    public Response getRules() {
        try {
            List<Rule> rules = this.trafficManager.getAllRules();
            String xml = RuleConverter.toXml(rules);
            return Response.ok().entity(xml).build();
        } catch (Exception ex) {
            LOG.error("Failed to retrieve rules",ex);
            return Response.serverError().entity(ex.getMessage()).build();
        }
    }

    @GET
    @Produces("text/html")
    @Path("/recordings")
    public Response getRecordings(@QueryParam("host") String host, @QueryParam("method") String method) throws IOException, URISyntaxException {
        if (StringUtils.isNotBlank(host) && StringUtils.isNotBlank(method)) {
            return Response.temporaryRedirect(new URI(String.format("/recordings/%s/%s", host, method))).build();
        } else {
            HtmlCanvas html = new HtmlCanvas();
            html.getPageContext().withString("host", host).withString("method", method);
            html.render(new SiteLayout(new RecordingsPage()));
            return Response.ok().entity(html.toHtml()).build();
        }
    }

    @GET
    @Produces("application/xml")
    @Path("/recordings/{host}/{method}")
    public Response getRecordings(
            @PathParam("host") String host,
            @PathParam("method") String method,
            @QueryParam("path") @DefaultValue("") String path,
            @QueryParam("query") @DefaultValue("")  String query,
            @QueryParam("limit") @DefaultValue("0")  int howMany
    ) {
        try {
            List<Recording> records = this.trafficManager.recordingDao.search(host,method,path,query,howMany);
            String xml = RecordingConverter.toXml(records);
            return Response.ok().entity(xml).build();
        } catch (Exception ex) {
            LOG.error("Failed to retrieve recordings",ex);
            return Response.serverError().entity(ex.getMessage()).build();
        }
    }


    @DELETE
    @Path("/recordings/{host}")
    public Response deleteRecordingsToHost(@PathParam("host") String host) {
        try {
            this.trafficManager.recordingDao.deleteRecordingsByHost(host);
            return Response.ok().build();
        } catch (Exception ex) {
            LOG.error("Failed to delete recordings",ex);
            return Response.serverError().entity(ex.getMessage()).build();
        }
    }

    @POST
    @Consumes("application/xml")
    @Path("/rules")
    public Response putRule(InputStream inputStream) {
        URI uri = null;
        try {
            Object oneOrMoreRules = RuleConverter.fromXml(inputStream);
            if (oneOrMoreRules instanceof Rule) {
            	Rule rule = (Rule)oneOrMoreRules;
            	this.trafficManager.addOrReplaceRule(rule);
            	uri = this.buildURI("/rules/" + (rule.id.replaceAll("\\s","%20")));
            } else { // it must be a list
            	@SuppressWarnings("unchecked")
                List<Rule> rules = (List<Rule>)oneOrMoreRules;
            	for (Rule each : rules) {
            		this.trafficManager.addOrReplaceRule(each);
            	}
            	uri = this.buildURI("/rules");
            }
        } catch (Exception ex) {
            LOG.error("Failed to create rule",ex);
            return Response.serverError().entity(ex.getMessage()).build();
        }
        return Response.created(uri).build();
    }

    @PUT
    @Consumes("application/xml")
    @Path("/rules/{id}")
    public Response putRule(@PathParam("id") String id, InputStream inputStream) {
        Rule rule = null;
        URI uri = null;
        try {
            rule = (Rule)RuleConverter.fromXml(inputStream);
            rule.id = id;
            this.trafficManager.addOrReplaceRule(rule);
            uri = this.buildURI("/rules/" + URLEncoder.encode(rule.id, "utf8"));
        } catch (Exception ex) {
            LOG.error("Failed to create rule:"+id,ex);
            return Response.serverError().entity(ex.getMessage()).build();
        }
        return Response.created(uri).build();
    }

    @GET
    @Path("/internal/selfdiagnose.html")
    @Produces("text/html")
    public Response reportSelfDiagnose() {
        DiagnoseRunReporter reporter = new HTMLReporter();
        SelfDiagnose.runTasks(reporter);
        return Response.ok().entity(reporter.getContent()).build();
    }

    private URI buildURI(String path) throws URISyntaxException {
    	return new URI("http://" + proxyHost + ":" + port + path);
    }

    @POST
    @Path("/internal/logger/{name}/{level}")
    public Response changeLogger(@PathParam("name") String name, @PathParam("level") String level) {
        this.loggerManager.setLoggerLevel(name, level);
        return Response.ok().entity(this.loggerManager.getLoggerLevel(name)).build();
    }

    @GET
    @Path("/example")
    @Produces("application/xml")
    public Response example() {
        return Response.ok().entity(this.getClass().getResourceAsStream("/rule-example.xml")).build();
    }

    @GET
    @Path("/help.html")
    @Produces("text/html")
    public Response help() throws IOException {
        HtmlCanvas html = new HtmlCanvas();
        html.getPageContext()
            .withBoolean("proxy.started", this.proxyServer.isStarted());
        html.render(new SiteLayout(new HelpPage()));
        return Response.ok().entity(html.toHtml()).build();
    }

    @POST
    @Path("/proxy/start")
    @Produces("text/plain")
    public Response startUpProxyServer() {
        this.proxyServer.startUp();
        return Response.ok("proxy server is started:"+this.proxyServer.isStarted()).build();
    }

    @POST
    @Path("/proxy/stop")
    @Produces("text/plain")
    public Response shutDownProxyServer() {
        this.proxyServer.shutDown();
        return Response.ok("proxy server is started:"+this.proxyServer.isStarted()).build();
    }

    @GET
    @Path("internal/stats.html")
    @Produces("text/html")
    public Response statistics(@QueryParam("flush") @DefaultValue("false") boolean doFlush) throws IOException {
        if (doFlush) MonitorFactory.reset();
        HtmlCanvas html = new HtmlCanvas();
        html.html().body().write(MonitorFactory.getReport(),false)._body()._html();
        return Response.ok().entity(html.toHtml()).build();
    }
}
