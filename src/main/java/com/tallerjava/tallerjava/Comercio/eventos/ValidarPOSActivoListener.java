package com.tallerjava.tallerjava.Comercio.eventos;

import com.tallerjava.tallerjava.Comercio.dominio.POS;
import com.tallerjava.tallerjava.Comercio.dominio.repositorio.ComercioRepository;
import com.tallerjava.tallerjava.Compra.eventos.ValidarPOSActivoEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class ValidarPOSActivoListener {

    @Inject
    private ComercioRepository comercioRepository;

    public void onValidar(@Observes ValidarPOSActivoEvent ev) {
        // Busca el POS en tu repositorio de Comercio
        boolean activo = comercioRepository
                .findPOSbyComercioAndId(ev.getIdComercio(), ev.getIdPos())
                .map(POS::getEstado)
                .orElse(false);
        ev.setPosActivo(activo);
    }
}
