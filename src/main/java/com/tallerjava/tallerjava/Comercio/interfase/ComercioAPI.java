package com.tallerjava.tallerjava.Comercio.interfase;

import com.tallerjava.tallerjava.Comercio.aplicacion.ComercioService;
import com.tallerjava.tallerjava.Comercio.dominio.repositorio.Comercio;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/comercio")
public class ComercioAPI {
    @Inject
    private ComercioService comercioService;
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void registroComercio(Comercio comercio) {
        comercioService.altaComercio("juan", "asdad", "asdasd", "asdad");
    }
}
