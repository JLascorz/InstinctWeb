/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.connection;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * ¡ IMPORTANT !
 * -------------------------------------------------
 * 
 * AQUEST DOCUMENT NO S'UTILITZA A L'HORA DE FER LA CONNEXIÓ
 * NOMÉS ERA UNA PROVA DE FER UN TUNEL SSH AL SERVIDOR
 * 
 * ----------------------------------------------------
 * 
 * ConnectMySQL te com a funcions la de obrir la conexio i tancarla, cada vegada
 * que es demana desde els MySQLDAO.
 * @author Jordi Lascorz
 * @since 03/03/2017
 * @version 1.0
 */
public abstract class connectMySQL {
    
    private static Session session;
    
    private static Connection connection;
    private static String link = "jdbc:mysql://172.16.7.150:3306/Instinct";
    private static String user = "root";
    private static String pass = "maletin";
    
    private static String dataBaseName = "Instinct";
    
    /**
     * ConnectToServer() obre la conexio a la Base de Dades.
     * @return conn Connection
     */
    private static String driverName = "com.mysql.jdbc.Driver";
    
    public static Connection connectToServer() throws SQLException {
        connectSSH();
        Connection conn = connectToDataBase();
        
        return conn;
    }
    
    public static void connectSSH() throws SQLException {
        
        String sshHost = "172.16.7.150";
        String sshUser = "jordi";
        String dbuserName = "root";
        String dbpassword = "maletin";
    
        int localPort = 8740;
        String remoteHost = "172.16.7.150";
        int remotePort = 3306;
        String localSSHurl = "localhost";
        
        try{
        //conn = (Connection) DriverManager.getConnection (link, user, pass);
        java.util.Properties config = new java.util.Properties();
        JSch jsch = new JSch();
        session = jsch.getSession(sshUser, sshHost, 22);
        //jsch.addIdentity(SshKeyFilepath);
        config.put("StrictHostKeyChecking", "no");
        config.put("ConnectionAttempts", "3");
        session.setConfig(config);
        session.connect();
        Class.forName(driverName).newInstance();
        int assinged_port = session.setPortForwardingL(localPort, remoteHost, remotePort);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    private static Connection connectToDataBase() throws SQLException {
        String dbuserName = "root";
        String dbpassword = "maletin";
        int localPort = 8740;
        String localSSHUrl = "localhost";
        
        try{
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setServerName(localSSHUrl);
            dataSource.setPortNumber(localPort);
            dataSource.setUser(dbuserName);
            dataSource.setAllowMultiQueries(true);
            
            dataSource.setPassword(dbpassword);
            dataSource.setDatabaseName(dataBaseName);

            connection = dataSource.getConnection();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return connection;
    }
    /**
     * ConnectionClose() tanca la conexio a la Base de Dades.
     */
    public static void closeConnections(){
        CloseDataBaseConnection();
        CloseSSHConnection();
    }
    
    private static void CloseDataBaseConnection(){
        try{
            if(connection != null && !connection.isClosed()){
                connection.close();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    private static void CloseSSHConnection() {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }
    

}

