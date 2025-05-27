package com.tallerjava.tallerjava.Transferencia.interfase;

import com.tallerjava.tallerjava.Transferencia.aplicacion.TransferenciaInterfase;

import com.tallerjava.tallerjava.Transferencia.dominio.Transferencia;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TransferenciaAPI {
    private TransferenciaInterfase transferenciaService;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/ventasPeriodo")
    public List<Transferencia> ventasPeriodo(
            @QueryParam("idComercio") int idComercio,
            @QueryParam("desde") String desde,
            @QueryParam("hasta") String hasta
    ) {

        LocalDate ldDesde = LocalDate.parse(desde);
        LocalDate ldHasta = LocalDate.parse(hasta);
        LocalDateTime fechaDesde = ldDesde.atStartOfDay();
        // incluir todo el día actual
        LocalDateTime fechaHasta = ldHasta.plusDays(1).atStartOfDay();

        List<Transferencia> transferenciasDiarias = transferenciaService.consultarDepositos(idComercio, fechaDesde, fechaHasta);
        System.out.println("---------- RESUMEN DE DEPOSITOS DEL PERIODO " + fechaDesde + " - " + fechaHasta +  "----------");
        for (Transferencia Transferencia : transferenciasDiarias) {

            System.out.println("id:" + Transferencia.getId());
            System.out.println("MontoTransferencia:" + Transferencia.getMontoTransferencia());
            System.out.println("Fecha:" + Transferencia.getFechaHora());
            System.out.println("Estado:" + Transferencia.getEstado());
            System.out.println("-------------------------------------------------------");
        }

        return transferenciaService.consultarDepositos(idComercio, fechaDesde, fechaHasta);
    }
}
