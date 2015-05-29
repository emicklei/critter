package com.philemonworks.critter.ui;

import org.rendersnake.HtmlAttributes;
import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import java.io.IOException;

import static org.rendersnake.HtmlAttributesFactory.*;

/**
 * Created by jruijgers on 26/05/15.
 */
public class RecordingsPage implements Renderable {
    @Override
    public void renderOn(HtmlCanvas html) throws IOException {
        html.h3().content("Search recordings");

        html.form(id("search").action("/recordings").method("get"));

        html.div(dataRole("fieldcontain"))
                .label(for_("host"))
                .content("Host:")
                .input(type("text").name("host").id("host").value(html.getPageContext().getString("host")))._div();
        html.div(dataRole("fieldcontain"))
                .label(for_("method"))
                .content("Method:")
                .select(type("text").name("method").id("method"));
        for (String method : new String[] {"GET", "POST", "PUT", "DELETE"}) {
            HtmlAttributes selected = null;
            if (method.equals(html.getPageContext().getString("method"))) {selected = selected("");}
            html.option(selected).content(method);
        }
        html._select();
        html._div();

        html.render(new SubmitCancel("search"));

        html._form();
    }
}
