// src/main/java/com/tallerjava/tallerjava/medioPago/dto/PagoRequest.java
package com.tallerjava.tallerjava.medioPago.dto;

public class PagoRequest {
    private int idComercio;
    private float monto;

    public PagoRequest() {}

    public int getIdComercio() {
        return idComercio;
    }
    public void setIdComercio(int idComercio) {
        this.idComercio = idComercio;
    }
    public float getMonto() {
        return monto;
    }
    public void setMonto(float monto) {
        this.monto = monto;
    }
}
