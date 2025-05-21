package com.tallerjava.tallerjava.Comercio.interfase;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

public class ExceptionMappers {

       @Provider
       public static class SecurityExceptionMapper implements ExceptionMapper<SecurityException> {
           @Override
           public Response toResponse(SecurityException exception) {
               return Response.status(Response.Status.UNAUTHORIZED)
                              .entity("Error de autenticación: " + exception.getMessage())
                              .type( "text/plain")
                              .build();
           }
       }

       @Provider
       public static class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {
           @Override
           public Response toResponse(IllegalArgumentException exception) {
               return Response.status(Response.Status.BAD_REQUEST)
                              .entity("Solicitud inválida: " + exception.getMessage())
                              .type( "text/plain")
                              .build();
           }
       }
    // Controla los @Valid
    @Provider
    public static class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

        @Override
        public Response toResponse(ConstraintViolationException exception) {
            StringBuilder mensaje = new StringBuilder("Datos incompletos: ");
            for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
                mensaje.append(violation.getPropertyPath()).append(" ").append(violation.getMessage()).append(". ");
            }

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(mensaje.toString())
                    .type("text/plain")
                    .build();
        }
    }


}