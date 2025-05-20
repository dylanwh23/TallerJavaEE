package com.tallerjava.tallerjava.Comercio.dominio.repositorio;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class POS {
    @Id
    int id;
    Boolean estado;
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
}
