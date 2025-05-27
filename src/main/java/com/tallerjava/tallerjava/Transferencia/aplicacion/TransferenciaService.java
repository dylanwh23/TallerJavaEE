package com.tallerjava.tallerjava.Transferencia.aplicacion;


import com.tallerjava.tallerjava.Transferencia.dominio.EnumEstadoTrans;
import com.tallerjava.tallerjava.Transferencia.dominio.Transferencia;
import com.tallerjava.tallerjava.Transferencia.dominio.repositorio.TransferenciaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class TransferenciaService implements TransferenciaInterfase {
    @Inject
    private TransferenciaRepository TransferenciaRepository;

    public boolean recibirNotificacionTransferenciaDesdeMedioPago (int monto,int idComercio) {
        Transferencia nueva = new Transferencia();
        nueva.setMontoTransferencia(monto);
        nueva.setMontoTransferenciaComision(monto);
        nueva.setEstado(EnumEstadoTrans.PROCESADO);
        nueva.setFechaHora(LocalDateTime.now());
        nueva.setIdComercio(idComercio);

        TransferenciaRepository.save(nueva);
        return true;
        //notificacion de banco va AQUI API SOAP

    }
    public List<Transferencia> consultarDepositos(int idComercio, LocalDateTime fInicial, LocalDateTime fFinal){
        return TransferenciaRepository.ventasPeriodo(idComercio,fInicial,fFinal);
    }
}
