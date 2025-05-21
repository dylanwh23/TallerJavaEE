package com.tallerjava.tallerjava.Comercio.aplicacion;

import com.tallerjava.tallerjava.Comercio.dominio.Comercio;

public interface ComercioInterface {
    public void altaComercio(Comercio comercio);
    public void modificarDatosComercio(String nombre, String telefono, String correo, String contrasenia, String nuevoCorreo, String nuevaContraseña);
    public void agregarPos(String correoComercio, String contraseñaComercio);
    public void cambiarEstadoPOS(String correoComercio, String contraseñaComercio, int idPOS, boolean estado);
    public void realizarReclamo(String correoComercio, String constraseñaComercio, String texto);
}
