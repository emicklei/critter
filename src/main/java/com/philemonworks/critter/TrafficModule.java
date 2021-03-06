package com.philemonworks.critter;

import com.google.inject.AbstractModule;
import com.philemonworks.critter.rest.ProtoResource;
import com.philemonworks.critter.ui.AdminUIResource;
import org.rendershark.core.RendersharkModule;
import org.rendershark.core.jmx.JMXAction;
import org.rendershark.http.HttpServer;

public class TrafficModule extends AbstractModule {

    @Override
    public void configure() {
        install(new RendersharkModule.REST());
        bind(HttpServer.class);
        bind(TrafficResource.class);
        bind(JMXAction.class);
        bind(AdminUIResource.class);
        bind(ProtoResource.class);
    }
}
