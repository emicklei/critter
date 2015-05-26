package com.philemonworks.critter.ui;

import static org.rendersnake.HtmlAttributesFactory.dataRole;
import static org.rendersnake.HtmlAttributesFactory.id;

import java.io.IOException;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;
import org.rendersnake.RenderableWrapper;
import org.rendersnake.ext.jquery.JQueryLibrary;

public class SiteLayout extends RenderableWrapper {

    public SiteLayout(Renderable component) {
        super(component);
    }

    @Override
    public void renderOn(HtmlCanvas html) throws IOException {
        html
            .html()
                .head()
                .macros().javascript("/ui/traffic.js")
                .render(JQueryLibrary.mobileTheme("1.2.0"))
                .render(JQueryLibrary.core("1.8.2"))
                .render(JQueryLibrary.mobile("1.2.0"))
                .macros().stylesheet("/ui/traffic.css")
                ._head()
                    .body()
                       .div(dataRole("page"))
                            .div(id("centered"))
                                .div(dataRole("header").dataTheme("a"))
                                    .render(new PageHeader())
                                    ._div()
                                .div(dataRole("content").dataTheme("a"))
                                    .render(this.component)
                                    ._div()
                                .div(dataRole("footer").dataTheme("a"))
                                    .render(new PageFooter())
                                    ._div()
                            ._div()
                        ._div()
                    ._body()
            ._html();
    }
}
