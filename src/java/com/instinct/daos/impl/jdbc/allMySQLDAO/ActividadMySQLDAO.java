/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.impl.jdbc.allMySQLDAO;

import static com.instinct.daos.connection.connectMySQL2.connect;
import static com.instinct.daos.connection.connectMySQL2.connectionClose;
import com.instinct.daos.contracts.ActividadDAO;
import com.instinct.daos.impl.jdbcUtils.JDBCUtils;
import com.instinct.exception.PersistenceException.PersistenceException;
import com.instinct.web.objects.Actividad;
import com.instinct.web.objects.Usuario;
import java.io.IOException;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author daw2017
 */
@ManagedBean(name="ActividadMySQLDAO")
@SessionScoped
public class ActividadMySQLDAO implements ActividadDAO{

    CallableStatement sql = null;
    ResultSet reader = null;
    
    @Override
    public void callCrear(Actividad activ, int idUser) throws PersistenceException, ClassNotFoundException {
        String devuelve = null;
        int comprueba = getActivityByNameYear(activ);
        if(comprueba == 0){
            devuelve = insertarActividad(activ, idUser);
            activ = getActividadByName(activ);
            if(devuelve != "insertado"){
                FacesMessage message = new FacesMessage("No se ha podido insertar la actividad.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }else{
                //return activ;
            }
            
        }else{
            FacesMessage message = new FacesMessage("Esta actividad ya ha sido creada anteriormente.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        //return null;
    }

    @Override
    public int getActivityByNameYear(Actividad activ) throws PersistenceException, ClassNotFoundException {
    Class.forName("com.mysql.jdbc.Driver");
        int comprobacion = 0;
        int idAct = 0;
        try{
            Connection conn = connect();
            sql = conn.prepareCall("CALL compruebaActividad(?)");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
                sql.setString(1, activ.getNombre());
                
            try{
                reader = sql.executeQuery();
                if(reader.next()){
                    idAct = reader.getInt("idAct");
                    
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

            if(idAct != 0){
                comprobacion++;         
            }else{
                comprobacion = 0;
            }
        
        
        return comprobacion;    
    }

    @Override
    public String insertarActividad(Actividad activ, int idUser) throws PersistenceException, ClassNotFoundException {
    //<editor-fold defaultstate="collapsed" desc="Atributos">
        Class.forName("com.mysql.jdbc.Driver");
                
        int i=0;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            Connection conn = connect();
            //<editor-fold defaultstate="collapsed" desc="Fecha de la Actividad">
            String fec = activ.getFecha();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = format.parse(fec);
            Date fecAct = new Date(date.getTime());
            //</editor-fold>
            
            sql = conn.prepareCall("CALL insertActividad(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
                sql.setInt(1, idUser);
                sql.setInt(2, activ.getIdTipo());
                sql.setString(3, activ.getNombre());
                sql.setString(4, activ.getDescripcion());
                sql.setString(5, activ.getEmail());
                sql.setString(6, activ.getWeb());
                sql.setString(7, activ.getTelefono());
                sql.setDate(8, fecAct);
                sql.setBoolean(9, FALSE);
                sql.setBoolean(10, FALSE);
                sql.setString(11, "NULL");
                
          i = sql.executeUpdate();
            
        }catch(SQLException e){
            throw new PersistenceException(e.getErrorCode());
        } catch (ParseException ex) {
            Logger.getLogger(ActividadMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
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

    @Override
    public List<Actividad> getActividadByUser(Usuario user) throws PersistenceException, ClassNotFoundException {
    Class.forName("com.mysql.jdbc.Driver");
    //<editor-fold defaultstate="collapsed" desc="Atributs">
        Connection conn = connect();
        List<Actividad> activities = new ArrayList<Actividad>();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            sql = conn.prepareCall("CALL getActByUser(?)");
            sql.setInt(1, user.getIdUser());
            reader = sql.executeQuery();
            
            while(reader.next()){
                activities.add(JDBCUtils.getActividad(reader));
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
    return activities;        
    }

    @Override
    public int mirarSiTieneActividad(Usuario user) throws PersistenceException, ClassNotFoundException {
    Class.forName("com.mysql.jdbc.Driver");
    //<editor-fold defaultstate="collapsed" desc="Atributs">
        Connection conn = connect();
        int cuenta = 0;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            sql = conn.prepareCall("CALL ifHaveActivity(?)");
            sql.setInt(1, user.getIdUser());
            reader = sql.executeQuery();
            
            while(reader.next()){
                cuenta = reader.getInt("cuenta");
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
    return cuenta;      
    }

    @Override
    public Actividad getActividadByName(Actividad activ) throws PersistenceException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        int comprobacion = 0;
        try{
            Connection conn = connect();
            sql = conn.prepareCall("CALL compruebaActividad(?)");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
                sql.setString(1, activ.getNombre());
                
            try{
                reader = sql.executeQuery();
                if(reader.next()){
                    activ.setIdAct(reader.getInt("idAct"));
                    activ.setIdUser(reader.getInt("idUser"));
                    activ.setIdTipo(reader.getInt("idTipo"));
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
        
        return activ;
    }

    @Override
    public void bajaActividad(Actividad activ) throws PersistenceException, ClassNotFoundException {
    Class.forName("com.mysql.jdbc.Driver");
    int i = 0;
        try{
        Connection conn = connect();
            sql = conn.prepareCall("CALL setActividadBaja(?, ?)");
            sql.setInt(1, activ.getIdAct());
            sql.setBoolean(2, TRUE);
            
         i = sql.executeUpdate();

        }catch(SQLException e){
            throw new PersistenceException(e.getErrorCode());
        }finally{
            try{

                if(sql !=null){
                 sql.close();
                }
                
                connectionClose();
            }catch(SQLException e){
                throw new PersistenceException(e.getErrorCode());
            }
            
        }
        
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("mis_actividades.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ActividadMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    @Override
    public void guardarSessionEditar(Actividad activ) throws PersistenceException, ClassNotFoundException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
        HttpSession httpSession = request.getSession(false);
        httpSession.setAttribute("activKey", activ);
        
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("editar_actividad.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ActividadMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        FacesContext.getCurrentInstance().responseComplete();
    }
    
    @Override
    public void borrarSession() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }

    @Override
    public void editarActividad(Actividad activ) throws PersistenceException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
