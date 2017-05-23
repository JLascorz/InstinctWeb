/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.contracts;

import com.instinct.exception.PersistenceException.PersistenceException;
import com.instinct.web.objects.Actividad;
import com.instinct.web.objects.Comunidades;
import com.instinct.web.objects.Localizacion;
import com.instinct.web.objects.Municipios;
import com.instinct.web.objects.Provincias;
import java.util.List;

/**
 * LocalizacionDAO classe per englobar les funcions de de localització.
 * @author Jordi Lascorz
 * @since 17/05/2017
 * @version 1.0
 */
public interface LocalizacionDAO {
    
    /**
     * Funcio que retorna la llista de totes les comunitats
     * @return List de Comunidades
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract List<Comunidades> getComunidades() throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció que retorna la llista de totes les provincies
     * @param idComunidad
     * @return List de Provincias
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract List<Provincias> getProvincias(int idComunidad) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció que retorna la llista de tots els municipis
     * @param idProvincia
     * @return List de Municipis
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract List<Municipios> getMunicipios(int idProvincia) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció que inserta a la base de dades la localització d'una activitat
     * @param activity
     * @param idComunidad
     * @param idProvincia
     * @param idMunicipio
     * @param calle
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String escogerLocalizacion(Actividad activity, int idComunidad, int idProvincia, int idMunicipio, String calle) throws PersistenceException, ClassNotFoundException;

    /**
     * Funció que agafa la localització depenent de la id de l'activitat
     * @param activ
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract void getLocalizacionByIdAct(Actividad activ) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció que agafa la comunitat per la seva id
     * @param local
     * @return Comunidad
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract Comunidades getComunidad(Localizacion local) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funcio que agafa la provincia per la seva id
     * @param local
     * @return Provincia
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract Provincias getProvincia(Localizacion local) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funcio que agafa el municipi per la seva id
     * @param local
     * @return Municipio
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract Municipios getMunicipio(Localizacion local) throws PersistenceException, ClassNotFoundException;

    /**
     * Edita la localització d'una activitat amb les noves dades
     * @param activity
     * @param idComunidad
     * @param idProvincia
     * @param idMunicipio
     * @param calle
     * @param error
     * @param pagina
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String editaLocalizacionAct(Actividad activity, int idComunidad, int idProvincia, int idMunicipio, String calle, int error, String pagina) throws PersistenceException, ClassNotFoundException;


    /**
     * Verificar la localidad que engloba la verificació de la provincia i el municipi
     * @param idComunidad
     * @param idProvincia
     * @param idMunicipio
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String verificarLocalidad(int idComunidad, int idProvincia, int idMunicipio) throws PersistenceException, ClassNotFoundException ;
    
    /**
     * Verifica que la provincia pertenezca a la comunitat que s'ha passat
     * @param idComunidad
     * @param idProvincia
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String verifProvincia(int idComunidad, int idProvincia) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Verifica que el municipi perteneixi a la provincia que s'ha passat
     * @param idProvincia
     * @param idMunicipio
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String verifMunicipio(int idProvincia, int idMunicipio) throws PersistenceException, ClassNotFoundException;

    /**
     * Selecciona el lugar de l'activitat de la base de dades
     * @param activ
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String callGetLugar(Actividad activ) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Selecciona la localització d'una activitat
     * @param activ
     * @return Localizacion
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract Localizacion getLugarAct(Actividad activ) throws PersistenceException, ClassNotFoundException;
}
