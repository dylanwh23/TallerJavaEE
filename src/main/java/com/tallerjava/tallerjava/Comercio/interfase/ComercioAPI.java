package com.tallerjava.tallerjava.Comercio.interfase;

import com.tallerjava.tallerjava.Comercio.aplicacion.ComercioInterface;
import com.tallerjava.tallerjava.Comercio.dominio.Comercio;
import com.tallerjava.tallerjava.Comercio.interfase.Requests.AgregarPosRequest;
import com.tallerjava.tallerjava.Comercio.interfase.Requests.ModificarComercioRequest;
import com.tallerjava.tallerjava.Comercio.interfase.Requests.ReclamoRequest;
import com.tallerjava.tallerjava.Comercio.interfase.Requests.cambiarEstadoPosRequest;
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
    public Response registroComercio(@Valid Comercio comercio) {
        try {
            comercioService.altaComercio(comercio);
            return Response.status(Response.Status.OK).entity("Registro satisfactorio.").build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/agregarPOS")
    public Response agregarPos(@Valid AgregarPosRequest request) {
        try{
            comercioService.agregarPos(request.getCorreo(), request.getContrasenia());
            return Response.status(Response.Status.OK).entity("POS agregado satisfactoriamente.").build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/reclamo")
    public Response realizarReclamo(ReclamoRequest request){
        try{
            comercioService.realizarReclamo(request.getCorreo(), request.getContrasenia(), request.getTexto());
            return Response.status(Response.Status.OK).entity("Reclamo realizado satisfactoriamente.").build();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/modificar")
    public Response modificarComercio(@Valid ModificarComercioRequest request) {
        try{
            comercioService.modificarDatosComercio(
                    request.getNombre(),
                    request.getTelefono(),
                    request.getCorreo(),
                    request.getContrasenia(),
                    request.getNuevoCorreo(),
                    request.getNuevaContrasenia()
            );
            return Response.status(Response.Status.OK).entity("Datos modificados satisfactoriamente.").build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/cambiarEstadoPOS")
    public Response cambiarEstadoPOS(cambiarEstadoPosRequest request){
        try{
            comercioService.cambiarEstadoPOS(request.getCorreoComercio(), request.getContraseñaComercio(), request.getIdPOS(), request.isEstado());
            return Response.status(Response.Status.OK).entity("Estado del POS modificado satisfactoriamente.").build();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public void setComercioService(ComercioInterface comercioService) {
        this.comercioService = comercioService;
    }
}