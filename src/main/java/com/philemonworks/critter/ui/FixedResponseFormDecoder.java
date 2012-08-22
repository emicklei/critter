package com.philemonworks.critter.ui;

import java.util.Properties;

public class FixedResponseFormDecoder {
    public Properties decode(String input) {
        Properties props = new Properties();
        String[] tokens = new String[]{"critter_id","critter_method","critter_url","critter_contenttype","critter_accepttype","critter_responsecontent","critter_requestcontent"};
        for (String each : tokens) {
            int begin = input.indexOf(each);
            if (begin != -1) {
                int end = input.indexOf("critter",begin+1);
                int slash = each.indexOf('_');
                String key = each.substring(slash+1);
                String value = "";
                if (begin + each.length() + 1 < input.length()) {
                  value = input.substring(begin + each.length() + 1, end == -1 ? input.length() - 1 : end - 1);
                }
                props.put(key, value);
            }
        }
        return props;
    }
}
