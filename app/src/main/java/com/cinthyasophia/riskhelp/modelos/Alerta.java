package com.cinthyasophia.riskhelp.modelos;

public class Alerta {
    private String id;
    private String descripcion;
    private String direccion;
    private boolean anonimo;
    private boolean tomada;
    private String denunciante;
    private String telefono;
    private String grupo;

    public Alerta(String descripcion, String direccion, boolean anonimo, boolean tomada, String denunciante, String telefono, String grupo) {
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
        this.tomada = tomada;
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
}
