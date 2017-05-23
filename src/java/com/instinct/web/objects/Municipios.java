/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.web.objects;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * Classe per al objecte Municipios.
 * @author Jordi Lascorz
 * @since 04/05/2017
 * @version 1.0
 */
@ManagedBean(name="Municipios")
@RequestScoped
public class Municipios {
    //<editor-fold defaultstate="collapsed" desc="Atributs">
    private int idProvincia, idMunicipio;
    private String Nombre;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getters/Setters">
    public int getIdProvincia(){
        return idProvincia;
    }
    
    public int getIdMunicipio() {
        return idMunicipio;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setIdProvincia(int idProvincia) {
        this.idProvincia = idProvincia;
    }

    public void setIdMunicipio(int idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Constructores">
    public Municipios(int idMunicipio, String Nombre, int idProvincia) {
        this.setIdMunicipio(idMunicipio);
        this.setNombre(Nombre);
        this.setIdProvincia(idProvincia);
        
    }
    
    public Municipios(){}
    //</editor-fold>
    
}
