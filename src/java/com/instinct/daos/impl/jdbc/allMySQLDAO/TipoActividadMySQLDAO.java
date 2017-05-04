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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getTipoByName(TipoActividad tipoAct) throws PersistenceException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String insertarTipoActividad(TipoActividad tipoAct) throws PersistenceException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
