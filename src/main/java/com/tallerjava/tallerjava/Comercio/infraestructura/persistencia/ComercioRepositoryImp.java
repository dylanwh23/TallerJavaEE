package com.tallerjava.tallerjava.Comercio.infraestructura.persistencia;
import com.tallerjava.tallerjava.Comercio.dominio.Comercio;
import com.tallerjava.tallerjava.Comercio.dominio.POS;
import com.tallerjava.tallerjava.Comercio.dominio.Reclamo;
import com.tallerjava.tallerjava.Comercio.dominio.repositorio.ComercioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class ComercioRepositoryImp implements ComercioRepository {
    @PersistenceContext(unitName = "TallerPU")
    private EntityManager em;
    @Override
    public Comercio findComercioByCorreo(String correo, String contraseña) {
        try {
            // Recuperar el comercio por correo
            Comercio comercio = em.createQuery(
                            "SELECT c FROM Comercio c WHERE c.correo = :correo",
                            Comercio.class)
                    .setParameter("correo", correo)
                    .getSingleResult();

            // Verificar la contraseña hasheada
            if (BCrypt.checkpw(contraseña, comercio.getContrasenia())) {
                return comercio; // Retornar el comercio si la contraseña es válida
            } else {
                return null; // Retornar null si la contraseña es incorrecta
            }
        } catch (NoResultException e) {
            return null; // Devuelve null si no hay resultados
        }
    }
    @Override
    public Boolean existeComercio(String correo){
        try {
            em.createQuery(
                    "SELECT c FROM Comercio c WHERE c.correo = :correo",
                    Comercio.class)
                    .setParameter("correo", correo)
                    .getSingleResult();
            return true;
        }catch (NoResultException e){
            return false;
        }
    }
    @Override
    public void save(Comercio comercio){
        em.persist(comercio);
    }
    @Override
    public void merge(Comercio comercio){
        em.merge(comercio);
    }

    @Override
    public void mergePos(POS pos){
        em.merge(pos);
    }

    @Override
    public void saveReclamo(Reclamo reclamo){
        em.persist(reclamo);
    }

    public void setEm(EntityManager emm) {
         em = emm;
    }

}
