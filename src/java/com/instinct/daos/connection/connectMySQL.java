/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.connection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ConnectMySQL te com a funcions la de obrir la conexio i tancarla, cada vegada
 * que es demana desde els MySQLDAO.
 * @author Jordi Lascorz
 * @since 03/03/2017
 * @version 1.0
 */
public class connectMySQL {
    
    private static Connection conn;
    private static String link = "jdbc:mysql://172.16.7.150:3306/instinct";
    private static String user = "root";
    private static String pass = "maletin";
    
    /**
     * Connect() obre la conexio a la Base de Dades.
     * @return conn Connection
     */
    public static Connection connect() {
        try{
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

