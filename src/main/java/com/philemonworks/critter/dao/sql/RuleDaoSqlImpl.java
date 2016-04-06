package com.philemonworks.critter.dao.sql;

import com.google.inject.Inject;
import com.philemonworks.critter.Utils;
import com.philemonworks.critter.dao.RuleDao;
import com.philemonworks.critter.dao.sql.support.JdbcTemplate;
import com.philemonworks.critter.dao.sql.support.ParamBinder;
import com.philemonworks.critter.dao.sql.support.RowMapper;
import com.philemonworks.critter.rule.Rule;
import com.philemonworks.critter.rule.RuleConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOG = LoggerFactory.getLogger(RuleDaoSqlImpl.class);

    private static final String INSERT = "INSERT INTO rules(id, rule_xml) values(?, ?);";
    private static final String UPDATE = "UPDATE rules set rule_xml = ? where id = ?";
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
        if (notPresent(rule)) {
            insert(rule);
        } else {
            update(rule);
        }
    }

    private boolean insert(final Rule rule) {
        return jdbcTemplate.execute(INSERT, new ParamBinder() {
            @Override
            public void bind(final PreparedStatement pstm) throws SQLException {
                pstm.setString(1, rule.id);
                pstm.setClob(2, new StringReader(RuleConverter.toXml(rule)));
            }
        });
    }

    private boolean update(final Rule rule) {
        return jdbcTemplate.execute(UPDATE, new ParamBinder() {
            @Override
            public void bind(final PreparedStatement pstm) throws SQLException {
                pstm.setClob(1, new StringReader(RuleConverter.toXml(rule)));
                pstm.setString(2, rule.id);
            }
        });

    }

    private boolean notPresent(final Rule rule) {
        return getRule(rule.id) == null;
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
            String errorMessage = RuleConverter.validateXML(xml);
            Rule rule = RuleConverter.fromXml(xml, false);
            if (errorMessage.length() > 0) {
                rule.enabled = false;
                rule.invalid = true;
            }
            return rule;
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
