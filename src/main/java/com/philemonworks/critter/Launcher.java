package com.philemonworks.critter;

import java.io.FileInputStream;
import java.util.Properties;

import org.rendershark.http.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.philemonworks.critter.dao.RuleDao;
import com.philemonworks.critter.dao.RuleDaoMemoryImpl;
import com.philemonworks.critter.dao.mongo.MongoModule;
import com.philemonworks.critter.dao.mongo.RuleDaoMongoImpl;
import com.philemonworks.critter.ui.AdminUIResource;
import com.sun.jersey.api.core.ClassNamesResourceConfig;
import com.sun.jersey.server.impl.container.netty.NettyHandlerContainer;

public class Launcher {
	private static final Logger LOG = LoggerFactory.getLogger(Launcher.class);
	
    public static void main(String[] args) {
    	if (args.length == 0) {
    		System.out.println("Usage: critter <properties.location>");
    		return;
    	}    
    	java.util.logging.Logger jersey = java.util.logging.Logger.getLogger("com.sun.jersey");
    	jersey.setLevel(java.util.logging.Level.OFF);
    	    	
    	final Properties mainProperties = createProperties(args[0]);

    	
        final TrafficManager manager = new TrafficManager();
        Module managerModule = new AbstractModule() {
            protected void configure() {        
                RuleDao ruleDao;
                if (mainProperties.containsKey("rule.database.mongo.host")) {
                    LOG.info("Using MongoDB rules database");
                    this.install(new MongoModule());
                    ruleDao = new RuleDaoMongoImpl();
                } else {
                    LOG.info("Using in memory rules database");
                    ruleDao = new RuleDaoMemoryImpl();
                }                
                this.bind(TrafficManager.class).toInstance(manager);
                this.bind(RuleDao.class).toInstance(ruleDao);
            }
        };
        LOG.info("Starting Proxy Server...");
        final HttpServer proxyServer = startProxyServer(mainProperties, managerModule);        
        Module proxyServerModule = new AbstractModule() {
            protected void configure() {                
                this.bind(HttpServer.class)
                    .annotatedWith(Names.named("Proxy"))
                    .toInstance(proxyServer);
            }
        };        
        LOG.info("Starting Traffic Server...");
        startTrafficServer(mainProperties, managerModule, proxyServerModule);
    }

	private static void startTrafficServer(Properties trafficProperties, Module managerModule, Module proxyServerModule) {
        trafficProperties.put(ClassNamesResourceConfig.PROPERTY_CLASSNAMES,
                TrafficResource.class.getName() + " " +
        		AdminUIResource.class.getName());
        String trafficPort = trafficProperties.getProperty("traffic.port");
		trafficProperties.put(
        		NettyHandlerContainer.PROPERTY_BASE_URI,
        		"http://" + trafficProperties.getProperty("host") + ":" + trafficPort + "/");        
        startUpServerWith(trafficPort,
                HttpServer.createPropertiesModule(trafficProperties), 
                managerModule, 
                proxyServerModule,
                new TrafficModule());
	}

	private static HttpServer startProxyServer(Properties proxyProperties, Module managerModule) {
        proxyProperties.put(ClassNamesResourceConfig.PROPERTY_CLASSNAMES,ProxyResource.class.getName());
        proxyProperties.put(ClassNamesResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS,ProxyFilter.class.getName());
        String proxyPort = proxyProperties.getProperty("proxy.port");
		proxyProperties.put(
				NettyHandlerContainer.PROPERTY_BASE_URI,
        		"http://" + proxyProperties.getProperty("host") + ":" + proxyPort + "/");
        return startUpServerWith(proxyPort,
                HttpServer.createPropertiesModule(proxyProperties), 
                managerModule, 
                new ProxyModule());
	}

    private static HttpServer startUpServerWith(String portString, Module ... modules) {
        int port = Integer.parseInt(portString);
        Injector injector = Guice.createInjector(modules);
        HttpServer server = (HttpServer)injector.getInstance(HttpServer.class);
        server.init(injector,port);
        server.startUp();
        return server;
    }
    public static Properties createProperties(String location) {
        final Properties serverProperties = new Properties(); 
        try {
            serverProperties.load(new FileInputStream(location));
        } catch (Exception ex) {
            System.err.println("Unable to load properties from;" + location);
            return null;
        }
        return serverProperties;
    }    
}
