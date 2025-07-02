package com.tallerjava.tallerjava.Compra.eventos;


public class TransferenciaSolicitadaEvent {
    private final int monto;
    private final long idComercio;
    private       boolean resultado;

    public TransferenciaSolicitadaEvent(int monto, long idComercio) {
        this.monto      = monto;
        this.idComercio = idComercio;
    }
    public int   getMonto()      { return monto; }
    public long  getIdComercio() { return idComercio; }
    public boolean isResultado() { return resultado; }
    public void    setResultado(boolean resultado) {
        this.resultado = resultado;
    }
}


