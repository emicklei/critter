package com.philemonworks.critter.ui;

import static org.rendersnake.HtmlAttributesFactory.*;

import java.io.IOException;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

public class SubmitCancel implements Renderable {

    private String formId;

    public SubmitCancel(String formId) { this.formId = formId; }
    
    @Override
    public void renderOn(HtmlCanvas html) throws IOException {
        html.div(dataRole("controlgroup").dataType("horizontal"))
            .a(dataRole("button").dataTheme("b")
                    .href("javascript:document.forms['" + formId + "'].submit();"))
                    .content("Ok")
            .a(dataRole("button").dataTheme("b")
                    .href("javascript:window.history.back();"))
                    .content("Cancel")                    
                    
            ._div();
    }
}
