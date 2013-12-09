package com.philemonworks.critter.dao.sql.support;

import com.google.common.collect.Lists;
import com.philemonworks.critter.SqlRuntimeException;
import com.philemonworks.critter.Utils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author jcraane
 */
public class JdbcTemplate {
    private final DataSource dataSource;

    public JdbcTemplate(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> List<T> queryForList(final String query, final RowMapper<T> rowMapper, final ParamBinder paramBinder) {
        final List<T> result = Lists.newArrayList();
        Connection connection = null;
        PreparedStatement pstm = null;

        try {
            connection = dataSource.getConnection();
            pstm = connection.prepareStatement(query);
            bindParamsIfBinderIsPresent(paramBinder, pstm);

            final ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                result.add(rowMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new SqlRuntimeException(e);
        } finally {
            Utils.close(pstm, connection);
        }

        return result;
    }

    private void bindParamsIfBinderIsPresent(final ParamBinder paramBinder, final PreparedStatement pstm) {
        if (paramBinder != null) {
            paramBinder.bind(pstm);
        }
    }
}
