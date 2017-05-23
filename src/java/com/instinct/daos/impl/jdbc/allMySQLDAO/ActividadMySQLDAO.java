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
import java.io.FileNotFoundException;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
 * ActividadMySQLDAO classe per englobar les funcions de activitats.
 * @author Jordi Lascorz
 * @since 12/05/2017
 * @version 1.0
 */
@ManagedBean(name="ActividadMySQLDAO")
@SessionScoped
public class ActividadMySQLDAO implements ActividadDAO{

    CallableStatement sql = null;
    ResultSet reader = null;
    
    /**
     * Funció per a juntar les funcions de verificar si l'activitat existeix
     * en el mateix any, a més de la d'insertar l'activitat
     * @param activ
     * @param idUser
     * @return null
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public String callCrear(Actividad activ, int idUser) throws PersistenceException, ClassNotFoundException {
        String devuelve = null;
        String error = "error";
        //verifica si una activitat amb el mateix nom existeix en el mateix any.
        int comprueba = getActivityByNameYear(activ);
        if(comprueba == 0){
            //Si no existeix ninguna activitat en aquell any l'inserta a la base de dades.
            devuelve = insertarActividad(activ, idUser);
            activ = getActividadByName(activ);
            if(!devuelve.equals("insertado")){
                //Mostra un error en cas de que no es pugui insertar l'activitat.
                FacesMessage message = new FacesMessage("No se ha podido insertar la actividad.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }            
        }else{
            //Mostra un error en cas de que ya existeixi un activitat amb el mateix nom i el mateix any.
            FacesMessage message = new FacesMessage("Esta actividad ya ha sido creada anteriormente.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        
        return null;
    }

    /**
     * Funció que verifica si una activitat amb el mateix nom existeix en el mateix any.
     * @param activ
     * @return int
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public int getActivityByNameYear(Actividad activ) throws PersistenceException, ClassNotFoundException {
        //<editor-fold defaultstate="collapsed" desc="Atributos">
        Class.forName("com.mysql.jdbc.Driver");
        int comprobacion = 0;
        int idAct = 0;
        String nombre = null;
        java.util.Date dfn = null;
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            Connection conn = connect();
            //Crida a un procedure per comprovar l'activitat.
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
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Return de la funció">
        //Si la id retornada no es 0
        if(idAct != 0){
            //Si la id coincideix amb la id de la activitat
                if(idAct == activ.getIdAct()){
                    //Si el nom es igual al nom de la activitat
                    if(nombre.equals(activ.getNombre())){
                        comprobacion = 0;
                        //En cas de que la data de l'activitat sigui null, la posa a partir de la
                        //base de dades (Nomes per a la funció d'editar).
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
        //</editor-fold>       
    }

    /**
     * Funció que inserta l'activitat corresponent a la base de dades
     * @param activ
     * @param idUser
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
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
            //Adapta la data agafada per una data adaptada a la base de dades.
            String fec = activ.getFecha();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = format.parse(fec);
            Date fecAct = new Date(date.getTime());
            //</editor-fold>
            //Procedure que fa un insert de les dades pasades
            sql = conn.prepareCall("CALL insertActividad(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
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

    /**
     * Funció que selecciona una llista d'activitats per la id de l'usuari.
     * @param user
     * @return Lista de Actividades
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public List<Actividad> getActividadByUser(Usuario user) throws PersistenceException, ClassNotFoundException {
    //<editor-fold defaultstate="collapsed" desc="Atributs">
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = connect();
        List<Actividad> activities = new ArrayList<Actividad>();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            //Agafa les activitats per la id de l'usuari
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
        
        return activities; 
    //</editor-fold>      
    }

    /**
     * Funció que comprova si l'usuari conté alguna activitat.
     * @param user
     * @return int
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public int mirarSiTieneActividad(Usuario user) throws PersistenceException, ClassNotFoundException {
    //<editor-fold defaultstate="collapsed" desc="Atributs">
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = connect();
        int cuenta = 0;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            //Comprova si l'usuari te alguna activitat
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
        return cuenta; 
    //</editor-fold>  
    }

    /**
     * Funció que comprova si existeix una activitat amb el mateix nom, independentment
     * de la data.
     * @param activ
     * @return Actividad
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public Actividad getActividadByName(Actividad activ) throws PersistenceException, ClassNotFoundException {
        //<editor-fold defaultstate="collapsed" desc="Atributs">
        Class.forName("com.mysql.jdbc.Driver");
        int comprobacion = 0;
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            Connection conn = connect();
            //Comprova si existeix alguna activitat per el nom
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
        }
        
        return activ;
        //</editor-fold>
    }

    /**
     * Funció que dona de baixa una activitat.
     * @param activ
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public void bajaActividad(Actividad activ) throws PersistenceException, ClassNotFoundException {
        //<editor-fold defaultstate="collapsed" desc="Atributs">
        Class.forName("com.mysql.jdbc.Driver");
        int i = 0;
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            Connection conn = connect();
            //Fa un update de l'activitat i la dona de baixa.
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
            //Redirecciona a la pagina de les meves activitats
            FacesContext.getCurrentInstance().getExternalContext().redirect("mis_actividades.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ActividadMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        FacesContext.getCurrentInstance().responseComplete();
        //</editor-fold>
        
    }

    /**
     * Funció que guarda una sessió d'una activitat, depenent de la pagina que se li passi.
     * @param activ
     * @param pagina
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public void guardarSession(Actividad activ, String pagina) throws PersistenceException, ClassNotFoundException {
        //<editor-fold defaultstate="collapsed" desc="Guarda Sessio">
        //Si la pagina es la del panell de administració retorna una sessio per a administrador de activitats
        if(pagina.equals("backoffice")){
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            httpSession.setAttribute("activAdm", activ);
        
            try {
                //Redirecciona a la pagina de edicio de administració.
                FacesContext.getCurrentInstance().getExternalContext().redirect("edit_backoffice.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ActividadMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }else{
            //Si la pagina no es de administració retorna una sessió d'activitat normal
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            httpSession.setAttribute("activKey", activ);

            //Si la pagina es la de editar activitat d'un organitzador redirecciona a la pagina d'editar activitat.
            if(pagina.equals("editar")){
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("editar_actividad.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(ActividadMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else if(pagina.equals("visualizar")){
                //Si la pagina es la de veure l'activitat redirecciona a la pagina de l'activitat.
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("actividad.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(ActividadMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
                //Si la pagina es per subir un fitxer redirecciona a la pagina de subir resultat.
            }else if(pagina.equals("subirFichero")){
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("subir_resultado.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(ActividadMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
                //Si la pagina es per subir una imatge redirecciona a la pagina de subir imatge.
            }else if(pagina.equals("subirImagen")){
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("subir_imagen.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(ActividadMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        FacesContext.getCurrentInstance().responseComplete();
        //</editor-fold>
    }
    
    /**
     * Funció que borra una sessió d'una activitat.
     */
    @Override
    public void borrarSession() {
        //Borra la sessió
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }
    
