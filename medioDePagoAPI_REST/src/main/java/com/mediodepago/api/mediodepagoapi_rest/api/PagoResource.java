package com.mediodepago.api.mediodepagoapi_rest.api;

import com.mediodepago.api.mediodepagoapi_rest.dto.PagoRequest;
import com.mediodepago.api.mediodepagoapi_rest.dto.PagoResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.Random;

@ApplicationScoped
@Path("/pagos")
public class PagoResource {

    private final Random random = new Random();

    // form-url-encoded con dataTarjeta como int

    @POST
    @Path("/procesarQuery")
    @Produces(MediaType.APPLICATION_JSON)
// ¡OJO! quitamos @Consumes porque no hay body, solo query params
    public Response procesarPagoQuery(
            @QueryParam("monto") double monto,
            @QueryParam("dataTarjeta") int dataTarjeta
    ) {
        // 1) Validaciones
        if (monto <= 0 || dataTarjeta <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Monto > 0 y dataTarjeta > 0 obligatorios")
                    .build();
        }

        // 2) Randomizer
        boolean aprobado = random.nextBoolean();

        // 3) Construir respuesta
        PagoResponse resp = new PagoResponse(
                aprobado ? "APROBADO" : "RECHAZADO",
                aprobado ? "Pago aprobado" : "Pago rechazado"
        );

        // 4) Devolver
        return Response.ok(resp).build();
    }

    // (opcional) JSON si quieres mantenerlo:
    @POST
    @Path("/procesarJson")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response procesarPagoJson(PagoRequest req) {
        if (req == null || req.getMonto() <= 0 || req.getDataTarjeta() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Monto > 0 y dataTarjeta > 0 obligatorios")
                    .build();
        }
        boolean aprobado = random.nextBoolean();
        PagoResponse resp = new PagoResponse(
                aprobado ? "APROBADO" : "RECHAZADO",
                aprobado ? "El pago ha sido aprobado."
                        : "El pago ha sido rechazado."
        );
        return Response.ok(resp).build();
    }
}
