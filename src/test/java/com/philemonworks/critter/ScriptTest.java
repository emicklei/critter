package com.philemonworks.critter;

import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.philemonworks.critter.rule.RuleContext;

public class ScriptTest {
    @Test
    public void testSum() {
        String source = "x = 3 + 4";
        try {
            Context ctx = Context.enter();
            Scriptable scope = ctx.initStandardObjects();
            Object result = ctx.evaluateString(scope, source, "<cmd>", 1, null);
            System.out.println(ctx.toString(result));
        } finally {
            Context.exit();
        }
    }
    @Test
    public void testAccessRuleContext() throws Exception{
        RuleContext rtx = new RuleContext();
        rtx.forwardURI = new URI("http://here.com");
        String source = "rtx.forwardURI";
        try {
            Context ctx = Context.enter();
            Scriptable scope = ctx.initStandardObjects();
            
            Object rtx_js = Context.javaToJS(rtx, scope);          
            ScriptableObject.putProperty(scope, "rtx", rtx_js);
            
            Object result = ctx.evaluateString(scope, source, "<cmd>", 1, null);
            
            System.out.println(ctx.toString(result));
        } finally {
            Context.exit();
        }
    }  
    @Test
    public void testAccessScriptContext() throws Exception {
        RuleContext rtx = new RuleContext();
        rtx.forwardURI = new URI("http://here.com");
        String source = IOUtils.toString(ScriptTest.class.getResourceAsStream("/respond_xml.js"));
        try {
            Context ctx = Context.enter();
            Scriptable scope = ctx.initStandardObjects();
            
            Object rtx_js = Context.javaToJS(rtx, scope);          
            ScriptableObject.putProperty(scope, "critter", rtx_js);
            
            Object result = ctx.evaluateString(scope, source, "<cmd>", 1, null);
            
            System.out.println(ctx.toString(result));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            Context.exit();
        }
    }
}
