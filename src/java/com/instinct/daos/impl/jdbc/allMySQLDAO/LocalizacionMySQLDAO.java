/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.impl.jdbc.allMySQLDAO;

import static com.instinct.daos.connection.connectMySQL2.connect;
import static com.instinct.daos.connection.connectMySQL2.connectionClose;
import com.instinct.daos.contracts.LocalizacionDAO;
import com.instinct.daos.impl.jdbcUtils.JDBCUtils;
import com.instinct.exception.PersistenceException.PersistenceException;
import com.instinct.web.objects.Actividad;
import com.instinct.web.objects.Comunidades;
import com.instinct.web.objects.Localizacion;
import com.instinct.web.objects.Municipios;
import com.instinct.web.objects.Provincias;
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
 *
 * @author daw2017
 */
@ManagedBean(name="LocalizacionMySQLDAO")
@SessionScoped
public class LocalizacionMySQLDAO implements LocalizacionDAO {

    CallableStatement sql = null;
    ResultSet reader = null;
    
    @Override
    public List<Comunidades> getComunidades() throws PersistenceException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        //<editor-fold defaultstate="collapsed" desc="Atributs">
            Connection conn = connect();
            List<Comunidades> comunidades = new ArrayList<>();
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
            try{
                sql = conn.prepareCall("CALL getComunidades()");
                reader = sql.executeQuery();

                while(reader.next()){
                    comunidades.add(JDBCUtils.getComunidad(reader));
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
        return comunidades;  
    }

    @Override
    public List<Provincias> getProvincias(int idComunidad) throws PersistenceException, ClassNotFoundException {
    Class.forName("com.mysql.jdbc.Driver");
        //<editor-fold defaultstate="collapsed" desc="Atributs">
            Connection conn = connect();
            List<Provincias> provincias = new ArrayList<>();
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
            try{
                sql = conn.prepareCall("CALL getProvincias(?)");
                sql.setInt(1, idComunidad);
                reader = sql.executeQuery();

                while(reader.next()){
                    provincias.add(JDBCUtils.getProvincia(reader));
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
        return provincias;
    }

    @Override
    public List<Municipios> getMunicipios(int idProvincia) throws PersistenceException, ClassNotFoundException {
    Class.forName("com.mysql.jdbc.Driver");
        //<editor-fold defaultstate="collapsed" desc="Atributs">
            Connection conn = connect();
            List<Municipios> municipios = new ArrayList<>();
            int val2= 2;
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
            try{
                sql = conn.prepareCall("CALL getMunicipios(?)");
                sql.setInt(1, idProvincia);
                reader = sql.executeQuery();

                while(reader.next()){
                    municipios.add(JDBCUtils.getMunicipio(reader));
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
        return municipios;
    }

    @Override
    public String escogerLocalizacion(Actividad activity, int idComunidad, int idProvincia, int idMunicipio, String calle) throws PersistenceException, ClassNotFoundException {
    //<editor-fold defaultstate="collapsed" desc="Atributos">
        Class.forName("com.mysql.jdbc.Driver");
                
        int i=0;
       
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            Connection conn = connect();
            
            sql = conn.prepareCall("CALL insertLocalizacion(?, ?, ?, ?, ?)");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
                sql.setInt(1, activity.getIdAct());
                sql.setInt(2, idComunidad);
                sql.setInt(3, idProvincia);
                sql.setInt(4, idMunicipio);
                sql.setString(5, calle);
                
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
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("mis_actividades.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(LocalizacionMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
             FacesContext.getCurrentInstance().responseComplete();
        }else{
            FacesMessage message = new FacesMessage("No se ha podido añadir la localizacion a la actividad.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return null;
    //</editor-fold>
    }
    
    @Override
    public Localizacion getLocalizacionByIdAct(Actividad activ) throws PersistenceException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        //<editor-fold defaultstate="collapsed" desc="Atributs">
            Connection conn = connect();
            Localizacion localizacion = null;
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
            try{
                sql = conn.prepareCall("CALL getLocalizacionByIdAct(?)");
                sql.setInt(1, activ.getIdAct());
                reader = sql.executeQuery();

                while(reader.next()){
                    localizacion = JDBCUtils.getLocalizacion(reader);
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
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
        HttpSession httpSession = request.getSession(false);
        httpSession.setAttribute("localKey", localizacion);
        
        return localizacion;    
    }

    @Override
    public Comunidades getComunidad(Localizacion local) throws PersistenceException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        //<editor-fold defaultstate="collapsed" desc="Atributs">
            Connection conn = connect();
            Comunidades comunidad = new Comunidades();
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
            try{
                sql = conn.prepareCall("CALL getComunidadById(?)");
                sql.setInt(1, local.getIdComunidad());
                reader = sql.executeQuery();

                while(reader.next()){
                    comunidad = JDBCUtils.getComunidad(reader);
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
        return comunidad;
    }

    @Override
    public Provincias getProvincia(Localizacion local) throws PersistenceException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        //<editor-fold defaultstate="collapsed" desc="Atributs">
            Connection conn = connect();
            Provincias provincia = new Provincias();
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
            try{
                sql = conn.prepareCall("CALL getProvinciaById(?)");
                sql.setInt(1, local.getIdProvincia());
                reader = sql.executeQuery();

                while(reader.next()){
                    provincia = JDBCUtils.getProvincia(reader);
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
        return provincia;
    }

    @Override
    public Municipios getMunicipio(Localizacion local) throws PersistenceException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        //<editor-fold defaultstate="collapsed" desc="Atributs">
            Connection conn = connect();
            Municipios municipio = new Municipios();
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
            try{
                sql = conn.prepareCall("CALL getMunicipioById(?)");
                sql.setInt(1, local.getIdMunicipio());
                reader = sql.executeQuery();

                while(reader.next()){
                    municipio = JDBCUtils.getMunicipio(reader);
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
        return municipio;
    }

}
