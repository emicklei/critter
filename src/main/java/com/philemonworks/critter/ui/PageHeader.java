package com.philemonworks.critter.ui;

import java.io.IOException;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;
import static org.rendersnake.HtmlAttributesFactory.*;

public class PageHeader implements Renderable {

    @Override
    public void renderOn(HtmlCanvas html) throws IOException {
        html.div(dataRole("header"))
            .a(dataIcon("home").dataDirection("reverse").href("/")).content("Home")
            .a(dataIcon("info").href("/help.html")).content("Help")
            .h1().content("Critter - the testing http(s) proxy server")
            ._div();
    }
}
