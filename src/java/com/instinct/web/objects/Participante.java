/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.web.objects;

/**
 *
 * @author daw2017
 */
public class Participante {
    private int idUsuario, idActividad;

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

    public Participante(int idUsuario, int idActividad) {
        this.setIdUsuario(idUsuario);
        this.setIdActividad(idActividad);
    }
    
    public Participante() {
    }

    
    
    
}
