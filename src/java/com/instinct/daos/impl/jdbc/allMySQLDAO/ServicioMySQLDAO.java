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
 * ServicioMySQLDAO classe per englobar les funcions de serveis.
 * @author Jordi Lascorz
 * @since 19/05/2017
 * @version 1.0
 */
@ManagedBean(name="ServicioMySQLDAO")
public class ServicioMySQLDAO implements ServicioDAO {

    CallableStatement sql = null;
    ResultSet reader = null;
    
    /**
     * Funcio per englobar les funcions de creació d'un servei
     * @param serv
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public String callCrear(Servicio serv) throws PersistenceException, ClassNotFoundException {
        String devuelve = null;
        //Verifica que no existeixi un serveix amb el mateix nom
        int comprueba = getServiceByName(serv);
        if(comprueba == 0){
            //Inserta el servei en la base de dades despres de la verificació
            devuelve = insertarServicio(serv);
            return devuelve;
        }else{
            FacesMessage message = new FacesMessage("Ya existe un servicio con este nombre.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return null;
    }

    /**
     * Funció per verificar que no existeixi un serveix amb el mateix nom
     * @param serv
     * @return int
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public int getServiceByName(Servicio serv) throws PersistenceException, ClassNotFoundException {
        //<editor-fold defaultstate="collapsed" desc="Atributs">
        Class.forName("com.mysql.jdbc.Driver");
        int comprobacion = 0;
        int idService = 0;
        //</editor-fold>
        
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

    /**
     * Funció per insertar el servei
     * @param serv
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
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

    /**
     * Funció per seleccionar tots els serveis que estiguin actius
     * @return List de Servicio
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
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

    /**
     * Funció per seleccionar tots els serveis
     * @return List de Servicio
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
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

    /**
     * Funció per guardar la sessión d'un servei depenent de la pagina
     * @param serv
     * @param pagina
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public void guardarSession(Servicio serv, String pagina) throws PersistenceException, ClassNotFoundException {
        //Si la pagina es la de administració guarda la sessió per utilitzarla en administració només.
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

    /**
     * Funcio que engloba les funcións per editar un servei
     * @param serv
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public String callEditar(Servicio serv) throws PersistenceException, ClassNotFoundException {
        String devuelve = null;
        //Verifica que no existeixi un serveix amb el mateix nom
        int comprueba = getServiceByName(serv);
        if(comprueba == 0){
            //Edita el servei en la base de dades despres de la verificació
            devuelve = editarServicio(serv);
            return devuelve;
        }else{
            FacesMessage message = new FacesMessage("Ya existe un servicio con este nombre.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return null;    
    }

    /**
     * Funcio per editar un servei com usuari normal o administrador
     * @param serv
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
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
