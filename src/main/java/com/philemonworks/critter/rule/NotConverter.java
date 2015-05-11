package com.philemonworks.critter.rule;

import com.philemonworks.critter.condition.*;
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
        Not not = (Not) aNot;
        if (not.condition != null) {
            writer.startNode(not.condition.getClass().getSimpleName().toLowerCase());
            context.convertAnother(not.condition);
            writer.endNode();
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext ctx) {
        Not not = new Not();
        reader.moveDown();
        this.convertNext(reader, ctx, not, "host", Host.class);
        this.convertNext(reader, ctx, not, "method", Method.class);
        this.convertNext(reader, ctx, not, "path", Path.class);
        this.convertNext(reader, ctx, not, "port", Port.class);
        this.convertNext(reader, ctx, not, "header", RequestHeader.class);
        this.convertNext(reader, ctx, not, "basicauthentication", BasicAuthentication.class);
        this.convertNext(reader, ctx, not, "xpath", XPath.class);
        reader.moveUp();

        return not;
    }

    private void convertNext(HierarchicalStreamReader reader, UnmarshallingContext ctx, Not not, String alias, Class<?> conditionClass) {
        if (alias.equals(reader.getNodeName())) {
            not.condition = (Condition) ctx.convertAnother(not, conditionClass);
        }
    }

}
