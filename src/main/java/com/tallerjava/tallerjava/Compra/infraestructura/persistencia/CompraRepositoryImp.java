package com.tallerjava.tallerjava.Compra.infraestructura.persistencia;

import com.tallerjava.tallerjava.Comercio.dominio.Comercio;
import com.tallerjava.tallerjava.Compra.dominio.Compra;
import com.tallerjava.tallerjava.Compra.dominio.MontoActualVendido;
import com.tallerjava.tallerjava.Compra.dominio.repositorio.CompraRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Stateless
public class CompraRepositoryImp implements CompraRepository {

    @PersistenceContext(unitName = "TallerPU")
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
    public float montoActualVendido(int idComercio) {
        // Consulta directa al campo `monto` de la tabla de montos
        TypedQuery<Double> q = em.createQuery(
                "SELECT m.monto FROM MontoActualVendido m WHERE m.idComercio = :idComercio",
                Double.class
        );
        q.setParameter("idComercio", idComercio);

        Double resultado = null;
        try {
            resultado = q.getSingleResult();
        } catch (NoResultException e) {
            // Si no hay fila, devolvemos 0
        }

        return resultado != null ? resultado.floatValue() : 0.0f;
    }

    public int aumentarMontoVendido(double monto, int idComercio) {
        return em.createQuery(
                        "UPDATE MontoActualVendido m " +
                                "   SET m.monto = m.monto + :monto " +
                                " WHERE m.idComercio = :idComercio"
                )
                .setParameter("monto", monto)
                .setParameter("idComercio", idComercio)
                .executeUpdate();
    }

    @Override
    public void crearMontoActualVendido(double monto, int idComercio) {
        MontoActualVendido m = new MontoActualVendido();
        m.setIdComercio(idComercio);
        m.setMonto(monto);
        em.persist(m);
    }

    @Override
    public boolean findComercio(int idComercio) {
        try {
            em.createQuery(
                            "SELECT c FROM Compra c WHERE c.idComercio = :idComercio",
                            Comercio.class)
                    .setParameter("idComercio", idComercio)
                    .getSingleResult();
            return true;
        }catch (NoResultException e){
            return false;
        }
    }


    @Override
    public Compra procesarPago(Compra compra) {
        // 1) Persistimos la compra
        em.persist(compra);

        // 2) Buscamos el registro de montoActualVendido para este comercio
        TypedQuery<MontoActualVendido> q =
                em.createQuery(
                        "SELECT m FROM MontoActualVendido m WHERE m.idComercio = :idComercio",
                        MontoActualVendido.class);
        q.setParameter("idComercio", compra.getIdComercio());
        List<MontoActualVendido> lista = q.getResultList();

        if (lista.isEmpty()) {
            // No existía ninguno: lo creamos
            MontoActualVendido nuevo = new MontoActualVendido();
            nuevo.setIdComercio(compra.getIdComercio());
            nuevo.setMonto(compra.getMonto());
            em.persist(nuevo);
        } else {
            // Ya había uno: le sumamos el monto de la compra
            MontoActualVendido existente = lista.get(0);
            existente.setMonto(existente.getMonto() + compra.getMonto());
            em.merge(existente);
        }

        // 3) Devolvemos la compra recién persistida (con su id generado)
        return compra;
    }

    @Override
    public Compra save(Compra compra) {
        em.persist(compra);
        return compra;
    }










}