    /**
     * Funcio que conté altres funcións per fer verificacions a l'hora d'editar una activitat.
     * @param activ
     * @param pagina
     * @param error
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public String callEditar(Actividad activ, String pagina, int error) throws PersistenceException, ClassNotFoundException {
        String devuelve = null;
        
        //<editor-fold defaultstate="collapsed" desc="Edicio de l'activitat">
        //Comprova si existeix una activitat amb el mateix nom i el mateix any.
        int comprueba = getActivityByNameYear(activ);
        if(comprueba == 0){
            
            if(pagina.equals("editActUs")){
                //Si la pagina es la editar una activitat com organitzador fa el edit corresponent.
                devuelve = editarActividad(activ);
            }else if(pagina.equals("editActAdm")){
                //Si la pagina es la de editar una activitat com organitzador fa l'altre edit
                devuelve = editarActividadAdm(activ);
            }
            if(!devuelve.equals("editado")){
                //En cas de que no funcioni surt un erro
                FacesMessage message = new FacesMessage("No se ha podido editar la actividad.");
                FacesContext.getCurrentInstance().addMessage(null, message);
                error = 1;
            }else{
                //return activ;
            }
            
        }else{
            //En cas de que ya existeixi un mostra un error
            FacesMessage message = new FacesMessage("Esta actividad ya ha sido creada anteriormente.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            error = 1;
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Crea una sessió per l'error">
          FacesContext context = FacesContext.getCurrentInstance();
          HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
          HttpSession httpSession = request.getSession(false);
          httpSession.setAttribute("errorTemp", error);  
        //</editor-fold>
        return null;
    }

    /**
     * Funció que edita una activitat amb les dades que s'han passat.
     * @param activ
     * @return
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
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
            //Crida a un procedure o un altre depenent de si la fecha esta buida o no.
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

    /**
     * Funció que agafa les activitat any i més.
     * @param year
     * @param month
     * @return Lista de actividades
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public List<Actividad> getActivitiesByYearMonth(int year, int month) throws PersistenceException, ClassNotFoundException {
    //<editor-fold defaultstate="collapsed" desc="Atributs">
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = connect();
        List<Actividad> activities = new ArrayList<>();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            //Crida un procedure que agafa les activitats per el mes i l'any.
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
        return activities;
    //</editor-fold>
         
    }

    /**
     * Comprova si existeix alguna activitat per l'any i el més.
     * @param year
     * @param month
     * @return int
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public int haveActYearMonth(int year, int month) throws PersistenceException, ClassNotFoundException {
    //<editor-fold defaultstate="collapsed" desc="Atributs">
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = connect();
        int count = 0;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            //Agafa el numero d'activitats per el mes i l'any.
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
        return count; 
    //</editor-fold>
    }

    /**
     * Funció que agafa el numero d'activitats que s'han creat.
     * @return int
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public int getTotalActividades() throws PersistenceException, ClassNotFoundException {
        //<editor-fold defaultstate="collapsed" desc="Atributs">
        Class.forName("com.mysql.jdbc.Driver");
        int actividades = 0;
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            Connection conn = connect();
            //Agafa el numero d'activitats creades
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
        }
        
        return actividades;
        //</editor-fold>
    }

    /**
     * Funció que agafa una llista de totes les activitats creades.
     * @return Lista de actividades
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public List<Actividad> getActividades() throws PersistenceException, ClassNotFoundException {
        //<editor-fold defaultstate="collapsed" desc="Atributs">
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = connect();
            List<Actividad> actividades = new ArrayList<>();
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
            try{
                //Crida un procedure per agafar totes les activitats.
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
            return actividades; 
        //</editor-fold>  
    }

    /**
     * Funció que edita una activitat només si l'usuari es administrador.
     * @param activ
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
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
                //Crida un procedure o un altre depenent de si la data esta buida o no.
                if(activ.getFecha() == null || activ.getFecha().isEmpty() || activ.getFecha().equals("")){
                    sql = conn.prepareCall("CALL editActivAdmWoFec(?, ?, ?, ?, ?, ?, ?, ?, ?)");
                }else{
                    String fec = activ.getFecha();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date date = format.parse(fec);
                    fecAct = new Date(date.getTime());
                    sql = conn.prepareCall("CALL editActivAdm(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
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

    /**
     * Funció que puja la ruta de la carpeta i el fitxer del resultat d'una activitat.
     * @param resultado
     * @param activ
     * @throws PersistenceException
     * @throws ClassNotFoundException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    @Override
    public void uploadResultado(UploadedFile resultado, Actividad activ) throws PersistenceException, ClassNotFoundException, FileNotFoundException, IOException{
        
        //<editor-fold defaultstate="collapsed" desc="Atributs">
        Class.forName("com.mysql.jdbc.Driver");
        int i = 0;
        int comprueba = 0;
        UploadedFile uploadedResultado = resultado;
        String filePath = "../applications/InstinctWeb/resources/uploads/actividades/resultados/";
        String filePathToUpload = "/resources/uploads/actividades/resultados/";
        String filename = "actividad_" + activ.getIdAct() + "_resultado";
        String key = ".pdf";
        String fullPath = filePathToUpload + filename + key;
        byte[] bytes = null;
        
        String realName = uploadedResultado.getFileName();
        String pattern = "^.*\\.(pdf|PDF)$";
        
        //Verifica que el resultat pujat només pugui ser un pdf.
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(realName);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Puja el fitxer al Servidor">
        if(m.find()){
        if(null != uploadedResultado){
            bytes = uploadedResultado.getContents();
            //String filename = FilenameUtils.getName(uploadedResultado.getFileName());
            //Puja el fitxer al servidor
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath+filename+key)));
            stream.write(bytes);
            stream.close();
            comprueba = 1;
        }else{
            //Mostra un error al importar l'imatge.
            FacesMessage message = new FacesMessage("Ha habido un error importando los resultados.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        if(comprueba == 1){
            try{
                Connection conn = connect();
                //Procedure que puja la ruta de on s'ha pujat el resultat.
                    sql = conn.prepareCall("CALL uploadResultado(?, ?)");
                    sql.setEscapeProcessing(true);
                    sql.setQueryTimeout(90);
                        sql.setInt(1, activ.getIdAct());
                        sql.setString(2, fullPath);
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
        }else{
            FacesMessage message = new FacesMessage("Ha habido un error al subir los resultados.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Return a la pagina">
            if(i>0){
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("mis_actividades.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(ActividadMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                FacesMessage message = new FacesMessage("Ha habido un error al subir los resultados.");
                FacesContext.getCurrentInstance().addMessage(null, message);
        
            }
        }else{
            //Mostra un erro en cas de que l'arxiu no sigui .pdf
            FacesMessage message = new FacesMessage("El archivo tiene que ser .pdf");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        //</editor-fold>
        
    }

    /**
     * Funció que puja la ruta de la carpeta i el fitxer de la imatge d'una activitat.
     * @param imagen
     * @param activ
     * @throws PersistenceException
     * @throws ClassNotFoundException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    @Override
    public void uploadImagen(UploadedFile imagen, Actividad activ) throws PersistenceException, ClassNotFoundException, FileNotFoundException, IOException {
        //<editor-fold defaultstate="collapsed" desc="Atributs">
        Class.forName("com.mysql.jdbc.Driver");
        int i = 0;
        int comprueba = 0;
        UploadedFile uploadedImagen = imagen;
        String filePath = "../applications/InstinctWeb/resources/uploads/actividades/imagenes/";
        String filePathToUpload = "/resources/uploads/actividades/imagenes/";
        String filename = "actividad_" + activ.getIdAct() + "_imagen";
        String key = ".jpg";
        String fullPath = filePathToUpload + filename + key;
        byte[] bytes = null;
        
        String realName = uploadedImagen.getFileName();
        String pattern = "^.*\\.(jpg|JPG|png|PNG)$";
        
        //Verifica que el resultat pujat només pugui ser un jpg o png.
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(realName);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Puja la imatge al Servidor">
        if(m.find()){
        if(null!= uploadedImagen){
            bytes = uploadedImagen.getContents();
            //String filename = FilenameUtils.getName(uploadedResultado.getFileName());
            //Puja la imatge al servidor
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath+filename+key)));
            stream.write(bytes);
            stream.close();
            comprueba = 1;
        }else{
            FacesMessage message = new FacesMessage("Ha habido un error importando la imagen.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        if(comprueba == 1){
            try{
                Connection conn = connect();
                //Puja la ruta de la imatge al servidor
                    sql = conn.prepareCall("CALL uploadImage(?, ?)");
                    sql.setEscapeProcessing(true);
                    sql.setQueryTimeout(90);
                        sql.setInt(1, activ.getIdAct());
                        sql.setString(2, fullPath);
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
        }else{
            FacesMessage message = new FacesMessage("Ha habido un error al subir la imagen.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Return a la pagina">
            if(i>0){
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("mis_actividades.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(ActividadMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                FacesMessage message = new FacesMessage("Ha habido un error al subir la imagen.");
                FacesContext.getCurrentInstance().addMessage(null, message);
        
            }
        }else{
            FacesMessage message = new FacesMessage("El archivo tiene que ser .jpg o .png");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }    
        //</editor-fold>   
    }
    

}
