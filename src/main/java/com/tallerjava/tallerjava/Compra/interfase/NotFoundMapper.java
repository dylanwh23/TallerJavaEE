package com.tallerjava.tallerjava.Compra.interfase;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class NotFoundMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException ex) {
        // Opcional: imprime stack en server.log
        ex.printStackTrace();

        // Devolver 404 con JSON {"error": "..."}
        Map<String,String> body = Map.of("error", ex.getMessage());
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(body)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
