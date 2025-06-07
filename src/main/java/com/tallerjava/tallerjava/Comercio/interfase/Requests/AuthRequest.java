package com.tallerjava.tallerjava.Comercio.interfase.Requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.QueryParam;

public class AuthRequest {
    
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Formato de correo inválido")
    @QueryParam(value = "c")
    private String correo;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @QueryParam(value = "p")
    private String contrasenia;

    // Getters y setters
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