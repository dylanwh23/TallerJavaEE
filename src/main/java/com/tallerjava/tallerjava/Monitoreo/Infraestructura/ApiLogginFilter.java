package com.tallerjava.tallerjava.Monitoreo.Infraestructura;

import com.influxdb.v3.client.InfluxDBClient;
import com.influxdb.v3.client.Point;
import com.influxdb.v3.client.write.WritePrecision;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.time.Instant;
import java.util.logging.Logger;

@Provider
public class ApiLogginFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOGGER = Logger.getLogger(ApiLogginFilter.class.getName());
    private static final String START_TIME_PROPERTY = "start-time";
    private static final String HOST     = "http://localhost:8181";
    private static final char[] TOKEN    = "apiv3_roLYC4qn2_5u4yiECh3vdyVOipO3DmMhT-3e-i3PPCouFoUziCKikiivri4qug3X0txhQhLv6Bx55mA-pg5iTQ".toCharArray();
    private static final String DATABASE = "tallerJava";

    /* ▶ Cliente InfluxDB compartido (una sola instancia) */
    private static final InfluxDBClient INFLUX_CLIENT =
            InfluxDBClient.getInstance(HOST, TOKEN, DATABASE);

    /* ───────────────────────── ENTRADA ───────────────────────── */
    @Override
    public void filter(ContainerRequestContext requestContext) {
        long startTime = System.currentTimeMillis();
        requestContext.setProperty(START_TIME_PROPERTY, startTime);

        String method  = requestContext.getMethod();
        String path    = requestContext.getUriInfo().getPath();
        String address = requestContext.getUriInfo().getRequestUri().toString();

        LOGGER.info(String.format("IN  > %s %s desde %s", method, path, address));

        /* Guarda punto "IN" (duration = 0 porque aún no terminó) */
        persistCall("IN", method, path, 0);
    }

    /* ───────────────────────── SALIDA ───────────────────────── */
    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {

        Long start = (Long) requestContext.getProperty(START_TIME_PROPERTY);
        long duration = start != null ? System.currentTimeMillis() - start : 0;

        String method = requestContext.getMethod();
        String path   = requestContext.getUriInfo().getPath();
        int status    = responseContext.getStatus();

        LOGGER.info(String.format("OUT < %s %s -> %d (%d ms)",
                                  method, path, status, duration));

        /* Guarda punto "OUT" con la duración real */
        persistCall("OUT", method, path, duration);
    }

    /* ───────────────────── Persistencia en InfluxDB ───────────────────── */
    private void persistCall(String direction,
                             String httpMethod,
                             String path,
                             long duration) {

        try {
            String module = extractModule(path);



            Point point = Point.measurement("api_requests")
                    .setTag("direction",  direction)      // IN | OUT
                    .setTag("method",     httpMethod)     // GET | POST | ...
                    .setTag("module",     module)         // Módulo (1º segmento)
                    .setTag("api",        path)           // Ruta completa
                    .setField("duration_ms", duration)    // Tiempo de ejecución
                    .setTimestamp(Instant.now().minusSeconds(-10));

            INFLUX_CLIENT.writePoint(point);
        } catch (Exception e) {
            LOGGER.warning("Error al escribir en InfluxDB: " + e.getMessage());
        }
    }

    /* Extrae el módulo (primer segmento de la ruta) */
    private String extractModule(String path) {
        if (path == null || path.isBlank()) {
            return "root";
        }
        String[] parts = path.split("/");
        return parts.length > 0 ? parts[0] : "root";
    }
}