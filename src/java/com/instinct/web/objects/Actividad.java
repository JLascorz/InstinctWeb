/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.web.objects;

import static java.lang.Boolean.FALSE;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

/**
 * Classe per al objecte Actividad.
 * @author Jordi Lascorz
 * @since 04/05/2017
 * @version 1.0
 */
@ManagedBean(name="Actividad")
@ViewScoped
public class Actividad {
    //<editor-fold defaultstate="collapsed" desc="Atributos">
    //Datos principales de la actividad.
    private String nombre, descripcion, email, telefono, web, fecha;
    //El booleano "Activo" verifica si esta pendiente de moderacion y "Baja" si esta eliminado.
    private Boolean activo, baja;
    //Identificadores de las otras tablas.
    private int idAct, idUser, idTipo;
    //Objetos necesarios para actividad, como la informacion y la localizacion en el mapa.
    //private InformacionAct infoAct;
    private Localizacion lugar;
    //Fichero de los resultados
    private String pathResultado, pathImagen;
    //Valores por defecto
    private static final boolean DEF_BAJA = FALSE;
    private static final boolean DEF_ACTIVO = FALSE;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Getters">
    public int getIdAct(){
	return idAct;
    }
	
    public int getIdUser(){
	return idUser;
    }
	
    public int getIdTipo(){
	return idTipo;
    }
	
    public String getNombre(){
    	return nombre;
    }
	
    public String getDescripcion(){
    	return descripcion;
    }
	
    public String getEmail(){
    	return email;
    }
	
    public String getTelefono(){
    	return telefono;
    }
	
    public String getWeb(){
	return web;
    }
	
    public String getFecha(){
    	return fecha;
    }
	
    public Boolean getActivo(){
    	return activo;
    }
	
    public Boolean getBaja(){
    	return baja;
    }
	
//    public InformacionAct getInformacion(){
//	return infoAct;
//    }
	
    public Localizacion getLocalizacion(){
	return lugar;
    }
	
    public String getPathResultado(){
    	return pathResultado;
    }
	
    public String getPathImagen(){
    	return pathImagen;
    }    
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Setters">
    public void setIdAct(int idAct){
        this.idAct = idAct;
    }

    public void setIdUser(int idUser){
        this.idUser = idUser;
    }

    public void setIdTipo(int idTipo){
        this.idTipo = idTipo;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setTelefono(String telefono){
        this.telefono = telefono;
    }   

    public void setWeb(String web){
        this.web = web;
    }   

    public void setFecha(String fecha){
        this.fecha = fecha;
    }

    public void setActivo(Boolean activo){
        this.activo = activo;
    }

    public void setBaja(Boolean baja){
        this.baja = baja;
    }

//    public void setInformacion(InformacionAct infoAct){
//        this.infoAct = infoAct;
//    }

    public void setLocalizacion(Localizacion lugar){
        this.lugar = lugar;
    }

    public void setPathResultado(String pathResultado){
        this.pathResultado = pathResultado;
    }

    public void setPathImagen(String pathImagen){
        this.pathImagen = pathImagen;
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Constructores">
    //Constructor completo
    public Actividad(int idAct, int idUser, int idTipo, String nombre, String descripcion, String email,
		     String telefono, String web, String fecha, Boolean activo, Boolean baja,
                     Localizacion lugar, String pathResultado, String pathImagen){
		this.setIdAct(idAct);
		this.setIdUser(idUser);
		this.setIdTipo(idTipo);
		this.setNombre(nombre);
		this.setDescripcion(descripcion);
		this.setEmail(email);
		this.setTelefono(telefono);
		this.setWeb(web);
		this.setFecha(fecha);
		this.setActivo(activo);
		this.setBaja(baja);
		this.setLocalizacion(lugar);
		this.setPathResultado(pathResultado);
		this.setPathImagen(pathImagen);
	}
	
	//Constructor registro completo
	public Actividad(int idAct, int idUser, int idTipo, String nombre, String descripcion, String email,
			String telefono, String web, String fecha, Localizacion lugar, String pathImagen){
		this.setIdAct(idAct);
		this.setIdUser(idUser);
		this.setIdTipo(idTipo);
		this.setNombre(nombre);
		this.setDescripcion(descripcion);
		this.setEmail(email);
		this.setTelefono(telefono);
		this.setWeb(web);
		this.setFecha(fecha);
		this.setActivo(DEF_ACTIVO);
		this.setBaja(DEF_BAJA);
		this.setLocalizacion(lugar);
		this.setPathImagen(pathImagen);
	}
        
        public Actividad(int idAct, int idUser, int idTipo, String nombre, String descripcion, String email,
		     String telefono, String web, String fecha, Boolean activo, Boolean baja,
                     String pathResultado, String pathImagen){
		this.setIdAct(idAct);
		this.setIdUser(idUser);
		this.setIdTipo(idTipo);
		this.setNombre(nombre);
		this.setDescripcion(descripcion);
		this.setEmail(email);
		this.setTelefono(telefono);
		this.setWeb(web);
		this.setFecha(fecha);
		this.setActivo(activo);
		this.setBaja(baja);
		this.setPathResultado(pathResultado);
		this.setPathImagen(pathImagen);
	}
	
	public Actividad(){}
    //</editor-fold>
}
