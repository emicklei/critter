package com.philemonworks.critter.condition;

import com.bol.protojx.xsdtypes.ProtoMessage;
import com.philemonworks.critter.proto.Definitions;
import com.philemonworks.critter.proto.Inspector;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by emicklei on 25/02/16.
 */
public class ProtobufPathTest {

    @Test
    public void someComplexType() {
        com.bol.protojx.test.binding.ProtoMessage.SomeComplexType st = com.bol.protojx.test.binding.ProtoMessage.SomeComplexType.newBuilder()
                .setBooleanElement(false)
                .setStringElement("hello some")
                .setIntElement(42)
                .setFloatElement(3.14f)
                .setLongElement(1234567890L)
                .setInteger32Element(toInteger32(23))
                .setInteger64Element(toInteger64(68))
                .setBoolElement(toBool(false))
                .setCharsElement(ProtoMessage.Chars.newBuilder().setValue("hello proto"))
                .setTimeElement(newTime_8_38_42())
                .setDateElement(newDay_2016_3_18())
                .setDateTimeElement(newDayTime_1967_12_20_8_41_42())
                .setDefaultableElement("missing")
                .setOtherElement(newOther_one_two())
                .setNestedTypeElement(newNested())
                .setSTRING("HELLO")
                .setTokenElement("token")
                .addItems(com.bol.protojx.test.binding.ProtoMessage.ItemType.newBuilder().setId(42))
                .addChars(ProtoMessage.Chars.newBuilder().setValue("three"))
                .addLongId(42)
                .addLongId(84)
                .build();
        byte[] payload = st.toByteArray();
        String messageType = "test.binding.SomeComplexType";
        String[] fixtures = new String[]{
                ".booleanElement", "false",
                ".stringElement", "hello some",
                ".intElement", "42",
                ".floatElement", "3.14",
                ".longElement", "1234567890",
                ".integer32Element.value", "23",
                ".integer64Element.value", "68",
                ".boolElement.value", "false",
                ".charsElement.value", "hello proto",
                ".timeElement.value.hours", "8",
                ".dateElement.value.year", "2016",
                ".dateElement.value.year", "2016",
                ".dateElement.value.year", "2016",
                ".defaultableElement", "missing",
                ".otherElement.textItem.0", "one",
                ".otherElement.textItem.1", "two",
                ".nestedTypeElement.decimalElement.value.unscaledValue", "161",
                ".nestedTypeElement.decimalElement.value.scale", "2",
                ".items.0.id", "42",
                ".chars.0.value", "three",
                ".longId.1", "84"
        };
        for (int c = 0; c < fixtures.length; c += 2) {
            new Sample(newDefinitions(), messageType, payload, fixtures[c], fixtures[c + 1]).check();
        }
        String[] failures = new String[]{
                ".items.0", Inspector.InvalidPath,
                "..", Inspector.InvalidPath,
                ".x", Inspector.InvalidPath,
        };
        for (int c = 0; c < failures.length; c += 2) {
            new Sample(newDefinitions(), messageType, payload, failures[c], failures[c + 1]).check();
        }
    }

    @Test
    public void chars() {
        Definitions d = newDefinitions();
        new Sample(d, "xsdtypes.Chars", ProtoMessage.Chars.newBuilder().setValue("hello proto").build().toByteArray(), ".value", "hello proto").check();
    }

    @Test
    public void bool() {
        Definitions d = newDefinitions();
        new Sample(d, "xsdtypes.Bool", ProtoMessage.Bool.newBuilder().setValue(true).build().toByteArray(), ".value", "true").check();
    }

    @Test
    public void integer32() {
        Definitions d = newDefinitions();
        new Sample(d, "xsdtypes.Integer32", ProtoMessage.Integer32.newBuilder().setValue(32).build().toByteArray(), ".value", "32").check();
    }

    @Test
    public void integer64() {
        Definitions d = newDefinitions();
        new Sample(d, "xsdtypes.Integer64", ProtoMessage.Integer64.newBuilder().setValue(42).build().toByteArray(), ".value", "42").check();
    }


