package com.tallerjava.tallerjava.Compra.interfase.exceptions;

import jakarta.annotation.Priority;
import jakarta.json.bind.JsonbException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.MediaType;
import java.util.Map;

@Provider
@Priority(Priorities.USER)
public class JsonbExceptionMapper implements ExceptionMapper<JsonbException> {
    @Override
    public Response toResponse(JsonbException ex) {
        Map<String,String> body = Map.of(
                "error", "JSON mal formado: " + ex.getMessage()
        );
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(body)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}

