package com.tallerjava.tallerjava.Compra.aplicacion;

import com.tallerjava.tallerjava.Compra.dominio.repositorio.DataTarjeta;

import java.util.Date;

public interface CompraInterface {
    public boolean enviarTransferencia(int monto, Date fecha, int idComercio, DataTarjeta tarjeta);
}
