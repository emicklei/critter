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
                this.badExpression = true;
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
        if (this.badExpression) {
            if (ctx.rule.tracing) {
                LOG.info("rule={} xpath expression is not valid", ctx.rule.id);
            }
            return false;
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(ctx.httpContext.getRequest().getEntity(String.class));
            String matchTarget = (String) xExp.evaluate(doc, XPathConstants.STRING);
            return matches.matches(matchTarget);
        } catch (Exception ex) {
            if (ctx.rule.tracing) {
                LOG.info("rule={} xml document parse failed, err={}", ctx.rule.id, ex.toString());
            }
            LOG.error("xml document parse failed", ex);
        }
        return false;
    }
}
