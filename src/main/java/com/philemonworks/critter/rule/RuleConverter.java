package com.philemonworks.critter.rule;

import com.google.common.collect.Sets;
import com.philemonworks.critter.action.*;
import com.philemonworks.critter.condition.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.util.Set;

public class RuleConverter {
    private static final Logger LOG = LoggerFactory.getLogger(RuleConverter.class);

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

    @SuppressWarnings("unchecked")
    public static <T> T fromXml(InputStream is) {
        StringWriter stringWriter = new StringWriter();
        try {
            IOUtils.copy(is, stringWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fromXml(stringWriter.toString());
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromXml(final String xml) {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        ErrorCollector errorHandler = new ErrorCollector();
        try {
            Schema schema = schemaFactory.newSchema(RuleConverter.class.getResource("/critter.xsd"));
            Validator validator = schema.newValidator();
            validator.setErrorHandler(errorHandler);
            validator.validate(new StreamSource(new StringReader(xml)));
        } catch (SAXException e) {
            System.err.println("whoeps: saxe: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("whoeps ioe: " + e.getMessage());
        }

        if (errorHandler.exceptions.size() > 0) {
            LOG.error("there are validation exceptions: {}", errorHandler.exceptions);
            throw new RuntimeException(StringUtils.join(errorHandler.exceptions, "<br>"));
        }

        T object = (T) getXStream().fromXML(xml);
        LOG.debug("the object T: {}", toXml(object));

        return object;
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

    private static class ErrorCollector implements ErrorHandler {
        private Set<String> exceptions = Sets.newLinkedHashSet();

        @Override
        public void warning(SAXParseException exception) throws SAXException {
            LOG.debug("SAX.warning {}", exception);
        }

        @Override
        public void error(SAXParseException exception) throws SAXException {
            LOG.debug("SAX.error {}", exception.toString());
            LOG.debug("\t{}", exception.getColumnNumber());
            LOG.debug("\t{}", exception.getLineNumber());
            LOG.debug("\t{}", exception.getPublicId());
            LOG.debug("\t{}", exception.getSystemId());
            LOG.debug("\t{}", exception.getMessage());
            exceptions.add(exception.getMessage());
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            LOG.debug("SAX.fatalError {}", exception);
            throw new SAXException("invalid xml");
        }
    }
}