    @Test
    public void dayTime() throws Exception {
        Definitions d = newDefinitions();
        new Sample(d, "xsdtypes.DayTime", newDayTime_1967_12_20_8_41_42().toByteArray(), ".value.time.hours", "8").check();
    }

    @Test
    public void time() throws Exception {
        Definitions d = newDefinitions();
        new Sample(d, "xsdtypes.Time", newTime_8_38_42().toByteArray(), ".value.hours", "8").check();
        new Sample(d, "xsdtypes.Time", newTime_8_38_42().toByteArray(), ".value.minutes", "38").check();
        new Sample(d, "xsdtypes.Time", newTime_8_38_42().toByteArray(), ".value.seconds", "42").check();
    }

    /////////// Helpers ---------------

    public static class Sample {
        byte[] data;
        String path;
        String result;
        Definitions defs;
        String messageType;

        public Sample(Definitions defs, String messageType, byte[] d, String path, String r) {
            this.defs = defs;
            this.messageType = messageType;
            this.data = d;
            this.path = path;
            this.result = r;
        }

        public void check() {
            Inspector i = this.defs.newInspector(this.messageType);
            i.read(this.data);
            assertEquals(this.path, this.result, i.path(this.path));
        }
    }

    private Definitions newDefinitions() {
        Definitions d = new Definitions();
        d.read(getClass().getResourceAsStream("/bol-xsdtypes-1.6.proto"));
        d.read(getClass().getResourceAsStream("/SomeComplexType.proto"));
        return d;
    }

    private ProtoMessage.DayTime newDayTime_1967_12_20_8_41_42() {
        return ProtoMessage.DayTime.newBuilder()
                .setValue(ProtoMessage.DayTime.Value.newBuilder()
                        .setDay(ProtoMessage.Day.Value.newBuilder().setYear(1967).setMonth(12).setDayInMonth(20))
                        .setTime(ProtoMessage.Time.Value.newBuilder().setHours(8).setMinutes(41).setSeconds(42))).build();
    }


    private ProtoMessage.Day newDay_2016_3_18() {
        return ProtoMessage.Day.newBuilder()
                .setValue(ProtoMessage.Day.Value.newBuilder()
                        .setYear(2016)
                        .setMonth(3)
                        .setDayInMonth(18)).build();
    }

    private ProtoMessage.Time newTime_8_38_42() {
        return ProtoMessage.Time.newBuilder()
                .setValue(ProtoMessage.Time.Value.newBuilder()
                        .setHours(8)
                        .setMinutes(38)
                        .setSeconds(42)).build();
    }

    private ProtoMessage.Integer32 toInteger32(int i) {
        return ProtoMessage.Integer32.newBuilder().setValue(i).build();
    }

    private ProtoMessage.Integer64 toInteger64(int i) {
        return ProtoMessage.Integer64.newBuilder().setValue(i).build();
    }

    private ProtoMessage.Bool toBool(boolean yesno) {
        return ProtoMessage.Bool.newBuilder().setValue(yesno).build();
    }

    private ProtoMessage.Decimal toDecimal(float f) {
        float unscaled = f;
        for (int d = 0; d < 2; d++) {
            unscaled = unscaled * 10;
        }
        return ProtoMessage.Decimal.newBuilder()
                .setValue(ProtoMessage.Decimal.Value.newBuilder()
                        .setScale(2)
                        .setUnscaledValue((long) unscaled)).build();
    }

    private com.bol.protojx.test.binding.ProtoMessage.OtherComplexType newOther_one_two() {
        return com.bol.protojx.test.binding.ProtoMessage.OtherComplexType.newBuilder()
                .addTextItem("one")
                .addTextItem("two")
                .build();
    }

    private com.bol.protojx.test.binding.ProtoMessage.nestedTypeElement newNested() {
        return com.bol.protojx.test.binding.ProtoMessage.nestedTypeElement.newBuilder()
                .setDecimalElement(toDecimal(1.61f)).build();
    }
}
