package com.philemonworks.critter.ui;

import static org.rendersnake.HtmlAttributesFactory.*;

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
			html.div(id("alertmessage")).content(alert, false);
		}
		
		html.h3().content("Edit the rule");
		
		html.form(id("newrule").action("/ui/newrule").method("post"));		
		
		html.div(dataRole("fieldcontain"))
			.label(for_("rulexml"))
				.content("xml definition:")
			.textarea(name("rulexml").id("rulexml").rows("10"))
				.content(rulexml)				
		
			._div();
		
		this.renderFullExampleOn(html);
		
        html.render(new SubmitCancel("newrule"));
        
        html._form();
	}
    private void renderFullExampleOn(HtmlCanvas html) throws IOException {
        html.a(href("/example").rel("external").target("_blank"))
            .content("Rule example with all features");
    }	
}
