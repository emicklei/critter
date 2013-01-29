package com.philemonworks.critter;

import com.thoughtworks.xstream.XStream;

public class RecordingConverter {

    private static XStream getXStream() {
        XStream xs = new XStream();
        xs.alias("recording", Recording.class);
        return xs;
    }
    
    public static String toXml(Object o) {
        return getXStream().toXML(o);
    }    
}
