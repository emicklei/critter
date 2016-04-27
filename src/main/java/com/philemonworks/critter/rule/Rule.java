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
    public boolean enabled = true;
    public boolean tracing = false;
    protected List<Condition> conditions = new ArrayList<Condition>();
    protected List<Action> actions = new ArrayList<Action>();
    public boolean invalid = false;

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

    @Override
    public boolean test(RuleContext context) {
        if (invalid) return false;
        if (conditions == null) return true;
        context.rule = this;
        for (Condition each : conditions) {
            final boolean matches = each.test(context);
            if (tracing) {
                LOG.info("rule={} condition={} matches={}", id, each.explain(), matches);
            }
            if (!matches) return false;
        }
        return true;
    }

    @Override
    public void perform(RuleContext context) {
        if (actions == null) return;
        context.rule = this;
        for (Action each : actions) {
            if (tracing) {
                LOG.info("rule={} perform={}", id, each.explain());
            }
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
        for (int i = 0; i < this.getConditions().size(); ++i) {
            if (i > 0) {
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
        for (int i = 0; i < this.getActions().size(); ++i) {
            if (i > 0) {
                sb.append("\nTHEN ");
            }
            Action each = this.getActions().get(i);
            sb.append(each.explain());
        }
        return sb.toString();
    }

    @Override
    public String explain() {
        return this.conditionsString() + "\n" + this.actionsString();
    }
}
