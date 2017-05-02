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
    public abstract String insertUsuario(Usuario user) throws PersistenceException, ClassNotFoundException;
    public abstract List<Usuario> getUsuarios() throws PersistenceException, ClassNotFoundException;
    public abstract String login(Usuario user) throws PersistenceException, ClassNotFoundException;
    public abstract String logout(Usuario user) throws PersistenceException, ClassNotFoundException;
    public abstract String editarUsuario(Usuario user) throws PersistenceException, ClassNotFoundException;
    public abstract Usuario getUser(Usuario user) throws PersistenceException, ClassNotFoundException;
    public abstract Usuario changeActivity(Usuario user, Boolean activity) throws PersistenceException, ClassNotFoundException;
    public abstract void guardaSession(Usuario user);
    public abstract Usuario recogeSession();


}
