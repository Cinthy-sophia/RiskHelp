package com.cinthyasophia.riskhelp.modelos;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {
    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private int codigoPostal;
    private ArrayList<String> alertas_hechas;

    public Usuario(String nombre, String apellido, String email, String telefono, int codigoPostal, ArrayList<String> alertas_hechas) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.codigoPostal = codigoPostal;
        this.alertas_hechas = alertas_hechas;
    }

    public Usuario() {

    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
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

    public ArrayList<String> getAlertas_hechas() {
        return alertas_hechas;
    }
}
