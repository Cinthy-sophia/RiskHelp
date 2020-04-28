package com.cinthyasophia.riskhelp.modelos;

import java.util.ArrayList;

public class GruposVoluntarios {
    private String id;
    private String nombre;
    private String direccion;
    private String telefono;
    private ArrayList<String> denunciasRespondidas;
    private ArrayList<String> integrantes;

    public GruposVoluntarios(String nombre, String direccion, String telefono, ArrayList<String> denunciasRespondidas, ArrayList<String> integrantes) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.denunciasRespondidas = denunciasRespondidas;
        this.integrantes = integrantes;
    }

    public String getId() {
        return id;
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

    public ArrayList<String> getDenunciasRespondidas() {
        return denunciasRespondidas;
    }

    public ArrayList<String> getIntegrantes() {
        return integrantes;
    }
}
