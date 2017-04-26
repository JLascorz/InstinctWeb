/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.contracts;

import com.instinct.exception.PersistenceException.PersistenceException;
import com.instinct.web.objects.Usuario;
import java.util.List;

/**
 *
 * @author daw2017
 */
public interface UsuarioDAO {
    public abstract String insertUsuario(Usuario user) throws PersistenceException;
    public abstract List<Usuario> getUsuarios() throws PersistenceException;
    public abstract Usuario getUser() throws PersistenceException;
    
}
