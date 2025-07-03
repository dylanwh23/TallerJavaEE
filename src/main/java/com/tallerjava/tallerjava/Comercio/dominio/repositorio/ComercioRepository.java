package com.tallerjava.tallerjava.Comercio.dominio.repositorio;

import com.tallerjava.tallerjava.Comercio.dominio.Comercio;
import com.tallerjava.tallerjava.Comercio.dominio.POS;
import com.tallerjava.tallerjava.Comercio.dominio.Reclamo;

import java.util.Optional;


public interface ComercioRepository {
    Comercio findComercioByCorreo(String correo, String constraseña);
    Boolean existeComercio(String correo);
    void save(Comercio comercio);
    void merge(Comercio comercio);
    void mergePos(POS pos);
    void saveReclamo(Reclamo reclamo);
    Optional<POS> findPOSbyComercioAndId(long idComercio, int idPos);
    }
