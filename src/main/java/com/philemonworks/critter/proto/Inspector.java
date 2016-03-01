package com.philemonworks.critter.proto;

import com.google.common.base.Optional;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.UnknownFieldSet;
import com.squareup.protoparser.DataType;
import com.squareup.protoparser.FieldElement;

import java.util.Map;

/**
 * Created by emicklei on 01/03/16.
 */
public class Inspector {

    Definitions messageDefinitions;
    UnknownFieldSet fieldSet;
    String messageName;

    public boolean read(byte[] data) {
        try {
            this.fieldSet = UnknownFieldSet.parseFrom(data);
            return true;
        } catch (InvalidProtocolBufferException ex) {
            return false;
        }
    }

    public String toString() {
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
                sb.append(toStringOf(e.get().name()));
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Returns an empty string if fieldName does not exist.
     *
     * @param fieldName
     * @return
     */
    public String toStringOf(String fieldName) {
        Optional<FieldElement> e = this.messageDefinitions.fieldElementNamed(this.messageName, fieldName);
        if (!e.isPresent()) {
            return "";
        }
        boolean qualified = e.get().type().toString().contains(".");
        if (qualified) {
            return "nested";
        }
        UnknownFieldSet.Field field = this.fieldSet.getField(e.get().tag());
        if (e.get().type() instanceof DataType.ScalarType) {
            return this.toStringOf((DataType.ScalarType) e.get().type(), field);
        }
        if (e.get().type() instanceof DataType.NamedType) {
            return this.toStringOf((DataType.NamedType) e.get().type(), field);
        }
        if (e.get().type() instanceof DataType.MapType) {
            return this.toStringOf((DataType.MapType) e.get().type(), field);
        }
        return "toStringOf-failed";
    }

    public String toStringOf(DataType.MapType type, UnknownFieldSet.Field field) {
        return "map";
    }


    public String toStringOf(DataType.NamedType type, UnknownFieldSet.Field field) {
        return "named";
    }

    public String toStringOf(DataType.ScalarType type, UnknownFieldSet.Field field) {
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

    private String dumpBytes(byte[] bytes) {
        boolean first = true;
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (byte b : bytes) {
            if (!first) {
                sb.append(',');
            } else {
                first = false;
            }
            sb.append(b);
        }
        sb.append(']');
        return sb.toString();
    }
}
