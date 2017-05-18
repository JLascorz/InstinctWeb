/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.contracts;

import com.instinct.exception.PersistenceException.PersistenceException;
import com.instinct.web.objects.Actividad;
import com.instinct.web.objects.Usuario;
import java.io.IOException;
import java.util.List;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author daw2017
 */
public interface ActividadDAO {
    
    //Crear Actividad
    public abstract String callCrear(Actividad activ, int idUser) throws PersistenceException, ClassNotFoundException;
    public abstract int getActivityByNameYear(Actividad activ) throws PersistenceException, ClassNotFoundException;
    public abstract String insertarActividad(Actividad activ, int idUser) throws PersistenceException, ClassNotFoundException;
    
    //Seleccionar actividad con la id del usuario
    public abstract List<Actividad> getActividadByUser(Usuario user) throws PersistenceException, ClassNotFoundException;
    
    //Mirar si tiene alguna actividad
    public abstract int mirarSiTieneActividad(Usuario user) throws PersistenceException, ClassNotFoundException;

    //Seleccionar actividad con el nombre
    public abstract Actividad getActividadByName(Actividad activ) throws PersistenceException, ClassNotFoundException;
    
    //Dar de baja una actividad
    public abstract void bajaActividad(Actividad activ) throws PersistenceException, ClassNotFoundException;
    
    //Guardar actividad en la session // Borrar la session.
    public abstract void guardarSession(Actividad activ, String pagina) throws PersistenceException, ClassNotFoundException;
    public abstract void borrarSession();
    
    //Editar la actividad
    public abstract String callEditar(Actividad activ, String pagina, int error) throws PersistenceException, ClassNotFoundException;
    public abstract String editarActividad(Actividad activ) throws PersistenceException, ClassNotFoundException;

    //Seleccionar la actividad por el mes y el año
    public abstract int haveActYearMonth(int year, int month) throws PersistenceException, ClassNotFoundException;
    public abstract List<Actividad> getActivitiesByYearMonth(int year, int month) throws PersistenceException, ClassNotFoundException;

    //Coger el total de actividades creadas
    public abstract int getTotalActividades() throws PersistenceException, ClassNotFoundException;
    
    //Seleccionar todas las actividades
    public abstract List<Actividad> getActividades() throws PersistenceException, ClassNotFoundException;
    
    //Editar la actividad como administrador
    public abstract String editarActividadAdm(Actividad activ) throws PersistenceException, ClassNotFoundException;

    //Subir los resultados de la actividad
    public void uploadResultado(FileUploadEvent e, Actividad activ) throws IOException;
}
