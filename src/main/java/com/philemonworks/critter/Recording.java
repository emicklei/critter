package com.philemonworks.critter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Recording {
    public Date   requestReceivedDate;
    public String requestContent;
    public Map<String,String> requestHeaders = new HashMap<String,String>();
    
    public String responseContent;    
    public Map<String,String> responseHeaders = new HashMap<String,String>();
}
