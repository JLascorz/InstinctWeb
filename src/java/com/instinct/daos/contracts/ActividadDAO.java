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
 *
 * @author daw2017
 */
public interface ActividadDAO {
    
    //Crear Actividad
    public abstract String callCrear(Actividad activ, int idUser, int idComunidad, int idProvincia, int idMunicipio, String calle) throws PersistenceException, ClassNotFoundException;
    public abstract int getActivityByNameYear(Actividad activ) throws PersistenceException, ClassNotFoundException;
    public abstract String insertarActividad(Actividad activ, int idUser) throws PersistenceException, ClassNotFoundException;
    
    //Seleccionar actividad con la id del usuario
    public abstract List<Actividad> getActividadByUser(Usuario user) throws PersistenceException, ClassNotFoundException;
    
    //Mirar si tiene alguna actividad
    public abstract int mirarSiTieneActividad(Usuario user) throws PersistenceException, ClassNotFoundException;

    //Seleccionar actividad con el nombre
    public abstract Actividad getActividadByName(Actividad activ) throws PersistenceException, ClassNotFoundException;
    
}
