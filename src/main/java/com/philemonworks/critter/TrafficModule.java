package com.philemonworks.critter;

import org.rendershark.core.RendersharkModule;
import org.rendershark.core.jmx.JMXAction;
import org.rendershark.http.HttpServer;

import com.google.inject.AbstractModule;
import com.philemonworks.critter.ui.AdminUIResource;

public class TrafficModule extends AbstractModule {
    
    @Override
    public void configure() {
        install(new RendersharkModule.REST());
        bind(HttpServer.class);
        bind(TrafficResource.class);
        bind(JMXAction.class);       
        bind(AdminUIResource.class);
    }
}
