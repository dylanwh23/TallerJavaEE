package com.tallerjava.tallerjava.Transferencia.aplicacion;


import com.tallerjava.tallerjava.Compra.dominio.Compra;
import com.tallerjava.tallerjava.Transferencia.dominio.EnumEstadoTrans;
import com.tallerjava.tallerjava.Transferencia.dominio.Transferencia;
import com.tallerjava.tallerjava.Transferencia.dominio.repositorio.TransferenciaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class TransferenciaService implements TransferenciaInterfase {
    @Inject
    private TransferenciaRepository TransferenciaRepository;

    public boolean recibirNotificacionTransferenciaDesdeMedioPago(int monto, int idComercio) {
        Transferencia nueva = new Transferencia();
        nueva.setMontoTransferencia(monto);
        nueva.setMontoTransferenciaComision(monto);
        nueva.setEstado(EnumEstadoTrans.PROCESANDO);
        nueva.setFechaHora(LocalDateTime.now());
        nueva.setIdComercio(idComercio);



        TransferenciaRepository.save(nueva);

        try {
            String comercioStr = "comercio" + idComercio;
            String urlString = "http://localhost:8080/bancoMOCK_SOAP_API-1.0-SNAPSHOT/notificarDeposito?comercio="
                    + URLEncoder.encode(comercioStr, StandardCharsets.UTF_8)
                    + "&monto=" + monto;
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
                    if ("APROBADO".equalsIgnoreCase(respuestaBanco)) {

                        System.out.println("-------------- COMPRA APROBADA --------------");
                        nueva.setEstado(EnumEstadoTrans.PROCESADO);
                        TransferenciaRepository.save(nueva);
                        return true;
                    } else {
                        System.out.println("-------------- COMPRA RECHAZADA --------------");
                        nueva.setEstado(EnumEstadoTrans.RECHAZADO);
                        TransferenciaRepository.save(nueva);
                        return false;
                    }
                }
            } else {
                System.out.println("-------------- COMPRA RECHAZADA POR ERROR TRANSFERENCIA"+responseCode+" --------------");
                nueva.setEstado(EnumEstadoTrans.RECHAZADO);
                TransferenciaRepository.save(nueva);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("-------------- COMPRA RECHAZADA POR ERROR de red=? TRANSFERENCIA" + e);
            nueva.setEstado(EnumEstadoTrans.RECHAZADO);
            TransferenciaRepository.save(nueva);
            return false;
        }
    }

    public List<Transferencia> consultarDepositos(int idComercio, LocalDateTime fInicial, LocalDateTime fFinal){
        if (idComercio <= 0) {
            throw new BadRequestException("El identificador del comercio es inválido.");
        }
        if (fInicial == null || fFinal == null) {
            throw new BadRequestException("Las fechas de inicio y fin son obligatorias.");
        }
        if (fInicial.isAfter(fFinal)) {
            throw new BadRequestException("La fecha de inicio debe ser anterior o igual a la fecha de fin.");
        }
        List<Transferencia> ventas = TransferenciaRepository.ventasPeriodo(idComercio, fInicial, fFinal);

        if (ventas.isEmpty()) {
            throw new NotFoundException(
                    String.format(
                            "No se encontraron transferencias para el comercio %d entre %tF y %tF.",
                            idComercio, fInicial, fFinal
                    )
            );
        }
        return TransferenciaRepository.ventasPeriodo(idComercio,fInicial,fFinal);
    }
}
