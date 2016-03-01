package com.philemonworks.critter.proto;

import com.google.common.base.Optional;
import com.google.protobuf.Option;
import com.squareup.protoparser.*;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by emicklei on 01/03/16.
 */
public class Definitions {

    HashMap<String, MessageElement> elementMap = new HashMap<String, MessageElement>();

    public boolean read(InputStream is) {
        try {
            ProtoFile pf = ProtoParser.parseUtf8("inspector", is);
            for (TypeElement each : pf.typeElements()) {
                if (each instanceof MessageElement) {
                    this.elementMap.put(each.qualifiedName(), (MessageElement) each);
                }
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public Inspector newInspector(String messageName) {
        Inspector i = new Inspector();
        i.messageName = messageName;
        i.messageDefinitions = this;
        return i;
    }

    public Optional<FieldElement> fieldElementAt(String messageName, int fieldSequence) {
        MessageElement m = this.elementMap.get(messageName);
        if (null == m) {
            return null;
        }
        for (FieldElement each : m.fields()) {
            if (each.tag() == fieldSequence) {
                return Optional.of(each);
            }
        }
        return Optional.absent();
    }

    public Optional<FieldElement> fieldElementNamed(String messageName, String fieldName) {
        MessageElement m = this.elementMap.get(messageName);
        if (null == m) {
            return null;
        }
        for (FieldElement each : m.fields()) {
            if (each.name() == fieldName) {
                return Optional.of(each);
            }
        }
        return Optional.absent();
    }
}
