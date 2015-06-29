package com.philemonworks.critter.ui.fixed;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;

public class RecordingInput {
    public static final String[] MIME_TYPES = new String[] { "application/xml", "application/json", "text/" };

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

    public String getResponsecontenttype() {
        return responsecontenttype;
    }

    public void setResponsecontenttype(String responsecontenttype) {
        this.responsecontenttype = responsecontenttype;
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
        return this.hasContenttype() && isTextualContentType(this.contenttype);
    }

    public boolean hasTextualResponseContent() {
        return this.hasResponseContentType() && isTextualContentType(this.responsecontenttype);
    }

    private boolean isTextualContentType(String contenttype) {
        ContentType contentType = ContentType.parse(contenttype);

        return isTextualMimeType(contentType, MIME_TYPES);
    }

    private boolean isTextualMimeType(ContentType contentType, String[] mimeTypes) {
        for (String mimeType : mimeTypes) {
            if (isTextualMimeType(contentType, mimeType)) {
                return true;
            }
        }

        return false;
    }

    private boolean isTextualMimeType(ContentType contentType, String mimeType) {
        return StringUtils.startsWithIgnoreCase(contentType.getMimeType(), mimeType);
    }
}
