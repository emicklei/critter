package com.philemonworks.critter.dao.sql.support;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author jcraane
 */
public interface RowMapper<T> {
    T map(ResultSet rs) throws SQLException;
}
