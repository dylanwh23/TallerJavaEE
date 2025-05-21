package com.tallerjava.tallerjava.Comercio.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Cuenta {
    @Id
    String numeroCuenta;
    String banco;
    String divisa;
    public Cuenta() {}

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getDivisa() {
        return divisa;
    }

    public void setDivisa(String divisa) {
        this.divisa = divisa;
    }
}
