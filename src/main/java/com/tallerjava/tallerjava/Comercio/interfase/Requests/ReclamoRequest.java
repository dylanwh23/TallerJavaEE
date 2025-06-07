package com.tallerjava.tallerjava.Comercio.interfase.Requests;

import jakarta.validation.constraints.NotBlank;

public class ReclamoRequest{
    @NotBlank(message = "El texto del reclamo es obligatorio")
    private String texto;

    // Getters y Setters
    public String getTexto() {
        return texto;
    }
    public void setTexto(String texto) {
        this.texto = texto;
    }
}
