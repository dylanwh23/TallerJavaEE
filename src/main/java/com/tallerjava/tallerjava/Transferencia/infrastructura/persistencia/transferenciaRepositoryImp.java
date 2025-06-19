package com.tallerjava.tallerjava.Transferencia.infrastructura.persistencia;

import com.tallerjava.tallerjava.Transferencia.dominio.Transferencia;
import com.tallerjava.tallerjava.Compra.dominio.repositorio.CompraRepository;
import com.tallerjava.tallerjava.Transferencia.dominio.repositorio.TransferenciaRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Stateless
public class transferenciaRepositoryImp implements TransferenciaRepository {

    @PersistenceContext(unitName = "TallerPU")
    private EntityManager em;

    @Override
    public List<Transferencia> ventasPeriodo(int idComercio, LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
        try {
            TypedQuery<Transferencia> query = em.createQuery(
                    "SELECT t FROM Transferencia t " +
                            "WHERE t.fechaHora BETWEEN :fechaInicial AND :fechaFinal AND t.idComercio = :idComercio " +
                            "ORDER BY t.fechaHora ASC",
                    Transferencia.class);
            query.setParameter("fechaInicial", fechaInicial);
            query.setParameter("fechaFinal", fechaFinal);
            query.setParameter("idComercio", idComercio);
            return query.getResultList();
        } catch (PersistenceException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    @Override
    public Transferencia save(Transferencia transferencia) {
        em.persist(transferencia);
        return transferencia;
    }










}
