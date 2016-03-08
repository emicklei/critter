package com.philemonworks.critter;

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
    private static final int DEFAULT_NUMBER_OF_WORKERS = Runtime.getRuntime().availableProcessors() * 2;
    
    public static final String FORWARD_HTTP_PROXY = "forward.http.proxy";
    public static final String FORWARD_HTTPS_PROXY = "forward.https.proxy";
    public static final String FORWARD_NO_PROXY = "forward.no.proxy";
    
    private static final Logger LOG = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {
        String propertiesFileName=null;
    	if (args.length == 1) {
            propertiesFileName = args[0];
    	}

        startWithConfiguration(loadProperties(propertiesFileName));
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
        startUpServerWith(trafficPort, HttpServer.createPropertiesModule(trafficProperties), managerModule, proxyServerModule, new TrafficModule());
	}

	private static HttpServer startProxyServer(Properties proxyProperties, Module managerModule) {
        proxyProperties.put(ClassNamesResourceConfig.PROPERTY_CLASSNAMES, ProxyResource.class.getName());
        proxyProperties.put(ClassNamesResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS, ProxyFilter.class.getName());
        String proxyPort = proxyProperties.getProperty(PROXY_PORT);
        String numberOfWorkers = proxyProperties.getProperty(PROXY_WORKERS);
		proxyProperties.put(NettyHandlerContainer.PROPERTY_BASE_URI, "http://" + proxyProperties.getProperty(PROXY_HOST) + ":" + proxyPort + "/");
        return startUpServerWith(proxyPort, (StringUtils.isNotBlank(numberOfWorkers) ? Integer.parseInt(numberOfWorkers) : DEFAULT_NUMBER_OF_WORKERS),
                HttpServer.createPropertiesModule(proxyProperties), 
                managerModule, 
                new ProxyModule());
	}

    private static HttpServer startUpServerWith(String portString, Module... modules) {
        return startUpServerWith(portString, DEFAULT_NUMBER_OF_WORKERS, modules);
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

    public static Properties loadProperties(String propertiesFileName) {
        Properties p = new Properties();

        p.putAll(getDefaultProperties());
        p.putAll(getPropertiesFromFile(propertiesFileName));
        p.putAll(getPropertiesFromEnvironment());

        return p;
    }

    private static Properties getDefaultProperties() {
        Properties properties = new Properties();

        properties.setProperty(PROXY_HOST, "localhost");
        properties.setProperty(PROXY_PORT, "8888");
        properties.setProperty(TRAFFIC_PORT, "8877");
        properties.setProperty("rule.database.h2.enabled", "true");

        return properties;
    }

    public static Properties getPropertiesFromFile(String location) {
        final Properties properties = new Properties();
        if (StringUtils.isNotBlank(location)) {
            try {
                properties.load(new FileInputStream(location));
            } catch (Exception ex) {
                LOG.warn("Unable to load properties from;" + location, ex);
            }
        }
        return properties;
    }

    public static Properties getPropertiesFromEnvironment() {
        final Properties properties = new Properties();
        setAvailableSystemProperty(properties, PROXY_HOST, "proxyHost");
        setAvailableSystemProperty(properties, PROXY_PORT, "proxyPort");
        
        setAvailableSystemProperty(properties, TRAFFIC_PORT, "traffic");
        setAvailableSystemProperty(properties, "rule.database.h2.enabled", "enabledH2");
        setAvailableSystemProperty(properties, PROXY_WORKERS, "proxyWorkers");
        
        setAvailableSystemProperty(properties, FORWARD_NO_PROXY, "forwardNoProxy", "no_proxy");
        setAvailableSystemProperty(properties, FORWARD_HTTPS_PROXY, "forwardHttpsProxy", "https_proxy");
        setAvailableSystemProperty(properties, FORWARD_HTTP_PROXY, "forwardHttpProxy", "http_proxy");
        
        return properties;
    }

    private static void setAvailableSystemProperty(Properties properties, String key, String... systemPropKeys){
        for (String systemPropKey : systemPropKeys){
            String value = System.getenv(systemPropKey);
            if (StringUtils.isNotEmpty(value)){
                LOG.info("Setting property with key [{}] to [{}]", value);
                properties.setProperty(key, value);
                return;
            }
        }
    }

}
