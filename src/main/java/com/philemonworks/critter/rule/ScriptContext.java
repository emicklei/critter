package com.philemonworks.critter.rule;

import java.net.URI;
/**
 * ScriptContext exists for a ScriptAction that executes Javascript to process the request/response.
 * 
 * @author emicklei
 */
public interface ScriptContext {
    // Return the URI of the incoming request
    URI getUri();
    
    // Return the HTTP method of the incoming request.
    String getMethod();
    
    // Return the value of the HTTP header by its name.
    String getHeader(String name);
    
    // Return the value of a generic parameter ; can be regular expression group used in a Condition. 
    String getParameter(String name);    
       
    // Replace (or create) a response with this content and status (e.g. 200) 
    void setResponseBody(int status, String content);
    
    // For development/debugging
    void log(String what);
}
