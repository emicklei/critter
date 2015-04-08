package com.philemonworks.critter;

import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.philemonworks.critter.ui.AdminUIResource;
import com.sun.jersey.api.core.ClassNamesResourceConfig;
import com.sun.jersey.server.impl.container.netty.NettyHandlerContainer;

import org.apache.commons.lang3.StringUtils;
import org.rendershark.http.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.Properties;

public class Launcher {
	private static final String PROXY_PORT = "proxy.port";
    private static final String TRAFFIC_PORT = "traffic.port";
    private static final String PROXY_HOST = "proxy.host";
    private static final String PROXY_WORKERS = "proxy.workers";
    private static final Logger LOG = LoggerFactory.getLogger(Launcher.class);
    private static final int DEFAULT_NUMBER_OF_WORKERS = Runtime.getRuntime().availableProcessors() * 2;

    public static void main(String[] args) {
    	if (args.length == 0) {
            System.out.println("No argument given - reading form environment variables");
            startWithConfiguration(createPropertiesFromEnv());
    		return;
    	}
        startWithConfiguration(createProperties(args[0]));
    }

    private static void startWithConfiguration(final Properties configProperties) {
        java.util.logging.Logger jersey = java.util.logging.Logger.getLogger("com.sun.jersey");
        jersey.setLevel(java.util.logging.Level.OFF);

        final TrafficManager manager = new TrafficManager();
        Module managerModule = new ManagerModule(new Properties(configProperties), manager);
        LOG.info("Starting Proxy Server...");
        LOG.info("using the following properties : " + configProperties.toString());
        final HttpServer proxyServer = startProxyServer(new Properties(configProperties), managerModule);
        Module proxyServerModule = new AbstractModule() {
            protected void configure() {
                this.bind(HttpServer.class)
                    .annotatedWith(Names.named("Proxy"))
                    .toInstance(proxyServer);
            }
        };
        LOG.info("Starting Traffic Server...");
        startTrafficServer(new Properties(configProperties), managerModule, proxyServerModule);
    }

    private static void startTrafficServer(Properties trafficProperties, Module managerModule, Module proxyServerModule) {
        trafficProperties.put(ClassNamesResourceConfig.PROPERTY_CLASSNAMES,
                TrafficResource.class.getName() + " " +
        		AdminUIResource.class.getName());
        String trafficPort = trafficProperties.getProperty(TRAFFIC_PORT);
		trafficProperties.put(
        		NettyHandlerContainer.PROPERTY_BASE_URI,
        		"http://" + trafficProperties.getProperty(PROXY_HOST) + ":" + trafficPort + "/");        
        startUpServerWith(trafficPort,
                HttpServer.createPropertiesModule(trafficProperties), 
                managerModule, 
                proxyServerModule,
                new TrafficModule());
	}

	private static HttpServer startProxyServer(Properties proxyProperties, Module managerModule) {
        proxyProperties.put(ClassNamesResourceConfig.PROPERTY_CLASSNAMES,ProxyResource.class.getName());
        proxyProperties.put(ClassNamesResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS,ProxyFilter.class.getName());
        String proxyPort = proxyProperties.getProperty(PROXY_PORT);
        String numberOfWorkers = proxyProperties.getProperty(PROXY_WORKERS);
		proxyProperties.put(
				NettyHandlerContainer.PROPERTY_BASE_URI,
        		"http://" + proxyProperties.getProperty(PROXY_HOST) + ":" + proxyPort + "/");
        return startUpServerWith(proxyPort, (StringUtils.isNotBlank(numberOfWorkers) ? Integer.parseInt(numberOfWorkers) : DEFAULT_NUMBER_OF_WORKERS),
                HttpServer.createPropertiesModule(proxyProperties), 
                managerModule, 
                new ProxyModule());
	}

    private static HttpServer startUpServerWith(String portString, Module... modules) {
        return startUpServerWith(portString, 2, modules);
    }

    private static HttpServer startUpServerWith(String portString, int numberOfWorkers, Module... modules) {
        int port = Integer.parseInt(portString);
        Injector injector = Guice.createInjector(modules);
        HttpServer server = injector.getInstance(HttpServer.class);
        server.setNumberOfWorkers(numberOfWorkers);
        server.init(injector, port);
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

    public static Properties createPropertiesFromEnv() {
        final Properties serverProperties = new Properties();
        serverProperties.put("proxy.host",
                StringUtils.isNotEmpty(System.getenv("proxyHost")) ? System.getenv("proxyHost") : "localhost");
        serverProperties.put("proxy.port",
                StringUtils.isNotEmpty(System.getenv("proxyPort")) ? System.getenv("proxyPort") : "8888");
        serverProperties.put("traffic.port",
                StringUtils.isNotEmpty(System.getenv("traffic")) ? System.getenv("traffic") : "8877");
        serverProperties.put("rule.database.h2.enabled",
                StringUtils.isNotEmpty(System.getenv("enabledH2")) ? System.getenv("enabledH2") : "true");
        return serverProperties;
    }

}
