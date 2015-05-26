package com.philemonworks.critter.ui.fixed;

import static org.rendersnake.HtmlAttributesFactory.dataRole;
import static org.rendersnake.HtmlAttributesFactory.for_;
import static org.rendersnake.HtmlAttributesFactory.id;
import static org.rendersnake.HtmlAttributesFactory.name;
import static org.rendersnake.HtmlAttributesFactory.type;

import java.io.IOException;
import java.util.Properties;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import com.philemonworks.critter.ui.SubmitCancel;

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

        html.div(dataRole("fieldcontain").class_("get-post"))
                .label(for_("accepttype"))
                .content("Accept:")
                .input(name("critter_accepttype").id("accepttype").value("application/xml"))
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
                .content("Response Content-Type:")
                .input(name("critter_responsetype").id("responsecontenttype").value("application/xml"))
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
    
    public static Properties decode(String input) {
        Properties props = new Properties();
        String[] tokens = new String[]{"critter_id","critter_method","critter_url","critter_contenttype","critter_accepttype","critter_responsecontent","critter_requestcontent","critter_responsetype"};
        for (String each : tokens) {
            int begin = input.indexOf(each);
            if (begin != -1) {
                int end = input.indexOf("critter",begin+1);
                int slash = each.indexOf('_');
                String key = each.substring(slash+1);
                String value = "";
                if (begin + each.length() + 1 < input.length()) {
                  value = input.substring(begin + each.length() + 1, end == -1 ? input.length() : end - 1);
                }
                props.put(key, value);
            }
        }
        return props;
    }
    public static RecordingInput toInput(Properties props) {
        RecordingInput input = new RecordingInput();
        input.id = props.getProperty("id");
        input.method = props.getProperty("method");
        input.contenttype = props.getProperty("contenttype");
        input.accepttype = props.getProperty("accepttype");
        input.requestcontent = props.getProperty("requestcontent");
        input.responsecontent = props.getProperty("responsecontent");
        input.responsecontenttype = props.getProperty("responsetype");
        input.url = props.getProperty("url");
        return input;
    }
}
