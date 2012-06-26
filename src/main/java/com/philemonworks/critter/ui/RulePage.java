package com.philemonworks.critter.ui;

import static org.rendersnake.ext.jquery.JQueryAttributesFactory.dataRole;
import static org.rendersnake.HtmlAttributesFactory.*;
import java.io.IOException;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import com.philemonworks.critter.rule.Rule;

public class RulePage implements Renderable {

    @Override
    public void renderOn(HtmlCanvas html) throws IOException {
        Rule rule = (Rule)html.getPageContext().getObject("rule");
        if (rule == null) return; // robustness
        html.h3().content(rule.id);
        html.h5().content(rule.enabled ? "enabled" : "disabled");
        html.pre().content(rule.explain(),false);
        
        html.div(dataRole("controlgroup").dataType("horizontal"))

            .a(dataRole("button").dataTheme("b")
            		.href("javascript:changeEnablementRule('" + rule.id + "'," + !rule.enabled + ")"))
            		.content(rule.enabled ? "Disable" : "Enable")
            .a(dataRole("button").dataTheme("b").dataIcon("gear")
            		.href("/ui/rules/"+rule.id+ "/edit")) // TODO path encode
            		.content("Edit")
            .a(dataRole("button").dataTheme("b")
                    .href("javascript:deleteRule('" + rule.id + "')"))
                    .content("Delete")
                    
            ._div();
    }
}
