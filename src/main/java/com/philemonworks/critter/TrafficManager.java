package com.philemonworks.critter;

import java.util.List;

import com.google.inject.Inject;
import com.philemonworks.critter.dao.RecordingDao;
import com.philemonworks.critter.dao.RuleDao;
import com.philemonworks.critter.rule.Rule;
import com.philemonworks.critter.rule.RuleContext;

public class TrafficManager {
    @Inject RuleDao ruleDao;
    @Inject RecordingDao recordingDao;
    
    public Rule detectRule(RuleContext context) {
        Rule result = null;
        for (Rule each : this.ruleDao.getRules()) {
            if (each.enabled && each.test(context) && (result == null || each.order<result.order)) {
                result = each;
            }
        }
        return result; // no matching rule
    }

    public void performRule(Rule aRule, RuleContext ruleContext) {
        aRule.perform(ruleContext);
    }

    public void addOrReplaceRule(Rule aRule) {
        aRule.ensureId();
        this.ruleDao.addOrReplaceRule(aRule);
    }

    public Rule getRule(String id) {
        return this.ruleDao.getRule(id);
    }

    public List<Rule> getAllRules() {
        return this.ruleDao.getRules();
    }

    public void deleteRule(String id) {
        this.ruleDao.deleteRule(id);
    }
}
