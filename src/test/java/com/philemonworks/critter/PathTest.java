package com.philemonworks.critter;

import java.net.URI;


import org.junit.Assert;
import org.junit.Test;

import com.philemonworks.critter.condition.Path;
import com.philemonworks.critter.rule.RuleContext;

public class PathTest {
    @Test
    public void testParameters() throws Exception{
        Path p = new Path();
        p.matches = "/products/(.+)";
        
        RuleContext ctx = new RuleContext();
        ctx.forwardURI = new URI("http://here.com/products/123");
        Assert.assertTrue(p.test(ctx));                
        Assert.assertEquals(ctx.parameters.get("path.1"), "123");
    }
}
