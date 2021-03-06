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
 * LocalizacionMySQLDAO classe per englobar les funcions de de localització.
 * @author Jordi Lascorz
 * @since 17/05/2017
 * @version 1.0
 */
@ManagedBean(name="LocalizacionMySQLDAO")
@SessionScoped
public class LocalizacionMySQLDAO implements LocalizacionDAO {

    CallableStatement sql = null;
    ResultSet reader = null;
    
    /**
     * Funcio que retorna la llista de totes les comunitats
     * @return List de Comunidades
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
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

    /**
     * Funció que retorna la llista de totes les provincies a partir de la comunitat
     * @param idComunidad
     * @return List de Provincias
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
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

    /**
     * Funció que retorna la llista de tots els municipis a partir de la provincia
     * @param idProvincia
     * @return List de Municipis
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
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

    /**
     * Funció que inserta a la base de dades la localització d'una activitat
     * @param activity
     * @param idComunidad
     * @param idProvincia
     * @param idMunicipio
     * @param calle
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public String escogerLocalizacion(Actividad activity, int idComunidad, int idProvincia, int idMunicipio, String calle) throws PersistenceException, ClassNotFoundException {
    //<editor-fold defaultstate="collapsed" desc="Atributos">
        Class.forName("com.mysql.jdbc.Driver");
                
        int i=0;
       String verifica = null;
        if(activity.getIdAct() != 0){
        //Verifica que la localitat existeixi
        verifica = verificarLocalidad(idComunidad, idProvincia, idMunicipio);
        if(verifica == "existe"){
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            Connection conn = connect();
            //Procedure que inserta la localització a la base de dades
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
                //Redirecciona a mis actividades
                FacesContext.getCurrentInstance().getExternalContext().redirect("mis_actividades.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(LocalizacionMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
             FacesContext.getCurrentInstance().responseComplete();
        }else{
            //Mostra error en cas de que no es pugui afegir la localització
            FacesMessage message = new FacesMessage("No se ha podido añadir la localizacion a la actividad.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }else{
        try {
            //Redirecciona a crear la activitat
            FacesContext.getCurrentInstance().getExternalContext().redirect("crear_actividad.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(LocalizacionMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
            FacesContext.getCurrentInstance().responseComplete();
    }
    }else{
        //Mostra un error en cas de que l'activitat no s'hagi pogut crear
        FacesMessage message = new FacesMessage("Error: Ha habido un problema al crear la actividad.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
        return null;
    //</editor-fold>
    }
    
    /**
     * Funció que agafa la localització depenent de la id de l'activitat
     * @param activ
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public void getLocalizacionByIdAct(Actividad activ) throws PersistenceException, ClassNotFoundException {
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
        //<editor-fold defaultstate="collapsed" desc="Sessio">
        //Sessió necessaria per poder mostrar la localitat en la pagina
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
        HttpSession httpSession = request.getSession(false);
        httpSession.setAttribute("localKey", localizacion);
        //</editor-fold>          
    }

    /**
     * Funció que agafa la comunitat per la seva id
     * @param local
     * @return Comunidad
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
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

    /**
     * Funcio que agafa la provincia per la seva id
     * @param local
     * @return Provincia
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
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

    /**
     * Funcio que agafa el municipi per la seva id
     * @param local
     * @return Municipio
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
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

    /**
     * Edita la localització d'una activitat amb les noves dades
     * @param activity
     * @param idComunidad
     * @param idProvincia
     * @param idMunicipio
     * @param calle
     * @param error
     * @param pagina
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public String editaLocalizacionAct(Actividad activity, int idComunidad, int idProvincia, int idMunicipio, String calle, int error, String pagina) throws PersistenceException, ClassNotFoundException {
    //<editor-fold defaultstate="collapsed" desc="Atributos">
        Class.forName("com.mysql.jdbc.Driver");
                
        int i=0;
        String verifica = null;
        
        if(error == 0){
        verifica = verificarLocalidad(idComunidad, idProvincia, idMunicipio);
        if(verifica == "existe"){
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            Connection conn = connect();
            //Procedure per editar la localització d'una activitat
            sql = conn.prepareCall("CALL editLocalizacionAct(?, ?, ?, ?, ?)");
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
                    if(pagina.equals("editActUs")){
                        try {
                            FacesContext.getCurrentInstance().getExternalContext().redirect("mis_actividades.xhtml");
                        } catch (IOException ex) {
                            Logger.getLogger(LocalizacionMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }else if(pagina.equals("editActAdm")){
                        try {
                            FacesContext.getCurrentInstance().getExternalContext().redirect("backoffice.xhtml");
                        } catch (IOException ex) {
                            Logger.getLogger(LocalizacionMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                     FacesContext.getCurrentInstance().responseComplete();
                }else{
                    FacesMessage message = new FacesMessage("No se ha podido editar la localizacion a la actividad.");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }else{
                return null;
            }
        }else{
            FacesMessage message = new FacesMessage("Error: Ha habido un problema al editar la actividad.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return null;
    //</editor-fold>
    }

    /**
     * Verificar la localidad que engloba la verificació de la provincia i el municipi
     * @param idComunidad
     * @param idProvincia
     * @param idMunicipio
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public String verificarLocalidad(int idComunidad, int idProvincia, int idMunicipio) throws PersistenceException, ClassNotFoundException  {
        String vProvincia = null;
        String vMunicipio = null;
        
        //Primer de tot verifica la provincia
        vProvincia = verifProvincia(idComunidad, idProvincia);
        //En cas de que existeixi verifica el municipi
        if(vProvincia == "ok"){
            vMunicipio = verifMunicipio(idProvincia, idMunicipio);
            //En cas de que existeixi retorna un ok
            if(vMunicipio == "ok"){
                return "existe";
            }else{
                //Mostra un error en cas de que aquest municipi no perteneixi a aquesta provincia
                FacesMessage message = new FacesMessage("Este municipio no pertenece a esa provincia.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }else{
            //Mostra un error en cas de que aquesta provincia no perteneixi a aquesta comunitat
            FacesMessage message = new FacesMessage("Esta provincia no pertenece a esa comunidad.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return null;
    }

    /**
     * Verifica que la provincia pertenezca a la comunitat que s'ha passat
     * @param idComunidad
     * @param idProvincia
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public String verifProvincia(int idComunidad, int idProvincia) throws PersistenceException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        //<editor-fold defaultstate="collapsed" desc="Atributs">
            Connection conn = connect();
            Provincias provincia = null;
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
            try{
                sql = conn.prepareCall("CALL verifProvincia(?, ?)");
                sql.setInt(1, idComunidad);
                sql.setInt(2, idProvincia);
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
            
            if(provincia != null){
                return "ok";
            }else{
                return "no";
            }
        //</editor-fold>

    }

    /**
     * Verifica que el municipi perteneixi a la provincia que s'ha passat
     * @param idProvincia
     * @param idMunicipio
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public String verifMunicipio(int idProvincia, int idMunicipio) throws PersistenceException, ClassNotFoundException {
    Class.forName("com.mysql.jdbc.Driver");
        //<editor-fold defaultstate="collapsed" desc="Atributs">
            Connection conn = connect();
            Municipios municipio = null;
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
            try{
                sql = conn.prepareCall("CALL verifMunicipio(?, ?)");
                sql.setInt(1, idProvincia);
                sql.setInt(2, idMunicipio);
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
            
            if(municipio != null){
                return "ok";
            }else{
                return "no";
            }
        //</editor-fold>
    }

    /**
     * Selecciona el lugar de l'activitat de la base de dades
     * @param activ
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public String callGetLugar(Actividad activ) throws PersistenceException, ClassNotFoundException {
        //<editor-fold defaultstate="collapsed" desc="Atributos">
        Localizacion local = null;
        Municipios municipio = null;
        Provincias provincia = null;
        Comunidades comunidad = null;
        String lugar = null;
        //</editor-fold>
        
        local = getLugarAct(activ);
        //Si la localitat no es null, retorna un string amb tot el municipi adaptat
        if(local != null){
            municipio = getMunicipio(local);
            provincia = getProvincia(local);
            comunidad = getComunidad(local);
            lugar = municipio.getNombre() + ", " + provincia.getNombre() + " | " + comunidad.getNombre();
            return lugar;
        }else{
            return null;
        }

    }

    /**
     * Selecciona la localització d'una activitat
     * @param activ
     * @return Localizacion
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public Localizacion getLugarAct(Actividad activ) throws PersistenceException, ClassNotFoundException {
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
            return localizacion;
    }

}
