/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.contracts;

import com.instinct.exception.PersistenceException.PersistenceException;
import com.instinct.web.objects.Actividad;
import com.instinct.web.objects.TipoActividad;
import java.util.List;

/**
 * TipoActividadDAO classe per englobar les funcions de tipos d'actividades.
 * @author Jordi Lascorz
 * @since 13/05/2017
 * @version 1.0
 */
public interface TipoActividadDAO {
    
    /**
     * Funció que engloba les funcións que s'utilitzen per crear tipus d'activitat
     * @param tipoAct
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String callCrear(TipoActividad tipoAct) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Verifica que no existeixi un tipus amb el mateix nom
     * @param tipoAct
     * @return int
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract int getTipoByName(TipoActividad tipoAct) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Inserta un tipus d'activitat en la base de dades
     * @param tipoAct
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String insertarTipoActividad(TipoActividad tipoAct) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Agafa els tipus d'activitats que esten actius
     * @return List de TipoActividad
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract List<TipoActividad> getTiposActividad() throws PersistenceException, ClassNotFoundException;
    
    /**
     * Agafa tots els tipus d'activitats 
     * @return List de TipoActividad
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract List<TipoActividad> getTiposActividadAdm() throws PersistenceException, ClassNotFoundException;
    
    /**
     * Seleccionar el tipo de actividad d'una actividad
     * @param activity
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String getTipoByAct(Actividad activity) throws PersistenceException, ClassNotFoundException;

    /**
     * Guarda la sessió del tipus d'activitat
     * @param tipoAct
     * @param pagina
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract void guardarSession(TipoActividad tipoAct, String pagina) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció que engloba les funcións per editar un tipus d'activitat
     * @param tipoAct
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String callEditar(TipoActividad tipoAct) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció que permet editar el Tipus d'activitat
     * @param tipoAct
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String editarTipoActividad(TipoActividad tipoAct) throws PersistenceException, ClassNotFoundException;
}
