package com.tallerjava.tallerjava.Compra.eventos;

import com.tallerjava.tallerjava.Comercio.eventos.ComercioCreadoEvent;
import com.tallerjava.tallerjava.Compra.dominio.Comercios;
import com.tallerjava.tallerjava.Compra.infraestructura.persistencia.CompraRepositoryImp;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CompraListener {

    @Inject
    CompraRepositoryImp compraComercioRepository;

    @Transactional
    public void onComercioCreado(@Observes ComercioCreadoEvent event) {
        long idComercio = event.getIdComercio();
        System.out.println("🟢 Evento recibido: se creó comercio con ID = " + idComercio);

        Comercios com = new Comercios();
        com.setId(idComercio);
        if (!compraComercioRepository.findComercio((int) idComercio)) {
            compraComercioRepository.saveComercio(com);
            System.out.println("✅ Comercio registrado en Compra_Comercio.");
        } else {
            System.out.println("ℹ️ El comercio ya existía en Compra_Comercio.");
        }
    }



}
