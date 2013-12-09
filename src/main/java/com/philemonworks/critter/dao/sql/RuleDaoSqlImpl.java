package com.philemonworks.critter.dao.sql;

import com.google.inject.Inject;
import com.philemonworks.critter.Utils;
import com.philemonworks.critter.dao.RuleDao;
import com.philemonworks.critter.dao.sql.support.JdbcTemplate;
import com.philemonworks.critter.dao.sql.support.ParamBinder;
import com.philemonworks.critter.dao.sql.support.RowMapper;
import com.philemonworks.critter.rule.Rule;
import com.philemonworks.critter.rule.RuleConverter;

import java.io.IOException;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Insert documentation here.
 *
 * @author jcraane
 */
public class RuleDaoSqlImpl implements RuleDao {
    private static final String INSERT = "INSERT INTO rules(id, rule_xml) values(?, ?);";
    private static final String SELECT_ALL = "SELECT id, rule_xml FROM rules";
    private static final String SELECT_ONE = "SELECT id, rule_xml FROM rules where id = ?";
    private static final String DELETE_ONE = "DELETE FROM rules WHERE id = ?";

    @Inject
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Rule> getRules() {
        return jdbcTemplate.queryForList(SELECT_ALL, new RuleMapper(), null);
    }

    @Override
    public void addOrReplaceRule(final Rule rule) {
        jdbcTemplate.execute(INSERT, new ParamBinder() {
            @Override
            public void bind(final PreparedStatement pstm) throws SQLException {
                pstm.setString(1, rule.id);
                pstm.setClob(2, new StringReader(RuleConverter.toXml(rule)));
            }
        });
    }

    @Override
    public Rule getRule(final String id) {
        return jdbcTemplate.queryForObject(SELECT_ONE, new RuleMapper(), new IdBinder(id));
    }

    @Override
    public void deleteRule(final String id) {
        jdbcTemplate.execute(DELETE_ONE, new IdBinder(id));
    }

    private static final class RuleMapper implements RowMapper<Rule> {
        @Override
        public Rule map(final ResultSet rs) throws SQLException, IOException {
            final String xml = Utils.clobToString(rs.getClob("rule_xml"));
            return RuleConverter.fromXml(xml);
        }
    }

    private static final class IdBinder implements ParamBinder {
        private final String id;

        private IdBinder(final String id) {
            this.id = id;
        }

        @Override
        public void bind(final PreparedStatement pstm) throws SQLException {
            pstm.setString(1, id);
        }
    }
}
