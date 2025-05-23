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




}
