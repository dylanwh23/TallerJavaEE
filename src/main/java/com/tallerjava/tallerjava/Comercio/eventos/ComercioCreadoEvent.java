package com.tallerjava.tallerjava.Comercio.eventos;

public class ComercioCreadoEvent {
    private final long idComercio;
    public ComercioCreadoEvent(long idComercio) {
        this.idComercio = idComercio;
    }
    public long getIdComercio() {
        return idComercio;
    }

}
