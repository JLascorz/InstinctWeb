/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.web.objects;

import static java.lang.Boolean.FALSE;

/**
 * Classe per al objecte Comentario - No s'utilitza.
 * @author Jordi Lascorz
 * @since 04/05/2017
 * @version 1.0
 */
public class Comentario {
        //<editor-fold defaultstate="collapsed" desc="Atributs">
        private int idAct, idCom, idUser, idComResp;
	private String fecha, contenido;
	//resp -> Es un booleano que nos dice si es una respuesta o no.
	private boolean resp;
	
	private static final boolean DEF_RESP = FALSE;
    //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Getters">
        public int getIdAct(){
            return idAct;
        }
	
        public int getIdUser(){
            return idUser;
        }
	
        public int getIdCom(){
            return idCom;
        }      
	
        public String getFecha(){
            return fecha;
        }
	
        public String getContenido(){
            return contenido;
        }
	
        public int getIdComRespondido(){
            return idComResp;
        }
	
        public Boolean getRespuestas(){
            return resp;
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Setters">
        public void setIdAct(int idAct){
            this.idAct = idAct;
        }

        public void setIdUser(int idUser){
            this.idUser = idUser;
        }

        public void setIdCom(int idCom){
            this.idCom = idCom;
        }

        public void setFecha(String fecha){
            this.fecha = fecha;
        }

        public void setContenido(String contenido){
            this.contenido = contenido;
}
	
        public void setIdComRespondido(int IdComResp){
            this.idComResp = IdComResp;
        }
	
        public void setRespuestas(Boolean resp){
            this.resp = resp;
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Constructores">
        public Comentario(int idAct, int idUser, int idCom, String fecha, String contenido, int IdComResp, Boolean resp){
		this.setIdAct(idAct);
		this.setIdUser(idUser);
		this.setIdCom(idCom);
		this.setFecha(fecha);
		this.setContenido(contenido);
		this.setIdComRespondido(IdComResp);
		this.setRespuestas(resp);
	}
	
	//Constructor escribir respuesta comentario
	public Comentario(int idAct, int idUser, int idCom, String fecha, String contenido, int IdComResp){
		this.setIdAct(idAct);
		this.setIdUser(idUser);
		this.setIdCom(idCom);
		this.setFecha(fecha);
		this.setContenido(contenido);
		this.setIdComRespondido(IdComResp);
		this.setRespuestas(DEF_RESP);
	}
	
	//Construcor escribir comentario normal
	public Comentario(int idAct, int idUser, int idCom, String fecha, String contenido){
		this.setIdAct(idAct);
		this.setIdUser(idUser);
		this.setIdCom(idCom);
		this.setFecha(fecha);
		this.setContenido(contenido);
		this.setRespuestas(DEF_RESP);
	}
        
        public Comentario(){}
        //</editor-fold>

	
}
