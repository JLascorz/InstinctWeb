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
public class Localizacion {
    	private int idAct;
	private String latitud, longitud;
	
	//Getters
	public int getIdAct(){
		return idAct;
	}
	
	public String getLatitud(){
		return latitud;
	}
	
	public String getLongitud(){
		return longitud;
	}
	
	//Setters
	public void setIdAct(int idAct){
		this.idAct = idAct;
	}
	
	public void setLatitud(String latitud){
		this.latitud = latitud;
	}
	
	public void setLongitud(String longitud){
		this.longitud = longitud;
	}
        
        public Localizacion(int idAct, String latitud, String longitud){
		this.setIdAct(idAct);
		this.setLatitud(latitud);
		this.setLongitud(longitud);
	}
	
	public Localizacion(){}
}
