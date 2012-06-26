package com.philemonworks.critter;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jamonapi.MonitorFactory;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.spi.container.ContainerRequest;

@Singleton
public class HttpClient {
    private static final Logger LOG = LoggerFactory.getLogger(HttpClient.class);
    public static final int DEFAULT_MAX_CONNECTIONS = 100;
    public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 10;

    private ThreadSafeClientConnManager connectionManager;
    protected DefaultHttpClient httpClient;
    protected HttpParams params;

    @Inject
    public void init() {
        LOG.info("Preparing for using Http connections");
        this.connectionManager = new ThreadSafeClientConnManager();
        this.httpClient = this.params == null ? new DefaultHttpClient(connectionManager) : new DefaultHttpClient(connectionManager, this.params);
        connectionManager.setMaxTotal(DEFAULT_MAX_CONNECTIONS);
        connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTIONS_PER_ROUTE);
    }

    public Response forward(HttpContext ctx, HttpRequestBase forwardRequest) {
        // get
        ContainerRequest containerRequest = (ContainerRequest) ctx.getRequest();
        URI requestURI = (URI) containerRequest.getProperties().get(ProxyFilter.PROXY_FILTER_URI);
        forwardRequest.setURI(Utils.extractForwardURIFrom(requestURI));
        
        if (forwardRequest instanceof HttpEntityEnclosingRequestBase) {
            HttpEntityEnclosingRequestBase encloser = (HttpEntityEnclosingRequestBase) forwardRequest;
            String lengthString = containerRequest.getHeaderValue("Content-Length");
            int length = 0;
            if (lengthString != null)
                length = Integer.parseInt(lengthString);
            encloser.setEntity(new InputStreamEntity(containerRequest.getEntityInputStream(), length));
        }

        // copy headers
        for (Entry<String, List<String>> each : containerRequest.getRequestHeaders().entrySet()) {
            // omit proxy headers
            if ("Proxy-Connection.Host.Content-Length".indexOf(each.getKey()) == -1) {
                for (String other : each.getValue()) {
                    LOG.trace("Forward copy header:{}", each);
                    forwardRequest.addHeader(each.getKey(), other);
                }
            } else {
                LOG.trace("Skip forward header:{}", each);
            }
        }

        HttpResponse forwardResponse = null;
        try {
            forwardResponse = httpClient.execute(forwardRequest);
        } catch (Exception ex) {
            LOG.error(forwardRequest.getRequestLine().toString(), ex);
            return Response.serverError().entity(ex.getMessage()).build();
        }

        Response.ResponseBuilder containerResponse = Response.status(forwardResponse.getStatusLine().getStatusCode());

        // copy headers
        for (Header each : forwardResponse.getAllHeaders()) {
            if ("Transfer-Encoding".indexOf(each.getName()) == -1) {
                LOG.trace("Backward copy header:" + each.toString());
                containerResponse.header(each.getName(), each.getValue());
            } else {
                LOG.trace("Skip backward header:" + each.toString());
            }
        }

        try {
            if (forwardResponse.getEntity() != null) {
                containerResponse.entity(forwardResponse.getEntity().getContent());
            }
            return containerResponse.build();
        } catch (Exception ex) {
            try {
                EntityUtils.consume(forwardResponse.getEntity());
            } catch (IOException ioex) {
                LOG.error("Consuming content from response failed", ioex);
            }
            LOG.error("Reading response failed", ex);
            return Response.serverError().entity(ex.getMessage()).build();
        } finally {
            LOG.trace("Connections in pool:{}", connectionManager.getConnectionsInPool());
            MonitorFactory.add("--critter.http.pool.size", "count", connectionManager.getConnectionsInPool() * 1.0d);
        }
    }
}
