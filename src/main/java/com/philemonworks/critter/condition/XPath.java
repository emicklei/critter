package com.philemonworks.critter.condition;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.philemonworks.critter.rule.RuleContext;

public class XPath implements Condition {
    private static final Logger LOG = LoggerFactory.getLogger(XPath.class);
    
    String expression, matches;
    XPathExpression cachedXPathExpression;
    boolean badExpression = false;
    
    @Override
    public String explain() {
        return "the xpath expression [" + expression + "] on the request XML body matches [" + matches + "]";
    }

    public XPathExpression getXPathExpression() {
        if (cachedXPathExpression == null) {
            XPathFactory xPathfactory = XPathFactory.newInstance();
            javax.xml.xpath.XPath xpath = xPathfactory.newXPath();
            try {
                cachedXPathExpression = xpath.compile(this.expression);
            } catch (XPathExpressionException e) {
                LOG.error("xpath compile failed",e);
                this.badExpression = true;  // TODO notify the user somehow
            }
        }
        return cachedXPathExpression;
    }
    
    @Override
    public boolean test(RuleContext ctx) {
        String contentType = ctx.httpContext.getRequest().getHeaderValue("Content-Type");
        if (!"application/xml".equals(contentType)) {
            return false;
        }
        XPathExpression xExp = this.getXPathExpression();
        if (this.badExpression) 
            return false;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(ctx.httpContext.getRequest().getEntity(String.class));
            String matchTarget = (String)xExp.evaluate(doc, XPathConstants.STRING);
            return matches.matches(matchTarget);
        } catch (Exception ex) {
            LOG.error("xml document parse failed", ex);            
        }
        return false;
    }

}
