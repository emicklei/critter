package com.philemonworks.critter.ui;

import static org.rendersnake.HtmlAttributesFactory.NO_ESCAPE;
import static org.rendersnake.HtmlAttributesFactory.class_;
import static org.rendersnake.HtmlAttributesFactory.dataRole;
import static org.rendersnake.HtmlAttributesFactory.dataTheme;
import static org.rendersnake.HtmlAttributesFactory.href;

import java.io.IOException;
import java.util.List;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import com.philemonworks.critter.rule.Rule;

public class RulesUI implements Renderable {

    @Override
    @SuppressWarnings("unchecked")
    public void renderOn(HtmlCanvas html) throws IOException {
        List<Rule> rules = (List<Rule>)html.getPageContext().getObject("rules");
        if (rules.isEmpty()) {
            html.h5().content("No rules defined.");
            return;
        }
        html.ul(dataRole("listview").dataInset(true).dataTheme("b"));
        for (Rule each : rules) {
            this.renderRuleOn(each,html);
        }
        html._ul();
    }

    private void renderRuleOn(Rule each, HtmlCanvas html) throws IOException {
        String description = UIUtils.strongify(each.conditionsString() + " " + each.actionsString());
        html.li(dataTheme("c"))
            .a(href("/ui/rules/"+each.id))
                .h1().content(each.id)
                .p().content(description,NO_ESCAPE)
                .div(class_("ui-li-aside")).content(each.enabled ? "enabled" : "disabled")
            ._a()
            ._li();
    }
}
