/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.web.objects;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * Classe per al objecte Comunidades.
 * @author Jordi Lascorz
 * @since 04/05/2017
 * @version 1.0
 */
@ManagedBean(name="Comunidades")
@RequestScoped
public class Comunidades {
    //<editor-fold defaultstate="collapsed" desc="Atributs">
    private int idComunidad;
    private String nombre;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getters/Setters">
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
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public Comunidades(int idComunidad, String nombre) {
        this.setIdComunidad(idComunidad);
        this.setNombre(nombre);
    }
    
    public Comunidades(){}
    //</editor-fold>
    
    
}
