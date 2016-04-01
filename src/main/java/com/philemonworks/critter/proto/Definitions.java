package com.philemonworks.critter.proto;

import com.google.common.base.Optional;
import com.squareup.protoparser.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Definitions holds all Protocolbuffers message definitions needed to inspect serialized data.
 * <p/>
 * Created by emicklei on 01/03/16.
 */
public class Definitions {

    private Map<String, MessageElement> elementMap = new HashMap<String, MessageElement>();

    // TEMP
    public String explainAll() {
        StringBuilder sb = new StringBuilder();
        for (String messageName : elementMap.keySet()) {
            sb.append("\n-----------\n");
            sb.append(messageName);
            sb.append("\n-----------\n");
            for (String path : this.allPathsOfMessage(messageName)) {
                sb.append(path);
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public List<String> allPathsOfMessage(String messageName) {
        List<String> paths = new ArrayList<String>();
        this.appendPathTo("", messageName, paths);
        return paths;
    }

    private void appendPathTo(String path, String messageName, List<String> paths) {
        MessageElement m = this.elementMap.get(messageName);
        if (null == m) {
            return;
        }
        for (FieldElement each : m.fields()) {
            String nextPath = path + "." + each.name();
            if (FieldElement.Label.REPEATED == each.label()) {
                nextPath += ".0";
            }
            if (each.type() instanceof DataType.NamedType) {
                DataType.NamedType namedType = (DataType.NamedType) each.type();
                String qName = this.qualifiedNameInNamespaceOf(namedType.name(), messageName);
                this.appendPathTo(nextPath, qName, paths);
            }
            if (each.type() instanceof DataType.ScalarType) {
                // end of path
                paths.add(nextPath);
            }
        }
    }

    /**
     * Returns a new Inspector of data that uses (one of) my definitions.
     *
     * @param messageName
     * @return
     */
    public Inspector newInspector(String messageName) {
        Inspector i = new Inspector();
        i.messageName = messageName;
        i.messageDefinitions = this;
        return i;
    }

    protected Optional<FieldElement> fieldElementNamed(String messageName, String fieldName) {
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

    public Optional<MessageElement> messageElementNamed(String messageName) {
        MessageElement m = this.elementMap.get(messageName);
        if (null == m) {
            return Optional.absent();
        }
        return Optional.of(m);
    }

    /**
     * Different strategies are used here to workaround the fact that typeName is unqualified.
     * <p/>
     * qualifiedNameInNamespaceOf
     *
     * @param typeName
     * @param qualifiedName
     * @return
     */
    String qualifiedNameInNamespaceOf(String typeName, String qualifiedName) {
        // can be nested message of qualifiedName
        String nested = qualifiedName + "." + typeName;
        if (this.elementMap.containsKey(nested)) {
            return nested;
        }
        // can be message in the parent namespace of qualifiedName
        // take largest namespace part
        String namespace = qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
        String namespaceSibling = namespace + "." + typeName;
        if (this.elementMap.containsKey(namespaceSibling)) {
            return namespaceSibling;
        }
        // can be message in the root namespace of qualifiedName
        // take smallest namespace part
        namespace = qualifiedName.substring(0, qualifiedName.indexOf("."));
        namespaceSibling = namespace + "." + typeName;
        if (this.elementMap.containsKey(namespaceSibling)) {
            return namespaceSibling;
        }
        // give up
        return typeName;
    }

    /**
     * Reads all ProtocolBuffers message definitions from an Inputstream on a specification resource.
     *
     * @param is
     * @return
     */
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

    /**
     * Inserts a new elements and all its nested elements (will override all).
     *
     * @param element
     */
    private void insert(MessageElement element) {
        this.elementMap.put(element.qualifiedName(), element);
        for (TypeElement each : element.nestedElements()) {
            if (each instanceof MessageElement) {
                insert((MessageElement) each);
                return;
            }
        }
    }
}
