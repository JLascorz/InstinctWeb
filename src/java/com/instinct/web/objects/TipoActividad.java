/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.web.objects;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author daw2017
 */
@ManagedBean(name="TipoAct")
@ViewScoped
public class TipoActividad {
	private int idTipo;
	private String nombre, descripcion;
	
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


//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Constructores">
        public TipoActividad(int idTipo, String nombre, String descripcion){
		this.setIdTipo(idTipo);
		this.setNombre(nombre);
		this.setDescripcion(descripcion);
	}
	
	public TipoActividad(){}
//</editor-fold>
	
	    
}
