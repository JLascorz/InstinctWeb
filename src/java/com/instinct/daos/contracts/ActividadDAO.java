/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.contracts;

import com.instinct.exception.PersistenceException.PersistenceException;
import com.instinct.web.objects.Actividad;
import com.instinct.web.objects.Usuario;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 * ActividadDAO classe per englobar les funcions de activitats.
 * @author Jordi Lascorz
 * @since 12/05/2017
 * @version 1.0
 */
public interface ActividadDAO {
    
    /**
     * Funció per a juntar les funcions de verificar si l'activitat existeix
     * en el mateix any, a més de la d'insertar l'activitat
     * @param activ
     * @param idUser
     * @return null
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String callCrear(Actividad activ, int idUser) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció que verifica si una activitat amb el mateix nom existeix en el mateix any.
     * @param activ
     * @return int
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract int getActivityByNameYear(Actividad activ) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció que inserta l'activitat corresponent a la base de dades
     * @param activ
     * @param idUser
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String insertarActividad(Actividad activ, int idUser) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció que selecciona una llista d'activitats per la id de l'usuari.
     * @param user
     * @return Lista de Actividades
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract List<Actividad> getActividadByUser(Usuario user) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció que comprova si l'usuari conté alguna activitat.
     * @param user
     * @return int
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract int mirarSiTieneActividad(Usuario user) throws PersistenceException, ClassNotFoundException;

    /**
     * Funció que comprova si existeix una activitat amb el mateix nom, independentment
     * de la data.
     * @param activ
     * @return Actividad
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract Actividad getActividadByName(Actividad activ) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció que dona de baixa una activitat.
     * @param activ
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract void bajaActividad(Actividad activ) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció que guarda una sessió d'una activitat, depenent de la pagina que se li passi.
     * @param activ
     * @param pagina
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract void guardarSession(Actividad activ, String pagina) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció que borra una sessió d'una activitat.
     */
    public abstract void borrarSession();
    
    /**
     * Funcio que conté altres funcións per fer verificacions a l'hora d'editar una activitat.
     * @param activ
     * @param pagina
     * @param error
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String callEditar(Actividad activ, String pagina, int error) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció que edita una activitat amb les dades que s'han passat.
     * @param activ
     * @return
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String editarActividad(Actividad activ) throws PersistenceException, ClassNotFoundException;

    /**
     * Comprova si existeix alguna activitat per l'any i el més.
     * @param year
     * @param month
     * @return int
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract int haveActYearMonth(int year, int month) throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció que agafa les activitat any i més.
     * @param year
     * @param month
     * @return Lista de actividades
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract List<Actividad> getActivitiesByYearMonth(int year, int month) throws PersistenceException, ClassNotFoundException;

    /**
     * Funció que agafa el numero d'activitats que s'han creat.
     * @return int
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract int getTotalActividades() throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció que agafa una llista de totes les activitats creades.
     * @return Lista de actividades
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract List<Actividad> getActividades() throws PersistenceException, ClassNotFoundException;
    
    /**
     * Funció que edita una activitat només si l'usuari es administrador.
     * @param activ
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    public abstract String editarActividadAdm(Actividad activ) throws PersistenceException, ClassNotFoundException;

    /**
     * Funció que puja la ruta de la carpeta i el fitxer del resultat d'una activitat.
     * @param resultado
     * @param activ
     * @throws PersistenceException
     * @throws ClassNotFoundException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void uploadResultado(UploadedFile resultado, Actividad activ) throws PersistenceException, ClassNotFoundException, FileNotFoundException, IOException;

    /**
     * Funció que puja la ruta de la carpeta i el fitxer de la imatge d'una activitat.
     * @param imagen
     * @param activ
     * @throws PersistenceException
     * @throws ClassNotFoundException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void uploadImagen(UploadedFile imagen, Actividad activ) throws PersistenceException, ClassNotFoundException, FileNotFoundException, IOException;
}
