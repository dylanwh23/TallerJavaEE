package com.tallerjava.tallerjava.Compra.interfase;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BadRequestMapper implements ExceptionMapper<BadRequestException> {
    @Override
    public Response toResponse(BadRequestException ex) {
        // imprimo en consola el stack
        ex.printStackTrace();

        // devuelvo un 400 con el mensaje en el body
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(ex.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}