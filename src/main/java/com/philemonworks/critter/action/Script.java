package com.philemonworks.critter.action;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.philemonworks.critter.rule.RuleContext;

public class Script implements Action {
    private static final Logger LOG = LoggerFactory.getLogger(Script.class);
    
    public String body;

    @Override
    public String explain() {
        return "executes a Javascript snippet";
    }

    @Override
    public void perform(RuleContext context) {
        try {
            Context ctx = Context.enter();
            Scriptable scope = ctx.initStandardObjects();
            Object rtx_js = Context.javaToJS(context, scope);
            ScriptableObject.putProperty(scope, "critter", rtx_js);
            ctx.evaluateString(scope, this.body, "<cmd>", 1, null);
        } catch (Exception ex) {
            LOG.error("Failed to execute script",ex);
        } finally {
            Context.exit();
        }
    }
}
