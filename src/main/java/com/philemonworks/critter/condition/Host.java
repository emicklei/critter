package com.philemonworks.critter.condition;

import com.philemonworks.critter.action.RuleIngredient;
import com.philemonworks.critter.rule.RuleContext;

public class Host extends RuleIngredient implements Condition {
	public String matches;
	
	@Override
	public boolean test(RuleContext ctx) {
		String hostAndPort = ctx.httpContext.getRequest().getHeaderValue("Host");
		int colon = hostAndPort.indexOf(':');
		String host = colon != -1 ? hostAndPort.substring(0,colon) : hostAndPort;
		return matches.matches(host);
	}
	@Override
	public String explain() {
		return "host name matches ["+matches+"]";
	}	
}
