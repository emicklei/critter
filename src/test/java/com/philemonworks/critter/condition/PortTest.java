package com.philemonworks.critter.condition;

import java.net.URI;

import com.philemonworks.critter.rule.Rule;
import com.philemonworks.critter.rule.RuleContext;
import junit.framework.TestCase;

public class PortTest extends TestCase {

    public void testPortConditionShouldTestTrueIfPortMatches() throws Exception {
        RuleContext context = new RuleContext();
        context.rule = new Rule();
        context.forwardURI = new URI("http://www.bol.com:8080/");

        Port portCondition = new Port();
        portCondition.matches = "8080";

        assertTrue(portCondition.test(context));
    }

    public void testPortConditionShouldTestFalseIfPortNotMatches() throws Exception {
        RuleContext context = new RuleContext();
        context.rule = new Rule();
        context.forwardURI = new URI("http://www.bol.com:8081/");

        Port portCondition = new Port();
        portCondition.matches = "8080";

        assertFalse(portCondition.test(context));
    }

    public void testPortConditionExplains() throws Exception {
        Port portCondition = new Port();
        portCondition.matches = "8080";

        assertEquals("port number matches [8080]", portCondition.explain());
    }

    public void testPortActionExplains() throws Exception {
        Port portAction = new Port();
        portAction.value = 8080;

        assertEquals("change the port to [8080]", portAction.explain());
    }

    public void testPerform() throws Exception {
        // To be created... Currently too complex to test, maybe implementation is too complex?
    }
}