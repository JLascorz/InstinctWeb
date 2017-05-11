/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.contracts;

import com.instinct.exception.PersistenceException.PersistenceException;
import com.instinct.web.objects.Actividad;
import java.util.List;

/**
 *
 * @author daw2017
 */
public interface ActWithServiceDAO {
    
    //Añadir servicios a la actividad
    public abstract void callServicioActividad(Actividad activity, List<String> serviciosSeleccionados) throws PersistenceException, ClassNotFoundException;
    
    //Editar los servicios de una actividad
    public abstract void callEditServAct(Actividad activity, List<String> serviciosSeleccionados) throws PersistenceException, ClassNotFoundException;
    public abstract void eliminarRelacion(Actividad activity) throws PersistenceException, ClassNotFoundException;
}
