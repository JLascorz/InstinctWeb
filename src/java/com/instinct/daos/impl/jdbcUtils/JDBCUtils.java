/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.impl.jdbcUtils;

import com.instinct.web.objects.ActServ;
import com.instinct.web.objects.Actividad;
import com.instinct.web.objects.Comunidades;
import com.instinct.web.objects.Localizacion;
import com.instinct.web.objects.Municipios;
import com.instinct.web.objects.Participante;
import com.instinct.web.objects.Provincias;
import com.instinct.web.objects.Servicio;
import com.instinct.web.objects.TipoActividad;
import com.instinct.web.objects.Usuario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * <p>La classe <b>JDBCUTILS</b> serveix per a crear objectes.
 * @author Jordi Lascorz
 * @since 10/05/2017
 * @version 1.0
 */
public class JDBCUtils {
    /**
     * El Constructor de JDBCUtils esta buit, i no s'utilitza.
     */
    private JDBCUtils(){}
    
    /**
     * La funció getUsuari, retorna un usuari que s'ha retornat de les dades de la BD.
     * @param reader ResultSet
     * @return Usuario
     * @throws SQLException 
     */
    public static Usuario getUsuario(ResultSet reader) throws SQLException{
        
        //Fecha del registro
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String fecRegistro  = dateFormat.format(reader.getTimestamp("fecRegistro"));
        
        //Fecha de Nacimiento
        Date dfn = reader.getDate("fecNac");
        SimpleDateFormat dfor = new SimpleDateFormat("dd-MM-yyyy");
        String fecNac = dfor.format(dfn);
        Usuario user = new Usuario(
            reader.getInt("idUser"),
            reader.getString("Nombre"),
            reader.getString("Apellidos"),
            reader.getString("Email"),
            reader.getString("Password"),
            reader.getString("Nif"),
            reader.getString("Genero"),
            fecNac,
            reader.getBoolean("Activo"),
            reader.getBoolean("Baja"),
            reader.getInt("Diffus"),
            reader.getString("fecUltVis"),
            reader.getInt("numVisitas"),
            fecRegistro);
            
            return user;
    }
    
    /**
     * Retorna un objecte activitat que ha agafat desde la base de dades
     * @param reader
     * @return Actividad
     * @throws SQLException 
     */
    public static Actividad getActividad(ResultSet reader) throws SQLException{
        
        //Fecha de Nacimiento
        Date dfn = reader.getDate("Fecha");
        SimpleDateFormat dfor = new SimpleDateFormat("dd-MM-yyyy");
        String fecha = dfor.format(dfn);
        
        Actividad activ = new Actividad(
            reader.getInt("idAct"),
            reader.getInt("idUser"),
            reader.getInt("idTipo"),
            reader.getString("Nombre"),
            reader.getString("Descripcion"),
            reader.getString("Email"),
            reader.getString("Telefono"),
            reader.getString("Web"),
            fecha,
            reader.getBoolean("Activo"),
            reader.getBoolean("Baja"),
            reader.getString("pathResultados"),
            reader.getString("pathImagen"));
        
        return activ;
    }
    
    /**
     * Agafa un objecte de la relació entre Activitat i Serveis de la base de dades
     * @param reader
     * @return ActServ
     * @throws SQLException 
     */
    public static ActServ getActServ(ResultSet reader) throws SQLException{
        
        ActServ actserv = new ActServ(
                reader.getInt("idActividad"),
                reader.getInt("idServicio")
        );
        
        return actserv;
    }
    
    /**
     * Agafa un objecte Participante de la relació entre Usuari i Activitat
     * @param reader
     * @return Participante
     * @throws SQLException 
     */
    public static Participante getParticipante(ResultSet reader) throws SQLException{
        
        Participante participante = new Participante(
                reader.getInt("idUsuario"),
                reader.getInt("idActividad")
        );
        
        return participante;
    }
    
    /**
     * Agafa un objecto TipoActividad de la base de dades
     * @param reader
     * @return
     * @throws SQLException 
     */
    public static TipoActividad getTipoActividad(ResultSet reader) throws SQLException{
        
        TipoActividad tipo = new TipoActividad(
                reader.getInt("idTipo"),
                reader.getString("Nombre"),
                reader.getString("Descripcion"),
                reader.getBoolean("Activo"));
        
        return tipo;
    }
    
    /**
     * Agafa un objecte de tipus Servicio de la base de dades
     * @param reader
     * @return
     * @throws SQLException 
     */
    public static Servicio getServicio(ResultSet reader) throws SQLException{
        
        Servicio service = new Servicio(
                reader.getInt("idServ"),
                reader.getString("Nombre"),
                reader.getString("Descripcion"),
                reader.getBoolean("Activo"));
        
        return service;
    }
    
    /**
     * Agafa un objecte localizacion de la base de dades i el retorna
     * @param reader
     * @return Localizacion
     * @throws SQLException 
     */
    public static Localizacion getLocalizacion(ResultSet reader) throws SQLException{
        
        Localizacion localizacion = new Localizacion(
                reader.getInt("idAct"),
                reader.getInt("idComunidad"),
                reader.getInt("idProvincia"),
                reader.getInt("idMunicipio"),
                reader.getString("Calle"));
        
        return localizacion;
    }
    
    /**
     * Agafa un objecte Comunidades de la base de dades
     * @param reader
     * @return Comunidades
     * @throws SQLException 
     */
    public static Comunidades getComunidad(ResultSet reader) throws SQLException{
        
        Comunidades comunidad = new Comunidades(
                reader.getInt("id"),
                reader.getString("comunidad"));
        
        return comunidad;
    }
    
    /**
     * Agafa un objecte Provincias de la base de dades
     * @param reader
     * @return
     * @throws SQLException 
     */
    public static Provincias getProvincia(ResultSet reader) throws SQLException{
        
        Provincias provincia = new Provincias(
                reader.getInt("id"),
                reader.getString("provincia"),
                reader.getInt("comunidad_id"));
        
        return provincia;
    }
    
    /**
     * Agafa un objecte Municipio de la base de dades
     * @param reader
     * @return
     * @throws SQLException 
     */
    public static Municipios getMunicipio(ResultSet reader) throws SQLException{
        
        Municipios municipio = new Municipios(
                reader.getInt("id"),
                reader.getString("municipio"),
                reader.getInt("provincia_id"));
        
        return municipio;
    }

}
