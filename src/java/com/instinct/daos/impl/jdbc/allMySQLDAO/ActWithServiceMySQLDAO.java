/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.impl.jdbc.allMySQLDAO;

import static com.instinct.daos.connection.connectMySQL2.connect;
import static com.instinct.daos.connection.connectMySQL2.connectionClose;
import com.instinct.daos.contracts.ActWithServiceDAO;
import com.instinct.daos.impl.jdbcUtils.JDBCUtils;
import com.instinct.exception.PersistenceException.PersistenceException;
import com.instinct.web.objects.ActServ;
import com.instinct.web.objects.Actividad;
import com.instinct.web.objects.Servicio;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * ActWithServiceMySQLDAO utilitza les funcions que li proporciona
 * la clase actwithServiceDAO, per a poder utilitzarles directament en la pagina web.
 * @author Jordi Lascorz
 * @since 18/05/2017
 * @version 1.0
 */
@ManagedBean(name="ActServiceMySQLDAO")
public class ActWithServiceMySQLDAO implements ActWithServiceDAO {
    
    CallableStatement sql = null;
    CallableStatement sql2 = null;
    ResultSet reader = null;
    
    /**
     * CallServicioActividad fa un insert dels serveis de les activitats en la relació.
     * @param activity
     * @param serviciosSeleccionados
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public void callServicioActividad(Actividad activity, List<String> serviciosSeleccionados) throws PersistenceException, ClassNotFoundException {
    //<editor-fold defaultstate="collapsed" desc="Atributos">
        Class.forName("com.mysql.jdbc.Driver");
                
        int i=0;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
    if(activity.getIdAct() != 0){
        try{
            Connection conn = connect();
            
            //Fa un insert a la base de dades per cada servei seleccionat
            for(int j=0; j < serviciosSeleccionados.size(); j++){
                int idServicio = Integer.parseInt(serviciosSeleccionados.get(j));
                sql = conn.prepareCall("CALL insertServicioActividad(?, ?)");
                sql.setEscapeProcessing(true);
                sql.setQueryTimeout(90);
                    sql.setInt(1, activity.getIdAct());
                    sql.setInt(2, idServicio);

                i = sql.executeUpdate();
            }
            
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
        }
        
        if(i<=0){
            //Mostra un error en cas de que no es pugui afegir serveis a la relació.
            FacesMessage message = new FacesMessage("No se ha podido añadir los servicios a la actividad.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }else{
        //Mostra un error en cas de que al crear l'activitat no hagi funcionat.
        FacesMessage message = new FacesMessage("Error: Ha habido un problema al crear la actividad.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    //</editor-fold>  
    }

    /**
     * Funció que crida a la funció d'eliminar serveis d'una activitat en la relació, amb opcions
     * de control d'errors
     * @param activity
     * @param serviciosSeleccionados
     * @param error
     * @return null
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public String callEditServAct(Actividad activity, List<String> serviciosSeleccionados, int error) throws PersistenceException, ClassNotFoundException {
        //Si al editar l'activitat no dona error entra en el if.
        if(error == 0){
            eliminarRelacion(activity);
            callServicioActividad(activity, serviciosSeleccionados);
        }else{
            //Mostra un error si no ha pogut editar l'activitat.
            FacesMessage message = new FacesMessage("Error: Ha habido un problema al editar la actividad.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        
        return null;
    }

    /**
     * Funció que elimina els serveis d'una activitat en la relació.
     * @param activity
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public void eliminarRelacion(Actividad activity) throws PersistenceException, ClassNotFoundException {
    //<editor-fold defaultstate="collapsed" desc="Atributos">
        Class.forName("com.mysql.jdbc.Driver");
                
        int i=0;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            Connection conn = connect();
            //Crida al procedure que elimina els serveis d'una activitat
            sql = conn.prepareCall("CALL deleteActWithService(?)");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
            sql.setInt(1, activity.getIdAct());


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
        }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Return a la pagina">
        if(i<=0){
            //Mostra un error en cas de que no es pugui eliminar els serveis de l'activitat.
            FacesMessage message = new FacesMessage("No se ha podido eliminar los servicios de la actividad.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    //</editor-fold>   
    }

    /**
     * Funció que agafa els serveis d'una activitat per la seva id en la relació.
     * @param activity
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public void getServiciosByIdAct(Actividad activity) throws PersistenceException, ClassNotFoundException {
        //<editor-fold defaultstate="collapsed" desc="Atributs">
        Class.forName("com.mysql.jdbc.Driver");
        int i = 0;
        Connection conn = connect();
        List<ActServ> actServ = new ArrayList<>();
        List<Servicio> servicios = new ArrayList<>();
        List<String> selected = new ArrayList<>();
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        
        try{
            //Selecciona els serveis d'una activitat per la id de l'activitat.
            sql = conn.prepareCall("CALL getActServiceById(?)");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
            sql.setInt(1, activity.getIdAct());
            reader = sql.executeQuery();
            
            while(reader.next()){
                //Per cada servei seleccionat l'afegeix a la arraylist de la relació.
                actServ.add(JDBCUtils.getActServ(reader));
            }
            
            //Bucle a partir del tamany de l'arraylist per seleccionar les dades
            //de cada servei per la id del servei que s'ha agafat anteriorment.
            for(int j=0; j < actServ.size(); j++){
                sql2 = conn.prepareCall("CALL getServicioById(?)");
                sql2.setEscapeProcessing(true);
                sql2.setQueryTimeout(90);
                sql2.setInt(1, actServ.get(j).getIdServicio());
                
                reader = sql2.executeQuery();
                while(reader.next()){
                    //S'afegeix totes les dades a una arraylist de serveis.
                    servicios.add(JDBCUtils.getServicio(reader));
                    //S'afegeix les id's de cada servei en una arraylist de strings.
                    selected.add(Integer.toString(servicios.get(j).getIdServicio()));
                }
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
        //<editor-fold defaultstate="collapsed" desc="Sessió">
        //Crea una sessio amb l'arraylist dels serveis seleccionats
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
        HttpSession httpSession = request.getSession(false);
        httpSession.setAttribute("servKey", selected);
        //</editor-fold>

    }

    /**
     * Funció que mostra tots els serveis que té una activitat, agafa els serveis
     * per la id de l'activitat, en aquest cas agafa els noms dels serveis, per mostrarles
     * en l'activitat.
     * @param activity
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public void showServicesinAct(Actividad activity) throws PersistenceException, ClassNotFoundException {
        //<editor-fold defaultstate="collapsed" desc="Atributos">
        Class.forName("com.mysql.jdbc.Driver");
        int i = 0;
        Connection conn = connect();
        List<ActServ> actServ = new ArrayList<>();
        List<Servicio> servicios = new ArrayList<>();
        List<String> selected = new ArrayList<>();
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            //Selecciona els serveis d'una activitat per la id de l'activitat.
            sql = conn.prepareCall("CALL getActServiceById(?)");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
            sql.setInt(1, activity.getIdAct());
            reader = sql.executeQuery();
            
            while(reader.next()){
                //Per cada servei seleccionat l'afegeix a la arraylist de la relació.
                actServ.add(JDBCUtils.getActServ(reader));
            }
            
            //Bucle a partir del tamany de l'arraylist per seleccionar les dades
            //de cada servei per la id del servei que s'ha agafat anteriorment.
            for(int j=0; j < actServ.size(); j++){
                sql2 = conn.prepareCall("CALL getServicioById(?)");
                sql2.setEscapeProcessing(true);
                sql2.setQueryTimeout(90);
                sql2.setInt(1, actServ.get(j).getIdServicio());
                
                reader = sql2.executeQuery();
                while(reader.next()){
                    //S'afegeix totes les dades a una arraylist de serveis.
                    servicios.add(JDBCUtils.getServicio(reader));
                    //S'afegeix el nom de cada servei en una arraylist de strings.
                    selected.add(servicios.get(j).getNombre());
                }
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
                if(sql2 !=null){
                 sql2.close();
                }
                connectionClose();
            }catch(SQLException e){
                throw new PersistenceException(e.getErrorCode());
            }
         }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Sessió">
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
        HttpSession httpSession = request.getSession(false);
        httpSession.setAttribute("servKey", selected);
        //</editor-fold>
        
     }
}
