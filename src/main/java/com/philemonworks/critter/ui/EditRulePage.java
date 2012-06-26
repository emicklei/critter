package com.philemonworks.critter.ui;

import static org.rendersnake.HtmlAttributesFactory.for_;
import static org.rendersnake.HtmlAttributesFactory.id;
import static org.rendersnake.HtmlAttributesFactory.name;
import static org.rendersnake.ext.jquery.JQueryAttributesFactory.dataRole;

import java.io.IOException;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;
// TODO http://codemirror.net/doc/manual.html
public class EditRulePage implements Renderable {

	@Override
	public void renderOn(HtmlCanvas html) throws IOException {
		String rulexml = html.getPageContext().getString("rulexml");
		
		String alert = html.getPageContext().getString("alert");
		if (alert != null) {
			html.div(id("alertmessage")).content(alert);
		}
		
		html.h3().content("Edit the rule");
		
		html.form(id("newrule").action("/ui/newrule").method("post"));		
		
		html.div(dataRole("fieldcontain"))
			.label(for_("rulexml"))
				.content("xml definition:")
			.textarea(name("rulexml").id("rulexml").rows("10"))
				.content(rulexml)				
			._div();
		
        html.div(dataRole("controlgroup").dataType("horizontal"))
            .a(dataRole("button").dataTheme("b")
            		.href("javascript:document.forms['newrule'].submit();"))
            		.content("Ok")
            .a(dataRole("button").dataTheme("b")
                    .href("javascript:window.history.back();"))
                    .content("Cancel")                    
            		
        	._div();
        
        html._form();
	}
}