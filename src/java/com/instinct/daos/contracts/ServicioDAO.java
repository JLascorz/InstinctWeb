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
 * ServicioDAO classe per englobar les funcions de serveis.
 * @author Jordi Lascorz
 * @since 19/05/2017
 * @version 1.0
 */
public interface ServicioDAO {
    
    /**
     * Funcio per englobar les funcions de creació d'un servei
     * @param serv
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String callCrear(Servicio serv) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció per verificar que no existeixi un serveix amb el mateix nom
     * @param serv
     * @return int
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract int getServiceByName(Servicio serv) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció per insertar el servei
     * @param serv
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String insertarServicio(Servicio serv) throws PersistenceException, ClassNotFoundException;

    /**
     * Funció per seleccionar tots els serveis que estiguin actius
     * @return List de Servicio
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract List<Servicio> getServiciosUs() throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció per seleccionar tots els serveis
     * @return List de Servicio
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract List<Servicio> getServiciosAdm() throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció per guardar la sessión d'un servei depenent de la pagina
     * @param serv
     * @param pagina
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract void guardarSession(Servicio serv, String pagina) throws PersistenceException, ClassNotFoundException;

    /**
     * Funcio que engloba les funcións per editar un servei
     * @param serv
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String callEditar(Servicio serv) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funcio per editar un servei com usuari normal o administrador
     * @param serv
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String editarServicio(Servicio serv) throws PersistenceException, ClassNotFoundException;
}
