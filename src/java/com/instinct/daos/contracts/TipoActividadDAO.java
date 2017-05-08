/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.contracts;

import com.instinct.exception.PersistenceException.PersistenceException;
import com.instinct.web.objects.TipoActividad;
import java.util.List;

/**
 *
 * @author daw2017
 */
public interface TipoActividadDAO {
    
    //Crear Tipo de Actividad
    public abstract String callCrear(TipoActividad tipoAct) throws PersistenceException, ClassNotFoundException;
    public abstract int getTipoByName(TipoActividad tipoAct) throws PersistenceException, ClassNotFoundException;
    public abstract String insertarTipoActividad(TipoActividad tipoAct) throws PersistenceException, ClassNotFoundException;
    
    //Seleccionar todos los tipos de Actividad
    public abstract List<TipoActividad> getTiposActividad() throws PersistenceException, ClassNotFoundException;
    
}