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
 *
 * @author daw2017
 */
public interface ServicioDAO {
    
    //Insertar Servicio
    public abstract String callCrear(Servicio serv) throws PersistenceException, ClassNotFoundException;
    public abstract int getServiceByName(Servicio serv) throws PersistenceException, ClassNotFoundException;
    public abstract String insertarServicio(Servicio serv) throws PersistenceException, ClassNotFoundException;

    //Seleccionar todos los tipos de Actividad
    public abstract List<Servicio> getServicios() throws PersistenceException, ClassNotFoundException;
    
    
}
