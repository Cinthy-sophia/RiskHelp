package com.cinthyasophia.riskhelp;

import java.util.ArrayList;

public class Usuario {
    private String id;
    private String nombre;
    private String apellido;
    private String direccion;
    private String correo_electronico;
    private String numero_telefono;
    private boolean voluntario;
    private ArrayList<String> denuncias_hechas;

    public Usuario(String nombre, String apellido, String direccion, String correo_electronico, String numero_telefono, boolean voluntario, ArrayList<String> denuncias_hechas) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.correo_electronico = correo_electronico;
        this.numero_telefono = numero_telefono;
        this.voluntario = voluntario;
        this.denuncias_hechas = denuncias_hechas;
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

    public String getDireccion() {
        return direccion;
    }

    public String getCorreo_electronico() {
        return correo_electronico;
    }

    public String getNumero_telefono() {
        return numero_telefono;
    }

    public boolean isVoluntario() {
        return voluntario;
    }

    public ArrayList<String> getDenuncias_hechas() {
        return denuncias_hechas;
    }
}
