package com.philemonworks.critter.dao.sql;

import com.google.inject.Inject;
import com.philemonworks.critter.dao.RuleDao;
import com.philemonworks.critter.rule.Rule;

import javax.sql.DataSource;
import java.util.List;

/**
 * Insert documentation here.
 *
 * @author jcraane
 */
public class RuleDaoSqlImpl implements RuleDao {
    @Inject
    DataSource dataSource;

    @Override
    public List<Rule> getRules() {

        return null;
    }

    @Override
    public void addOrReplaceRule(final Rule aRule) {

    }

    @Override
    public Rule getRule(final String id) {
        return null;
    }

    @Override
    public void deleteRule(final String id) {

    }


}
