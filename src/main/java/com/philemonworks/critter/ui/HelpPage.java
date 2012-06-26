package com.philemonworks.critter.ui;

import java.io.IOException;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;
import org.rendersnake.StringResource;
import static org.rendersnake.HtmlAttributesFactory.*;
import static org.rendersnake.ext.jquery.JQueryAttributesFactory.*;

public class HelpPage implements Renderable {

    @Override
    public void renderOn(HtmlCanvas html) throws IOException {
        html.h1().content("Critter - User's Guide");
        StringResource.flush();
        html.render(new StringResource("/help-intro.html"));
        html.render(new StringResource("/help-rule.html"));
        html.render(new StringResource("/help-conditions.html"));
        html.render(new StringResource("/help-actions.html"));

        html.h3().content("External resources");
        //html.ul(dataRole("listview").dataInset(true));
        html.ul();
            html.li();
            this.renderFullExampleOn(html);
            html._li();
            
            html.li();
            this.renderWADLOn(html);
            html._li();
            
            html.li();
            this.renderDiagnoseOn(html);
            html._li(); 
            
            html.li();
            this.renderStatsOn(html);
            html._li(); 
        html._ul();
    }
    private void renderWADLOn(HtmlCanvas html) throws IOException {
        html.a(href("/application.wadl").rel("external")).content("WADL : documentation of the REST-API");
    }
    private void renderFullExampleOn(HtmlCanvas html) throws IOException {
        html.a(href("/example").rel("external")).content("Rule example with all features");
    }
    private void renderDiagnoseOn(HtmlCanvas html) throws IOException {
        html.a(href("/internal/selfdiagnose.html").rel("external")).content("SelfDiagnose : includes summary of proxied traffic");
    }
    private void renderStatsOn(HtmlCanvas html) throws IOException {
        html.a(href("/internal/stats.html").rel("external")).content("Monitor Stats");
    }      
}