package com.philemonworks.critter.db;

import com.philemonworks.critter.dao.sql.support.SqlRuntimeException;
import com.philemonworks.critter.Utils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Responsible for creating the initial database tables if they does not exist yet.
 *
 * @author jcraane
 */
public final class DbCreator {
    private static final String[] STATEMENTS = new String[] {
        "CREATE TABLE RULES\n" +
            "(\n" +
            "    ID VARCHAR(100) NOT NULL,\n" +
            "    RULE_XML CLOB NOT NULL\n" +
            ");\n"
    };

    private DbCreator() {
    }

    public static void create(final DataSource dataSource) {
        Connection connection = null;
        PreparedStatement pstm = null;

        try {
            connection = dataSource.getConnection();
            for (final String statement : STATEMENTS) {
                pstm = prepareAndExecute(connection, statement);
            }
        } catch (SQLException e) {
            throw new SqlRuntimeException("An exception occured creating the embedded database. See the stacktrace for details.", e);
        } finally {
            Utils.close(pstm, connection);
        }
    }

    private static PreparedStatement prepareAndExecute(final Connection connection, final String statement) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement(statement);
        pstm.execute();
        return pstm;
    }
}
