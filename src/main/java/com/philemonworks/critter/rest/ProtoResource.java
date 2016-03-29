package com.philemonworks.critter.rest;

import com.google.common.base.Optional;
import com.philemonworks.critter.proto.Definitions;
import com.squareup.protoparser.MessageElement;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.InputStream;

/**
 * Created by emicklei on 29/03/16.
 */
@Path("/proto")
public class ProtoResource {

    @Inject
    @Named("SharedDefinitions")
    Definitions definitions;

    @GET
    @Produces("text/plain")
    @Path("/{messageType}")
    public Response getProtoDefinition(@PathParam("messageType") String messageType) {
        Optional<MessageElement> element = this.definitions.messageElementNamed(messageType);
        if (!element.isPresent()) {
            return Response.status(404).build();
        }
        return Response.ok(element.get().toSchema()).build();
    }

    @POST
    @Produces("text/plain")
    @Consumes("text/plain")
    @Path("/")
    public Response uploadProtoDefinition(InputStream inputStream) {
        boolean ok = this.definitions.read(inputStream);
        if (!ok) {
            return Response.status(400).build();
        }
        return Response.status(204).build();
    }
}
