package com.philemonworks.critter.dao.mongo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.philemonworks.critter.dao.RuleDao;
import com.philemonworks.critter.rule.Rule;

public class RuleDaoMongoImpl implements RuleDao {
    @Inject
    @Named("rules")
    DBCollection collection;
    private RuleMapper mapper = new RuleMapper();
    private List<Rule> cachedRules;

    public synchronized List<Rule> getRules() {
        // Lazy fetch
        if (cachedRules == null) {
            cachedRules = this.fetchAllRules();
        }
        return cachedRules;
    }

    private void flush() {
        cachedRules = null;
    }

    private List<Rule> fetchAllRules() {
        List<Rule> rules = new ArrayList<Rule>();
        DBCursor cursor = collection.find();
        cursor.batchSize(100);
        while (cursor.hasNext()) {
            rules.add(mapper.fromDBObject((BasicDBObject) cursor.next()));
        }
        cursor.close();
        return rules;
    }

    public void addOrReplaceRule(Rule aRule) {
        DBObject query = new BasicDBObject();
        query.put("id", aRule.id);
        collection.findAndModify(
                query, 
                null, // DBObject fields
                null, // DBObject sort
                false, //boolean remove
                mapper.toDBObject(aRule), // DBObject update
                false, // boolean returnNew
                true); // boolean upsert 
        this.flush();
    }

    public Rule getRule(String id) {
        for (Rule each : this.getRules()) {
            if (each.id.equals(id))
                return each;
        }
        return null;
    }

    public void deleteRule(String id) {
        DBObject query = new BasicDBObject();
        query.put("id", id);
        collection.remove(query);
        this.flush();
    }
}