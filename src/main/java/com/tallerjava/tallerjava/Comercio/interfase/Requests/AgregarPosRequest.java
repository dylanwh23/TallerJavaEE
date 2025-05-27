package com.tallerjava.tallerjava.Comercio.interfase.Requests;

// Clase para el request de agregar POS
public class AgregarPosRequest {
    private String correo;
    private String contrasenia;

    // Getters y Setters
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
