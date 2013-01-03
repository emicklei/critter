package com.philemonworks.critter.rule;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.http.client.methods.HttpRequestBase;

import com.philemonworks.critter.HttpClient;
import com.sun.jersey.api.core.HttpContext;

public class RuleContext {
	public HttpContext httpContext;
	public HttpClient httpClient;
	public URI forwardURI;
	public HttpRequestBase forwardMethod;
	public Response forwardResponse;
	public Response proxyResponse;
	
	public Map<String,String> parameters = new HashMap<String,String>();	
}
