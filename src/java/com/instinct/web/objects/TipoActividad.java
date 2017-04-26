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
public class TipoActividad {
	private int idTipo;
	private String nombre, descripcion, pathImg;
	
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

        public String getPathImg(){
            return pathImg;
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

        public void setPathImg(String pathImg){
            this.pathImg = pathImg;
        }
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Constructores">
        public TipoActividad(int idTipo, String nombre, String descripcion, String pathImg){
		this.setIdTipo(idTipo);
		this.setNombre(nombre);
		this.setDescripcion(descripcion);
		this.setPathImg(pathImg);
	}
	
	public TipoActividad(){}
//</editor-fold>
	
	    
}
