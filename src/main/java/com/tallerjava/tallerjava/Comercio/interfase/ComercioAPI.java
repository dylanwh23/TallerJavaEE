package com.tallerjava.tallerjava.Comercio.interfase;

import com.tallerjava.tallerjava.Comercio.aplicacion.ComercioInterface;
import com.tallerjava.tallerjava.Comercio.dominio.Comercio;
import com.tallerjava.tallerjava.Comercio.eventos.ComercioCreadoEvent;
import com.tallerjava.tallerjava.Comercio.interfase.Requests.AuthRequest;
import com.tallerjava.tallerjava.Comercio.interfase.Requests.ModificarComercioRequest;
import com.tallerjava.tallerjava.Comercio.interfase.Requests.ReclamoRequest;
import com.tallerjava.tallerjava.Comercio.interfase.Requests.cambiarEstadoPosRequest;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("/comercio")
public class ComercioAPI {
    @Inject
    private ComercioInterface comercioService;



    @Inject
    Event<ComercioCreadoEvent> comercioCreadoEvent;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/registro")
    public Response registroComercio(@Valid Comercio comercio) {
        try {
            comercioService.altaComercio(comercio);
            if (comercioCreadoEvent != null) {
                comercioCreadoEvent.fire(new ComercioCreadoEvent(comercio.getId()));
            }
            return Response.status(Response.Status.OK).entity("Registro satisfactorio.").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Registro fallido, correo ya utilizado").build();
        }
    }

    @POST
    @Path("/agregarPOS")
    public Response agregarPos(@BeanParam @Valid AuthRequest auth) {
        try{
            comercioService.agregarPos(auth.getCorreo(), auth.getContrasenia());
            return Response.status(Response.Status.OK).entity("POS agregado satisfactoriamente.").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("POS agregado fallido, verifique los campos").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/reclamo")
    public Response realizarReclamo(@BeanParam @Valid AuthRequest auth, ReclamoRequest request){
        try{
            comercioService.realizarReclamo(auth.getCorreo(), auth.getContrasenia(), request.getTexto());
            return Response.status(Response.Status.OK).entity("Reclamo realizado satisfactoriamente.").build();
        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Reclamo fallido, verifique los campos").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/modificar")
    public Response modificarComercio(@BeanParam @Valid AuthRequest auth, ModificarComercioRequest request) {
        try{
            comercioService.modificarDatosComercio(
                    request.getNombre(),
                    request.getTelefono(),
                    auth.getCorreo(),
                    auth.getContrasenia(),
                    request.getNuevoCorreo(),
                    request.getNuevaContrasenia()
            );
            return Response.status(Response.Status.OK).entity("Datos modificados satisfactoriamente.").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Datos modificados fallidos, verifique los campos").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/cambiarEstadoPOS")
    public Response cambiarEstadoPOS(@BeanParam @Valid AuthRequest auth, cambiarEstadoPosRequest request){
        try{
            comercioService.cambiarEstadoPOS(auth.getCorreo(), auth.getContrasenia(), request.getIdPOS(), request.isEstado());
            return Response.status(Response.Status.OK).entity("Estado del POS modificado satisfactoriamente.").build();
        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Estado del POS modificado fallido, verifique los campos").build();
        }
    }

    public void setComercioService(ComercioInterface comercioService) {
        this.comercioService = comercioService;
    }
}