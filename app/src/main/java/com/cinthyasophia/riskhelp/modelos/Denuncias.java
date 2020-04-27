package com.cinthyasophia.riskhelp;

public class Denuncias {
    private String id;
    private String descripcion;
    private String direccion;
    private boolean anonimo;
    private String denunciante;
    private String telefono;
    private String respondio;

    public Denuncias(String descripcion, String direccion, boolean anonimo, String denunciante, String telefono, String respondio) {
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.anonimo = anonimo;
        if (anonimo){
            this.denunciante = "Anónimo";
        }else{
            this.denunciante = denunciante;
        }
        this.telefono = telefono;
        this.respondio = respondio;
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

    public String getRespondio() {
        return respondio;
    }
}
