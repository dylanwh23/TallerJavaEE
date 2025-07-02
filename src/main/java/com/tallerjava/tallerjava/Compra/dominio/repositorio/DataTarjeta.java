package com.tallerjava.tallerjava.Compra.dominio.repositorio;

import jakarta.persistence.Embeddable;

import java.util.Date;
@Embeddable
public class DataTarjeta {

    private int numero;
    private int cvv;
    private Date vencimiento;
    private String propietario;


    public DataTarjeta(int numero, int cvv, Date vencimiento, String propietario) {
        this.numero      = numero;
        this.cvv         = cvv;
        this.vencimiento = vencimiento;
        this.propietario = propietario;
    }

    public DataTarjeta() {

    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public Date getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(Date vencimiento) {
        this.vencimiento = vencimiento;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }
}
