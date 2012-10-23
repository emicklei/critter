package com.philemonworks.critter.rule;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.philemonworks.critter.action.Action;
import com.philemonworks.critter.condition.Condition;

public class Rule implements Condition, Action {
	private static final Logger LOG = LoggerFactory.getLogger(Rule.class);
	
	public String id;
	public boolean enabled = false;
	public List<Condition> conditions = new ArrayList<Condition>();
	public List<Action> actions = new ArrayList<Action>();
	
	@Override
	public boolean test(RuleContext context) {
		if (conditions == null) return true;
		for (Condition each : conditions) {
			final boolean matches = each.test(context);
			LOG.trace("condition [{}] matches:{}",each.explain(),matches);
			if (!matches) return false;
		}
		return true;
	}

	@Override
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
        for (int i=0;i<conditions.size();++i) {
            if (i>0) {
            	sb.append("\nAND  ");
            }
        	Condition each = conditions.get(i);
            sb.append(each.explain());
        }
        return sb.toString();
    }

    public String actionsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("THEN ");
        for (int i=0;i<actions.size();++i) {
            if (i>0) {
            	sb.append("\nTHEN ");
            }
        	Action each = actions.get(i);
            sb.append(each.explain());
        }
        return sb.toString();
    }

	@Override
	public String explain() {
		return this.conditionsString() +"\n" + this.actionsString();
	}
}
