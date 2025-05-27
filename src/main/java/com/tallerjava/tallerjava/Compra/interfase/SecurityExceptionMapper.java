package com.tallerjava.tallerjava.Compra.interfase;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class SecurityExceptionMapper implements ExceptionMapper<SecurityException> {
    @Override
    public Response toResponse(SecurityException ex) {
        return Response.status(Response.Status.FORBIDDEN)
                .entity(Map.of(
                        "status", 403,
                        "error",  "Forbidden",
                        "message", ex.getMessage()
                ))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}