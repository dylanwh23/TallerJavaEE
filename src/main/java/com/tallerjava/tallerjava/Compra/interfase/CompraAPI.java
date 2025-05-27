package com.tallerjava.tallerjava.Compra.interfase;

import com.tallerjava.tallerjava.Compra.aplicacion.CompraInterface;


import com.tallerjava.tallerjava.Compra.dominio.Compra;

import com.tallerjava.tallerjava.Compra.dominio.DataTarjeta;
import com.tallerjava.tallerjava.Compra.dominio.EnumEstadoCompra;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@Path("/compra")
public class CompraAPI {

    @Inject
    private CompraInterface compraService;

    @GET @Path("/pago-simple")
    @Produces(MediaType.APPLICATION_JSON)
    public Compra procesarPagoSimple(
            @QueryParam("idComercio")    Integer idComercio,
            @QueryParam("monto")         Double monto,
            @QueryParam("numero")        Integer numero,
            @QueryParam("cvv")           Integer cvv,
            @QueryParam("propietario")   String propietario,
            @QueryParam("vencimiento")   String vencimientoIso
    ) {
        // 1) revisa que vengan todos
        if (idComercio == null || monto == null || numero == null
                || cvv == null || propietario == null || vencimientoIso == null) {
            throw new BadRequestException("Faltan parámetros obligatorios para procesar el pago.");
        }
        // 2) revisa formato de fecha
        Instant inst;
        try {
            inst = Instant.parse(vencimientoIso);
        } catch (DateTimeParseException ex) {
            throw new BadRequestException("Formato de fecha inválido: " + vencimientoIso);
        }

        DataTarjeta dt = new DataTarjeta( numero, cvv, Date.from(inst), propietario);
        Compra c = new Compra();
        c.setIdComercio(idComercio);
        c.setMonto(monto.intValue());
        c.setDataTarjeta(dt);
        c.setEstado(EnumEstadoCompra.PROCESANDOSE);

        System.out.println("-------------- COMPRA PROCESADA --------------");

        System.out.println("Id del comercio: " + c.getIdComercio());
        System.out.println("Monto de la compra: " + c.getMonto());
        System.out.println("Estado: " + c.getEstado());
        System.out.println("----------------------------");
        return compraService.procesarPago(c);
    }


    @POST
    @Path("/pago")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public  Response procesarPago(Compra compra) {
        Compra creada = compraService.procesarPago(compra);
        return Response.status(Response.Status.CREATED).entity(creada).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/ventasDiarias")
    public List<Compra> ventasDiarias(@QueryParam("idComercio") int idComercio) {
        List<Compra> ventasDiarias = compraService.resumenVentasDiarias(idComercio);
        System.out.println(">> Found ventasDiarias.size() = " + ventasDiarias.size());
        System.out.println("---------- RESUMEN DE VENTAS DEL DIA ----------");
        for (Compra venta : ventasDiarias) {
            System.out.println("Precio: $ " + venta.getMonto());
            System.out.println("Fecha: " + venta.getFechaHora());
            System.out.println("Estado: " + venta.getEstado());
            System.out.println("- - - - - - - - - - - - - - - -");
        }
        return ventasDiarias;
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/ventasPeriodo")
    public List<Compra> ventasPeriodo(
            @QueryParam("idComercio") int idComercio,
            @QueryParam("desde") String desde,     // “2025-05-01”
            @QueryParam("hasta") String hasta      // “2025-05-23”
    ) {

        LocalDate ldDesde = LocalDate.parse(desde);
        LocalDate ldHasta = LocalDate.parse(hasta);
        Date fechaDesde = Date.from(ldDesde.atStartOfDay(ZoneId.systemDefault()).toInstant());
        // Para incluir todo el día "hasta", avanzamos un día
        Date fechaHasta = Date.from(ldHasta.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Compra> ventasDiarias = compraService.resumenVentasPorPeriodo(idComercio, fechaDesde, fechaHasta);
         System.out.println("---------- RESUMEN DE VENTAS DEL PERIODO " + fechaDesde + " - " + fechaHasta +  "----------");
        for (Compra venta : ventasDiarias) {

            System.out.println("Precio:" + venta.getMonto());
            System.out.println("Fecha:" + venta.getFechaHora());
            System.out.println("Estado:" + venta.getEstado());
            System.out.println("-------------------------------------------------------");
        }

        return compraService.resumenVentasPorPeriodo(idComercio, fechaDesde, fechaHasta);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/montoActualVendido")
    public Map<String, Float> montoActualVendido(@QueryParam("idComercio") int idComercio) {
        float monto = compraService.montoActualVendido(idComercio);

        System.out.println("Monto vendido hoy: " + monto);
        return Map.of("montoActualVendido", monto);
    }

}
