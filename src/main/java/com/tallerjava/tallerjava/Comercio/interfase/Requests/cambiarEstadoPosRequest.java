package com.tallerjava.tallerjava.Comercio.interfase.Requests;

import jakarta.validation.constraints.NotBlank;

public class cambiarEstadoPosRequest{
    @NotBlank(message = "El id del POS es obligatorio")
    int idPOS;
    @NotBlank(message = "El estado del POS es obligatorio")
    boolean estado;

    public int getIdPOS() {
        return idPOS;
    }

    public void setIdPOS(int idPOS) {
        this.idPOS = idPOS;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
