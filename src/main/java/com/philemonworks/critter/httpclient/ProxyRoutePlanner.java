package com.philemonworks.critter.httpclient;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.impl.conn.DefaultRoutePlanner;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.philemonworks.critter.Launcher;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import static com.philemonworks.critter.Constants.*;
/**
 * <p>
 * This {@link org.apache.http.conn.routing.HttpRoutePlanner HttpRoutePlanner} implementation determines whether a proxy must be used for the current request
 * based on the given properties. 
 * </p>
 * <p>
 * The {@link DefaultRoutePlanner} creates the corresponding {@link org.apache.http.conn.routing.HttpRoute}, so this class can return <code>NULL</code> when no
 * proxy is needed.
 * </p>
 */
@Singleton
public class ProxyRoutePlanner extends DefaultRoutePlanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyRoutePlanner.class);

    private final String httpProxy;
    private final String httpsProxy;
    private final String noProxy;

    @Inject
    public ProxyRoutePlanner(
            @Named(FORWARD_HTTP_PROXY) String httpProxy, 
            @Named(FORWARD_HTTPS_PROXY) String httpsProxy,
            @Named(FORWARD_NO_PROXY) String noProxy) {
        super(null);
        this.httpProxy = httpProxy;
        this.httpsProxy = httpsProxy;
        this.noProxy = noProxy;
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
        if (StringUtils.isNotEmpty(noProxy)) {
            for (String noProxyHost : StringUtils.split(noProxy, ",")) {
                if (target.getHostName().endsWith(noProxyHost)) {
                    LOGGER.debug("not proxying {}", target);
                    return false;
                }
            }
        }
        return true;
    }

    private HttpHost determineEnvironmentProxy(String schemeName) {
        if (StringUtils.isNotEmpty(getProxyForSchemeName(schemeName))) {
            return createEnvironmentProxy(schemeName);
        }
        return null;
    }

    private String getProxyForSchemeName(String schemeName) {
        if (StringUtils.equalsIgnoreCase(schemeName, "http")) {
            return httpProxy;
        }

        if (StringUtils.equalsIgnoreCase(schemeName, "https")) {
            return httpsProxy;
        }

        throw new IllegalArgumentException("No proxy for schemeName [" + schemeName + "]");
    }

    private HttpHost createEnvironmentProxy(String schemeName) {
        try {
            URI proxyUri = new URI(getProxyForSchemeName(schemeName));
            LOGGER.debug("creating proxy host {}", proxyUri);
            return new HttpHost(proxyUri.getHost(), proxyUri.getPort(), proxyUri.getScheme());
        } catch (URISyntaxException e) {
            LOGGER.warn("invalid proxy uri format: {}", e);
            return null;
        }
    }
}
