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
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

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
    public String callCrear(Actividad activ, int idUser) throws PersistenceException, ClassNotFoundException {
        String devuelve = null;
        String error = "error";
        int comprueba = getActivityByNameYear(activ);
        if(comprueba == 0){
            devuelve = insertarActividad(activ, idUser);
            activ = getActividadByName(activ);
            if(devuelve != "insertado"){
                FacesMessage message = new FacesMessage("No se ha podido insertar la actividad.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }            
        }else{
            FacesMessage message = new FacesMessage("Esta actividad ya ha sido creada anteriormente.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        
        return null;
    }

    @Override
    public int getActivityByNameYear(Actividad activ) throws PersistenceException, ClassNotFoundException {
    Class.forName("com.mysql.jdbc.Driver");
        int comprobacion = 0;
        int idAct = 0;
        String nombre = null;
        java.util.Date dfn = null;
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
                    nombre = reader.getString("Nombre");
                    dfn = reader.getDate("Fecha");
                    
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
                if(idAct == activ.getIdAct()){
                    if(nombre.equals(activ.getNombre())){
                        comprobacion = 0;
                        if(activ.getFecha() == null || activ.getFecha().isEmpty()){
                            SimpleDateFormat dfor = new SimpleDateFormat("yyyy-MM-dd");
                            String fecha = dfor.format(dfn);
                            activ.setFecha(fecha);
                        }
                    }else{
                        comprobacion++;
                    }
                }else{
                comprobacion++;
                }
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
                sql.setString(6, activ.getTelefono());
                sql.setString(7, activ.getWeb());
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
    public void guardarSession(Actividad activ, String pagina) throws PersistenceException, ClassNotFoundException {
        
        if(pagina.equals("backoffice")){
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            httpSession.setAttribute("activAdm", activ);
        
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("edit_backoffice.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ActividadMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }else{
        
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            httpSession.setAttribute("activKey", activ);

            if(pagina.equals("editar")){
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("editar_actividad.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(ActividadMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else if(pagina.equals("visualizar")){
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("actividad.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(ActividadMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else if(pagina.equals("subirFichero")){
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("subir_resultado.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(ActividadMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        FacesContext.getCurrentInstance().responseComplete();
    }
    
    @Override
    public void borrarSession() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }
    
    @Override
    public String callEditar(Actividad activ, String pagina, int error) throws PersistenceException, ClassNotFoundException {
        String devuelve = null;
        
        int comprueba = getActivityByNameYear(activ);
        if(comprueba == 0){
            if(pagina.equals("editActUs")){
                devuelve = editarActividad(activ);
            }else if(pagina.equals("editActAdm")){
                devuelve = editarActividadAdm(activ);
            }
            if(!devuelve.equals("editado")){
                FacesMessage message = new FacesMessage("No se ha podido editar la actividad.");
                FacesContext.getCurrentInstance().addMessage(null, message);
                error = 1;
            }else{
                //return activ;
            }
            
        }else{
            FacesMessage message = new FacesMessage("Esta actividad ya ha sido creada anteriormente.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            error = 1;
        }
        

          FacesContext context = FacesContext.getCurrentInstance();
          HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
          HttpSession httpSession = request.getSession(false);
          httpSession.setAttribute("errorTemp", error);  
        
        
        return null;
    }

    @Override
    public String editarActividad(Actividad activ) throws PersistenceException, ClassNotFoundException {
    //<editor-fold defaultstate="collapsed" desc="Atributos">
        Class.forName("com.mysql.jdbc.Driver");
        Date fecAct = null;
        int i=0;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            Connection conn = connect();
            //<editor-fold defaultstate="collapsed" desc="Fecha de la Actividad">
            if(activ.getFecha() == null || activ.getFecha().isEmpty() || activ.getFecha().equals("")){
                sql = conn.prepareCall("CALL editActivUsWoFec(?, ?, ?, ?, ?, ?, ?)");
            }else{
                String fec = activ.getFecha();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = format.parse(fec);
                fecAct = new Date(date.getTime());
                sql = conn.prepareCall("CALL edictActivUs(?, ?, ?, ?, ?, ?, ?, ?)");
                sql.setDate(8, fecAct);
            }
                //</editor-fold>

                sql.setEscapeProcessing(true);
                sql.setQueryTimeout(90);
                    sql.setInt(1, activ.getIdAct());
                    sql.setInt(2, activ.getIdTipo());
                    sql.setString(3, activ.getNombre());
                    sql.setString(4, activ.getDescripcion());
                    sql.setString(5, activ.getEmail());
                    sql.setString(6, activ.getTelefono());
                    sql.setString(7, activ.getWeb());
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
            return "editado";
        }else{
            return "error";
        } 
    //</editor-fold>    
    }

    @Override
    public List<Actividad> getActivitiesByYearMonth(int year, int month) throws PersistenceException, ClassNotFoundException {
    Class.forName("com.mysql.jdbc.Driver");
    //<editor-fold defaultstate="collapsed" desc="Atributs">
        Connection conn = connect();
        List<Actividad> activities = new ArrayList<>();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            sql = conn.prepareCall("CALL getActivitiesByMonthYear(?, ?)");
            sql.setInt(1, year);
            sql.setInt(2, month);
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
    public int haveActYearMonth(int year, int month) throws PersistenceException, ClassNotFoundException {
    Class.forName("com.mysql.jdbc.Driver");
    //<editor-fold defaultstate="collapsed" desc="Atributs">
        Connection conn = connect();
        int count = 0;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            sql = conn.prepareCall("CALL getActivitiesByMonthYear(?, ?)");
            sql.setInt(1, year);
            sql.setInt(2, month);
            reader = sql.executeQuery();
            
            while(reader.next()){
                count = reader.getInt("cuenta");
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
    return count;  
    }

    @Override
    public int getTotalActividades() throws PersistenceException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        int actividades = 0;
        try{
            Connection conn = connect();
            sql = conn.prepareCall("CALL getTotalActividades()");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);

            try{
                reader = sql.executeQuery();
                if(reader.next()){
                    actividades = reader.getInt("cuenta");
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
        
        return actividades;
    }

    @Override
    public List<Actividad> getActividades() throws PersistenceException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        //<editor-fold defaultstate="collapsed" desc="Atributs">
            Connection conn = connect();
            List<Actividad> actividades = new ArrayList<>();
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
            try{
                sql = conn.prepareCall("CALL getActividades()");
                reader = sql.executeQuery();

                while(reader.next()){
                    actividades.add(JDBCUtils.getActividad(reader));
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
        return actividades; 
    }

    @Override
    public String editarActividadAdm(Actividad activ) throws PersistenceException, ClassNotFoundException {
    //<editor-fold defaultstate="collapsed" desc="Atributos">
        Class.forName("com.mysql.jdbc.Driver");
        Date fecAct = null;
        int i=0;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            Connection conn = connect();
            //<editor-fold defaultstate="collapsed" desc="Fecha de la Actividad">
            if(activ.getFecha() == null || activ.getFecha().isEmpty() || activ.getFecha().equals("")){
                sql = conn.prepareCall("CALL editActivAdmWoFec(?, ?, ?, ?, ?, ?, ?, ?, ?)");
            }else{
                String fec = activ.getFecha();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = format.parse(fec);
                fecAct = new Date(date.getTime());
                sql = conn.prepareCall("CALL editActivAdm(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                sql.setDate(10, fecAct);
            }
                //</editor-fold>

                sql.setEscapeProcessing(true);
                sql.setQueryTimeout(90);
                    sql.setInt(1, activ.getIdAct());
                    sql.setInt(2, activ.getIdTipo());
                    sql.setString(3, activ.getNombre());
                    sql.setString(4, activ.getDescripcion());
                    sql.setString(5, activ.getEmail());
                    sql.setString(6, activ.getTelefono());
                    sql.setString(7, activ.getWeb());
                    sql.setBoolean(8, activ.getActivo());
                    sql.setBoolean(9, activ.getBaja());
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
            return "editado";
        }else{
            return "error";
        } 
    //</editor-fold>
    }

    @Override
    public void uploadResultado(FileUploadEvent e, Actividad activ) throws IOException {
        UploadedFile uploadedResultado=e.getFile();
 
        String filePath="/uploads/actividades/resultados/";
        String fileNameActividad = Integer.toString(activ.getIdAct());
        byte[] bytes=null;
 
            if (null!=uploadedResultado) {
                    bytes = uploadedResultado.getContents();
                    String filename = FilenameUtils.getName(uploadedResultado.getFileName());
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath+fileNameActividad)));
                    stream.write(bytes);
                    stream.close();
            }

            FacesContext.getCurrentInstance().addMessage("messages",new FacesMessage(FacesMessage.SEVERITY_INFO,"Your Photo (File Name "+ uploadedResultado.getFileName()+ " with size "+ uploadedResultado.getSize()+ ")  Uploaded Successfully", ""));
    }
    

}
