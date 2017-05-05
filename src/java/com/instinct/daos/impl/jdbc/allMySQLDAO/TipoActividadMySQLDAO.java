/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.impl.jdbc.allMySQLDAO;

import static com.instinct.daos.connection.connectMySQL2.connect;
import static com.instinct.daos.connection.connectMySQL2.connectionClose;
import com.instinct.daos.contracts.TipoActividadDAO;
import com.instinct.daos.impl.jdbcUtils.JDBCUtils;
import com.instinct.exception.PersistenceException.PersistenceException;
import com.instinct.web.objects.TipoActividad;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author daw2017
 */
@ManagedBean(name="TiposMySQLDAO")
@SessionScoped
public class TipoActividadMySQLDAO implements TipoActividadDAO{

    CallableStatement sql = null;
    ResultSet reader = null;
    
    @Override
    public String callCrear(TipoActividad tipoAct) throws PersistenceException, ClassNotFoundException {
        String devuelve = null;
        int comprueba = getTipoByName(tipoAct);
        if(comprueba == 0){
            devuelve = insertarTipoActividad(tipoAct);
            return devuelve;
        }else{
            FacesMessage message = new FacesMessage("Ya existe un tipo de actividad con este nombre.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return null;
    }

    @Override
    public int getTipoByName(TipoActividad tipoAct) throws PersistenceException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        int comprobacion = 0;
        int idTipo = 0;
        try{
            Connection conn = connect();
            sql = conn.prepareCall("CALL compruebaTipo(?)");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
                sql.setString(1, tipoAct.getNombre());
                
            try{
                reader = sql.executeQuery();
                if(reader.next()){
                    idTipo = reader.getInt("idTipo");
                    
                }
            }catch(SQLException e){
                 throw new PersistenceException(e.getErrorCode());
            }
            
        }catch(SQLException e){
            throw new PersistenceException(e.getErrorCode());
        }finally{
            try{
                if(reader != null){
                    reader.close();
                }
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

            if(idTipo != 0){
                comprobacion++;         
            }else{
                comprobacion = 0;
            }
        
        
        return comprobacion;    
    }

    @Override
    public String insertarTipoActividad(TipoActividad tipoAct) throws PersistenceException, ClassNotFoundException {
    //<editor-fold defaultstate="collapsed" desc="Atributos">
        Class.forName("com.mysql.jdbc.Driver");
                
        int i=0;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            Connection conn = connect();
            
            sql = conn.prepareCall("CALL insertTipo(?, ?)");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
                sql.setString(1, tipoAct.getNombre());
                sql.setString(2, tipoAct.getDescripcion());
                
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
            return "tipos_de_actividad";
        }else{
            return "backoffice";
        }
    //</editor-fold>
    }
    
    
    @Override
    public List<TipoActividad> getTiposActividad() throws PersistenceException, ClassNotFoundException {
    Class.forName("com.mysql.jdbc.Driver");
    //<editor-fold defaultstate="collapsed" desc="Atributs">
        Connection conn = connect();
        List<TipoActividad> tiposActividad = new ArrayList<TipoActividad>();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            sql = conn.prepareCall("CALL getTiposActividad()");
            reader = sql.executeQuery();
            
            while(reader.next()){
                tiposActividad.add(JDBCUtils.getTipoActividad(reader));
            }
        } catch(SQLException ex){
            throw new PersistenceException(ex.getErrorCode());
        }finally{
            try{
                if(reader != null){
                    reader.close();
                }
                if(sql !=null){
                 sql.close();
                }
                
                connectionClose();
            }catch(SQLException e){
                throw new PersistenceException(e.getErrorCode());
            }
            
        }
    //</editor-fold>
    return tiposActividad;    
    }
    
}
