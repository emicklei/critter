package com.philemonworks.critter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.philemonworks.critter.rule.Rule;
import com.philemonworks.critter.rule.RuleContext;
import com.sun.jersey.api.NotFoundException;
import com.sun.jersey.api.core.HttpContext;

public class TrafficManager {
    private List<Rule> rules = Collections.synchronizedList(new ArrayList<Rule>());

    public Rule detectRule(HttpContext context) {
        for (Rule each : this.rules) {
            RuleContext ruleContext = new RuleContext();
            ruleContext.httpContext = context;
            if (each.enabled && each.test(ruleContext))
                return each;
        }
        return null; // no matching rule
    }

    public void performRule(Rule aRule, RuleContext ruleContext) {
        aRule.perform(ruleContext);
    }

    public void addOrReplaceRule(Rule aRule) {
        aRule.ensureId();
        for (Rule each : rules.toArray(new Rule[0])) { // create a copy to iterate
            if (each.id.equals(aRule.id)) {
                rules.remove(each);  // about to replace
                break;
            }
        }
        rules.add(aRule);
    }

    public Rule getRule(String id) {
        for (Rule each : rules) {
            if (each.id.equals(id))
                return each;
        }
        return null;
    }

    public List<Rule> getAllRules() {
        List<Rule> sorted = new ArrayList<Rule>();
		// TODO implement sorting
        for (Rule each : rules) sorted.add(each);
        return sorted;
    }

    public void deleteRule(String id) {
        for (Rule each : rules) {
            if (each.id.equals(id)) {
                rules.remove(each);
                return;
            }
        }
        throw new NotFoundException("No such rule with id:"+id);
    }
}