package com.philemonworks.critter.rule;

import java.io.InputStream;

import com.philemonworks.critter.action.Close;
import com.philemonworks.critter.action.Delay;
import com.philemonworks.critter.action.Forward;
import com.philemonworks.critter.action.Record;
import com.philemonworks.critter.action.Respond;
import com.philemonworks.critter.action.ResponseBody;
import com.philemonworks.critter.action.ResponseHeader;
import com.philemonworks.critter.action.Scheme;
import com.philemonworks.critter.action.StatusCode;
import com.philemonworks.critter.action.Trace;
import com.philemonworks.critter.condition.BasicAuthentication;
import com.philemonworks.critter.condition.Equals;
import com.philemonworks.critter.condition.RequestHeader;
import com.philemonworks.critter.condition.Host;
import com.philemonworks.critter.condition.Method;
import com.philemonworks.critter.condition.Not;
import com.philemonworks.critter.condition.Path;
import com.philemonworks.critter.condition.Port;
import com.philemonworks.critter.condition.RequestBody;
import com.philemonworks.critter.condition.XPath;
import com.thoughtworks.xstream.XStream;

public class RuleConverter {
	
	private static XStream getXStream() {
		XStream xs = new XStream();
		xs.alias("rule", Rule.class);
		xs.useAttributeFor(Rule.class, "id");
		xs.useAttributeFor(Rule.class, "enabled");
		
		// conditions
		xs.alias("host", Host.class);
		xs.useAttributeFor(Host.class, "matches");
		
        xs.alias("method", Method.class);
        xs.useAttributeFor(Method.class, "matches");		
		
		xs.alias("path", Path.class);
		xs.useAttributeFor(Path.class, "matches");
        
		xs.alias("requestheader", RequestHeader.class);
		xs.useAttributeFor(RequestHeader.class, "name");
        xs.useAttributeFor(RequestHeader.class, "matches");
		
        xs.alias("equals", Equals.class);
		xs.useAttributeFor(Equals.class, "parameter");
		xs.useAttributeFor(Equals.class, "value");
		
        xs.alias("port", Port.class);
        xs.useAttributeFor(Port.class, "matches");
        xs.useAttributeFor(Port.class, "value");
        
        xs.alias("basicauthentication", BasicAuthentication.class);
        xs.useAttributeFor(BasicAuthentication.class, "username");        
        xs.useAttributeFor(BasicAuthentication.class, "password");        
		
        xs.alias("not", Not.class);
        xs.registerConverter(new NotConverter());
        
        xs.alias("xpath", XPath.class);
        xs.useAttributeFor(XPath.class, "expression");
        xs.useAttributeFor(XPath.class, "matches");
        
        xs.alias("requestbody", RequestBody.class);
        
		// actions
		xs.alias("forward", Forward.class);
		xs.alias("delay", Delay.class);
		xs.useAttributeFor(Delay.class, "milliSeconds");
		xs.aliasAttribute("ms", "milliSeconds");
		
		xs.alias("status", StatusCode.class);
		xs.useAttributeFor(StatusCode.class, "code");
		
        xs.alias("scheme", Scheme.class);
        xs.useAttributeFor(Scheme.class, "name");		
		
		xs.alias("respond", Respond.class);
		xs.alias("responsebody",ResponseBody.class);
		
		xs.alias("responseheader",ResponseHeader.class);
		xs.useAttributeFor(ResponseHeader.class, "add");
		xs.useAttributeFor(ResponseHeader.class, "remove");
		xs.useAttributeFor(ResponseHeader.class, "value");
		
		xs.alias("close",Close.class);
		xs.alias("trace",Trace.class);		
		xs.alias("record", Record.class);
		
		return xs;
	}

	public static Object fromXml(InputStream is) {
		return (Object) getXStream().fromXML(is);
	}
	
	public static String toXml(Object o) {
		return getXStream().toXML(o);
	}
}
