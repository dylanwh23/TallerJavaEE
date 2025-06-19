package com.tallerjava.tallerjava.Monitoreo.aplicacion;

import com.tallerjava.tallerjava.Comercio.dominio.repositorio.ComercioRepository;
import com.tallerjava.tallerjava.Monitoreo.dominio.MonitoreoRepository;
import jakarta.inject.Inject;
import java.util.logging.Logger;
import jakarta.enterprise.context.ApplicationScoped;
@ApplicationScoped
public class MonitoreoService implements MonitoreoInterface {
    private static final Logger logger = Logger.getLogger(MonitoreoService.class.getName());




     @Override
    public void notificarPagoOk() {
         System.out.println("Se realizó un pago exitosamente.");
    }

    @Override
    public void notificarPagoError() {
        System.out.println("El pago fue rechazado.");
    }

    @Override
    public void notificarTransferencia() {
        System.out.println("Se realizó una transferencia desde el MedioDePago.");
    }

    @Override
    public void notificarReclamoComercio() {
        System.out.println("Un comercio realizó un reclamo.");
    }
}
