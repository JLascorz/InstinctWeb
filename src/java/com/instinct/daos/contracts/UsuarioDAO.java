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
    //Registro del usuario
    public abstract String register(Usuario user) throws PersistenceException, ClassNotFoundException;
    public abstract int getUserByEmail(Usuario user, String caller) throws PersistenceException, ClassNotFoundException;
    public abstract String insertUsuario(Usuario user) throws PersistenceException, ClassNotFoundException;
    
    public abstract List<Usuario> getUsuarios() throws PersistenceException, ClassNotFoundException;
    
    //Login
    public abstract String login(Usuario user) throws PersistenceException, ClassNotFoundException;
    public abstract String logout(Usuario user) throws PersistenceException, ClassNotFoundException;
    public abstract Usuario getUser(Usuario user) throws PersistenceException, ClassNotFoundException;
    public abstract Usuario changeActivity(Usuario user, Boolean activity) throws PersistenceException, ClassNotFoundException;
    public abstract void guardaSession(Usuario user);
    public abstract Usuario recogeSession();
    
    //Editar Usuario
    public abstract String callEditar(Usuario user) throws PersistenceException, ClassNotFoundException;
    public abstract String editarUsuario(Usuario user) throws PersistenceException, ClassNotFoundException;

    //Dar usuario de baja
    public abstract String callBaja(Usuario user) throws PersistenceException, ClassNotFoundException;
    public abstract Usuario changeBaja(Usuario user, Boolean baja) throws PersistenceException, ClassNotFoundException;
    

}
