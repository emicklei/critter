package com.philemonworks.critter.dao.mongo;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public class MongoModule extends AbstractModule{
    private static final Logger LOG = LoggerFactory.getLogger(MongoModule.class);    
    public static final String HOST = "rule.database.mongo.host";
    public static final String PORT = "rule.database.mongo.port";

    Properties constructorProperties;
    
    public MongoModule(Properties props) {
        super();
        this.constructorProperties = props;
    }
    
    @Override
    protected void configure() {
        Mongo mongoDB = null;
        try {
            // TODO Use MongoClient?
            mongoDB = new Mongo(constructorProperties.getProperty(HOST), Integer.parseInt(constructorProperties.getProperty(PORT)));
        } catch (Exception ex) {
            LOG.error("Initialize Mongo driver failed",ex);
        }
        bind(DBCollection.class)
            .annotatedWith(Names.named("rules"))
            .toInstance(mongoDB.getDB("critter").getCollection("rules"));
        bind(DBCollection.class)
            .annotatedWith(Names.named("recordings"))
            .toInstance(mongoDB.getDB("critter").getCollection("recordings"));        
    }
}