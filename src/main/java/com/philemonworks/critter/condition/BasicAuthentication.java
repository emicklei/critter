package com.philemonworks.critter.condition;

import java.nio.charset.Charset;

import com.philemonworks.critter.action.RuleIngredient;
import com.philemonworks.critter.rule.RuleContext;
import com.thoughtworks.xstream.core.util.Base64Encoder;

public class BasicAuthentication extends RuleIngredient implements Condition {

    public String username, password;
    
    @Override
    public boolean test(RuleContext ctx) {
        String value = ctx.httpContext.getRequest().getHeaderValue("Authorization");
        return value != null && value.matches(this.computeBasicAuthenticationHeaderValue(username,password));
    }
	public String computeBasicAuthenticationHeaderValue(String username2, String password2) {
        String toEncode = username2 + ":" + password2;
        return 
                "Basic " +
                new Base64Encoder().encode(toEncode.getBytes(Charset.forName("utf-8")));
        //return Base64.encodeBase64String(toEncode.getBytes(Charset.forName("utf-8")));
    }
    @Override
	public String explain() {
		return "basic authenticated with ["+username+ "] and [" + password + "]";
	}    
}
