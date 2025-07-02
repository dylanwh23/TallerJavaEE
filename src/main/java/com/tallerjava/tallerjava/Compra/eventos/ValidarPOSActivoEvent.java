package com.tallerjava.tallerjava.Compra.eventos;

public class ValidarPOSActivoEvent {
    private final long idComercio;
    private final int  idPos;
    private       boolean posActivo;

    public ValidarPOSActivoEvent(long idComercio, int idPos) {
        this.idComercio = idComercio;
        this.idPos      = idPos;
    }
    public long    getIdComercio()       { return idComercio; }
    public int     getIdPos()            { return idPos; }
    public boolean isPosActivo()         { return posActivo; }
    public void    setPosActivo(boolean activo) {
        this.posActivo = activo;
    }
}