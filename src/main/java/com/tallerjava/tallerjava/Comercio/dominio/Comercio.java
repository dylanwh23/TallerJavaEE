package com.tallerjava.tallerjava.Comercio.dominio;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "comercio", uniqueConstraints = @UniqueConstraint(columnNames = "correo"))
public class Comercio {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    @NotEmpty
    private String nombre;
    @NotEmpty
    private String telefono;
    @NotEmpty
    private String correo;
    @NotEmpty
    private String contrasenia;
    @OneToMany(mappedBy = "comercio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<POS> pos = new ArrayList<>();
    public Comercio() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public void setId(Long id) {
        this.id = id;
    }

    public List<POS> getPos() {
        return pos;
    }

    public void setPos(List<POS> pos) {
        this.pos = pos;
    }

    public void addPos(POS pos) {
        this.pos.add(pos);
        pos.setComercio(this);
    }

    public void removePos(POS pos) {
        this.pos.remove(pos);
        pos.setComercio(null);
    }

}
