package com.philemonworks.critter.ui;

import static org.rendersnake.HtmlAttributesFactory.for_;
import static org.rendersnake.HtmlAttributesFactory.id;
import static org.rendersnake.HtmlAttributesFactory.name;
import static org.rendersnake.ext.jquery.JQueryAttributesFactory.dataRole;

import java.io.IOException;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

public class EditFixedResponsePage implements Renderable {

    @Override
    public void renderOn(HtmlCanvas html) throws IOException {
        String alert = html.getPageContext().getString("alert");
        if (alert != null) {
            html.div(id("alertmessage")).content(alert);
        }
        html.h3().content("Edit the request and its response");
                
        html.form(id("newrule").action("/ui/newresponse").method("post"));  

        html.div(dataRole("fieldcontain"))
            .label(for_("method"))
                .content("Method:")
            .input(name("method").id("method").value("GET"))              
            ._div();        
        
        html.div(dataRole("fieldcontain"))
            .label(for_("url"))
                .content("URL:")
            .input(name("url").id("url").value("http://here.com/find/me"))              
            ._div();        
        
        html.div(dataRole("fieldcontain"))
            .label(for_("contenttype"))
                .content("Content-Type:")
            .input(name("contenttype").id("contenttype").value("application/xml"))              
            ._div();        
        
        
        html.div(dataRole("fieldcontain"))        
            .label(for_("response"))
                .content("Response:")
            .textarea(name("response").id("response").rows("10"))
                .content("")               
            ._div();
        
        html._form();
    }
}
