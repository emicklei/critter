package com.philemonworks.critter;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.BeforeClass;
import org.junit.Test;

public class SystemTest {
    private static boolean ENABLED = true;
    private static HttpClient HTTP;
    private static final String BASE = "http://localhost:8888";
    
    @BeforeClass
    public static void setupClient() {
        HTTP = new DefaultHttpClient();
        HttpHost proxy = new HttpHost("localhost", 8888);
        HTTP.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
    }
    @Test
    public void testNoDestination() throws Exception {
        if (!ENABLED) return;
        String content = this.get("http://nu.nl");
        System.out.println(content);
    }
    private String get(String url) throws Exception {
        HttpGet get = new HttpGet(url);
        HttpResponse response = HTTP.execute(get);
        String content = IOUtils.toString(response.getEntity().getContent());        
        EntityUtils.consume(response.getEntity());
        return content;
    }
}
