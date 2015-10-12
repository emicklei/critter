package com.philemonworks.critter;

import com.philemonworks.critter.dao.RuleDao;
import com.philemonworks.critter.rule.Rule;
import com.philemonworks.critter.rule.RuleContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TrafficManagerTest {
    private TrafficManager subject;

    @Mock
    private RuleDao ruleDao;

    @Before
    public void setUp() {
        subject = new TrafficManager();
        subject.ruleDao = ruleDao;
    }

    @Test
    public void detectRuleShouldMatchFirstRule() {
        Rule rule1 = new Rule();
        Rule rule2 = new Rule();
        RuleContext context = new RuleContext();

        when(ruleDao.getRules()).thenReturn(Arrays.asList(rule1, rule2));

        Rule resultRule = subject.detectRule(context);

        assertThat(resultRule, is(notNullValue()));
        assertThat(resultRule, is(rule1));
        assertThat(resultRule, is(not(rule2)));
    }

    @Test
    public void detectRuleShouldMatchRuleWithLowestOrder() {
        Rule rule1 = new Rule();
        rule1.order = 2;
        Rule rule2 = new Rule();
        rule2.order= 1;
        RuleContext context = new RuleContext();

        when(ruleDao.getRules()).thenReturn(Arrays.asList(rule1, rule2));

        Rule resultRule = subject.detectRule(context);

        assertThat(resultRule, is(notNullValue()));
        assertThat(resultRule, is(rule2));
        assertThat(resultRule, is(not(rule1)));
    }
}
