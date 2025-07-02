package com.tallerjava.tallerjava.Transferencia.eventos;

import com.tallerjava.tallerjava.Compra.eventos.TransferenciaSolicitadaEvent;
import com.tallerjava.tallerjava.Transferencia.aplicacion.TransferenciaInterfase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class TransferenciaListener {

    @Inject
    private TransferenciaInterfase transferenciaService;

    public void onTransferenciaSolicitada(@Observes TransferenciaSolicitadaEvent evento) {
        boolean ok = transferenciaService.recibirNotificacionTransferenciaDesdeMedioPago(
                evento.getMonto(),
                Math.toIntExact(evento.getIdComercio())
        );
        evento.setResultado(ok);
    }
}