package com.tallerjava.tallerjava.Compra.aplicacion;

import com.tallerjava.tallerjava.Compra.dominio.Compra;
import com.tallerjava.tallerjava.Compra.dominio.repositorio.DataTarjeta;

import java.util.Date;
import java.util.List;

public interface CompraInterface {
    public Compra procesarPago(Compra compra);
    public List<Compra> resumenVentasDiarias(Integer idComercio);
    public List<Compra> resumenVentasPorPeriodo(int idComercio, Date fechaInicio, Date fechaFin);
    public float montoActualVendido(int idComercio);
}
