package com.philemonworks.critter.dao;

import java.util.List;

import com.philemonworks.critter.Recording;

public interface RecordingDao {
    void save(Recording recording);
    List<Recording> search(String host, String method, String path, String query, int limit);
    void deleteRecordingsByHost(String hostOrEmpty); 
}
