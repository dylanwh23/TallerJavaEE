package com.tallerjava.tallerjava.Compra.dominio.repositorio;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="compra")
public class Compra {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)

    private long id;
    @Embedded
    private DataTarjeta DataTarjeta;
    private int monto;
    private EnumEstadoCompra estado;
    private Date fechaHora;
    private int idComercio;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DataTarjeta getDataTarjeta() {
        return DataTarjeta;
    }

    public void setDataTarjeta(DataTarjeta dataTarjeta) {
        DataTarjeta = dataTarjeta;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public EnumEstadoCompra getEstado() {
        return estado;
    }

    public void setEstado(EnumEstadoCompra estado) {
        this.estado = estado;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public int getIdComercio() {
        return idComercio;
    }

    public void setIdComercio(int idComercio) {
        this.idComercio = idComercio;
    }
}
