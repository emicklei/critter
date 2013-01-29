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
    public List<Recording> search(String host, String method, String path, String queryString, int limit) {
        List<Recording> records = new ArrayList<Recording>();
        BasicDBObject query = new BasicDBObject();
        if (!host.isEmpty()) {
            query.put("host", host);
        }
        if (!method.isEmpty()) {
            query.put("method", method);
        }
        if (!path.isEmpty()) {
            query.put("path", path);
        }
        if (!queryString.isEmpty()) {
            query.put("query", queryString);
        }         
        DBCursor cursor = collection.find(query);
        if (limit > 0) {
            cursor.limit(limit);
        } else {
            cursor.limit(10);
        }
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

    @Override
    public void deleteRecordingsByHost(String hostOrEmpty) {
        BasicDBObject query = new BasicDBObject();
        if (!hostOrEmpty.isEmpty()) {
            query.put("host", hostOrEmpty);
        }
        collection.remove(query);
    }
}
