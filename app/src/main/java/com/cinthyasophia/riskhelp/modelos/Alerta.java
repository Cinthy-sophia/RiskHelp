package com.cinthyasophia.riskhelp.modelos;

import com.cinthyasophia.riskhelp.Util.Lib;

import java.io.Serializable;
import java.util.GregorianCalendar;

public class Alerta implements Serializable {
    private String id;
    private String descripcion;
    private String direccion;
    private boolean anonimo;
    private boolean tomada;
    private String denunciante;
    private String telefono;
    private String grupo;
    private int codigo_postal;
    private GregorianCalendar FechaHora;
    private Lib lib;


    public Alerta(String descripcion, String direccion, boolean anonimo, String denunciante, String telefono, String grupo, int codigo_postal, String FechaHora) {
        lib = new Lib();
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
        this.codigo_postal = codigo_postal;
        this.FechaHora =lib.getFecha(FechaHora);
    }

    public Alerta() {
    }

    public String getFechaHora() {
        return lib.getFecha(FechaHora);
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

    public int getCodigo_postal() {
        return codigo_postal;
    }
}
