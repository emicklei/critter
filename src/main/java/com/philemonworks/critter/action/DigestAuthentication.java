package com.philemonworks.critter.action;

import java.nio.charset.Charset;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import com.philemonworks.critter.rule.RuleContext;
import com.thoughtworks.xstream.core.util.Base64Encoder;

public class DigestAuthentication extends RuleIngredient implements Action {

    private static final String NONCE  = "critter.proxy.simulated.nonce";
    private static final String OPAQUE  = "critter.proxy.simulated.opaque";
    
    public String username, password, realm;
    
    @Override
    public void perform(RuleContext context) {
        String value = context.httpContext.getRequest().getHeaderValue("Authorization");
        if (StringUtils.isEmpty(value)) {
            context.forwardResponse = Response.status(401).header("WWW-Authenticate", this.compute401Header()).build();
            return;
        }
    }          
    
    public String compute401Header() {
        StringBuilder sb = new StringBuilder();
        sb
            .append("Digest realm=\"").append(this.realm).append("\",")
            .append("gop=\"auth,auth-int\",")
            .append("nonce=\"").append(NONCE).append("\",")
            .append("opaque=\"").append(OPAQUE).append("\",");            
        return sb.toString();
    }
    
	public String computeDigestAuthenticationHeaderValue(String username2, String password2) {
        String toEncode = username2 + ":" + password2;
        return 
                "Basic " +
                new Base64Encoder().encode(toEncode.getBytes(Charset.forName("utf-8")));
    }
    @Override
	public String explain() {
		return "digest authenticated with ["+username+ "] and [" + password + "] for [" + realm + "]";
	}  
}
