package com.philemonworks.critter.condition;

import com.google.protobuf.UnknownFieldSet;
import com.philemonworks.critter.proto.Definitions;
import com.philemonworks.critter.proto.Inspector;
import com.squareup.protoparser.*;
import org.junit.Test;

import java.util.Map;

/**
 * Created by emicklei on 11/03/16.
 */
public class Kanweg {

    @Test
    public void readProto() throws Exception {
        ProtoFile pf = ProtoParser.parseUtf8("test", getClass().getResourceAsStream("/SomeComplexType.proto"));
        //System.out.println(pf.typeElements().toString());

        MessageElement some = null;
        for (TypeElement each : pf.typeElements()) {
            if (each.name().equals("SomeComplexType")) {
                some = (MessageElement) each;
            }
        }
        //System.out.println(some.toString());

/**
 ProtobufMapper mapper = new ProtobufMapper();
 byte[] data = new byte[]{1, 2, 3};
 mapper.readerFor(Object.class).readValue(data);
 **/

/**
 ProtobufFactory f = new ProtobufFactory();

 ProtobufParser p = f.createParser(data);
 System.out.println(p.nextToken());
 **/
        byte[] data = new byte[]{10, 6, 71, 78, 73, 82, 84, 83, 16, 1, 24, 42, 37, 0, 0, 40, 66, 40, -64, -84, -128, 2, 48, -107, -102, -17, 58, 48, 42, 48, -128, 8, 58, 3, 8, -128, 8, 66, 3, 8, -128, 2, 74, 2, 8, 1, 82, 7, 10, 5, 72, 101, 108, 108, 111, 90, 10, 10, 8, 8, 16, 16, 34, 24, 45, 40, 111, 98, 9, 10, 7, 8, -32, 15, 16, 2, 24, 29, 106, 6, 116, 111, 107, 107, 105, 101, 114, 21, 10, 19, 10, 7, 8, -32, 15, 16, 2, 24, 29, 18, 8, 8, 16, 16, 34, 24, 45, 40, 108, -126, 1, 8, 115, 116, 97, 110, 100, 97, 114, 100, -118, 1, 11, 10, 9, 116, 101, 120, 116, 32, 105, 116, 101, 109, -110, 1, 9, 10, 7, 10, 5, 8, -46, 9, 16, 2, -102, 1, 7, 115, 116, 114, 105, 110, 103, 121, -94, 1, 2, 8, 42};

        Definitions d = new Definitions();
        d.read(getClass().getResourceAsStream("/SomeComplexType.proto"));
        d.read(getClass().getResourceAsStream("/bol-xsdtypes-1.6.proto"));
        Inspector i = d.newInspector("test.binding.SomeComplexType");
        i.read(data);
        System.out.println(i.toString());


        UnknownFieldSet us = UnknownFieldSet.parseFrom(data);
        System.out.println(us.asMap());

        for (Map.Entry<Integer, UnknownFieldSet.Field> each : us.asMap().entrySet()) {
            System.out.println(nameOfField(some, each.getKey()) + "(" + typeNameOfField(some, each.getKey()) + "):" + each.getKey().toString() + "=" + value(each.getValue()));
        }
    }


    public String nameOfField(MessageElement e, int i) {

        for (FieldElement each : e.fields()) {
            if (each.tag() == i) {
                return each.name();
            }
        }
        return "?";
    }

    public String typeNameOfField(MessageElement e, int i) {

        for (FieldElement each : e.fields()) {
            if (each.tag() == i) {
                return each.type().toString();
            }
        }
        return "?";
    }

    public String dumpBytes(byte[] bytes) {
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

    public String value(UnknownFieldSet.Field f) {
        if (!f.getFixed32List().isEmpty()) {
            if (f.getFixed32List().size() == 1) {
                return String.valueOf(f.getFixed32List().get(0)) + "(fixed32)";
            }
            return f.getFixed32List().toString();
        }
        if (!f.getFixed64List().isEmpty()) {
            if (f.getFixed64List().size() == 1) {
                return String.valueOf(f.getFixed64List().get(0)) + "(fixed64)";
            }
            return f.getFixed64List().toString();
        }
        if (!f.getGroupList().isEmpty()) {
            // TODO
            return "group";
        }
        if (!f.getVarintList().isEmpty()) {
            if (f.getVarintList().size() == 1) {
                return String.valueOf(f.getVarintList().get(0)) + "(varint)";
            }
            return f.getVarintList().toString();
        }
        if (!f.getLengthDelimitedList().isEmpty()) {
            if (f.getLengthDelimitedList().size() == 1) {
                return dumpBytes(f.getLengthDelimitedList().get(0).toByteArray()) + " (length delimited)";
            }
            return f.getLengthDelimitedList().toString();
        }
        // unknown
        return "!";
    }
}
