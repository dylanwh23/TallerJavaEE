package com.tallerjava.tallerjava.Transferencia.aplicacion;

import com.tallerjava.tallerjava.Transferencia.dominio.Transferencia;

import java.time.LocalDateTime;
import java.util.List;

public interface TransferenciaInterfase {
    boolean recibirNotificacionTransferenciaDesdeMedioPago (int monto,int idComercio);


    List<Transferencia> consultarDepositos(int idComercio, LocalDateTime fInicial, LocalDateTime fFinal);

}
