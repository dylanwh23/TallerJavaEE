package com.tallerjava.tallerjava.Compra.dominio.repositorio;

import com.tallerjava.tallerjava.Compra.dominio.Compra;

import java.util.Date;
import java.util.List;

public interface CompraRepository{
    void save(Compra compra);
    List<Compra> ventasPeriodo(int idComerio, Date fechaInicial, Date fechaFinal);
    List<Compra> ventasDiarias(int idComercio);
    float montoVendido(int idComercio);
    void aumentarMontoVendido(float monto, int idComercio);


}
