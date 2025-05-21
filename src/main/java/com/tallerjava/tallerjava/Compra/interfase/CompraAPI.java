package com.tallerjava.tallerjava.Compra.interfase;

import com.tallerjava.tallerjava.Comercio.dominio.repositorio.Comercio;
import com.tallerjava.tallerjava.Compra.aplicacion.CompraService;
import com.tallerjava.tallerjava.Compra.dominio.repositorio.Compra;
import com.tallerjava.tallerjava.Compra.dominio.repositorio.DataTarjeta;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

import java.util.Date;

@ApplicationScoped
@Path("/compra")
public class CompraAPI {
    @Inject
    private CompraService compraService;
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void enviarTransferencia(Compra compra) {
        DataTarjeta tarjeta = new DataTarjeta();
        Date fecha = new Date();
        fecha.setYear(fecha.getYear() + 1900);
        fecha.setMonth(fecha.getMonth() + 1);
        fecha.setDate(fecha.getDate() + 1);
        tarjeta.setCvv(134);
        tarjeta.setNumero(1234);
        tarjeta.setPropietario("Bobi");
        tarjeta.setVencimiento(fecha);
        int monto = 1000;
        int id = 123;
        compraService.enviarTransferencia(monto, fecha, id, tarjeta);
    }
}
