package com.cinthyasophia.riskhelp.modelos;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String nombre;
    private String email;
    private String telefono;
    private int codigoPostal;
    private boolean voluntario;

    public Usuario(String nombre, String email, String telefono, int codigoPostal, boolean voluntario) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.codigoPostal = codigoPostal;
        this.voluntario = voluntario;

    }

    public Usuario() {

    }

    public boolean isVoluntario() {
        return voluntario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public int getCodigoPostal() {
        return codigoPostal;
    }

}
