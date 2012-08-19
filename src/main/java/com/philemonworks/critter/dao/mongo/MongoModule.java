package com.philemonworks.critter.dao.mongo;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public class MongoModule extends AbstractModule{

    @Override
    protected void configure() {
        Mongo mongoDB = null;
        try {
            mongoDB = new Mongo("localhost", 27017); // TODO get from props
        } catch (Exception e) {
            e.printStackTrace();
        }
        bind(DBCollection.class)
            .annotatedWith(Names.named("rules"))
            .toInstance(mongoDB.getDB("critter").getCollection("rules"));
    }
}