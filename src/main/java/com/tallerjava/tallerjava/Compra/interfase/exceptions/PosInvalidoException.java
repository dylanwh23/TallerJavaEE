package com.tallerjava.tallerjava.Compra.interfase.exceptions;


public class PosInvalidoException extends RuntimeException {
    public PosInvalidoException(long idComercio, int idPos) {
        super("El POS " + idPos + " para comercio " + idComercio + " no existe o esta inactivo");
    }
}



