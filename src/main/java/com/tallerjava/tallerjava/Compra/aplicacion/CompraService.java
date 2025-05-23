package com.tallerjava.tallerjava.Compra.aplicacion;

import com.tallerjava.tallerjava.Comercio.dominio.repositorio.ComercioRepository;
import com.tallerjava.tallerjava.Compra.dominio.Compra;
import com.tallerjava.tallerjava.Compra.dominio.DataTarjeta;
import com.tallerjava.tallerjava.Compra.dominio.repositorio.CompraRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class CompraService implements CompraInterface {
    @PersistenceContext(unitName = "CompraPU")


    @Inject
    private CompraRepository compraRepository;

    @Override
    public  boolean procesarPago(Compra compra){
           compraRepository.save(compra);
           compraRepository.aumentarMontoVendido(compra.getMonto(), compra.getIdComercio());
           return true;
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
        return compraRepository.montoVendido(idComercio);
    }

}
