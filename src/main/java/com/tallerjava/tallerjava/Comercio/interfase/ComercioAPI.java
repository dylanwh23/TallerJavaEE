package com.tallerjava.tallerjava.Comercio.interfase;

import com.tallerjava.tallerjava.Comercio.aplicacion.ComercioInterface;
import com.tallerjava.tallerjava.Comercio.dominio.Comercio;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("/comercio")
public class ComercioAPI {
    @Inject
    private ComercioInterface comercioService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/registro")
    public void registroComercio(@Valid Comercio comercio) {
        comercioService.altaComercio(comercio);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/agregarPOS")
    public Response agregarPos(@Valid AgregarPosRequest request) {
        System.out.println("Correo recibido: " + request.getCorreo());
        System.out.println("Contraseña recibida: " + request.getContrasenia());

        comercioService.agregarPos(request.getCorreo(), request.getContrasenia());

        return Response.status(Response.Status.OK).entity("POS agregado satisfactoriamente.").build();
    }

    public static class AgregarPosRequest {
        private String correo;
        private String contrasenia;

        // Getters y Setters
        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }

        public String getContrasenia() {
            return contrasenia;
        }

        public void setContrasenia(String contrasenia) {
            this.contrasenia = contrasenia;
        }
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/reclamo")
    public void realizarReclamo(ReclamoRequest request){
        comercioService.realizarReclamo(request.getCorreo(), request.getContrasenia(), request.getTexto());
    }
    public static class ReclamoRequest {
        private String correo;
        private String contrasenia;
        private String texto;

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }

        public String getContrasenia() {
            return contrasenia;
        }

        public void setContrasenia(String contrasenia) {
            this.contrasenia = contrasenia;
        }

        public String getTexto() {
            return texto;
        }

        public void setTexto(String texto) {
            this.texto = texto;
        }
    }

    //modificarComercio
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/modificar")
    public void modificarComercio(ModificarComercioRequest request) {
        comercioService.modificarDatosComercio(
                request.getNombre(),
                request.getTelefono(),
                request.getCorreo(),
                request.getContrasenia(),
                request.getNuevoCorreo(),
                request.getNuevaContrasenia()
        );
    }

    protected static class ModificarComercioRequest {
        protected String nombre;
        protected String telefono;
        protected String correo;
        protected String contrasenia;
        protected String nuevoCorreo;
        protected String nuevaContrasenia;

        public ModificarComercioRequest() {
        }


        //getters y setters
        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getTelefono() {
            return telefono;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }

        public String getContrasenia() {
            return contrasenia;
        }

        public void setContrasenia(String contrasenia) {
            this.contrasenia = contrasenia;
        }

        public String getNuevoCorreo() {
            return nuevoCorreo;
        }

        public void setNuevoCorreo(String nuevoCorreo) {
            this.nuevoCorreo = nuevoCorreo;
        }

        public String getNuevaContrasenia() {
            return nuevaContrasenia;
        }

        public void setNuevaContrasenia(String nuevaContrasenia) {
            this.nuevaContrasenia = nuevaContrasenia;
        }
    }
    // fin


}