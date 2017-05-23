/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.web.objects;

/**
 * Classe per al objecte de la relacio entre Usuario-Actividad.
 * @author Jordi Lascorz
 * @since 04/05/2017
 * @version 1.0
 */
public class Participante {
    //<editor-fold defaultstate="collapsed" desc="Atributs">
    private int idUsuario, idActividad;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getters/Setters">
    public int getIdActividad() {
        return idActividad;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public Participante(int idUsuario, int idActividad) {
        this.setIdUsuario(idUsuario);
        this.setIdActividad(idActividad);
    }
    
    public Participante() {
    }
    //</editor-fold>
    

    
    
    
}
