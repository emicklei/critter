package com.philemonworks.critter.dao.file;

import com.philemonworks.critter.Recording;
import com.philemonworks.critter.dao.RecordingDao;

import java.util.List;

/**
 * Insert documentation here.
 *
 * @author jcraane
 */
public class RecordingDaoFileImpl implements RecordingDao {
    @Override
    public void save(final Recording recording) {

    }

    @Override
    public List<Recording> search(final String host, final String method, final String path, final String query, final int limit) {
        return null;
    }

    @Override
    public void deleteRecordingsByHost(final String hostOrEmpty) {

    }
}
