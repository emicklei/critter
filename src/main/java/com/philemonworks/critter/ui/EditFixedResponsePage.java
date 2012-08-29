package com.philemonworks.critter.ui;

import static org.rendersnake.HtmlAttributesFactory.dataRole;
import static org.rendersnake.HtmlAttributesFactory.for_;
import static org.rendersnake.HtmlAttributesFactory.id;
import static org.rendersnake.HtmlAttributesFactory.name;
import static org.rendersnake.HtmlAttributesFactory.type;

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
        html.h3().content("Edit a HTTP Request and Response recording");
                
        html.form(id("newresponse").action("/ui/newresponse").method("post"));  

        html.div(dataRole("fieldcontain"))
            .label(for_("id"))
                .content("ID:")
            .input(name("critter_id").id("id").value("fixed-1"))              
            ._div();        
        
        html.div(dataRole("fieldcontain"))
            .fieldset(dataRole("controlgroup").dataType("horizontal"))
                .legend().content("Method")
                .input(type("radio").name("critter_method").id("radio-get").value("GET").checked("checked").onChange("clickedMethod(this.value);"))
                .label(for_("radio-get")).content("GET")
                
                .input(type("radio").name("critter_method").id("radio-post").value("POST").onChange("clickedMethod(this.value);"))
                .label(for_("radio-post")).content("POST")
                
                .input(type("radio").name("critter_method").id("radio-put").value("PUT").onChange("clickedMethod(this.value);"))
                .label(for_("radio-put")).content("PUT")
                
                .input(type("radio").name("critter_method").id("radio-delete").value("DELETE").onChange("clickedMethod(this.value);"))
                .label(for_("radio-delete")).content("DELETE")
            ._fieldset()
            ._div();
        
        
        html.div(dataRole("fieldcontain"))
            .label(for_("url"))
                .content("URL:")
            .input(name("critter_url").id("url").value("http://here.com/find/me"))              
            ._div();        
        
        html.div(dataRole("fieldcontain").class_("post-put").style("display:none"))
            .label(for_("contenttype"))
                .content("Content-Type:")
            .input(name("critter_contenttype").id("contenttype").value("application/xml"))              
            ._div();      
        
        html.div(dataRole("fieldcontain").class_("post-put").style("display:none"))        
            .label(for_("body"))
                .content("Request content:")
            .textarea(name("critter_requestcontent").id("body").rows("10"))
                .content("")               
            ._div();
                     
        html.div(dataRole("fieldcontain").class_("get-post"))
            .label(for_("accepttype"))
                .content("Accept:")
            .input(name("critter_accepttype").id("accepttype").value("application/xml"))              
            ._div();          
        
        html.div(dataRole("fieldcontain").class_("get-post"))        
            .label(for_("response"))
                .content("Response content:")
            .textarea(name("critter_responsecontent").id("response").rows("10"))
                .content("")               
            ._div();
        
        html.render(new SubmitCancel("newresponse"));        
        
        html._form();
    }
}
