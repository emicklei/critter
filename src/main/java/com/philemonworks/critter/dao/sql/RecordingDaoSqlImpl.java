package com.philemonworks.critter.dao.sql;

import com.google.inject.Inject;
import com.philemonworks.critter.Recording;
import com.philemonworks.critter.dao.RecordingDao;

import javax.sql.DataSource;
import java.util.List;

/**
 * Insert documentation here.
 *
 * @author jcraane
 */
public class RecordingDaoSqlImpl implements RecordingDao {
    @Inject
    private DataSource dataSource;

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
