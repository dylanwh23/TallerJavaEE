package com.mediodepago.api.mediodepagoapi_rest.dto;

public class PagoRequest {
    private double monto;
    private int dataTarjeta;        // ahora es int

    public PagoRequest() {}
    public PagoRequest(double monto, int dataTarjeta) {
        this.monto = monto;
        this.dataTarjeta = dataTarjeta;
    }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public int getDataTarjeta() { return dataTarjeta; }
    public void setDataTarjeta(int dataTarjeta) { this.dataTarjeta = dataTarjeta; }
}
