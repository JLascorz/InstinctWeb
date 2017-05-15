/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.contracts;

import com.instinct.exception.PersistenceException.PersistenceException;
import com.instinct.web.objects.Actividad;
import com.instinct.web.objects.Usuario;

/**
 *
 * @author daw2017
 */
public interface ParticipaDAO {
    
    //Hacer que participe un usuario en una actividad
    public abstract String callParticipa(Actividad activity, Usuario user) throws PersistenceException, ClassNotFoundException;
    public abstract String insertaRelacion(Actividad activity, Usuario user) throws PersistenceException, ClassNotFoundException;
}
