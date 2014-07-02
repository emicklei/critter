package com.philemonworks.critter.condition;

import java.net.URI;

import com.philemonworks.critter.rule.RuleContext;
import junit.framework.TestCase;

public class HostTest extends TestCase {

    public void testHostConditionShouldTestTrueIfHostMatches() throws Exception {
        RuleContext context = new RuleContext();
        context.forwardURI = new URI("http://www.bol.com:8080/");

        Host hostCondition = new Host();
        hostCondition.matches = "www.bol.com";

        assertTrue(hostCondition.test(context));
    }

    public void testHostConditionShouldTestFalseIfHostNotMatches() throws Exception {
        RuleContext context = new RuleContext();
        context.forwardURI = new URI("http://www.bol2.com:8080/");

        Host hostCondition = new Host();
        hostCondition.matches = "www.bol.com";

        assertFalse(hostCondition.test(context));
    }

    public void testHostConditionExplainsHostMatches() throws Exception {
        Host hostCondition = new Host();
        hostCondition.matches = "www.bol.com";

        assertEquals("host name matches [www.bol.com]", hostCondition.explain());
    }

    public void testHostActionExplainsHostMatches() throws Exception {
        Host hostAction = new Host();
        hostAction.value = "www.bol.com";

        assertEquals("change the host to [www.bol.com]", hostAction.explain());
    }

    public void testHostActionPerform() throws Exception {
        // To be created... Currently too complex to test, maybe implementation is too complex?
    }
}