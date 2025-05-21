package com.tallerjava.tallerjava.Transferencia.interfase;

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
public class TransferenciaAPI {
    private CompraService compraService;

}
