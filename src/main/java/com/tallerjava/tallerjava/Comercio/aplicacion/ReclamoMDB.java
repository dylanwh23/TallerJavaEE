package com.tallerjava.tallerjava.Comercio.aplicacion;

import com.tallerjava.tallerjava.Comercio.dominio.Comercio;
import com.tallerjava.tallerjava.Comercio.dominio.Reclamo;
import com.tallerjava.tallerjava.Comercio.dominio.repositorio.ComercioRepository;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.JMSException;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


@MessageDriven(
        activationConfig = {
                @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/queue/ReclamosQueue"),
                @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue")
        }
)
@ApplicationScoped
public class ReclamoMDB implements MessageListener
{
    @Inject
    public ComercioRepository comercioRepository;

    @Inject
    public AIService aiService;

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof MapMessage) {
                MapMessage mapMessage = (MapMessage) message;
                String correoComercio = mapMessage.getString("correoComercio");
                String texto = mapMessage.getString("texto");
                String contraseniaComercio = mapMessage.getString("contraseniaComercio");
                System.out.println("MDB: Mensaje recibido para el comercio: " + correoComercio);
                Comercio comercio = comercioRepository.findComercioByCorreo(correoComercio, contraseniaComercio);
                if (comercio == null) {
                    System.err.println("MDB: El comercio " + correoComercio + " no fue encontrado. Descartando mensaje.");
                    return;
                }

                String prioridadJson = aiService.analizarReclamo(texto);
                //cast a string
                JsonObject obj = JsonParser.parseString(prioridadJson).getAsJsonObject();
                String prioridad = obj.get("prioridad").getAsString();

                Reclamo reclamo = new Reclamo();

                reclamo.setComercio(comercio);
                reclamo.setTexto(texto); // Guardamos el texto (quizás ya analizado)
                reclamo.setFechaHora(java.time.LocalDateTime.now());
                reclamo.setPrioridad(prioridad);
                comercioRepository.saveReclamo(reclamo);
                System.out.println("MDB: Reclamo procesado y guardado exitosamente para el comercio: " + correoComercio);
            }
        } catch (JMSException e) {
            System.err.println("MDB: Error al procesar el mensaje JMS: " + e.getMessage());
        }
    }

}
