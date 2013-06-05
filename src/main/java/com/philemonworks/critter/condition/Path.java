package com.philemonworks.critter.condition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.philemonworks.critter.action.RuleIngredient;
import com.philemonworks.critter.rule.RuleContext;

public class Path extends RuleIngredient implements Condition {
	public String matches;
	
	@Override
	public boolean test(RuleContext ctx) {		
	    Pattern p = Pattern.compile(this.matches);
	    Matcher m = p.matcher(ctx.forwardURI.getPath());
	    if (m.matches()) {	        
	        // take any group values
	        for (int g=0;g<=m.groupCount();g++) {
	            ctx.parameters.put("path."+g,m.group(g));
	        }
	        return true;
	    } else {
	        return false;
	    }		
	}
	@Override
	public String explain() {
		return "url path matches ["+matches+"]";
	}	
}
