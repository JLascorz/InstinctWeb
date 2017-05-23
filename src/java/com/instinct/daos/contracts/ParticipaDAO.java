/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.contracts;

import com.instinct.exception.PersistenceException.PersistenceException;
import com.instinct.web.objects.Actividad;
import com.instinct.web.objects.Usuario;
import java.util.List;

/**
 * UsuarioDAO classe per englobar les funcions de la relacio entre usuaris i activitats.
 * @author Jordi Lascorz
 * @since 16/05/2017
 * @version 1.0
 */
public interface ParticipaDAO {
    
    /**
     * Funció que engloba altres funcións per que al insertar la relació 
     * del usuari amb l'activitat verifiqui errors.
     * @param activity
     * @param user
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String callParticipa(Actividad activity, Usuario user) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Verifica que la inscripcio d'aquest usuari ya estigui feta
     * @param activity
     * @param user
     * @return int
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract int verificaInscripcion(Actividad activity, Usuario user) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Fa el inerció de la inscripció d'un usuari en una activitat
     * @param activity
     * @param user
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String insertaRelacion(Actividad activity, Usuario user) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Retorna la llista de tots els participants d'una activitat
     * @param activity
     * @return List de Usuario
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract List<Usuario> getParticipantes(Actividad activity) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Retorna la llista de totes les activitats a la que participa l'usuari
     * @param user
     * @return Lista de Actividades
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract List<Actividad> getInscripcionesUser(Usuario user) throws PersistenceException, ClassNotFoundException;

    /**
     * Agafa el numero de participants en activitats total
     * @return int
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract int getTotalParticipantes() throws PersistenceException, ClassNotFoundException;
}
