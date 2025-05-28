// src/main/java/com/tallerjava/tallerjava/medioPago/api/MedioPagoAPI.java
package com.tallerjava.tallerjava.medioPago.dto.api;

import com.tallerjava.tallerjava.medioPago.dto.PagoRequest;
import com.tallerjava.tallerjava.medioPago.dto.PagoResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.BadRequestException;

@ApplicationScoped
@Path("/medioPago")
public class MedioPagoAPI {

    @POST
    @Path("/pago")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response procesarPago(PagoRequest req) {
        // --- validaciones básicas ---
        if (req == null) {
            throw new BadRequestException("El cuerpo de la petición es obligatorio");
        }
        if (req.getIdComercio() <= 0) {
            throw new BadRequestException("El idComercio debe ser mayor que cero");
        }
        if (req.getMonto() <= 0) {
            throw new BadRequestException("El monto debe ser un valor positivo");
        }

        // --- aquí llamarías a tu lógica de negocio / cliente HTTP al Medio de Pago real ---
        boolean pagoOk = true;        // simulado
        String mensaje  = "Pago aprobado";

        // construimos la respuesta
        PagoResponse resp = new PagoResponse(pagoOk, mensaje);
        return Response
                .ok(resp)                        // 200 OK
                .build();
    }
}
