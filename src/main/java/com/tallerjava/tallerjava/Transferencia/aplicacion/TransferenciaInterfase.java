package com.tallerjava.tallerjava.Transferencia.aplicacion;

import com.tallerjava.tallerjava.Transferencia.dominio.Transferencia;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.List;

public interface TransferenciaInterfase {
    public boolean recibirNotificacionTransferenciaDesdeMedioPago (int monto,int idComercio) ;


    public List<Transferencia> consultarDepositos(int idComercio, LocalDateTime fInicial, LocalDateTime fFinal);

}
