package com.philemonworks.critter.dao.sql;

import com.philemonworks.critter.action.Delay;
import com.philemonworks.critter.condition.Equals;
import com.philemonworks.critter.rule.Rule;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Insert documentation here.
 *
 * @author jcraane
 */
public class RuleDaoSqlImplTest {
    private DataSource dataSource;
    private RuleDaoSqlImpl ruleDao = new RuleDaoSqlImpl();

    @Before
    public void setUp() throws Exception {
        dataSource = new BasicDataSource();
//        TODO: Set url to in memory database.
        ruleDao.dataSource = dataSource;
    }

    @Test
    public void addAndGetRules() throws Exception {
        ruleDao.addOrReplaceRule(createRule("1"));
        ruleDao.addOrReplaceRule(createRule("2"));
        assertThat(ruleDao.getRules().size(), is(2));
    }

    @Test
    public void getRule() throws Exception {
        ruleDao.addOrReplaceRule(createRule("1"));
        final String id = "2";
        ruleDao.addOrReplaceRule(createRule(id));
        final Rule retrieved = ruleDao.getRule(id);
        assertThat(retrieved, is(notNullValue()));
    }

    @Test
    public void deleteRule() throws Exception {
        ruleDao.addOrReplaceRule(createRule("1"));
        final String id = "2";
        ruleDao.addOrReplaceRule(createRule(id));
        assertThat(ruleDao.getRules().size(), is(2));
        ruleDao.deleteRule(id);
        assertThat(ruleDao.getRules().size(), is(1));
    }

    private Rule createRule(String id) {
        final Rule rule = new Rule();
        rule.id = "id";
        rule.getActions().add(new Delay());
        rule.getConditions().add(new Equals());

        return rule;
    }
}

