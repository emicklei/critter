package com.philemonworks.critter.dao.mongo;

import java.util.Map.Entry;

import com.mongodb.BasicDBObject;
import com.philemonworks.critter.Recording;

public class RecordingMapper {

    public BasicDBObject toDBObject(Recording record) {
        BasicDBObject dbo = new BasicDBObject();
        dbo.append("host", record.host);
        dbo.append("method", record.method);
        dbo.append("path", record.path);
        dbo.append("query", record.query);
        
        dbo.append("requestContent", record.requestContent);
        dbo.append("date", record.requestReceivedDate);
        dbo.append("responseContent", record.responseContent);
        
        BasicDBObject headersIn = new BasicDBObject();       
        for (String each : record.requestHeaders.keySet()) {
            String value = record.requestHeaders.get(each);
            if (!each.startsWith("_")) {
                headersIn.append(each, value);
            }
        }
        dbo.append("requestHeaders", headersIn);
        
        BasicDBObject headersOut = new BasicDBObject();       
        for (String each : record.responseHeaders.keySet()) {
            String value = record.responseHeaders.get(each);
            if (!each.startsWith("_")) {
                headersOut.append(each, value);
            }
        }
        dbo.append("responseHeaders", headersOut);
        return dbo;
    }

    public Recording fromDBObject(BasicDBObject recDoc) {
        Recording record = new Recording();
        record.host = recDoc.getString("host");
        record.method = recDoc.getString("method");
        record.path = recDoc.getString("path");
        record.query = recDoc.getString("query");
        
        record.requestContent = recDoc.getString("requestContent");
        record.requestReceivedDate = recDoc.getDate("date");
        record.responseContent = recDoc.getString("responseContent");
        
        BasicDBObject headersIn = (BasicDBObject)recDoc.get("requestHeaders");
        if (headersIn != null) {
            for (Entry<String,Object> each : headersIn.entrySet()) {
                record.requestHeaders.put(each.getKey(),(String)each.getValue());
            }
        }
        BasicDBObject headersOut = (BasicDBObject)recDoc.get("responseHeaders");
        if (headersOut != null) {
            for (Entry<String,Object> each : headersOut.entrySet()) {
                record.responseHeaders.put(each.getKey(),(String)each.getValue());
            }
        }        
        return record;
    }
}
