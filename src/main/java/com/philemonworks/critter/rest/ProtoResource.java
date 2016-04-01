package com.philemonworks.critter.rest;

import com.google.common.base.Optional;
import com.philemonworks.critter.proto.Definitions;
import com.philemonworks.critter.proto.DefinitionsPerRule;
import com.squareup.protoparser.MessageElement;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.InputStream;

/**
 * Created by emicklei on 29/03/16.
 */
@Path("/rules/{id}/proto")
public class ProtoResource {

    @Inject
    DefinitionsPerRule definitions;

    @GET
    @Produces("text/plain")
    @Path("/{messageType}")
    public Response getProtoDefinition(@PathParam("id") String ruleID, @PathParam("messageType") String messageType) {
        Definitions definitionsForRule = this.definitions.getDefinitions(ruleID);
        Optional<MessageElement> element = definitionsForRule.messageElementNamed(messageType);
        if (!element.isPresent()) {
            return Response.status(404).build();
        }
        return Response.ok(element.get().toSchema()).build();
    }

    @GET
    @Produces("text/plain")
    @Path("/")
    public Response getProtoDefinition(@PathParam("id") String ruleID) {
        Definitions definitionsForRule = this.definitions.getDefinitions(ruleID);
        return Response.ok(definitionsForRule.explainAll()).build();
    }

    @POST
    @Produces("text/plain")
    @Consumes("text/plain")
    @Path("/")
    public Response uploadProtoDefinition(@PathParam("id") String ruleID, InputStream inputStream) {
        Definitions definitionsForRule = this.definitions.getDefinitions(ruleID);
        boolean ok = definitionsForRule.read(inputStream);
        if (!ok) {
            return Response.status(400).build();
        }
        return Response.status(204).build();
    }
}
