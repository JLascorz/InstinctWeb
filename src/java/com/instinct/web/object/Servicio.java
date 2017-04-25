/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.web.object;

/**
 *
 * @author daw2017
 */
public class Servicio {
    //<editor-fold defaultstate="collapsed" desc="Atributos">
	private int idServicio;
	private String descripcion;
//</editor-fold>
	
//<editor-fold defaultstate="collapsed" desc="Getters">
        public int getIdServicio(){
            return idServicio;
        }

        public String getDescripcion(){
            return descripcion;
        }
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Setters">
        public void setIdServicio(int idServicio){
            this.idServicio = idServicio;
        }

        public void setDescricpion(String descripcion){
            this.descripcion = descripcion;
        }
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Constructores">
	public Servicio(int idServicio, String descripcion){
            this.setIdServicio(idServicio);
            this.setDescricpion(descripcion);
	}
	
	public Servicio(){}
//</editor-fold>
	
  
}
