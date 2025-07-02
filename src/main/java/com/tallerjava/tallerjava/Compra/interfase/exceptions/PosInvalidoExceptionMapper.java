package com.tallerjava.tallerjava.Compra.interfase.exceptions;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;
import java.util.Map;

@Provider
@Priority(Priorities.USER)
public class PosInvalidoExceptionMapper implements ExceptionMapper<PosInvalidoException> {
    @Override
    public Response toResponse(PosInvalidoException ex) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error: ", ex.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
