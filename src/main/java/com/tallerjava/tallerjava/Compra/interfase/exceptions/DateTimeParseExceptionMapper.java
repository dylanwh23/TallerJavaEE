package com.tallerjava.tallerjava.Compra.interfase.exceptions;

import java.time.format.DateTimeParseException;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.MediaType;
import java.util.Map;

@Provider
@Priority(Priorities.USER)
public class DateTimeParseExceptionMapper implements ExceptionMapper<DateTimeParseException> {
    @Override
    public Response toResponse(DateTimeParseException ex) {
        String badText = ex.getParsedString();
        Map<String,String> body = Map.of(
                "error", "Formato de fecha inválido: " + badText
        );
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(body)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
