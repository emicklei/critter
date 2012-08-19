package com.philemonworks.critter.dao.mongo;

import javax.inject.Inject;
import javax.inject.Named;

import com.mongodb.DBCollection;
import com.philemonworks.critter.dao.RuleDao;

public class RuleDaoMongoImpl implements RuleDao{
    @Inject @Named("rules")  DBCollection collection;
}
