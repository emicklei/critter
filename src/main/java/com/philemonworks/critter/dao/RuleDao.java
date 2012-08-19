package com.philemonworks.critter.dao;

import java.util.List;

import com.philemonworks.critter.rule.Rule;

public interface RuleDao {
    List<Rule> getRules();
    void addOrReplaceRule(Rule aRule);
    Rule getRule(String id);
    void deleteRule(String id);
}
