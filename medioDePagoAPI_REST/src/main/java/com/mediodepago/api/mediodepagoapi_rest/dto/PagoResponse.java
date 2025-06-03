package com.mediodepago.api.mediodepagoapi_rest.dto;

public class PagoResponse {
    private String estado;
    private String mensaje;

    public PagoResponse() {}
    public PagoResponse(String estado, String mensaje) {
        this.estado = estado;

    }

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}