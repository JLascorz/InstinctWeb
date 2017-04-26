/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.impl.jdbc.UsuariDAO;

import static com.instinct.daos.connection.connectMySQL.closeConnections;
import static com.instinct.daos.connection.connectMySQL.connectToServer;
import static com.instinct.daos.connection.connectMySQL2.connect;
import static com.instinct.daos.connection.connectMySQL2.connectionClose;
import com.instinct.daos.contracts.UsuarioDAO;
import com.instinct.exception.PersistenceException.PersistenceException;
import com.instinct.web.objects.Usuario;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import static java.lang.Boolean.FALSE;
import java.sql.Date;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author daw2017
 */
@ManagedBean(name="UsuarioMySQLDAO")
@SessionScoped
public class UsuarioMySQLDAO implements UsuarioDAO {

    CallableStatement sql = null;
    ResultSet reader = null;
    @Override
    public void insertUsuario(Usuario user) throws PersistenceException ,ClassNotFoundException{
    //<editor-fold defaultstate="collapsed" desc="Atributos">
        Class.forName("com.mysql.jdbc.Driver");
        java.util.Date dfNac = null;
        try {
            dfNac = new SimpleDateFormat("dd-MM-yyyy").parse(user.getFecNac());
        } catch (ParseException ex) {
            //Logger.getLogger(UsuarioMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        Date fecNac =  new java.sql.Date(dfNac.getTime());
        Timestamp fecRegistro = new Timestamp(System.currentTimeMillis());
        int i=0;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            Connection conn = connect();
            //Lama al procedure per a guardar els atributs del objecte alumne
            //String query = "INSERT INTO Usuarios (Nombre, Apellidos, Email)";
            sql = conn.prepareCall("CALL insertUser(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
                sql.setString(1, user.getNombre());
                sql.setString(2, user.getApellidos());
                sql.setString(3, user.getEmail());
                sql.setString(4, user.getPassword());
                sql.setString(5, user.getNif());
                sql.setString(6, user.getGenero());
                sql.setDate(7, fecNac);
                sql.setBoolean(8, FALSE);
                sql.setBoolean(9, FALSE);
                sql.setInt(10, user.getDiffus());
                sql.setTimestamp(11, fecRegistro);
                
           sql.executeQuery();
            
        }catch(SQLException e){
            throw new PersistenceException(e.getErrorCode());
        }finally{
            try{
                if(sql !=null){
                     sql.close();
                }
            }catch(SQLException e){
                throw new PersistenceException(e.getErrorCode());
            }
            //Llama a la funció per a tancar la conexio
            connectionClose();
           //closeConnections();
        }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Return a la pagina">
       /* if(i>0){
            return "index";
        }else{
            return "register";
        } */
    //</editor-fold>
    }

    @Override
    public List<Usuario> getUsuarios() throws PersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Usuario getUser() throws PersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
