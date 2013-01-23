package com.philemonworks.critter.dao.mongo;

import javax.inject.Inject;
import javax.inject.Named;

import com.mongodb.DBCollection;
import com.philemonworks.critter.Recording;
import com.philemonworks.critter.dao.RecordingDao;

public class RecordingDaoMongoImpl implements RecordingDao {
    @Inject
    @Named("recordings")
    DBCollection collection;

    @Override
    public void save(Recording recording) {
        // TODO Auto-generated method stub
        
    }
}
