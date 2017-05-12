/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.contracts;

import com.instinct.exception.PersistenceException.PersistenceException;
import com.instinct.web.objects.Actividad;
import com.instinct.web.objects.Comunidades;
import com.instinct.web.objects.Localizacion;
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

    //Escoger la localizacion por la id de la actividad.
    public abstract void getLocalizacionByIdAct(Actividad activ) throws PersistenceException, ClassNotFoundException;
    public abstract Comunidades getComunidad(Localizacion local) throws PersistenceException, ClassNotFoundException;
    public abstract Provincias getProvincia(Localizacion local) throws PersistenceException, ClassNotFoundException;
    public abstract Municipios getMunicipio(Localizacion local) throws PersistenceException, ClassNotFoundException;

    //Editar la localizacion de una actividad
    public abstract String editaLocalizacionAct(Actividad activity, int idComunidad, int idProvincia, int idMunicipio, String calle) throws PersistenceException, ClassNotFoundException;

    //Verificar si existe alguna provincia en esa comunidad o un monicipio en esa provincia.
    public abstract String verificarLocalidad(int idComunidad, int idProvincia, int idMunicipio) throws PersistenceException, ClassNotFoundException ;
    public abstract String verifProvincia(int idComunidad, int idProvincia) throws PersistenceException, ClassNotFoundException;
    public abstract String verifMunicipio(int idProvincia, int idMunicipio) throws PersistenceException, ClassNotFoundException;

    //Seleccionar el lugar de la actividad
    public abstract String callGetLugar(Actividad activ) throws PersistenceException, ClassNotFoundException;
    public abstract Localizacion getLugarAct(Actividad activ) throws PersistenceException, ClassNotFoundException;
}
