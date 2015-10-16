package com.philemonworks.critter;

import com.philemonworks.critter.condition.Host;
import com.philemonworks.critter.condition.Path;
import com.philemonworks.critter.dao.RuleDao;
import com.philemonworks.critter.rule.Rule;
import com.philemonworks.critter.rule.RuleContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
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

    @Test
    public void detectRuleShouldReturnBestMatchingRule() throws URISyntaxException {
        Host host = new Host();
        host.matches = "test.critter.com";
        Rule rule1 = new Rule();
        rule1.getConditions().add(host);

        Path path = new Path();
        path.matches = "/test.html";
        Rule rule2 = new Rule();
        rule2.getConditions().add(host);
        rule2.getConditions().add(path);

        RuleContext context = new RuleContext();
        context.forwardURI = new URI("http://test.critter.com/test.html");

        System.out.println(context.forwardURI.getPath());

        when(ruleDao.getRules()).thenReturn(Arrays.asList(rule1, rule2));

        Rule resultRule = subject.detectRule(context);

        assertThat(resultRule, is(notNullValue()));
        assertThat(resultRule, is(rule2));
        assertThat(resultRule, is(not(rule1)));
    }

    @Test
    public void detectRuleWithPartlyMatchingPathShouldReturnBestMatchingRule() throws URISyntaxException {
        Host host = new Host();
        host.matches = "test.critter.com";
        Rule rule1 = new Rule();
        rule1.getConditions().add(host);

        Path path = new Path();
        path.matches = "/test.html";
        Rule rule2 = new Rule();
        rule2.getConditions().add(host);
        rule2.getConditions().add(path);

        RuleContext context = new RuleContext();
        context.forwardURI = new URI("http://test.critter.com/not_matching.html");

        System.out.println(context.forwardURI.getPath());

        when(ruleDao.getRules()).thenReturn(Arrays.asList(rule1, rule2));

        Rule resultRule = subject.detectRule(context);

        assertThat(resultRule, is(notNullValue()));
        assertThat(resultRule, is(rule1));
        assertThat(resultRule, is(not(rule2)));
    }

    @Test
    public void detectRuleWithNoMatchesShouldReturnNull() throws URISyntaxException {
        Host host = new Host();
        host.matches = "test.critter.com";
        Rule rule1 = new Rule();
        rule1.getConditions().add(host);

        Path path = new Path();
        path.matches = "/test.html";
        Rule rule2 = new Rule();
        rule2.getConditions().add(host);
        rule2.getConditions().add(path);

        RuleContext context = new RuleContext();

        context.forwardURI = new URI("http://pro.critter.com/not_matching.html");

        when(ruleDao.getRules()).thenReturn(Arrays.asList(rule1, rule2));

        Rule resultRule = subject.detectRule(context);

        assertThat(resultRule, is(nullValue()));
    }
}
