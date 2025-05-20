package com.tallerjava.tallerjava.Comercio.aplicacion;

import com.tallerjava.tallerjava.Comercio.dominio.repositorio.Comercio;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
@Stateless
public class ComercioService {
    @PersistenceContext(unitName = "ComercioPU")
    EntityManager em;
    public void altaComercio(String nombre, String telefono, String correo, String contrasenia) {
        Comercio comercio = new Comercio();
        comercio.setNombre(nombre);
        comercio.setTelefono(telefono);
        comercio.setCorreo(correo);
        comercio.setContrasenia(contrasenia);
        em.persist(comercio);
    }
}
