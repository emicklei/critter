package com.philemonworks.critter.rule;

import com.philemonworks.critter.condition.BasicAuthentication;
import com.philemonworks.critter.condition.Condition;
import com.philemonworks.critter.condition.Equals;
import com.philemonworks.critter.condition.RequestHeader;
import com.philemonworks.critter.condition.Host;
import com.philemonworks.critter.condition.Not;
import com.philemonworks.critter.condition.Path;
import com.philemonworks.critter.condition.Port;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class NotConverter implements Converter {

    @Override
    public boolean canConvert(@SuppressWarnings("rawtypes") Class who) {
        return who == Not.class;
    }

    @Override
    public void marshal(Object aNot, HierarchicalStreamWriter writer, MarshallingContext context) {
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext ctx) {
        Not not = new Not();
        reader.moveDown();
        this.convertNext(reader, ctx, not, "host", Host.class);
        this.convertNext(reader, ctx, not, "equals", Equals.class);
        this.convertNext(reader, ctx, not, "Header", RequestHeader.class);
        this.convertNext(reader, ctx, not, "not", Not.class);
        this.convertNext(reader, ctx, not, "path", Path.class);
        this.convertNext(reader, ctx, not, "port", Port.class);
        this.convertNext(reader, ctx, not, "basicauthentication", BasicAuthentication.class);
        reader.moveUp();
        return not;
    }

    private void convertNext(HierarchicalStreamReader reader, UnmarshallingContext ctx, Not not, String alias, Class<?> conditionClass) {
        if (alias.equals(reader.getNodeName())) {
            not.condition = (Condition)ctx.convertAnother(not,conditionClass);
        }
    }

}
