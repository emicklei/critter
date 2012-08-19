package com.philemonworks.critter.dao.mongo;

import com.mongodb.BasicDBObject;
import com.philemonworks.critter.rule.Rule;

public class RuleMapper {
    public BasicDBObject toDBObject(Rule aRule) {
        BasicDBObject dbo = new BasicDBObject();

        return dbo;
    }

    public Rule fromDBObject(BasicDBObject aRule) {
        Rule rule = new Rule();

        return rule;
    }
}
