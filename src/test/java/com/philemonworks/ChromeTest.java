package com.philemonworks;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class ChromeTest {
    @Ignore @Test
    public void test_newrule() {
        WebDriver d = new ChromeDriver();
        d.get("http://localhost:8899");
        d.quit();
    }
}
