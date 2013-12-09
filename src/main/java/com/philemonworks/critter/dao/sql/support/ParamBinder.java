package com.philemonworks.critter.dao.sql.support;

import java.sql.PreparedStatement;

/**
 * @author jcraane
 */
public interface ParamBinder {
    void bind(PreparedStatement pstm);
}
