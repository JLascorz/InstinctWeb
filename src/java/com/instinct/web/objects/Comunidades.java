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
@ManagedBean(name="Comunidades")
@RequestScoped
public class Comunidades {
    private int idComunidad;
    private String nombre;

    public int getIdComunidad() {
        return idComunidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setIdComunidad(int idComunidad) {
        this.idComunidad = idComunidad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Comunidades(int idComunidad, String nombre) {
        this.setIdComunidad(idComunidad);
        this.setNombre(nombre);
    }
    
    public Comunidades(){}
    
}
