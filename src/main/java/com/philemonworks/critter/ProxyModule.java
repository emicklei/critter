package com.philemonworks.critter;

import org.rendershark.core.RendersharkModule;
import org.rendershark.http.HttpServer;

import com.google.inject.AbstractModule;

public class ProxyModule extends AbstractModule {
    
    @Override
    public void configure() {
        install(new RendersharkModule.REST());
        bind(HttpServer.class);
        bind(ProxyResource.class); // needed?
        bind(HttpClient.class);
        bind(ProxyFilter.class); // works?
    }
}
