package com.philemonworks.critter.rule;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.http.client.methods.HttpRequestBase;

import com.philemonworks.critter.HttpClient;
import com.philemonworks.critter.dao.RecordingDao;
import com.sun.jersey.api.core.HttpContext;

public class RuleContext {
	public HttpContext httpContext;
	public HttpClient httpClient;
	public URI forwardURI;
	public HttpRequestBase forwardMethod;
	public Response forwardResponse;
	public Response proxyResponse;
	
	// for Record action
    public Date requestReceivedDate = new Date();	
    public RecordingDao recordingDao;
    
	public Map<String,String> parameters = new HashMap<String,String>();	
}
