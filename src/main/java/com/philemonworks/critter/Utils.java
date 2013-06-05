package com.philemonworks.critter;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;
import java.util.Properties;

import com.philemonworks.critter.rule.RuleContext;

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
    public static String applyContextParametersTo(RuleContext context, String content) {
        String result = content;
        for (Map.Entry<String, String> entry : context.parameters.entrySet()) {            
            // speed is not a requirement so use brute force method here
            result = result.replaceAll("\\$" + entry.getKey() + "\\$", entry.getValue());
        }        
        return result;
    }       
}
