package com.philemonworks.critter.db;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;

/**
 * Insert documentation here.
 *
 * @author jcraane
 */
public class DbCreatorTest {
    @Test
    public void createDatabase() throws Exception {
        final BasicDataSource dataSource = new BasicDataSource();
        // TODO: Use in memory URL here.
        dataSource.setUrl("");
        DbCreator.create(dataSource);
    }
}
