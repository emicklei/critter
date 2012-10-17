package com.philemonworks.critter.ui;

import java.io.IOException;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;
import static org.rendersnake.HtmlAttributesFactory.*;

public class HomePage implements Renderable {

	@Override
	public void renderOn(HtmlCanvas html) throws IOException {

		html.render(new RulesUI());
		
        html.div(dataRole("controlgroup").dataType("horizontal"))
            .a(dataRole("button").dataTheme("b").dataTest("newrule")
                .href("/ui/newdelay"))
                .content("New Delay") 
            .a(dataRole("button").dataTheme("b").dataTest("newrule")
                .href("/ui/newresponse"))
                .content("New Recording") 
            .a(dataRole("button").dataTheme("b").dataTest("newrule")
                .href("/ui/newrule"))
                .content("New Generic Rule")                 
        	._div();
	}
}
