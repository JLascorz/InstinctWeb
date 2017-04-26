/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.web.objects;

import static java.lang.Boolean.FALSE;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author daw2017
 */
@ManagedBean(name="Usuario")
public class Usuario{
    private String nombre, apellidos, email, password, nif, genero;
    private Boolean activo, baja;
    private int diffus, idUser, numVisitas;

    private String fecNac, fecUltVis, fecRegistro;
    private String rePassword;
    private static final boolean DEF_BAJA = FALSE;
    private static final boolean DEF_ACTIVO = FALSE;
    private static final int DEF_VISITA = 0;

    //<editor-fold defaultstate="collapsed" desc="Getters">
    public int getIdUser(){
    	return idUser;
    }
	
    public String getNombre(){
	return nombre;
    }
	
    public String getApellidos(){
	return apellidos;
    }
	
    public String getEmail(){
	return email;
    }
	
    public String getPassword(){
    	return password;
    }
	
    public String getNif(){
	return nif;
    }
	
    public String getGenero(){
	return genero;
    }
	
    public int getDiffus(){
    	return diffus;
    }
	
    public Boolean getActivo(){
    	return activo;
    }
	
    public Boolean getBaja(){
	return baja;
    }
	
    public String getFecNac(){
    	return fecNac;
    }
	
    public String getFecUltVis(){
	return fecUltVis;
    }
	
    public int getNumVisitas(){
	return numVisitas;
    }
	
    public String getFecRegistro(){
    	return fecRegistro;
    }

    public String getRePassword() {
        return rePassword;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Setters">
    public void setIdUser(int idUser){
    	this.idUser = idUser;
    }
	
    public void setNombre(String nombre){
    	this.nombre = nombre;
    }
	
    public void setApellidos(String apellidos){
    	this.apellidos = apellidos;
    }
	
    public void setEmail(String email){
    	this.email = email;
    }
	
    public void setPassword(String password){
    	this.password = password;
    }
	
    public void setNif(String nif){
    	this.nif = nif;
    }
	
    public void setGenero(String genero){
    	this.genero = genero;
    }
	
    public void setFecNac(String fecNac){
    	this.fecNac = fecNac;
    }
	
    public void setActivo(Boolean activo){
    	this.activo = activo;
    }
	
    public void setBaja(Boolean baja){
    	this.baja = baja;
    }
	
    public void setDiffus(int diffus){
    	this.diffus = diffus;
    }
	
    public void setFecUltVis(String fecUltVis){
    	this.fecUltVis = fecUltVis;
    }
	
    public void setNumVisitas(int numVisitas){
    	this.numVisitas = numVisitas;
    }
	
    public void setFecRegistro(String fecRegistro){
	this.fecRegistro = fecRegistro;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }
    
    
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Constructores">
    //Constructor completo
	public Usuario(int idUser, String nombre, String apellidos, String email, String password,
				   String nif, String genero, String fecNac, Boolean activo, Boolean baja, int diffus, 
				   String fecUltVis, int numVisitas, String fecRegistro){
		this.setIdUser(idUser);
		this.setNombre(nombre);
		this.setApellidos(apellidos);
		this.setEmail(email);
		this.setPassword(password);
		this.setNif(nif);
		this.setGenero(genero);
		this.setFecNac(fecNac);
		this.setActivo(activo);
		this.setBaja(baja);
		this.setDiffus(diffus);
		this.setFecUltVis(fecUltVis);
		this.setNumVisitas(numVisitas);
		this.setFecRegistro(fecRegistro);
	}
	
	//Constructor registro completo
	public Usuario(String nombre, String apellidos, String email, String password,
				   String nif, String genero, String fecNac, int diffus){
		this.setNombre(nombre);
		this.setApellidos(apellidos);
		this.setEmail(email);
		this.setPassword(password);
		this.setNif(nif);
		this.setGenero(genero);
		this.setFecNac(fecNac);
		this.setActivo(DEF_ACTIVO);
		this.setBaja(DEF_BAJA);
		this.setDiffus(diffus);
		this.setNumVisitas(DEF_VISITA);
	}
	
        //Constructor registro
        
	public Usuario(){}
    //</editor-fold>

}