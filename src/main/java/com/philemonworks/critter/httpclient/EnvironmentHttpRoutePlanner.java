package com.philemonworks.critter.httpclient;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.impl.conn.DefaultRoutePlanner;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * <p>This {@link org.apache.http.conn.routing.HttpRoutePlanner HttpRoutePlanner}
 * implementation determines whether a proxy must be used for the current
 * request based on the environment variables {code}http_proxy{code},
 * {code}https_proxy{code} and {code}no_proxy{code}. These variables can be set
 * on system level and are the same in usuage as in regular Linux systems.</p>
 *
 * <p>The {@link DefaultRoutePlanner} creates the corresponding
 * {@link org.apache.http.conn.routing.HttpRoute}, so this class can return
 * <code>NULL</code> when no proxy is needed.</p>
 */
public class EnvironmentHttpRoutePlanner extends DefaultRoutePlanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnvironmentHttpRoutePlanner.class);

    public EnvironmentHttpRoutePlanner() {
        super(null);
    }

    @Override
    protected HttpHost determineProxy(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
        HttpHost proxyHost = null;

        if (hostMustBeProxied(target)) {
            proxyHost = determineEnvironmentProxy(target.getSchemeName());
        }

        return proxyHost;
    }

    private boolean hostMustBeProxied(HttpHost target) {
        if (System.getenv().containsKey("no_proxy")) {
            for (String noProxyHost : StringUtils.split(System.getenv("no_proxy"), ",")) {
                if (target.getHostName().endsWith(noProxyHost)) {
                    LOGGER.debug("not proxying {}", target);
                    return false;
                }
            }
        }
        return true;
    }

    private HttpHost determineEnvironmentProxy(String schemeName) {
        if (System.getenv().containsKey(schemeName + "_proxy")) {
            return createEnvironmentProxy(schemeName);
        }

        return null;
    }

    private HttpHost createEnvironmentProxy(String schemeName) {
        try {
            URI proxyUri = new URI(System.getenv().get(schemeName + "_proxy"));
            LOGGER.debug("creating proxy host {}", proxyUri);
            return new HttpHost(proxyUri.getHost(), proxyUri.getPort(), proxyUri.getScheme());
        } catch (URISyntaxException e) {
            LOGGER.warn("invalid proxy uri format: {}", e);
            return null;
        }
    }
}
