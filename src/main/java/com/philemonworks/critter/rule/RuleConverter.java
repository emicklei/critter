package com.philemonworks.critter.rule;

import com.philemonworks.critter.action.Close;
import com.philemonworks.critter.action.Delay;
import com.philemonworks.critter.action.DigestAuthentication;
import com.philemonworks.critter.action.Forward;
import com.philemonworks.critter.action.Record;
import com.philemonworks.critter.action.Respond;
import com.philemonworks.critter.action.ResponseBody;
import com.philemonworks.critter.action.ResponseHeader;
import com.philemonworks.critter.action.Scheme;
import com.philemonworks.critter.action.Script;
import com.philemonworks.critter.action.StatusCode;
import com.philemonworks.critter.action.Trace;
import com.philemonworks.critter.condition.BasicAuthentication;
import com.philemonworks.critter.condition.Equals;
import com.philemonworks.critter.condition.Host;
import com.philemonworks.critter.condition.Method;
import com.philemonworks.critter.condition.Not;
import com.philemonworks.critter.condition.Path;
import com.philemonworks.critter.condition.Port;
import com.philemonworks.critter.condition.RequestBody;
import com.philemonworks.critter.condition.RequestHeader;
import com.philemonworks.critter.condition.XPath;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

import java.io.InputStream;
import java.io.Writer;

public class RuleConverter {

    private static XStream getXStream() {
        XStream xs = newXStream();
        xs.alias("rule", Rule.class);
        xs.useAttributeFor(Rule.class, "id");
        xs.useAttributeFor(Rule.class, "enabled");

        // conditions
        xs.alias("host", Host.class);
        xs.useAttributeFor(Host.class, "matches");
        // is action too        
        xs.useAttributeFor(Host.class, "value");

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
        // is action too
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
        xs.alias("script", Script.class);

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
        xs.alias("responsebody", ResponseBody.class);

        xs.alias("responseheader", ResponseHeader.class);
        xs.useAttributeFor(ResponseHeader.class, "add");
        xs.useAttributeFor(ResponseHeader.class, "remove");
        xs.useAttributeFor(ResponseHeader.class, "value");

        xs.alias("close", Close.class);
        xs.alias("trace", Trace.class);
        xs.alias("record", Record.class);

        xs.alias("digestauthentication", DigestAuthentication.class);
        xs.useAttributeFor(DigestAuthentication.class, "username");
        xs.useAttributeFor(DigestAuthentication.class, "password");
        xs.useAttributeFor(DigestAuthentication.class, "realm");

        return xs;
    }

	public static Object fromXml(InputStream is) {
		return (Object) getXStream().fromXML(is);
	}

    @SuppressWarnings("unchecked")
    public static <T> T fromXml(final String xml) {
        return (T) getXStream().fromXML(xml);
    }

    public static String toXml(Object o) {
		return getXStream().toXML(o);
	}
       

    // http://oktryitnow.com/?p=11
    private static XStream newXStream() {
        return new XStream(new XppDriver() {
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out) {
                    boolean cdata = false;

                    @SuppressWarnings("rawtypes")
                    public void startNode(String name, Class clazz) {
                        super.startNode(name, clazz);
                        cdata = name.equals("body");
                    }

                    protected void writeText(QuickWriter writer, String text) {
                        if (cdata) {
                            writer.write("<![CDATA[");
                            writer.write(text);
                            writer.write("]]>");
                        } else {
                            writer.write(text);
                        }
                    }
                };
            }
        });
    }
}
