package com.tallerjava.tallerjava.Compra.aplicacion;

import com.tallerjava.tallerjava.Compra.dominio.repositorio.Compra;
import com.tallerjava.tallerjava.Compra.dominio.repositorio.DataTarjeta;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Date;

@Stateless
public class CompraService {
    @PersistenceContext(unitName = "ComercioPU")
    static EntityManager em;
    public static boolean enviarTransferencia(int monto, Date fecha, int idComercio, DataTarjeta tarjeta){
        Compra compra = new Compra();

        try {
            compra.setMonto(monto);
            compra.setDataTarjeta(tarjeta);
            compra.setFechaHora(fecha);
            compra.setIdComercio(idComercio);
            em.persist(compra);
            return true;
        } catch (Exception e){
            return false;
        }

    }
}
