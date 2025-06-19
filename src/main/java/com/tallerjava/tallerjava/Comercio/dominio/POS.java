package com.tallerjava.tallerjava.Comercio.dominio;

import jakarta.persistence.*;
import jakarta.ws.rs.DefaultValue;

@Entity
@Table(name = "Comercio_POS")
public class POS {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @DefaultValue("true")
    private Boolean estado;
    @ManyToOne
    @JoinColumn(name = "comercio_id", nullable = false)
    private Comercio comercio;

    public POS() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public void setComercio(Comercio comercio) {
        this.comercio = comercio;
    }

    public Comercio getComercio() {
        return this.comercio;
    }


}
