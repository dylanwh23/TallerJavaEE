package com.tallerjava.tallerjava.Compra.interfase.exceptions;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
@Priority(Priorities.USER)
public class IllegalArgumentMapper implements ExceptionMapper<IllegalArgumentException> {
    @Override
    public Response toResponse(IllegalArgumentException ex) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of(
                        "status", 400,
                        "error",  "Bad Request",
                        "message", ex.getMessage()
                ))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}