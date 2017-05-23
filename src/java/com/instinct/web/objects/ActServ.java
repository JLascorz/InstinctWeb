/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.web.objects;

/**
 * Classe per a la relacio entre activitat i serveis
 * @author Jordi Lascorz
 * @since 14/05/2017
 * @version 1.0
 */
public class ActServ {
    //<editor-fold defaultstate="collapsed" desc="Atributs">
    private int idActividad, idServicio;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getters/Setters">
    public int getIdActividad() {
        return idActividad;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public ActServ(int idActividad, int idServicio) {
        this.setIdActividad(idActividad);
        this.setIdServicio(idServicio);
    }
    
    public ActServ(){}
    //</editor-fold>
    
    
}
