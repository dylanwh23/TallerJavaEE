package com.tallerjava.tallerjava.Comercio.interfase.Requests;

public class cambiarEstadoPosRequest {
    String correoComercio;
    String contraseñaComercio;
    int idPOS;
    boolean estado;

    public String getCorreoComercio() {
        return correoComercio;
    }

    public void setCorreoComercio(String correoComercio) {
        this.correoComercio = correoComercio;
    }

    public String getContraseñaComercio() {
        return contraseñaComercio;
    }

    public void setContraseñaComercio(String contraseñaComercio) {
        this.contraseñaComercio = contraseñaComercio;
    }

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
