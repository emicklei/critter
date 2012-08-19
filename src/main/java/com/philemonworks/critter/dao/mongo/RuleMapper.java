package com.philemonworks.critter.dao.mongo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.mongodb.BasicDBObject;
import com.philemonworks.critter.rule.Rule;
import com.philemonworks.critter.rule.RuleConverter;

public class RuleMapper {
    
    public BasicDBObject toDBObject(Rule aRule) {
        BasicDBObject dbo = new BasicDBObject();
        dbo.append("id",aRule.id);        
        dbo.append("xml", RuleConverter.toXml(aRule));
        return dbo;
    }

    public Rule fromDBObject(BasicDBObject ruleDoc) {
        String xml = ruleDoc.getString("xml");
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        Rule rule = (Rule)RuleConverter.fromXml(is);
        return rule;
    }
}
