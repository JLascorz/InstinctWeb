/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.web.objects;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author daw2017
 */
@ManagedBean(name="Provincias")
@RequestScoped
public class Provincias {
    private int idProvincia, idComunidad;
    private String nombre;

    public int getIdProvincia(){
        return idProvincia;
    }
    
    public int getIdComunidad() {
        return idComunidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setIdProvincia(int idProvincia) {
        this.idProvincia = idProvincia;
    }

    public void setIdComunidad(int idComunidad) {
        this.idComunidad = idComunidad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Provincias(int idProvincia, String nombre, int idComunidad) {
        this.setIdProvincia(idProvincia);
        this.setNombre(nombre);
        this.setIdComunidad(idComunidad);
        
    }
    
    public Provincias(){}
}
