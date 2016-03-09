package com.philemonworks.critter.httpclient;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.junit.Assert;
import org.junit.Test;

public class ProxyRoutePlannerTest {


    private static final String HTTPS_PROXY = "http://192.168.128.1:5678";
    private static final String HTTP_PROXY = "http://192.168.64.1:1234";

    @Test
    public void testHttpRouteWithProxies() throws HttpException {
        ProxyRoutePlanner proxyRoutePlanner = new ProxyRoutePlanner(HTTP_PROXY, HTTPS_PROXY, "");
        HttpHost httpHost = createHttpHost();
        HttpHost result = proxyRoutePlanner.determineProxy(httpHost, null, null);
        Assert.assertEquals("192.168.64.1", result.getHostName());
        Assert.assertEquals(1234, result.getPort());

    }

    @Test
    public void testHttpsRouteWithProxies() throws HttpException {
        ProxyRoutePlanner proxyRoutePlanner = new ProxyRoutePlanner(HTTP_PROXY, HTTPS_PROXY, "");
        HttpHost httpHost = createHttpsHost();
        HttpHost result = proxyRoutePlanner.determineProxy(httpHost, null, null);
        Assert.assertEquals("192.168.128.1", result.getHostName());
        Assert.assertEquals(5678, result.getPort());
    }

    @Test
    public void testHttpsRouteWithProxiesAndNoProxyHost() throws HttpException {
        ProxyRoutePlanner proxyRoutePlanner = new ProxyRoutePlanner(HTTP_PROXY, HTTPS_PROXY, "www.google.nl");
        HttpHost httpHost = createHttpsHost();
        Assert.assertNull(proxyRoutePlanner.determineProxy(httpHost, null, null));
    }
    
    @Test
    public void testHttpsRouteWithoutProxies() throws HttpException {
        ProxyRoutePlanner proxyRoutePlanner = new ProxyRoutePlanner(null, null, "");
        HttpHost httpHost = createHttpsHost();
        Assert.assertNull(proxyRoutePlanner.determineProxy(httpHost, null, null));
    }

    @Test
    public void testHttpRouteWithoutProxies() throws HttpException {
        ProxyRoutePlanner proxyRoutePlanner = new ProxyRoutePlanner(null, null, "");
        HttpHost httpHost = createHttpHost();
        Assert.assertNull(proxyRoutePlanner.determineProxy(httpHost, null, null));
    }

    public HttpHost createHttpHost() {
        return new HttpHost("www.google.nl", 80, "http");
    }

    public HttpHost createHttpsHost() {
        return new HttpHost("www.google.nl", 443, "https");
    }

}
