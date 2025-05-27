package com.tallerjava.tallerjava.Compra.aplicacion;

import com.tallerjava.tallerjava.Compra.dominio.Compra;
import com.tallerjava.tallerjava.Compra.dominio.EnumEstadoCompra;
import com.tallerjava.tallerjava.Compra.dominio.repositorio.CompraRepository;
import com.tallerjava.tallerjava.Transferencia.aplicacion.TransferenciaInterfase;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Date;
import java.util.List;

@Stateless
public class CompraService implements CompraInterface {


    @Inject
    private TransferenciaInterfase  transferenciaService;

    @Inject
    private CompraRepository compraRepository;

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
        transferenciaService.recibirNotificacionTransferenciaDesdeMedioPago((int) compra.getMonto(),compra.getIdComercio());
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
