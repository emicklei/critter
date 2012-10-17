package com.philemonworks.critter.ui;

import static org.rendersnake.HtmlAttributesFactory.class_;
import static org.rendersnake.HtmlAttributesFactory.for_;
import static org.rendersnake.HtmlAttributesFactory.href;
import static org.rendersnake.HtmlAttributesFactory.id;
import static org.rendersnake.HtmlAttributesFactory.type;
import static org.rendersnake.ext.jquery.JQueryAttributesFactory.dataRole;

import java.io.IOException;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import com.philemonworks.critter.Utils;

public class PageFooter implements Renderable {

    @Override
    public void renderOn(HtmlCanvas html) throws IOException {   
        html.div(class_("ui-grid-b"));
            html.div(class_("ui-block-a"));
                this.renderProxyControlOn(html);            
            html._div();
            
            html.div(class_("ui-block-b"));
                html.a(dataRole("button").href("/internal/stats.html").rel("external")).content("Monitor Stats");
            html._div();
            
            html.div(class_("ui-block-c"));
                html.h5(class_("ui-btn-right").style("font-size:small")).content("(c) 2012, philemonworks.com - " + Utils.getMavenVersion());
            html._div()            
            ._div();
    }
    
    private void renderProxyControlOn(HtmlCanvas html) throws IOException {
        boolean proxyActive = html.getPageContext().getBoolean("proxy.started",false);
        html.form(id("toggleproxy").method("post").action("/ui/toggleproxy"))
                .fieldset(dataRole("controlgroup"))
                    .input(type("checkbox")
                            .name("proxyon")
                            .id("proxyon")
                            .checked(proxyActive ? "checked" : null)
                            .onChange("javascript:document.forms['toggleproxy'].submit();"))
                    .label(for_("proxyon")).content("Proxy is " + (proxyActive ? "running" : "stopped"))
                ._fieldset()
            ._form();
    }
}