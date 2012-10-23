package com.philemonworks.critter;

import javax.inject.Singleton;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

@Singleton
public class ProxyFilter implements ContainerRequestFilter {

    public static final String PROXY_FILTER_URI = "ProxyFilter.URI";
    public static final String UNPROXIED_URI = "ProxyFilter.URI.Unproxied";

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        // remember the original request uri
        request.getProperties().put(PROXY_FILTER_URI, request.getRequestUri());
        // pre-process the proxied uri and store the unproxied uri
        request.getProperties().put(UNPROXIED_URI, Utils.extractForwardURIFrom(request.getRequestUri()));
        request.setUris(request.getBaseUri(), request.getBaseUri());        
        return request;
    }
}
