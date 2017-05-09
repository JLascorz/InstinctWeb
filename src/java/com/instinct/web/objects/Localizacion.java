/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.web.objects;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author daw2017
 */
@ManagedBean(name="Localizacion")
@ViewScoped
public class Localizacion implements Serializable {
    	private int idAct;
        private int idComunidad, idProvincia, idMunicipio;
        private String calle;
	
	//Getters
	public int getIdAct(){
		return idAct;
	}
	
        public int getIdComunidad() {
            return idComunidad;
        }

        public int getIdMunicipio() {
            return idMunicipio;
        }

        public int getIdProvincia() {
            return idProvincia;
        }

        public String getCalle() {
            return calle;
        }

	//Setters
	public void setIdAct(int idAct){
		this.idAct = idAct;
	}

        public void setIdComunidad(int idComunidad) {
            this.idComunidad = idComunidad;
        }

        public void setIdProvincia(int idProvincia) {
            this.idProvincia = idProvincia;
        }

        public void setIdMunicipio(int idMunicipio) {
            this.idMunicipio = idMunicipio;
        }

        public void setCalle(String calle) {
            this.calle = calle;
        }

        public Localizacion(int idAct, int idComunidad, int idProvincia, int idMunicipio, String calle){
		this.setIdAct(idAct);
                this.setIdComunidad(idComunidad);
                this.setIdProvincia(idProvincia);
                this.setIdMunicipio(idMunicipio);
                this.setCalle(calle);

	}
	
	public Localizacion(){}
}
