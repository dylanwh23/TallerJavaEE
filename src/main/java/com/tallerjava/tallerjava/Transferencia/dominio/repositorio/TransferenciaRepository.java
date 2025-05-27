package com.tallerjava.tallerjava.Transferencia.dominio.repositorio;

import com.tallerjava.tallerjava.Compra.dominio.Compra;
import com.tallerjava.tallerjava.Transferencia.dominio.Transferencia;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface TransferenciaRepository {
    Transferencia save(Transferencia Transferencia);
    List<Transferencia> ventasPeriodo(int idComerio, LocalDateTime fechaInicial, LocalDateTime fechaFinal);



}
