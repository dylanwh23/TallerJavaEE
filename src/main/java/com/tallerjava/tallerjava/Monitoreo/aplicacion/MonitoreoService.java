package com.tallerjava.tallerjava.Monitoreo.aplicacion;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.write.Point;
import com.influxdb.client.domain.WritePrecision;

import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.logging.Logger;

@ApplicationScoped
public class MonitoreoService implements MonitoreoInterface {

    private static final Logger logger = Logger.getLogger(MonitoreoService.class.getName());

    private static final String INFLUX_URL = "http://localhost:8086";
    private static final String DATABASE = "tallerjava";
    private static final char[] TOKEN = "".toCharArray(); // vacío si Influx no usa auth

    private final InfluxDBClient influxDBClient;

    public MonitoreoService() {
        this.influxDBClient = InfluxDBClientFactory.create(INFLUX_URL, TOKEN);
    }

    private void registrarEvento(String nombreEvento) {
        Point punto = Point
                .measurement("monitoreo")
                .addTag("evento", nombreEvento)
                .addField("valor", 1)
                .time(Instant.now(), WritePrecision.MS);

        try (WriteApi writeApi = influxDBClient.getWriteApi()) {
            writeApi.writePoint(DATABASE, "", punto); // retention policy vacío por defecto
            logger.info("Evento registrado: " + nombreEvento);
        } catch (Exception e) {
            logger.severe("Error al registrar en InfluxDB: " + e.getMessage());
        }
    }

    @Override
    public void notificarPagoOk() {
        System.out.println("Se realizó un pago exitosamente.");
        registrarEvento("pago_ok");
    }

    @Override
    public void notificarPagoError() {
        System.out.println("El pago fue rechazado.");
        registrarEvento("pago_error");
    }

    @Override
    public void notificarTransferencia() {
        System.out.println("Se realizó una transferencia desde el MedioDePago.");
        registrarEvento("transferencia");
    }

    @Override
    public void notificarReclamoComercio() {
        System.out.println("Un comercio realizó un reclamo.");
        registrarEvento("reclamo_comercio");
    }
}
