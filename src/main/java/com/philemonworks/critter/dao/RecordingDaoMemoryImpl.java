package com.philemonworks.critter.dao;

import java.util.ArrayList;
import java.util.List;

import com.philemonworks.critter.Recording;

public class RecordingDaoMemoryImpl implements RecordingDao {

    List<Recording> recordings = new ArrayList<Recording>();

    @Override
    public void save(Recording recording) {
        recordings.add(recording);
    }

    @Override
    public List<Recording> search(String host, String method, String path, String query, int limit) {
        List<Recording> result = new ArrayList<Recording>();
        for (Recording each : this.recordings) {
            if ((!host.isEmpty() && host.equals(each.host)) && (!method.isEmpty() && method.equals(each.method)) && (path.isEmpty() || path.equals(each.path))
                    && (query.isEmpty() || query.equals(each.query))) {
                result.add(each);
                if (limit > 0 && result.size() == limit) {
                    return result; // early exit
                }
            }
        }
        return result;
    }

    @Override
    public void deleteRecordingsByHost(String hostOrEmpty) {
        List<Recording> result = new ArrayList<Recording>();
        if (!hostOrEmpty.isEmpty()) {
            for (Recording each : this.recordings) {
                if (!hostOrEmpty.equals(each.host)) {
                    result.add(each);
                }
            }
        }
        recordings = result;
    }
}
