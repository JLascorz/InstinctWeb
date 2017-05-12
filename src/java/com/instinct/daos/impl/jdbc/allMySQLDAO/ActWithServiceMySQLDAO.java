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
 *
 * @author daw2017
 */
@ManagedBean(name="ActServiceMySQLDAO")
public class ActWithServiceMySQLDAO implements ActWithServiceDAO {
    
    CallableStatement sql = null;
    CallableStatement sql2 = null;
    ResultSet reader = null;
    
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
           //closeConnections();
        }
        
        if(i<=0){
            FacesMessage message = new FacesMessage("No se ha podido añadir los servicios a la actividad.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }else{
        FacesMessage message = new FacesMessage("Error: Ha habido un problema al crear la actividad.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    //</editor-fold>  
    }

    @Override
    public String callEditServAct(Actividad activity, List<String> serviciosSeleccionados) throws PersistenceException, ClassNotFoundException {
        if(activity.getIdAct() != 0){
            eliminarRelacion(activity);
            callServicioActividad(activity, serviciosSeleccionados);
        }else{
            FacesMessage message = new FacesMessage("Error: Ha habido un problema al editar la actividad.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        
        return null;
    }

    @Override
    public void eliminarRelacion(Actividad activity) throws PersistenceException, ClassNotFoundException {
    //<editor-fold defaultstate="collapsed" desc="Atributos">
        Class.forName("com.mysql.jdbc.Driver");
                
        int i=0;
       
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            Connection conn = connect();
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
           //closeConnections();
        }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Return a la pagina">
        if(i<=0){
            FacesMessage message = new FacesMessage("No se ha podido eliminar los servicios de la actividad.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    //</editor-fold>   
    }

    @Override
    public void getServiciosByIdAct(Actividad activity) throws PersistenceException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        int i = 0;
        Connection conn = connect();
        List<ActServ> actServ = new ArrayList<>();
        List<Servicio> servicios = new ArrayList<>();
        List<String> selected = new ArrayList<>();
        
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        
        try{
            sql = conn.prepareCall("CALL getActServiceById(?)");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
            sql.setInt(1, activity.getIdAct());
            reader = sql.executeQuery();
            
            while(reader.next()){
                actServ.add(JDBCUtils.getActServ(reader));
            }
            
            for(int j=0; j < actServ.size(); j++){
                sql2 = conn.prepareCall("CALL getServicioById(?)");
                sql2.setEscapeProcessing(true);
                sql2.setQueryTimeout(90);
                sql2.setInt(1, actServ.get(j).getIdServicio());
                
                reader = sql2.executeQuery();
                while(reader.next()){
                    servicios.add(JDBCUtils.getServicio(reader));
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
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
        HttpSession httpSession = request.getSession(false);
        httpSession.setAttribute("servKey", selected);

    }
}
