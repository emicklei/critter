package com.philemonworks.critter.dao.sql;

import com.philemonworks.critter.action.Delay;
import com.philemonworks.critter.condition.Equals;
import com.philemonworks.critter.dao.sql.support.JdbcTemplate;
import com.philemonworks.critter.db.DbCreator;
import com.philemonworks.critter.rule.Rule;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Insert documentation here.
 *
 * @author jcraane
 */
public class RuleDaoSqlImplTest {
    private RuleDaoSqlImpl ruleDao = new RuleDaoSqlImpl();
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() throws Exception {
        final BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:h2:mem:test");
        DbCreator.create(dataSource);

        jdbcTemplate = new JdbcTemplate(dataSource);
        ReflectionTestUtils.setField(ruleDao, "jdbcTemplate", jdbcTemplate);
    }

    @After
    public void tearDown() throws Exception {
        jdbcTemplate.execute("DROP TABLE rules", null);
    }

    @Test
    public void addAndGetRules() throws Exception {
        ruleDao.addOrReplaceRule(createRule("1"));
        ruleDao.addOrReplaceRule(createRule("2"));
        assertThat(ruleDao.getRules().size(), is(2));
    }

    @Test
    public void updateExistingRule() throws Exception {
        final Rule rule_1 = createRule("1");
        ruleDao.addOrReplaceRule(rule_1);
        final Rule rule_2 = createRule("2");
        ruleDao.addOrReplaceRule(rule_2);
        assertThat(ruleDao.getRules().size(), is(2));

        ruleDao.addOrReplaceRule(rule_2);
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
        rule.id = id;
        rule.getActions().add(new Delay());
        rule.getConditions().add(new Equals());

        return rule;
    }
}

