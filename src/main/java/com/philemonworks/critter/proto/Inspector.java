package com.philemonworks.critter.proto;

import com.google.common.base.Optional;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.UnknownFieldSet;
import com.squareup.protoparser.DataType;
import com.squareup.protoparser.FieldElement;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by emicklei on 01/03/16.
 */
public class Inspector {

    Definitions messageDefinitions;
    UnknownFieldSet fieldSet;
    String messageName;
    String fieldName = ""; // if not empty then the valueString will show the value of the field in the fieldset.

    /**
     * Reads one protocol buffers message.
     *
     * @param data
     * @return whether message is valid.
     */
    public boolean read(byte[] data) {
        try {
            this.fieldSet = UnknownFieldSet.parseFrom(data);
            return true;
        } catch (InvalidProtocolBufferException ex) {
            return false;
        }
    }

    /**
     * Return the String value by navigating a path in the message structure.
     *
     * @param dottedPath , always start with .
     * @return
     */
    public String path(String dottedPath) {
        if (dottedPath == null || dottedPath.length() == 0) {
            return "";
        }
        String[] tokens = dottedPath.split("\\.");
        Optional<Inspector> sub = this.pathFindIn(1, tokens); // because of .dot start with 1
        if (!sub.isPresent()) {
            return "";
        }
        return sub.get().toString();
    }

    private Optional<Inspector> pathFindIn(int index, String[] tokens) {
        if (index >= tokens.length) {
            return Optional.absent();
        }
        String token = tokens[index];
        if (token.length() == 0) {
            return Optional.absent();
        }
        // token is either digits (index) or string (label)
        int indexToken = 0;
        boolean isIndex = false;
        try {
            indexToken = Integer.parseInt(token);
        } catch (NumberFormatException ex) {
            isIndex = false;
        }
        this.fieldName = token;
        if (tokens.length == index + 1) {
            return Optional.of(this);
        }

        Optional<FieldElement> element = this.messageDefinitions.fieldElementNamed(this.messageName, token);
        if (!element.isPresent()) {
            return Optional.absent();
        }
        UnknownFieldSet.Field field = this.fieldSet.getField(element.get().tag());
        if (element.get().type() instanceof DataType.NamedType) {
            String qName = this.messageDefinitions.qualifiedNameInNamespaceOf(((DataType.NamedType) element.get().type()).name(), this.messageName);
            Inspector sub = this.messageDefinitions.newInspector(qName);
            try {
                sub.read(field.getLengthDelimitedList());
            } catch (IOException e) {
                return Optional.absent();
            }
            return sub.pathFindIn(index + 1, tokens);
        }
        return Optional.of(this);
    }

    public String toString() {
        if (this.fieldName.length() > 0) {
            Optional<FieldElement> element = this.messageDefinitions.fieldElementNamed(this.messageName, this.fieldName);
            if (!element.isPresent()) {
                return "";
            }
            UnknownFieldSet.Field field = this.fieldSet.getField(element.get().tag());
            if (element.get().type() instanceof DataType.ScalarType) {
                return this.toStringOf((DataType.ScalarType) element.get().type(), field);
            }
            return "non-scalar-type TODO";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, UnknownFieldSet.Field> each : this.fieldSet.asMap().entrySet()) {
            Optional<FieldElement> e = this.messageDefinitions.fieldElementAt(this.messageName, each.getKey());
            if (e.isPresent()) {
                sb.append(each.getKey());
                sb.append(" : ");
                sb.append(e.get().name());
                sb.append(" (");
                sb.append(e.get().type());
                sb.append(") = ");
                sb.append("?");
                sb.append("\n");
            }
        }
        return sb.toString();
    }


    private void read(List<ByteString> lengthDelimited) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (ByteString each : lengthDelimited) {
            each.writeTo(bos);
        }
        read(bos.toByteArray());
    }

    private String toStringOf(DataType.ScalarType type, UnknownFieldSet.Field field) {
        switch (type) {
            case BOOL:
                return String.valueOf(field.getVarintList().get(0) == 1);
            case INT32:
            case INT64:
            case UINT32:
            case UINT64:
            case SINT32:
            case SINT64:
                return String.valueOf(field.getVarintList().get(0));
            case FLOAT:
                return String.valueOf(Float.intBitsToFloat(field.getFixed32List().get(0)));
            case STRING:
                return field.getLengthDelimitedList().get(0).toStringUtf8();
            default:
                // TODO
                return "a " + type.toString();
        }
    }
}
