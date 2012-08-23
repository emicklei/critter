package com.philemonworks.critter.condition;

import com.philemonworks.critter.rule.RuleContext;

public class RequestBody implements Condition {

    public String body;
    
    @Override
    public String explain() {
        return "equal to a fixed request body";
    }

    @Override
    public boolean test(RuleContext ctx) {
        // TODO Auto-generated method stub
        return false;
    }

}
