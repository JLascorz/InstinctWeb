/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.contracts;

import com.instinct.exception.PersistenceException.PersistenceException;
import com.instinct.web.objects.Actividad;
import com.instinct.web.objects.Comunidades;
import com.instinct.web.objects.Municipios;
import com.instinct.web.objects.Provincias;
import java.util.List;

/**
 *
 * @author daw2017
 */
public interface LocalizacionDAO {
    
    //Escoger la localizacion de una actividad.
    public abstract List<Comunidades> getComunidades() throws PersistenceException, ClassNotFoundException;
    public abstract List<Provincias> getProvincias(int idComunidad) throws PersistenceException, ClassNotFoundException;
    public abstract List<Municipios> getMunicipios(int idProvincia) throws PersistenceException, ClassNotFoundException;
    public abstract String escogerLocalizacion(Actividad activity, int idComunidad, int idProvincia, int idMunicipio, String calle) throws PersistenceException, ClassNotFoundException;
}