package com.philemonworks.critter.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.philemonworks.critter.rule.Rule;

public class RuleDaoMemoryImpl implements RuleDao {
    private List<Rule> rules = Collections.synchronizedList(new ArrayList<Rule>());

    public List<Rule> getRules() {
        return rules;
    }
    public void addOrReplaceRule(Rule aRule) {
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
    public void deleteRule(String id) {
        for (Rule each : rules) {
            if (each.id.equals(id)) {
                rules.remove(each);
                return;
            }
        }
        throw new IllegalArgumentException("No such rule with id:"+id);
    } 
}
