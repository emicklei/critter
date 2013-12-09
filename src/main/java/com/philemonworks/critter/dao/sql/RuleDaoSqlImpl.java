package com.philemonworks.critter.dao.sql;

import com.google.inject.Inject;
import com.philemonworks.critter.dao.RuleDao;
import com.philemonworks.critter.dao.sql.support.JdbcTemplate;
import com.philemonworks.critter.dao.sql.support.RowMapper;
import com.philemonworks.critter.rule.Rule;
import com.philemonworks.critter.rule.RuleConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Insert documentation here.
 *
 * @author jcraane
 */
public class RuleDaoSqlImpl implements RuleDao {
    private static final String INSERT = "INSERT INTO rules(rule_xml) values(?);";
    private static final String SELECT_ALL = "SELECT id, rule_xml FROM rules";

    @Inject
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Rule> getRules() {
        return jdbcTemplate.queryForList(SELECT_ALL, new RowMapper<Rule>() {
            @Override
            public Rule map(final ResultSet rs) throws SQLException {
                return RuleConverter.fromXml(rs.getString(1));
            }
        }, null);
    }

    @Override
    public void addOrReplaceRule(final Rule rule) {

    }

    @Override
    public Rule getRule(final String id) {
        return null;
    }

    @Override
    public void deleteRule(final String id) {

    }


}
