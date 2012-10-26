package com.philemonworks.critter.ui;

public class UIUtils {
    
    public static String strongify(String input) {
        return input.replaceAll("\\[", "<strong>").replaceAll("\\]", "</strong>");
    }
}
