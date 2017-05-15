/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.impl.jdbc.allMySQLDAO;

import static com.instinct.daos.connection.connectMySQL2.connect;
import static com.instinct.daos.connection.connectMySQL2.connectionClose;
import com.instinct.daos.contracts.ParticipaDAO;
import com.instinct.exception.PersistenceException.PersistenceException;
import com.instinct.web.objects.Actividad;
import com.instinct.web.objects.Usuario;
import static java.lang.Boolean.FALSE;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author daw2017
 */
@ManagedBean(name="ParticipaMySQLDAO")
@SessionScoped
public class ParticipaMySQLDAO implements ParticipaDAO {
    
    CallableStatement sql = null;
    ResultSet reader = null;

    @Override
    public String callParticipa(Actividad activity, Usuario user) throws PersistenceException, ClassNotFoundException {
        String comprueba = null;
        if(user.getIdUser() != 0){
            comprueba = insertaRelacion(activity, user);
            if(comprueba.equals("insertado")){
                FacesMessage message = new FacesMessage("Has sido inscrito correctamente.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }else{
                FacesMessage message = new FacesMessage("No se ha podido tramitar la inscripcion.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }else{
            FacesMessage message = new FacesMessage("Necesitas estar conectado para poder hacer la inscripcion.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        
        return null;
    }

    @Override
    public String insertaRelacion(Actividad activity, Usuario user) throws PersistenceException, ClassNotFoundException {
        //<editor-fold defaultstate="collapsed" desc="Atributos">
        Class.forName("com.mysql.jdbc.Driver");
                
        int i=0;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            Connection conn = connect();
            //<editor-fold defaultstate="collapsed" desc="Fecha del Registro">
            Timestamp fecInscripcion = new Timestamp(System.currentTimeMillis());
            //</editor-fold>
            
            sql = conn.prepareCall("CALL insertActividad(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
                sql.setInt(1, user.getIdUser());
                sql.setInt(2, activity.getIdAct());
                sql.setTimestamp(3, fecInscripcion);
                
          i = sql.executeUpdate();
            
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
        if(i>0){
            return "insertado";
        }else{
            return "error";
        } 
    //</editor-fold>
    }
    
}
