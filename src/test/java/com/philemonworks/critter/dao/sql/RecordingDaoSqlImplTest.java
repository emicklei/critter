package com.philemonworks.critter.dao.sql;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Before;
import org.junit.Test;

/**
 * Insert documentation here.
 *
 * @author jcraane
 */
public class RecordingDaoSqlImplTest {
    private BasicDataSource dataSource;

    @Before
    public void setUp() throws Exception {
        dataSource = new BasicDataSource();
        dataSource.setUrl(""); // TODO: Set in memory URL.

    }

    @Test
    public void save() throws Exception {

    }

    @Test
    public void search() throws Exception {

    }

    @Test
    public void deleteRecordingsByHost() throws Exception {

    }
}
