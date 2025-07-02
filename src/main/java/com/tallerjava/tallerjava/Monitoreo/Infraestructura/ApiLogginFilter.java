package com.tallerjava.tallerjava.Monitoreo.Infraestructura;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

@Provider
public class ApiLogginFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOGGER = Logger.getLogger(ApiLogginFilter.class.getName());
    private static final String START_TIME_PROPERTY = "start-time";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // --- INTERCEPTANDO EL "IN" ---

        // 1. Guardamos el tiempo de inicio para calcular la duración total
        long startTime = System.currentTimeMillis();
        requestContext.setProperty(START_TIME_PROPERTY, startTime);

        // 2. Registramos la información de la petición entrante
        String method = requestContext.getMethod();
        String path = requestContext.getUriInfo().getPath();
        String address = requestContext.getUriInfo().getRequestUri().toString(); // O puedes obtener la IP del header "X-Forwarded-For"

        LOGGER.info(String.format("IN > Petición Recibida: %s %s desde %s", method, path, address));

        // Aquí podrías leer headers si es necesario
        // requestContext.getHeaders().forEach((key, value) ->
        //    LOGGER.info(String.format("  Header: %s = %s", key, value))
        // );
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        // --- INTERCEPTANDO EL "OUT" ---

        // 1. Obtenemos el tiempo de inicio que guardamos en la petición
        Long startTime = (Long) requestContext.getProperty(START_TIME_PROPERTY);
        long duration = 0;
        if (startTime != null) {
            duration = System.currentTimeMillis() - startTime;
        }

        // 2. Obtenemos información de la respuesta
        String method = requestContext.getMethod();
        String path = requestContext.getUriInfo().getPath();
        int status = responseContext.getStatus();

        LOGGER.info(String.format("OUT < Respuesta Enviada: %s %s -> %d (%d ms)", method, path, status, duration));
    }
}
