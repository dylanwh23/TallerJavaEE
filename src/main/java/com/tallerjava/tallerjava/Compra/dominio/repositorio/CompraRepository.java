package com.tallerjava.tallerjava.Compra.dominio.repositorio;

import com.tallerjava.tallerjava.Compra.dominio.Comercios;
import com.tallerjava.tallerjava.Compra.dominio.Compra;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface CompraRepository{

    Compra procesarPago(Compra compra);
    Compra save(Compra compra);
    List<Compra> ventasPeriodo(int idComerio, Date fechaInicial, Date fechaFinal);
    List<Compra> ventasDiarias(int idComercio);
    float montoActualVendido(int idComercio);
    int aumentarMontoVendido(double monto, int idComercio);
    void crearMontoActualVendido(double monto, int idComercio);
    boolean findComercio(int idComercio);
    void saveComercio(Comercios comercios);
    boolean findByComercioId(int idComercio);
}
