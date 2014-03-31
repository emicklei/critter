package com.philemonworks.critter.dao.sql.support;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author jcraane
 */
public interface ParamBinder {
    void bind(PreparedStatement pstm) throws SQLException;
}
