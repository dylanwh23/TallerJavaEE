package com.tallerjava.tallerjava.Comercio.aplicacion;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class AIService {

    private static final String AI_SERVER_URL = "http://localhost:8000/analizar-reclamo";

    public String analizarReclamo(String texto) {
        Client client = null;
        try {
            client = ClientBuilder.newClient();
            String jsonPayload = "{\"texto\": \"" + escapeJson(texto) + "\"}";
            Response response = client.target(AI_SERVER_URL)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(jsonPayload));
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                String jsonResponse = response.readEntity(String.class);
                System.out.println("Respuesta de la IA: " + jsonResponse);
                return jsonResponse;
            } else {
                System.err.println("Error al llamar al servicio de IA. Status: " + response.getStatus());
                return "{\"error\": \"No se pudo procesar el reclamo\"}";
            }

        } finally {
            if (client != null) {
                client.close();
            }
        }
    }
    private String escapeJson(String text) {
        return text.replace("\"", "\\\"");
    }
}

