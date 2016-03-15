package com.philemonworks.critter.condition;

import com.philemonworks.critter.proto.Definitions;
import com.philemonworks.critter.proto.Inspector;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by emicklei on 25/02/16.
 */
public class ProtobufPathTest {

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

    @Test
    public void xsdtypes() throws Exception {
        Definitions d = new Definitions();
        d.read(getClass().getResourceAsStream("/bol-xsdtypes-1.6.proto"));
        new Sample(d, "xsdtypes.Chars", new byte[]{10, 5, 72, 101, 108, 108, 111}, ".value", "Hello").check();
        new Sample(d, "xsdtypes.Integer64", new byte[]{8, -128, 8}, ".value", "1024").check();
        new Sample(d, "xsdtypes.Integer32", new byte[]{8, -128, 2}, ".value", "256").check();
        new Sample(d, "xsdtypes.Bool", new byte[]{8, 1}, ".value", "true").check();
        new Sample(d, "xsdtypes.Time", new byte[]{10, 8, 8, 16, 16, 34, 24, 45, 40, 111}, ".value.hours", "16").check();
        new Sample(d, "xsdtypes.DayTime", new byte[]{10, 19, 10, 7, 8, -32, 15, 16, 2, 24, 29, 18, 8, 8, 16, 16, 34, 24, 45, 40, 108}, ".value.time.hours", "16").check();
    }
}
