package com.philemonworks.critter;

import javax.inject.Singleton;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

@Singleton
public class ProxyFilter implements ContainerRequestFilter {

    public static final String PROXY_FILTER_URI = "ProxyFilter.URI";

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        request.getProperties().put(PROXY_FILTER_URI, request.getRequestUri());
        request.setUris(request.getBaseUri(), request.getBaseUri());        
        return request;
    }
}
