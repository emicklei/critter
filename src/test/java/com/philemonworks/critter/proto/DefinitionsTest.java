package com.philemonworks.critter.proto;

import org.junit.Test;

import java.util.List;

/**
 * Created by emicklei on 29/03/16.
 */
public class DefinitionsTest {


    @Test
    public void explainMessage() {
        Definitions d = newDefinitions();
        List<String> paths = d.allPathsOfMessage("test.binding.SomeComplexType");
        for (String each : paths) {
            System.out.println(each);
        }
    }

    private Definitions newDefinitions() {
        Definitions d = new Definitions();
        d.read(getClass().getResourceAsStream("/bol-xsdtypes-1.6.proto"));
        d.read(getClass().getResourceAsStream("/SomeComplexType.proto"));
        return d;
    }
}
