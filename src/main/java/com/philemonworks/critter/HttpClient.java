package com.philemonworks.critter;

import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;

import com.philemonworks.critter.httpclient.NoRedirectStrategy;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;
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
    public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 100;

    private PoolingHttpClientConnectionManager connectionManager;
    protected org.apache.http.client.HttpClient httpClient;

    @Inject
    public void init() {
        LOG.info("Preparing for using Http connections");
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(DEFAULT_MAX_CONNECTIONS);
        connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTIONS_PER_ROUTE);

        httpClient = HttpClients.custom().setConnectionManager(connectionManager).setRedirectStrategy(new NoRedirectStrategy()).build();
    }

    public Response forward(HttpContext ctx, HttpRequestBase forwardRequest, URI forwardURI) {
        // get
        ContainerRequest containerRequest = (ContainerRequest) ctx.getRequest();
        forwardRequest.setURI(forwardURI);
        
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
        } catch (UnknownHostException uhe) {
            return Response.status(404).entity("Unknown host: "+uhe.getMessage()).build();
        } catch (Exception ex) {
            LOG.error(forwardRequest.getRequestLine().toString()); // not the whole stack
            LOG.trace(forwardRequest.getRequestLine().toString(), ex); // with the whole stack
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
            PoolStats totalStats = connectionManager.getTotalStats();
            LOG.trace("Connections in pool:{}", totalStats);
            MonitorFactory.add("--critter.http.pool.size", "count", (totalStats.getAvailable() + totalStats.getLeased()) * 1.0d);
        }
    }
}
