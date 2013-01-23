package com.philemonworks.critter.dao;

import java.util.List;

import com.philemonworks.critter.Recording;

public interface RecordingDao {
    void save(Recording recording);
    List<Recording> search(String pattern);
}
