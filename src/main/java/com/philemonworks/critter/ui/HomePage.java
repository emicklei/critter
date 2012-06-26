package com.philemonworks.critter.ui;

import static org.rendersnake.ext.jquery.JQueryAttributesFactory.dataRole;

import java.io.IOException;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

public class HomePage implements Renderable {

	@Override
	public void renderOn(HtmlCanvas html) throws IOException {

		html.render(new RulesUI());
		
        html.div(dataRole("controlgroup").dataType("horizontal"))
        	.a(dataRole("button").dataTheme("b").dataTest("newrule")
        		.href("/ui/newrule"))
        		.content("New Rule")                	
        	._div();
	}
}
