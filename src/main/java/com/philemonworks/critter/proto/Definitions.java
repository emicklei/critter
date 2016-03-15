package com.philemonworks.critter.proto;

import com.google.common.base.Optional;
import com.google.protobuf.Message;
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
                    this.insert((MessageElement) each);
                }
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    private void insert(MessageElement element) {
        this.elementMap.put(element.qualifiedName(), element);
        for (TypeElement each : element.nestedElements()) {
            if (each instanceof MessageElement) {
                insert((MessageElement) each);
                return;
            }
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
            return Optional.absent();
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
            return Optional.absent();
        }
        for (FieldElement each : m.fields()) {
            if (each.name().equals(fieldName)) {
                return Optional.of(each);
            }
        }
        return Optional.absent();
    }

    /**
     * @param qualifiedName
     * @return
     */
    public Optional<MessageElement> messageElementNamed(String qualifiedName) {
        return Optional.fromNullable(this.elementMap.get(qualifiedName));
    }

    public String qualifiedNameInNamespaceOf(String typeName, String qualifiedName) {
        // try different strategies here to workaround the fact that typeName is unqualified:
        // can be nested message of qualifiedName
        // can be message in the namespace of qualifiedName
        // we check for known definitions of the right case
        String nested = qualifiedName + "." + typeName;
        if (this.elementMap.containsKey(nested)) {
            return nested;
        }
        String namespaceSibling = qualifiedName.split("\\.")[0] + "." + typeName;
        if (this.elementMap.containsKey(namespaceSibling)) {
            return namespaceSibling;
        }
        // give up
        return typeName;
    }
}
