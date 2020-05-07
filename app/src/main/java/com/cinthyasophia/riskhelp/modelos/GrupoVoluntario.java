package com.cinthyasophia.riskhelp.modelos;

import java.util.ArrayList;

public class GrupoVoluntario {
    private String id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private int codigoPostal;
    private ArrayList<String> alertasRespondidas;

    public GrupoVoluntario(String nombre, String direccion, String telefono, ArrayList<String> alertasRespondidas, int codigoPostal, String email) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.codigoPostal = codigoPostal;
        this.alertasRespondidas = alertasRespondidas;
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

    public int getCodigoPostal() {
        return codigoPostal;
    }

    public ArrayList<String> getAlertasRespondidas() {
        return alertasRespondidas;
    }

}
