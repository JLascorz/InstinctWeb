/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author daw2017
 */
public abstract class connectMySQL2 {
    private static Connection conn;
    private static String link = "jdbc:mysql://172.16.7.150:3306/Instinct";
    private static String user = "jordi";
    private static String pass = "maletin";
    
    
    /**
     * Connect() obre la conexio a la Base de Dades.
     * @return conn Connection
     */
    public static Connection connect() {
        try{
            java.util.Properties config = new java.util.Properties();
            
            //conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jspfinal", "root", "maletin");
            conn = (Connection) DriverManager.getConnection (link, user, pass);
        }catch(SQLException e){
            System.err.printf("No se ha podido conectar a la Base de Datos.\n");
        }
        return conn;
    }
    
    /**
     * ConnectionClose() tanca la conexio a la Base de Dades.
     */
    public static void connectionClose(){
        try{
           if(conn !=null){
              conn.close();
           }
                
            }catch(SQLException e){
                System.err.printf("No se ha podido cerrar bien la conexion a la Base de Datos.\n");
        }
    }
    

}
