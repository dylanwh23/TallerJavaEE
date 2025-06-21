package com.tallerjava.tallerjava.Compra.aplicacion;
import com.tallerjava.tallerjava.Monitoreo.aplicacion.MonitoreoInterface;
import com.tallerjava.tallerjava.Compra.dominio.Compra;
import com.tallerjava.tallerjava.Compra.dominio.EnumEstadoCompra;
import com.tallerjava.tallerjava.Compra.dominio.repositorio.CompraRepository;
import com.tallerjava.tallerjava.Compra.interfase.RateLimiter;
import com.tallerjava.tallerjava.Transferencia.aplicacion.TransferenciaInterfase;
import com.tallerjava.tallerjava.Transferencia.aplicacion.TransferenciaService;
import com.tallerjava.tallerjava.Transferencia.dominio.repositorio.TransferenciaRepository;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Stateless
public class CompraService implements CompraInterface {



    @Inject
    private CompraRepository compraRepository;

    @Inject
    private TransferenciaInterfase  transferenciaService;

    @Inject
    private MonitoreoInterface monitoreo;


    @Override
    public Compra procesarPago(Compra compra) {


        if (!RateLimiter.tryConsume()) {
            throw new WebApplicationException("Rate limit alcanzado", Response.Status.TOO_MANY_REQUESTS);
        }
        // --- validaciones iniciales ---
        if (compra == null) {
            throw new BadRequestException("La solicitud de pago no puede estar vacía.");
        }
        if (compra.getIdComercio() <= 0) {
            throw new BadRequestException("El identificador del comercio es inválido.");
        }
        if (compra.getMonto() <= 0) {
            throw new BadRequestException("El monto debe ser un valor positivo.");
        }
        if (compra.getDataTarjeta() == null) {
            throw new BadRequestException("Los datos de la tarjeta son obligatorios.");
        }

        // --- procesamiento ---
        compra.setFechaHora(new Date());
        compraRepository.save(compra);

        //Medio de pago

        try {

            String urlString = "http://localhost:8080/medioDePagoAPI_REST-1.0-SNAPSHOT/api/pagos/procesarQuery?monto=" + compra.getMonto()
                    + "&dataTarjeta=" + compra.getDataTarjeta().getNumero();
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    String respuestaBanco = response.toString();
                    System.out.println("-------------- "+respuestaBanco+" --------------");

                    // intentamos actualizar el monto vendido
                    if ("{\"estado\":\"APROBADO\"}".equalsIgnoreCase(respuestaBanco)){

                        if (transferenciaService.recibirNotificacionTransferenciaDesdeMedioPago((int) compra.getMonto(),compra.getIdComercio())) {
                            compra.setEstado(EnumEstadoCompra.APROBADA);
                            compraRepository.save(compra);
                            monitoreo.notificarPagoOk();
                            int rows = compraRepository.aumentarMontoVendido(compra.getMonto(), compra.getIdComercio());
                            if (rows == 0) {

                                // si no existía registro previo, lo creamos
                                compraRepository.crearMontoActualVendido(compra.getMonto(), compra.getIdComercio());
                            }

                        }else{
                            System.out.println("-------------- COMPRA RECHAZADA POR TRANSFERENCIA "+responseCode+" --------------");
                            compra.setEstado(EnumEstadoCompra.DESAPROBADA);
                            compraRepository.save(compra);
                            monitoreo.notificarPagoError();
                        }

                    }else{
                        System.out.println("-------------- COMPRA RECHAZADA POR MEDIO DE PAGO"+responseCode+" --------------");
                        compra.setEstado(EnumEstadoCompra.DESAPROBADA);
                        compraRepository.save(compra);
                        monitoreo.notificarPagoError();
                    }



                }
            } else {
                System.out.println("-------------- COMPRA RECHAZADA POR ERROR DE RED"+responseCode+" --------------");
                compra.setEstado(EnumEstadoCompra.DESAPROBADA);
                compraRepository.save(compra);
                monitoreo.notificarPagoError();

            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("-------------- COMPRA RECHAZADA POR ERROR de red=? / prende el bancpo tarad--------------");
            compra.setEstado(EnumEstadoCompra.DESAPROBADA);
            compraRepository.save(compra);
            monitoreo.notificarPagoError();

        }



        return compra;
    }

    @Override
    public List<Compra> resumenVentasDiarias(Integer idComercio) {

        if (idComercio == null) {
            throw new BadRequestException("El identificador del comercio es inválido.");
        }

        List<Compra> ventas = compraRepository.ventasDiarias(idComercio);
        if (ventas.isEmpty()) {
            throw new NotFoundException("No se encontraron ventas para el comercio con ID " + idComercio + " en el día de hoy.");
        }
        return ventas;
    }

    @Override
    public List<Compra> resumenVentasPorPeriodo(int idComercio, Date fechaInicio, Date fechaFin) {
        if (idComercio <= 0) {
            throw new BadRequestException("El identificador del comercio es inválido.");
        }
        if (fechaInicio == null || fechaFin == null) {
            throw new BadRequestException("Las fechas de inicio y fin son obligatorias.");
        }
        if (fechaInicio.after(fechaFin)) {
            throw new BadRequestException("La fecha de inicio debe ser anterior o igual a la fecha de fin.");
        }
        List<Compra> ventas = compraRepository.ventasPeriodo(idComercio, fechaInicio, fechaFin);
        if (ventas.isEmpty()) {
            throw new NotFoundException(
                    String.format(
                            "No se encontraron ventas para el comercio %d entre %tF y %tF.",
                            idComercio, fechaInicio, fechaFin
                    )
            );
        }
        return ventas;
    }

    @Override
    public float montoActualVendido(int idComercio) {
        if (idComercio <= 0) {
            throw new BadRequestException("El identificador del comercio es inválido.");
        }
        Float monto = compraRepository.montoActualVendido(idComercio);
        if (monto == null) {
            throw new NotFoundException("No existe registro de monto vendido para el comercio con ID " + idComercio);
        }
        return monto;
    }

}
