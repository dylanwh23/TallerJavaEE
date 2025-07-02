package com.tallerjava.tallerjava.Compra.interfase.exceptions;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class NullPointerExceptionMapper implements ExceptionMapper<NullPointerException> {
    @Override
    public Response toResponse(NullPointerException ex) {
        // aquí pones el JSON que quieras en lugar de "text"
        Map<String,String> body = Map.of(
                "error", "No se puede ejecutar la consulta",
                "detalle", "La consulta esta mal escrita"
        );
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(body)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
