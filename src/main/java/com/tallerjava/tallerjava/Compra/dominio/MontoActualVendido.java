package com.tallerjava.tallerjava.Compra.dominio;

import jakarta.persistence.*;

@Entity
@Table(name="Compra_montoActualVendido")
public class MontoActualVendido {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private double monto;
    private int idComercio;


    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public int getIdComercio() {
        return idComercio;
    }

    public void setIdComercio(int idComercio) {
        this.idComercio = idComercio;
    }
}
