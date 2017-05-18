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
@ManagedBean(name="Servicio")
@ViewScoped
public class Servicio {
    //<editor-fold defaultstate="collapsed" desc="Atributos">
	private int idServicio;
        private String nombre;
	private String descripcion;
        private boolean activo;
//</editor-fold>
	
//<editor-fold defaultstate="collapsed" desc="Getters">
        public int getIdServicio(){
            return idServicio;
        }

        public String getNombre() {
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
        public void setIdServicio(int idServicio){
            this.idServicio = idServicio;
        }

        public void setNombre(String nombre) {
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
	public Servicio(int idServicio, String nombre, String descripcion, boolean activo){
            this.setIdServicio(idServicio);
            this.setNombre(nombre);
            this.setDescripcion(descripcion);
            this.setActivo(activo);
	}
	
	public Servicio(){}
//</editor-fold>
	
  
}
