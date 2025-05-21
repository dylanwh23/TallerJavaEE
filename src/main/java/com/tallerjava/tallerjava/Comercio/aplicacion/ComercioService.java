package com.tallerjava.tallerjava.Comercio.aplicacion;

import com.tallerjava.tallerjava.Comercio.dominio.Comercio;
import com.tallerjava.tallerjava.Comercio.dominio.POS;
import com.tallerjava.tallerjava.Comercio.dominio.Reclamo;
import com.tallerjava.tallerjava.Comercio.dominio.repositorio.ComercioRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

@Stateless
public class ComercioService implements ComercioInterface{


    @Inject
    private ComercioRepository comercioRepository;


    public void altaComercio(Comercio comercio) {
        if(comercioRepository.existeComercio(comercio.getCorreo())){
            throw new IllegalArgumentException("El correo ya esta utilizado");
        }else{
            comercioRepository.save(comercio);
        }
    }

    public void modificarDatosComercio(String nombre, String telefono, String correo, String contrasenia, String nuevoCorreo, String nuevaContraseña) {
        Comercio comercio = comercioRepository.findComercioByCorreo(correo, contrasenia);
        if (comercio == null) {
            throw new SecurityException("Correo o contraseña incorrectos. Intente de nuevo.");
        }
        Boolean hayCambios = false;
        if (!nombre.isEmpty()) {
            comercio.setNombre(nombre);
            hayCambios = true;
        }
        if (!telefono.isEmpty()) {
            comercio.setTelefono(telefono);
            hayCambios = true;
        }
        if (!nuevoCorreo.isEmpty()) {
            comercio.setCorreo(nuevoCorreo);
            hayCambios = true;
        }
        if (!nuevaContraseña.isEmpty()) {
            comercio.setContrasenia(nuevaContraseña);
            hayCambios = true;
        }
        if (hayCambios) {
            comercioRepository.merge(comercio);
        } else {
            throw new IllegalArgumentException("No se realizaron cambios porque todos los campos estaban vacíos o inválidos.");
        }


    }

    public void agregarPos(String correoComercio, String contraseñaComercio){
        Comercio comercio = comercioRepository.findComercioByCorreo(correoComercio, contraseñaComercio);
        POS pos = new POS();
        pos.setEstado(true);
        pos.setComercio(comercio);
        comercio.addPos(pos);
        comercioRepository.merge(comercio);
    }

    public void cambiarEstadoPOS(String correoComercio, String contraseñaComercio, int idPOS, boolean estado){
        Comercio comercio = comercioRepository.findComercioByCorreo(correoComercio, contraseñaComercio);
        POS pos = comercio.getPos().get(idPOS);
        pos.setEstado(estado);
        comercioRepository.mergePos(pos);
    }

    public void realizarReclamo(String correoComercio, String constraseñaComercio, String texto){
        Reclamo reclamo = new Reclamo();

        Comercio comercio = comercioRepository.findComercioByCorreo(correoComercio,constraseñaComercio);

        if(comercio == null){
            throw new SecurityException("Comercio no encontrado. Intente de nuevo con los datos correctos.");
        }else{
            reclamo.setComercio(comercio);
            reclamo.setTexto(texto);
            reclamo.setFechaHora(java.time.LocalDateTime.now());
            comercioRepository.saveReclamo(reclamo);
        }
    }

}