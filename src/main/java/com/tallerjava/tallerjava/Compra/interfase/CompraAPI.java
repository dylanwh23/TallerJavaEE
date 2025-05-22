package com.tallerjava.tallerjava.Compra.interfase;

import com.tallerjava.tallerjava.Compra.aplicacion.CompraService;
import com.tallerjava.tallerjava.Compra.dominio.Compra;
import com.tallerjava.tallerjava.Compra.dominio.DataTarjeta;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

import java.util.Date;
import java.util.List;

@ApplicationScoped
@Path("/compra")
public class CompraAPI {
    @Inject
    private CompraService compraService;


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/pago")
    public void procesarPago(Compra compra) {
        compraService.procesarPago(compra);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/ventasDiarias")
    public void ventasDiarias(int idComercio) {
        List<Compra> ventasDiarias = compraService.resumenVentasDiarias(idComercio);

        for (Compra venta : ventasDiarias) {
            System.out.println("---------- RESUMEN DE VENTAS DEL DIA ----------");
            System.out.println("Precio:" + venta.getMonto());
            System.out.println("Fecha:" + venta.getFechaHora());
            System.out.println("Estado:" + venta.getEstado());
        }

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/ventasPeriodo")
    public void ventasPeriodo(int idComercio, Date fechaInicio, Date fechaFin) {

        List<Compra> ventasDiarias = compraService.resumenVentasPorPeriodo(idComercio, fechaInicio, fechaFin);

        for (Compra venta : ventasDiarias) {
            System.out.println("---------- RESUMEN DE VENTAS DEL PERIODO " + fechaFin + " - " + fechaFin +  "----------");
            System.out.println("Precio:" + venta.getMonto());
            System.out.println("Fecha:" + venta.getFechaHora());
            System.out.println("Estado:" + venta.getEstado());
        }


    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/montoActualVendido")
    public void montoActualVendido(int idComercio) {
        float monto = compraService.montoActualVendido(idComercio);

        System.out.println("Monto vendido hoy: " + monto);
    }

}
