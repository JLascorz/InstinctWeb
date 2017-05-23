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
 * UsuarioDAO classe per englobar les funcions de usuaris.
 * @author Jordi Lascorz
 * @since 12/05/2017
 * @version 1.0
 */
public interface UsuarioDAO {
    
    /**
     * Funcio que engloba altres funcions per a fer el registre dels usuaris
     * @param user
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String register(Usuario user) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funcio que agafa verifica si existeix un usuari amb el mateix mail.
     * @param user
     * @param caller
     * @return int
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract int getUserByEmail(Usuario user, String caller) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funcio que fa un insert amb les dades d'un usuari.
     * @param user
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String insertUsuario(Usuario user) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Mostra una llista de tots els usuaris
     * @return Lista de usuarios
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract List<Usuario> getUsuarios() throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció per a fer el login de l'usuari engloba altres funcions
     * @param user
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String login(Usuario user) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció per tancar la sessio d'un usuari.
     * @param user
     * @return
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String logout(Usuario user) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció per agafar un usuari a partir del mail i la password.
     * @param user
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract Usuario getUser(Usuario user) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció que canvia l'estat d'un usuari de false a true.
     * @param user
     * @param activity
     * @return Usuario
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract Usuario changeActivity(Usuario user, Boolean activity) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció per guardar la sessio d'un usuari depenent de la pagina.
     * @param user
     * @param pagina 
     */
    public abstract void guardaSession(Usuario user, String pagina);
    
    /**
     * Funció per agafar la sessió - No s'utilitza
     * @return user
     */
    public abstract Usuario recogeSession();
    
    /**
     * Funcio que engloba altres funcions per editar l'usuari
     * @param user
     * @param pagina
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String callEditar(Usuario user, String pagina) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció per editar l'usuari amb les dades insertades.
     * @param user
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String editarUsuario(Usuario user) throws PersistenceException, ClassNotFoundException;

    /**
     * Funció que crida a la funcio de canviar l'estat de l'usuari a baixa.
     * @param user
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String callBaja(Usuario user) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció que canvia l'estat de l'usuari per a que no es pugui conectar mai.
     * @param user
     * @param baja
     * @return Usuario
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract Usuario changeBaja(Usuario user, Boolean baja) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció que retorna el numero de visitas dels usuaris
     * @return int
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract int getTotalVisitas() throws PersistenceException, ClassNotFoundException;

    /**
     * Funció que retorna el numero d'usuaris registrats
     * @return int
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract int getTotalUsers() throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció per editar un usuari com administrador.
     * @param user
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String editUserLikeAdmin(Usuario user) throws PersistenceException, ClassNotFoundException;
}
