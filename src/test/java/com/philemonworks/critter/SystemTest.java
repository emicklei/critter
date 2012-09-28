package com.philemonworks.critter;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.BeforeClass;
import org.junit.Test;

public class SystemTest {
    @BeforeClass
    public static void setupClient() {
        HttpClient http = new DefaultHttpClient();
    }
    @Test
    public void testNoDestination() {
        
    }
}
