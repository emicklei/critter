package com.philemonworks.critter.dao.mongo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.philemonworks.critter.Recording;
import com.philemonworks.critter.dao.RecordingDao;

public class RecordingDaoMongoImpl implements RecordingDao {
    @Inject
    @Named("recordings")
    DBCollection collection;
    RecordingMapper mapper = new RecordingMapper();
    
    @Override
    public List<Recording> search(String pattern) {
        List<Recording> records = new ArrayList<Recording>();
        DBCursor cursor = collection.find();
        cursor.batchSize(100);
        while (cursor.hasNext()) {
            records.add(mapper.fromDBObject((BasicDBObject) cursor.next()));
        }
        cursor.close();
        return records;
    }

    @Override
    public void save(Recording recording) {
        collection.save(mapper.toDBObject(recording));
    }
}
