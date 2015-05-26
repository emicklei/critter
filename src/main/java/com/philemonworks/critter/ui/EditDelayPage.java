package com.philemonworks.critter.ui;

import static org.rendersnake.HtmlAttributesFactory.dataRole;
import static org.rendersnake.HtmlAttributesFactory.for_;
import static org.rendersnake.HtmlAttributesFactory.id;
import static org.rendersnake.HtmlAttributesFactory.name;

import java.io.IOException;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

public class EditDelayPage implements Renderable {

    @Override
    public void renderOn(HtmlCanvas html) throws IOException {
        String alert = html.getPageContext().getString("alert");
        if (alert != null) {
            html.div(id("alertmessage")).content(alert);
        }
        html.h3().content("Add a delay on getting a Response");
                
        html.form(id("newdelay").action("/ui/newdelay").method("post"));  

        html.div(dataRole("fieldcontain"))
            .label(for_("id"))
                .content("ID:")
            .input(name("critter_id").id("id").value("delay-1"))              
            ._div();               
        
        html.div(dataRole("fieldcontain"))
            .label(for_("url"))
                .content("URL:")
            .input(name("critter_url").id("url").value("http://here.com/find/me"))              
            ._div();               
                     
        html.div(dataRole("fieldcontain"))
            .label(for_("delay"))
                .content("Delay (milliseconds):")
            .input(name("critter_delay").id("delay").value("5000"))              
            ._div();          

        html.render(new SubmitCancel("newdelay"));        
        
        html._form();
    }   
}
