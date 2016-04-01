package com.philemonworks.critter.proto;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by emicklei on 01/04/16.
 */
public class DefinitionsPerRule {
    private ConcurrentHashMap<String, Definitions> definitions = new ConcurrentHashMap<String, Definitions>();

    /**
     * Return the Protomessage definitions for the rule. (can be empty).
     *
     * @param ruleID
     * @return
     */
    public synchronized Definitions getDefinitions(String ruleID) {
        Definitions d = this.definitions.get(ruleID);
        if (null == d) {
            d = new Definitions();
            this.definitions.put(ruleID, d);
        }
        return d;
    }
}
