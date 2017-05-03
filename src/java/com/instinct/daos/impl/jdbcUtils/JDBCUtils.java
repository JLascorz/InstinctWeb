/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.impl.jdbcUtils;

import com.instinct.web.objects.Usuario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * <p>La classe <b>JDBCUTILS</b> serveix per a crear objectes Alumne i Moduls,
 * a més tambe retorna una string de les dades de la relació.</p>
 * @author Jordi Lascorz
 * @since 03/03/2017
 * @version 1.0
 */
public class JDBCUtils {
    /**
     * El Constructor de JDBCUtils esta buit, i no s'utilitza.
     */
    private JDBCUtils(){}
    /**
     * La funció getAlumne, retorna un alumne que s'ha retornat de les dades de la BD.
     * @param reader ResultSet
     * @return Alumne
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

}