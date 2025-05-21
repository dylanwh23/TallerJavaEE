package com.tallerjava.tallerjava.Transferencia.dominio.repositorio;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Table(name = "transferencia")
@Entity
public class Transferencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private EnumEstadoTrans estado;
    private int montoTransferencia;
    private LocalDateTime fechaHora;
    private int idComercio;

    public Transferencia() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public EnumEstadoTrans getEstado() {
        return estado;
    }

    public void setEstado(EnumEstadoTrans estado) {
        this.estado = estado;
    }

    public int getMontoTransferencia() {
        return montoTransferencia;
    }

    public void setMontoTransferencia(int montoTransferencia) {
        this.montoTransferencia = montoTransferencia;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public int getIdComercio() {
        return idComercio;
    }

    public void setIdComercio(int idComercio) {
        this.idComercio = idComercio;
    }
}
