package com.tallerjava.tallerjava.Compra.interfase;

import com.tallerjava.tallerjava.Compra.aplicacion.CompraInterface;


import com.tallerjava.tallerjava.Compra.dominio.Compra;

import com.tallerjava.tallerjava.Compra.dominio.DataTarjeta;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@Path("/compra")
public class CompraAPI {

    @Inject
    private CompraInterface compraService;

    @GET
    @Path("/pago-simple")
    @Produces(MediaType.APPLICATION_JSON)
    public Compra procesarPagoSimple(
            @QueryParam("idComercio") int idComercio,
            @QueryParam("monto")      double monto,
            @QueryParam("numero")     int numero,
            @QueryParam("cvv")        int cvv,
            @QueryParam("propietario") String propietario,
            @QueryParam("vencimiento") String vencimientoIso
    ) {

        Date venc = Date.from( Instant.parse(vencimientoIso) );


        DataTarjeta dt = new DataTarjeta();
        dt.setNumero(numero);
        dt.setCvv(cvv);
        dt.setPropietario(propietario);
        dt.setVencimiento(venc);


        Compra c = new Compra();
        c.setIdComercio(idComercio);
        c.setMonto((int) monto);
        c.setDataTarjeta(dt);


        System.out.println("---------- COMPRA PROCESADA ----------");
        System.out.println("El id del comercio es: " + idComercio);
        System.out.println("El monto del compra es: " + monto);
        System.out.println("El propietario del compra es: " + propietario);
        System.out.println("La fecha del compra es: " + venc);
        System.out.println("---------------------------------------------");
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
