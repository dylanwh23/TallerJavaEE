package com.tallerjava.tallerjava.Compra.aplicacion;

import com.tallerjava.tallerjava.Compra.dominio.Compra;
import com.tallerjava.tallerjava.Compra.dominio.EnumEstadoCompra;
import com.tallerjava.tallerjava.Compra.dominio.repositorio.CompraRepository;
import com.tallerjava.tallerjava.Transferencia.aplicacion.TransferenciaInterfase;
import com.tallerjava.tallerjava.Transferencia.aplicacion.TransferenciaService;
import com.tallerjava.tallerjava.Transferencia.dominio.repositorio.TransferenciaRepository;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.util.Date;
import java.util.List;

@Stateless
public class CompraService implements CompraInterface {



    @Inject
    private CompraRepository compraRepository;

    @Inject
    private TransferenciaInterfase  transferenciaService;


    @Override
    public Compra procesarPago(Compra compra) {
        // --- validaciones iniciales ---
        if (compra == null) {
            throw new BadRequestException("La solicitud de pago no puede estar vacía.");
        }
        if (compra.getIdComercio() <= 0) {
            throw new BadRequestException("El identificador del comercio es inválido.");
        }
        if (compra.getMonto() <= 0) {
            throw new BadRequestException("El monto debe ser un valor positivo.");
        }
        if (compra.getDataTarjeta() == null) {
            throw new BadRequestException("Los datos de la tarjeta son obligatorios.");
        }
        // podrías añadir más validaciones sobre número, cvv, etc.

        // --- procesamiento ---
        compra.setEstado(EnumEstadoCompra.PROCESANDOSE);
        compra.setFechaHora(new Date());
        compraRepository.save(compra);

        // intentamos actualizar el monto vendido
        int rows = compraRepository.aumentarMontoVendido(compra.getMonto(), compra.getIdComercio());
        if (rows == 0) {
            // si no existía registro previo, lo creamos
            compraRepository.crearMontoActualVendido(compra.getMonto(), compra.getIdComercio());
        }
        transferenciaService.recibirNotificacionTransferenciaDesdeMedioPago((int) compra.getMonto(),compra.getIdComercio());
        return compra;
    }

    @Override
    public List<Compra> resumenVentasDiarias(Integer idComercio) {

        if (idComercio == null) {
            throw new BadRequestException("El identificador del comercio es inválido.");
        }

        List<Compra> ventas = compraRepository.ventasDiarias(idComercio);
        if (ventas.isEmpty()) {
            throw new NotFoundException("No se encontraron ventas para el comercio con ID " + idComercio + " en el día de hoy.");
        }
        return ventas;
    }

    @Override
    public List<Compra> resumenVentasPorPeriodo(int idComercio, Date fechaInicio, Date fechaFin) {
        if (idComercio <= 0) {
            throw new BadRequestException("El identificador del comercio es inválido.");
        }
        if (fechaInicio == null || fechaFin == null) {
            throw new BadRequestException("Las fechas de inicio y fin son obligatorias.");
        }
        if (fechaInicio.after(fechaFin)) {
            throw new BadRequestException("La fecha de inicio debe ser anterior o igual a la fecha de fin.");
        }
        List<Compra> ventas = compraRepository.ventasPeriodo(idComercio, fechaInicio, fechaFin);
        if (ventas.isEmpty()) {
            throw new NotFoundException(
                    String.format(
                            "No se encontraron ventas para el comercio %d entre %tF y %tF.",
                            idComercio, fechaInicio, fechaFin
                    )
            );
        }
        return ventas;
    }

    @Override
    public float montoActualVendido(int idComercio) {
        if (idComercio <= 0) {
            throw new BadRequestException("El identificador del comercio es inválido.");
        }
        Float monto = compraRepository.montoActualVendido(idComercio);
        if (monto == null) {
            throw new NotFoundException("No existe registro de monto vendido para el comercio con ID " + idComercio);
        }
        return monto;
    }

}
