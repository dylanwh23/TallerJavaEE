package com.tallerjava.tallerjava.Comercio.interfase.Requests;

// Clase para el request de modificar comercio
public class ModificarComercioRequest{
    private String nombre = "";
    private String telefono = "";
    private String nuevoCorreo = "";
    private String nuevaContrasenia = "";

    // Constructor vacío
    public ModificarComercioRequest() {
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


    public String getNuevoCorreo() {
        return nuevoCorreo;
    }

    public void setNuevoCorreo(String nuevoCorreo) {
        this.nuevoCorreo = nuevoCorreo;
    }

    public String getNuevaContrasenia() {
        return nuevaContrasenia;
    }

    public void setNuevaContrasenia(String nuevaContrasenia) {
        this.nuevaContrasenia = nuevaContrasenia;
    }
}
