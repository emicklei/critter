package com.philemonworks.critter.ui;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLDecoder;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.rendershark.http.HttpServer;
import org.rendersnake.HtmlCanvas;
import org.rendersnake.StringResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.philemonworks.critter.TrafficManager;
import com.philemonworks.critter.rule.Rule;
import com.philemonworks.critter.rule.RuleConverter;

@Path("/ui")
public class AdminUIResource {
    private static final Logger LOG = LoggerFactory.getLogger(AdminUIResource.class);
    
	@Inject TrafficManager trafficManager;
	@Inject @Named("Proxy") HttpServer proxyServer;  
	@Inject @Named("public.host") String publicHostname;
	
	@GET
	@Path("/newrule")
	@Produces("text/html")
	public Response newRule() throws IOException {
		HtmlCanvas html = new HtmlCanvas();
		html.getPageContext()
		    .withString("rulexml", StringResource.get(("/newrule.xml")))
		    .withBoolean("proxy.started", this.proxyServer.isStarted());
		html.render(new SiteLayout(new EditRulePage()));
		return Response.ok().entity(html.toHtml()).build();
	}
	
    @GET
    @Path("/newresponse")
    @Produces("text/html")
    public Response newFixedResponse() throws IOException {
        HtmlCanvas html = new HtmlCanvas();
        html.getPageContext()
            .withObject("rule", new Rule())
            .withBoolean("proxy.started", this.proxyServer.isStarted());
        html.render(new SiteLayout(new EditFixedResponsePage()));
        return Response.ok().entity(html.toHtml()).build();
    }	
	
	@POST
	@Path("/toggleproxy")
	public Response toggleProxyActivation() throws Exception {
	    if (this.proxyServer.isStarted()) {
	        this.proxyServer.shutDown();
	    } else {
	        this.proxyServer.startUp();
	    }
	    return Response.seeOther(new URI("http://"+publicHostname)).build();
	}
	
	@POST
	@Path("/newrule")
	@Produces("text/html")
	public Response saveRule(InputStream input) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String decoded = URLDecoder.decode(reader.readLine(),"utf8"); // Despite the name, this utility class is for HTML form decoding
		int eq = decoded.indexOf('=');
		String rulexml = null;
		try {
			rulexml = decoded.substring(eq+1);
			Rule rule = (Rule)RuleConverter.fromXml(new ByteArrayInputStream(rulexml.getBytes()));
			this.trafficManager.addOrReplaceRule(rule);
		} catch (Exception ex) {
			HtmlCanvas html = new HtmlCanvas();
			html.getPageContext().withString("alert","This definition is not valid, please correct.");
			html.getPageContext().withString("rulexml",rulexml);
			html.render(new SiteLayout(new EditRulePage()));
			return Response.ok().entity(html.toHtml()).build();
		}
		return Response.seeOther(new URI("http://"+publicHostname)).build();
	}	
	
	@POST
	@Path("/newresponse")
	@Produces("text/html")
	public Response saveFixedResponse(InputStream input) throws Exception {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	    String decoded = URLDecoder.decode(reader.readLine(),"utf8"); // Despite the name, this utility class is for HTML form decoding
	    System.out.println(new FixedResponseFormDecoder().decode(decoded));
	    return Response.ok().build();
	}
	
    @GET
    @Path("/traffic.css")
    @Produces("text/css")
    public Response trafficCss() {
        return Response.ok().entity(this.getClass().getResourceAsStream("/traffic.css")).build();
    }
    
    @GET
    @Path("/traffic.js")
    @Produces("text/plain")
    public Response trafficJs() {
        return Response.ok().entity(this.getClass().getResourceAsStream("/traffic.js")).build();
    }      
    
    @GET
    @Path("/rules/{id}")
    @Produces("text/html")
    public Response showRule(@PathParam("id") String id) throws IOException {
        HtmlCanvas html = new HtmlCanvas();
        html.getPageContext()
            .withObject("rule",trafficManager.getRule(id))
            .withBoolean("proxy.started", this.proxyServer.isStarted());
        html.render(new SiteLayout(new RulePage()));
        return Response.ok().entity(html.toHtml()).build(); 
    }
    
    @GET
    @Path("/rules/{id}/edit")
    @Produces("text/html")
    public Response editRule(@PathParam("id") String id) throws IOException {
        HtmlCanvas html = new HtmlCanvas();
        Rule rule = trafficManager.getRule(id);
        html.getPageContext()
            .withObject("rule",rule)
            .withString("rulexml",RuleConverter.toXml(rule))
            .withBoolean("proxy.started", this.proxyServer.isStarted());
        html.render(new SiteLayout(new EditRulePage()));
        return Response.ok().entity(html.toHtml()).build(); 
    }       
}
