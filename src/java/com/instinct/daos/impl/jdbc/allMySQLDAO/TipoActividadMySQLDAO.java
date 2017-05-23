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
import com.instinct.web.objects.Actividad;
import com.instinct.web.objects.TipoActividad;
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
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * TipoActividadMySQLDAO classe per englobar les funcions de tipos d'actividades.
 * @author Jordi Lascorz
 * @since 13/05/2017
 * @version 1.0
 */
@ManagedBean(name="TiposMySQLDAO")
@SessionScoped
public class TipoActividadMySQLDAO implements TipoActividadDAO{

    CallableStatement sql = null;
    ResultSet reader = null;
    
    /**
     * Funció que engloba les funcións que s'utilitzen per crear tipus d'activitat
     * @param tipoAct
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public String callCrear(TipoActividad tipoAct) throws PersistenceException, ClassNotFoundException {
        String devuelve = null;
        //Verifica que no existeixi un tipus amb el mateix nom
        int comprueba = getTipoByName(tipoAct);
        if(comprueba == 0){
            //Inserta un tipus d'activitat en la base de dades
            devuelve = insertarTipoActividad(tipoAct);
            return devuelve;
        }else{
            FacesMessage message = new FacesMessage("Ya existe un tipo de actividad con este nombre.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return null;
    }

    /**
     * Verifica que no existeixi un tipus amb el mateix nom
     * @param tipoAct
     * @return int
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public int getTipoByName(TipoActividad tipoAct) throws PersistenceException, ClassNotFoundException {
        //<editor-fold defaultstate="collapsed" desc="Atributs">
        Class.forName("com.mysql.jdbc.Driver");
        int comprobacion = 0;
        int idTipo = 0;
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
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
                if(idTipo == tipoAct.getIdTipo()){
                    comprobacion = 0;
                }else{
                    comprobacion++;
                }
            }else{
                comprobacion = 0;
            }
        
        
        return comprobacion;
        //</editor-fold>
            
    }

    /**
     * Inserta un tipus d'activitat en la base de dades
     * @param tipoAct
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public String insertarTipoActividad(TipoActividad tipoAct) throws PersistenceException, ClassNotFoundException {
    //<editor-fold defaultstate="collapsed" desc="Atributos">
        Class.forName("com.mysql.jdbc.Driver");
                
        int i=0;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            Connection conn = connect();
            
            sql = conn.prepareCall("CALL insertTipo(?, ?, ?)");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
                sql.setString(1, tipoAct.getNombre());
                sql.setString(2, tipoAct.getDescripcion());
                sql.setBoolean(3, tipoAct.getActivo());
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
     * Agafa els tipus d'activitats que esten actius
     * @return List de TipoActividad
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public List<TipoActividad> getTiposActividad() throws PersistenceException, ClassNotFoundException {
    Class.forName("com.mysql.jdbc.Driver");
    //<editor-fold defaultstate="collapsed" desc="Atributs">
        Connection conn = connect();
        List<TipoActividad> tiposActividad = new ArrayList<TipoActividad>();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            sql = conn.prepareCall("CALL getTiposActividadUs()");
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

    /**
     * Seleccionar el tipo de actividad d'una actividad
     * @param activity
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public String getTipoByAct(Actividad activity) throws PersistenceException, ClassNotFoundException {
    Class.forName("com.mysql.jdbc.Driver");
    //<editor-fold defaultstate="collapsed" desc="Atributs">
        Connection conn = connect();
        String tipo = null;
        TipoActividad tipoAct = null;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            sql = conn.prepareCall("CALL getTipoByAct(?)");
            sql.setInt(1, activity.getIdTipo());
            reader = sql.executeQuery();
            
            while(reader.next()){
                tipoAct = JDBCUtils.getTipoActividad(reader);
                tipo = tipoAct.getNombre();
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
        return tipo;    
    }

    /**
     * Agafa tots els tipus d'activitats 
     * @return List de TipoActividad
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public List<TipoActividad> getTiposActividadAdm() throws PersistenceException, ClassNotFoundException {
    Class.forName("com.mysql.jdbc.Driver");
    //<editor-fold defaultstate="collapsed" desc="Atributs">
        Connection conn = connect();
        List<TipoActividad> tiposActividad = new ArrayList<TipoActividad>();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            sql = conn.prepareCall("CALL getTiposActividadAdm()");
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

    /**
     * Guarda la sessió del tipus d'activitat
     * @param tipoAct
     * @param pagina
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public void guardarSession(TipoActividad tipoAct, String pagina) throws PersistenceException, ClassNotFoundException {
        //Verifica que la pagina desde que s'ha entrat sigui la del panel d'administració i assigna una sessio de tipus d'activitat
        if(pagina.equals("backoffice")){
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            httpSession.setAttribute("tipoAdm", tipoAct);
            
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("edit_backoffice.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(UsuarioMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            FacesContext.getCurrentInstance().responseComplete();
        }    
    }

    /**
     * Funció que engloba les funcións per editar un tipus d'activitat
     * @param tipoAct
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public String callEditar(TipoActividad tipoAct) throws PersistenceException, ClassNotFoundException {
        String devuelve = null;
        //Verifica que no existeixi un tipus amb el mateix nom
        int comprueba = getTipoByName(tipoAct);
        if(comprueba == 0){
            //Edita un tipus d'activitat en la base de dades
            devuelve = editarTipoActividad(tipoAct);
            return devuelve;
        }else{
            FacesMessage message = new FacesMessage("Ya existe un tipo de actividad con este nombre.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return null;
    }

    /**
     * Funció que permet editar el Tipus d'activitat
     * @param tipoAct
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public String editarTipoActividad(TipoActividad tipoAct) throws PersistenceException, ClassNotFoundException {
        //<editor-fold defaultstate="collapsed" desc="Atributos">
        Class.forName("com.mysql.jdbc.Driver");
                
        int i=0;
    //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
            try{
                Connection conn = connect();

                sql = conn.prepareCall("CALL editTipo(?, ?, ?, ?)");
                sql.setEscapeProcessing(true);
                sql.setQueryTimeout(90);
                    sql.setInt(1, tipoAct.getIdTipo());
                    sql.setString(2, tipoAct.getNombre());
                    sql.setString(3, tipoAct.getDescripcion());
                    sql.setBoolean(4, tipoAct.getActivo());
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
