package com.philemonworks.critter.dao.sql.support;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.philemonworks.critter.Utils;

import javax.sql.DataSource;
import java.io.IOException;
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

    @Inject
    public JdbcTemplate(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> T queryForObject(final String sql, final RowMapper<T> rowMapper, final ParamBinder paramBinder) {
        Connection connection = null;
        PreparedStatement pstm = null;

        try {
            connection = dataSource.getConnection();
            pstm = connection.prepareStatement(sql);
            bindParamsIfBinderIsPresent(paramBinder, pstm);

            final ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                return rowMapper.map(rs);
            }
        } catch (SQLException e) {
            throw new SqlRuntimeException(e);
        } catch (IOException e) {
            throw new SqlRuntimeException(e);
        } finally {
            Utils.close(pstm, connection);
        }

        return null;
    }

    public <T> List<T> queryForList(final String sql, final RowMapper<T> rowMapper, final ParamBinder paramBinder) {
        final List<T> result = Lists.newArrayList();
        Connection connection = null;
        PreparedStatement pstm = null;

        try {
            connection = dataSource.getConnection();
            pstm = connection.prepareStatement(sql);
            bindParamsIfBinderIsPresent(paramBinder, pstm);

            final ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                result.add(rowMapper.map(rs));
            }
        } catch (SQLException e) {
            throw new SqlRuntimeException(e);
        } catch (IOException e) {
            throw new SqlRuntimeException(e);
        } finally {
            Utils.close(pstm, connection);
        }

        return result;
    }

    public boolean execute(final String sql, final ParamBinder paramBinder) {
        Connection connection = null;
        PreparedStatement pstm = null;

        try {
            connection = dataSource.getConnection();
            pstm = connection.prepareStatement(sql);
            if (paramBinder != null) {
                paramBinder.bind(pstm);
            }

            return pstm.execute();
        } catch (SQLException e) {
            throw new SqlRuntimeException(e);
        } finally {
            Utils.close(pstm, connection);
        }
    }

    private void bindParamsIfBinderIsPresent(final ParamBinder paramBinder, final PreparedStatement preparedStatement) throws SQLException {
        if (paramBinder != null) {
            paramBinder.bind(preparedStatement);
        }
    }
}
