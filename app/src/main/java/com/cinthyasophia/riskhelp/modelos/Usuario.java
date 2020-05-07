package com.cinthyasophia.riskhelp.modelos;

import java.util.ArrayList;

public class Usuario {
    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private int codigoPostal;
    private ArrayList<String> denuncias_hechas;

    public Usuario(String nombre, String apellido, String email, String telefono, int codigoPostal, ArrayList<String> denuncias_hechas) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.codigoPostal = codigoPostal;
        this.denuncias_hechas = denuncias_hechas;
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

    public ArrayList<String> getDenuncias_hechas() {
        return denuncias_hechas;
    }
}
