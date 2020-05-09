package com.cinthyasophia.riskhelp.modelos;

import java.io.Serializable;

public class Alerta implements Serializable {
    private String id;
    private String descripcion;
    private String direccion;
    private boolean anonimo;
    private boolean tomada;
    private String denunciante;
    private String telefono;
    private String grupo;
    private int codigoPostal;


    public Alerta(String descripcion, String direccion, boolean anonimo, String denunciante, String telefono, String grupo, int codigoPostal) {
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.anonimo = anonimo;
        if (anonimo){
            this.denunciante = "Anónimo";
        }else{
            this.denunciante = denunciante;
        }
        this.telefono = telefono;
        this.grupo = grupo;
        this.codigoPostal= codigoPostal;
    }

    public void setTomada(boolean tomada) {
        this.tomada = tomada;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public boolean isAnonimo() {
        return anonimo;
    }

    public String getDenunciante() {
        return denunciante;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getGrupo() {
        return grupo;
    }

    public boolean isTomada() {
        return tomada;
    }

    public int getCodigoPostal() {
        return 0;
    }
}
