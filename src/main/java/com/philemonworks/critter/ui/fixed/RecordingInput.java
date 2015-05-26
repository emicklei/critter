package com.philemonworks.critter.ui.fixed;

import org.apache.commons.lang3.StringUtils;

public class RecordingInput {
    public String id, requestcontent, method, url, contenttype, accepttype, responsecontent;
    public String responsecontenttype;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestcontent() {
        return requestcontent;
    }

    public void setRequestcontent(String requestcontent) {
        this.requestcontent = requestcontent;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContenttype() {
        return contenttype;
    }

    public void setContenttype(String contenttype) {
        this.contenttype = contenttype;
    }

    public String getAccepttype() {
        return accepttype;
    }

    public void setAccepttype(String accepttype) {
        this.accepttype = accepttype;
    }

    public String getResponsecontent() {
        return responsecontent;
    }

    public void setResponsecontent(String responsecontent) {
        this.responsecontent = responsecontent;
    }
    public boolean hasRequestContent() { return !StringUtils.isEmpty(requestcontent); }
    public boolean hasAccepttype() { return !StringUtils.isEmpty(accepttype); }
    public boolean hasResponsecontent() { return !StringUtils.isEmpty(responsecontent); }
    public boolean hasResponseContentType() { return !StringUtils.isEmpty(responsecontenttype); }
    public boolean hasContenttype() { return !StringUtils.isEmpty(contenttype); }
    public boolean hasUrl() { return !StringUtils.isEmpty(url); }
    public boolean hasMethod() { return !StringUtils.isEmpty(method); }
    public boolean hasId() { return !StringUtils.isEmpty(id); }

    public boolean hasTextualRequestContent() {
        return this.hasContenttype() 
                && ("application/xml".equalsIgnoreCase(contenttype) ||
                    "application/json".equalsIgnoreCase(contenttype) ||
                    "text/html".equalsIgnoreCase(contenttype)
                   );
    } 
}
