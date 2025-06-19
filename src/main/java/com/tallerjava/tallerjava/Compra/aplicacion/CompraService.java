package com.tallerjava.tallerjava.Compra.aplicacion;

import com.tallerjava.tallerjava.Compra.dominio.Compra;
import com.tallerjava.tallerjava.Compra.dominio.EnumEstadoCompra;
import com.tallerjava.tallerjava.Compra.dominio.repositorio.CompraRepository;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import com.tallerjava.tallerjava.Monitoreo.aplicacion.MonitoreoInterface;
import java.util.Date;
import java.util.List;

@Stateless
public class CompraService implements CompraInterface {



    @Inject
    private CompraRepository compraRepository;
    @Inject
    private MonitoreoInterface monitoreoService;

    @Override
    public  Compra procesarPago(Compra compra){
        compra.setEstado(EnumEstadoCompra.PROCESANDOSE);
        compra.setFechaHora(new Date());
        compraRepository.save(compra);
        int rows = compraRepository.aumentarMontoVendido(compra.getMonto(), compra.getIdComercio());
        if (rows == 0) {
            compraRepository.crearMontoActualVendido(compra.getMonto(),
                    compra.getIdComercio());
        }
        System.out.println("hola");
        monitoreoService.notificarPagoOk();
        return compra;
    }


    @Override
    public List<Compra> resumenVentasDiarias(int idComercio){
        List<Compra> ventasDiarias = compraRepository.ventasDiarias(idComercio);

        return ventasDiarias;
    }

    @Override
    public List<Compra> resumenVentasPorPeriodo(int idComercio , Date fechaInicio, Date fechaFin){
        List<Compra> ventasPeriodo = compraRepository.ventasPeriodo(idComercio, fechaInicio, fechaFin);
        return ventasPeriodo;
    }

    @Override
    public float montoActualVendido(int idComercio){
        return compraRepository.montoActualVendido(idComercio);
    }

}
