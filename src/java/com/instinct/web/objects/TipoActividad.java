/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.web.objects;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * Classe per al objecte TipoActividad.
 * @author Jordi Lascorz
 * @since 04/05/2017
 * @version 1.0
 */
@ManagedBean(name="TipoAct")
@ViewScoped
public class TipoActividad {
    //<editor-fold defaultstate="collapsed" desc="Atributs">
        private int idTipo;
	private String nombre, descripcion;
        private boolean activo;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Getters">
            public int getIdTipo(){
                return idTipo;
            }

            public String getNombre(){
                return nombre;
            }

            public String getDescripcion(){
                return descripcion;
            }

            public boolean getActivo(){
                return activo;
            }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Setters">
            public void setIdTipo(int idTipo){
                this.idTipo = idTipo;
            }

            public void setNombre(String nombre){
                this.nombre = nombre;
            }

            public void setDescripcion(String descripcion){
                this.descripcion = descripcion;
            }

            public void setActivo(boolean activo) {
                this.activo = activo;
            }


    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Constructores">
            public TipoActividad(int idTipo, String nombre, String descripcion, boolean activo){
                    this.setIdTipo(idTipo);
                    this.setNombre(nombre);
                    this.setDescripcion(descripcion);
                    this.setActivo(activo);
            }

            public TipoActividad(){}
    //</editor-fold>
	
	    
}
