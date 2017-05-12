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
public class ActServ {
    private int idActividad, idServicio;

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

    public ActServ(int idActividad, int idServicio) {
        this.setIdActividad(idActividad);
        this.setIdServicio(idServicio);
    }
    
    public ActServ(){}
    
}
