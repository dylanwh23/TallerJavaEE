package com.tallerjava.tallerjava.Compra.infraestructura.persistencia;

import com.tallerjava.tallerjava.Comercio.dominio.Comercio;
import com.tallerjava.tallerjava.Compra.dominio.Compra;
import com.tallerjava.tallerjava.Compra.dominio.MontoActualVendido;
import com.tallerjava.tallerjava.Compra.dominio.repositorio.CompraRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class CompraRepositoryImp implements CompraRepository {

    @PersistenceContext(unitName = "CompraPU")
    private EntityManager em;

    @Override
    public List<Compra> ventasPeriodo(int idComercio, Date fechaInicial, Date fechaFinal) {
        try {
            TypedQuery<Compra> query = em.createQuery(
                    "SELECT c FROM Compra c " +
                            "WHERE c.fecha BETWEEN :fechaInicial AND :fechaFinal AND c.idComercio = :idComercio " +
                            "ORDER BY c.fecha ASC",
                    Compra.class);
            query.setParameter("fechaInicial", fechaInicial);
            query.setParameter("fechaFinal", fechaFinal);
            query.setParameter("idComercio", idComercio);    // ← ¡No lo olvides!
            return query.getResultList();
        } catch (PersistenceException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    @Override
    public List<Compra> ventasDiarias(int idComercio) {
        // 1. Definimos el inicio del día (00:00:00.000)…
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date inicioDia = cal.getTime();

        // 2. …y el inicio del siguiente día (para usar < finDia en la consulta)
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date finDia = cal.getTime();

        // 3. Creamos la consulta JPQL, filtrando por idComercio y rango de fechas
        TypedQuery<Compra> query = em.createQuery(
                "SELECT c FROM Compra c " +
                        "WHERE c.idComercio = :idComercio " +
                        "  AND c.fecha >= :inicioDia " +
                        "  AND c.fecha  < :finDia " +
                        "ORDER BY c.fecha ASC",
                Compra.class
        );
        query.setParameter("idComercio", idComercio);
        query.setParameter("inicioDia",   inicioDia);
        query.setParameter("finDia",      finDia);

        // 4. Devolvemos la lista (vacía si no hay resultados)
        return query.getResultList();
    }

    @Override
    public float montoVendido(int idComercio) {
        return 0;
    }

    @Override
    public void aumentarMontoVendido(float monto, int idComercio) {

            em.getTransaction().begin();

            int filas = em.createQuery(
                            "UPDATE MontoActualVendido m " +
                                    "   SET m.monto = m.monto + :monto " +
                                    " WHERE m.idComercio = :idComercio")
                    .setParameter("monto", monto)
                    .setParameter("idComercio", idComercio)
                    .executeUpdate();

            em.getTransaction().commit();

    }


    public void save(Compra compra){
        em.persist(compra);
    }










}
