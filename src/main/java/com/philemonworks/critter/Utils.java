package com.philemonworks.critter;

import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

public class Utils {
    /**
     * If no forward URI is given then return null.
     * @param requestURI
     * @return
     */
    public static URI extractForwardURIFrom(URI requestURI) {
        // http://localhost:8888http://boldapp26.dev.bol.com:9998/testspecs
        int http = requestURI.toString().indexOf("http",4);
        if (http == -1) 
            return null;
        return URI.create(requestURI.toString().substring(http));
    }
    public static String getMavenVersion() {
        try {
            InputStream is = Utils.class.getResourceAsStream("/META-INF/maven/com.philemonworks/critter/pom.properties");        
            Properties props = new Properties();
            props.load(is);
            return props.getProperty("version");
        } catch (Exception ex) {
            return "";
        }
    }    
}
