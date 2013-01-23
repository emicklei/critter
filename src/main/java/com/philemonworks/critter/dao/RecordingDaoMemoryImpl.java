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
    public List<Recording> search(String pattern) {
        return recordings;
    }
}
