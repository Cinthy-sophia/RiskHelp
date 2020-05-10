package com.cinthyasophia.riskhelp.modelos;

import java.io.Serializable;

public class GrupoVoluntario implements Serializable {
    private String id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private int codigo_postal;

    public GrupoVoluntario(String nombre, String direccion, String telefono, int codigo_postal, String email) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.codigo_postal = codigo_postal;
        this.email = email;
    }

    public GrupoVoluntario() {

    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public int getCodigo_postal() {
        return codigo_postal;
    }

}
