package com.tallerjava.tallerjava.Compra.interfase;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable ex) {
        // Si ya es un WebApplicationException (400,404,409, etc),
        // devolvemos exactamente su Response:
        if (ex instanceof WebApplicationException) {
            return ((WebApplicationException) ex).getResponse();
        }

        // Si no, seguimos devolviendo 500:
        ex.printStackTrace();
        Map<String,String> body = Map.of(
                "error", "Error interno del servidor, verifique los parametros de la solicitud"
        );
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(body)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
