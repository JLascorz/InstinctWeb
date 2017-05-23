/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.contracts;

import com.instinct.exception.PersistenceException.PersistenceException;
import com.instinct.web.objects.Actividad;
import com.instinct.web.objects.Servicio;
import java.util.List;

/**
 * ActWithServiceDAO proporciona les funcios per al seu us en 
 * la relació entre l'activitat i serveis.
 * @author Jordi Lascorz
 * @since 18/05/2017
 * @version 1.0
 */
public interface ActWithServiceDAO {
    
    /**
     * CallServicioActividad fa un insert dels serveis de les activitats en la relació.
     * @param activity
     * @param serviciosSeleccionados
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract void callServicioActividad(Actividad activity, List<String> serviciosSeleccionados) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció que crida a la funció d'eliminar serveis d'una activitat en la relació, amb opcions
     * de control d'errors
     * @param activity
     * @param serviciosSeleccionados
     * @param error
     * @return null
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String callEditServAct(Actividad activity, List<String> serviciosSeleccionados, int error) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció que elimina els serveis d'una activitat en la relació.
     * @param activity
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract void eliminarRelacion(Actividad activity) throws PersistenceException, ClassNotFoundException;

    /**
     * Funció que agafa els serveis d'una activitat per la seva id en la relació.
     * @param activity
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract void getServiciosByIdAct(Actividad activity) throws PersistenceException, ClassNotFoundException;

    /**
     * Funció que mostra tots els serveis que té una activitat, agafa els serveis
     * per la id de l'activitat.
     * @param activity
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract void showServicesinAct(Actividad activity) throws PersistenceException, ClassNotFoundException;
}
