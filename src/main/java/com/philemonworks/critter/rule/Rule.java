package com.philemonworks.critter.rule;

import com.philemonworks.critter.action.Action;
import com.philemonworks.critter.condition.Condition;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Rule {
	private static final Logger LOG = LoggerFactory.getLogger(Rule.class);
	
	public String id;
	public boolean enabled = true;
	protected List<Condition> conditions = new ArrayList<Condition>();
	protected List<Action> actions = new ArrayList<Action>();
	public int order = Integer.MAX_VALUE;

	public List<Condition> getConditions() {
        if (conditions == null)
            conditions = new ArrayList<Condition>();
        return conditions;
    }	
	
	public List<Action> getActions() { 
	    if (actions == null)
	        actions = new ArrayList<Action>();
	    return actions;
	}
	
	public RuleResult test(RuleContext context) {
		RuleResult result = new RuleResult();
		if (conditions != null) {
			for (Condition each : conditions) {
				final boolean matches = each.test(context);
				result.match(matches);
				LOG.trace("condition [{}] matches:{}", each.explain(), matches);
			}
		}

		return result;
	}

	public void perform(RuleContext context) {
		if (actions == null) return;
		for (Action each : actions) {
			LOG.trace("perform [{},{}]",each,each.explain());
			each.perform(context);
		}
	}

    public void ensureId() {
        if (null != id) return;
        this.id = Long.toString(System.currentTimeMillis());
    }

    public String conditionsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IF   ");
        for (int i=0;i<this.getConditions().size();++i) {
            if (i>0) {
            	sb.append("\nAND  ");
            }
        	Condition each = this.getConditions().get(i);
            sb.append(each.explain());
        }
        return sb.toString();
    }

    public String actionsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("THEN ");
        for (int i=0;i<this.getActions().size();++i) {
            if (i>0) {
            	sb.append("\nTHEN ");
            }
        	Action each = this.getActions().get(i);
            sb.append(each.explain());
        }
        return sb.toString();
    }

	public String explain() {
		return this.conditionsString() +"\n" + this.actionsString();
	}

	@Override public String toString() {
		return explain();
	}

	public class RuleResult {
		public Rule rule = Rule.this;
		public boolean matches = true;
		private int matchCount = 0;

		public void match(boolean matches) {
			this.matches &= matches;
			this.matchCount += matches ? 1 : 0;
		}

		public boolean isBetterMatch(RuleResult other) {
			return matches && (matchCount > other.matchCount || rule.order < other.rule.order);
		}

		public String toString() {
			return new ToStringBuilder(this).append(matches).append(matchCount).append(rule).toString();
		}
	}
}
