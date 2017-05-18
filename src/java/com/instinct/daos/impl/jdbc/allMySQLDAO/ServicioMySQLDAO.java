/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.impl.jdbc.allMySQLDAO;

import static com.instinct.daos.connection.connectMySQL2.connect;
import static com.instinct.daos.connection.connectMySQL2.connectionClose;
import com.instinct.daos.contracts.ServicioDAO;
import com.instinct.daos.impl.jdbcUtils.JDBCUtils;
import com.instinct.exception.PersistenceException.PersistenceException;
import com.instinct.web.objects.Actividad;
import com.instinct.web.objects.Servicio;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author daw2017
 */
@ManagedBean(name="ServicioMySQLDAO")
public class ServicioMySQLDAO implements ServicioDAO {

    CallableStatement sql = null;
    ResultSet reader = null;
    
    @Override
    public String callCrear(Servicio serv) throws PersistenceException, ClassNotFoundException {
        String devuelve = null;
        int comprueba = getServiceByName(serv);
        if(comprueba == 0){
            devuelve = insertarServicio(serv);
            return devuelve;
        }else{
            FacesMessage message = new FacesMessage("Ya existe un servicio con este nombre.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return null;
    }

    @Override
    public int getServiceByName(Servicio serv) throws PersistenceException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        int comprobacion = 0;
        int idService = 0;
        try{
            Connection conn = connect();
            sql = conn.prepareCall("CALL compruebaServicio(?)");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
                sql.setString(1, serv.getNombre());
                
            try{
                reader = sql.executeQuery();
                if(reader.next()){
                    idService = reader.getInt("idServ");
                    
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

            if(idService != 0){
                if(idService == serv.getIdServicio()){
                    comprobacion = 0;
                }else{
                    comprobacion++;         
                }
            }else{
                comprobacion = 0;
            }
        
        
        return comprobacion;
    }

    @Override
    public String insertarServicio(Servicio serv) throws PersistenceException, ClassNotFoundException {
    //<editor-fold defaultstate="collapsed" desc="Atributos">
        Class.forName("com.mysql.jdbc.Driver");
                
        int i=0;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            Connection conn = connect();
            
            sql = conn.prepareCall("CALL insertServicio(?, ?, ?)");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
                sql.setString(1, serv.getNombre());
                sql.setString(2, serv.getDescripcion());
                sql.setBoolean(3, serv.getActivo());
                
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
            return "backoffice";
        }else{
            return "crear_backoffice";
        }
    //</editor-fold>
    }

    @Override
    public List<Servicio> getServiciosUs() throws PersistenceException, ClassNotFoundException {
    Class.forName("com.mysql.jdbc.Driver");
    //<editor-fold defaultstate="collapsed" desc="Atributs">
        Connection conn = connect();
        List<Servicio> servicios = new ArrayList<Servicio>();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            sql = conn.prepareCall("CALL getServiciosUs()");
            reader = sql.executeQuery();
            
            while(reader.next()){
                servicios.add(JDBCUtils.getServicio(reader));
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
    return servicios;    
    }

    @Override
    public List<Servicio> getServiciosAdm() throws PersistenceException, ClassNotFoundException {
    Class.forName("com.mysql.jdbc.Driver");
    //<editor-fold defaultstate="collapsed" desc="Atributs">
        Connection conn = connect();
        List<Servicio> servicios = new ArrayList<Servicio>();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            sql = conn.prepareCall("CALL getServiciosAdm()");
            reader = sql.executeQuery();
            
            while(reader.next()){
                servicios.add(JDBCUtils.getServicio(reader));
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
    return servicios;  
    }

    @Override
    public void guardarSession(Servicio serv, String pagina) throws PersistenceException, ClassNotFoundException {
        if(pagina.equals("backoffice")){
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            httpSession.setAttribute("servAdm", serv);
            
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("edit_backoffice.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(UsuarioMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            FacesContext.getCurrentInstance().responseComplete();
        }
        
    }

    @Override
    public String callEditar(Servicio serv) throws PersistenceException, ClassNotFoundException {
        String devuelve = null;
        int comprueba = getServiceByName(serv);
        if(comprueba == 0){
            devuelve = editarServicio(serv);
            return devuelve;
        }else{
            FacesMessage message = new FacesMessage("Ya existe un servicio con este nombre.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return null;    
    }

    @Override
    public String editarServicio(Servicio serv) throws PersistenceException, ClassNotFoundException {
    //<editor-fold defaultstate="collapsed" desc="Atributos">
        Class.forName("com.mysql.jdbc.Driver");
                
        int i=0;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            Connection conn = connect();
            
            sql = conn.prepareCall("CALL editServicio(?, ?, ?, ?)");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
                sql.setInt(1, serv.getIdServicio());
                sql.setString(2, serv.getNombre());
                sql.setString(3, serv.getDescripcion());
                sql.setBoolean(4, serv.getActivo());
                
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
            return "backoffice";
        }else{
            return "edit_backoffice";
        }
    //</editor-fold>    
    }

}
