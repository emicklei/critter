package com.philemonworks;

import org.junit.Assert;
import org.junit.Test;

import com.philemonworks.critter.condition.BasicAuthentication;
import com.philemonworks.critter.rule.Rule;
import com.philemonworks.critter.rule.RuleConverter;

public class RuleTest {
	@Test
	public void testParse() {
		Rule r = (Rule)RuleConverter.fromXml(getClass().getResourceAsStream("/rules.xml"));
		System.out.println(r.explain());
	}
	@Test
	public void testParseExample() {
		Rule r = (Rule)RuleConverter.fromXml(getClass().getResourceAsStream("/rule-example.xml"));
		System.out.println(r.explain());
	}
	@Test
	public void testBasicAuth() {
	    BasicAuthentication ba = new BasicAuthentication();
	    ba.username = "Aladdin";
	    ba.password = "open sesame";
	    Assert.assertEquals("Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==",ba.computeBasicAuthenticationHeaderValue(ba.username, ba.password));
	            
	}
}
