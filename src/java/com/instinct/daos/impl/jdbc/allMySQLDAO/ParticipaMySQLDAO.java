/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.impl.jdbc.allMySQLDAO;

import static com.instinct.daos.connection.connectMySQL2.connect;
import static com.instinct.daos.connection.connectMySQL2.connectionClose;
import com.instinct.daos.contracts.ParticipaDAO;
import com.instinct.daos.impl.jdbcUtils.JDBCUtils;
import com.instinct.exception.PersistenceException.PersistenceException;
import com.instinct.web.objects.Actividad;
import com.instinct.web.objects.Participante;
import com.instinct.web.objects.Usuario;
import static java.lang.Boolean.FALSE;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author daw2017
 */
@ManagedBean(name="ParticipaMySQLDAO")
@SessionScoped
public class ParticipaMySQLDAO implements ParticipaDAO {
    
    CallableStatement sql = null;
    CallableStatement sql2 = null;
    ResultSet reader = null;

    @Override
    public String callParticipa(Actividad activity, Usuario user) throws PersistenceException, ClassNotFoundException {
        String comprueba = null;
        int verificado = 0;
        int verifecha = 0;
        //<editor-fold defaultstate="collapsed" desc="Fecha actual">
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            java.util.Date dt = new java.util.Date();
            String fechaActual = dateFormat.format(dt);
            String fac[] = fechaActual.split("/");
            int actyear = Integer.parseInt(fac[0]);
            int actmonth = Integer.parseInt(fac[1]);
            int actday = Integer.parseInt(fac[2]);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Fecha Actividad">
            String fecAct[] = activity.getFecha().split("-");
            int DiaActiv  = Integer.parseInt(fecAct[0]);
            int MesActiv  = Integer.parseInt(fecAct[1]);
            int YearActiv = Integer.parseInt(fecAct[2]);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Comparar Fechas">
            if(actyear < YearActiv){
                verifecha = 0;
            }else if(actyear == YearActiv){
                if(actmonth < MesActiv){
                   verifecha = 0;
                }else if(actmonth == MesActiv){
                    if(actday < DiaActiv-1){
                        verifecha = 0;
                    }else if(actday >= DiaActiv-1){
                        verifecha = 1;
                    }
                }else if(actmonth > MesActiv){
                    verifecha = 1;
                }
            }else if(actyear > YearActiv){
                verifecha = 1;
            }
        //</editor-fold>
        
        if(verifecha == 0){
            if(user != null){
                if(activity.getIdUser() != user.getIdUser()){
                    verificado = verificaInscripcion(activity, user);
                    if(verificado == 0){
                        comprueba = insertaRelacion(activity, user);
                        if(comprueba.equals("insertado")){
                            FacesMessage message = new FacesMessage("Has sido inscrito correctamente.");
                            FacesContext.getCurrentInstance().addMessage("inscrito", message);
                        }else{
                            FacesMessage message = new FacesMessage("No se ha podido tramitar la inscripcion.");
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        }
                    }else{
                        FacesMessage message = new FacesMessage("Ya estas inscrito en esta actividad.");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                }else{
                    FacesMessage message = new FacesMessage("No puedes apuntarte a una actividad tuya.");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }else{
                FacesMessage message = new FacesMessage("Necesitas estar conectado para poder hacer la inscripcion.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }else{
            FacesMessage message = new FacesMessage("Esta actividad ya se ha celebrado, no puedes inscribirte.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        
        return null;
    }

    @Override
    public String insertaRelacion(Actividad activity, Usuario user) throws PersistenceException, ClassNotFoundException {
    //<editor-fold defaultstate="collapsed" desc="Atributos">
        Class.forName("com.mysql.jdbc.Driver");
                
        int i=0;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            Connection conn = connect();
            //<editor-fold defaultstate="collapsed" desc="Fecha del Registro">
            Timestamp fecInscripcion = new Timestamp(System.currentTimeMillis());
            //</editor-fold>
            
            sql = conn.prepareCall("CALL insertParticipa(?, ?, ?)");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
                sql.setInt(1, user.getIdUser());
                sql.setInt(2, activity.getIdAct());
                sql.setTimestamp(3, fecInscripcion);
                
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
            return "insertado";
        }else{
            return "error";
        } 
    //</editor-fold>
    }

    @Override
    public int verificaInscripcion(Actividad activity, Usuario user) throws PersistenceException, ClassNotFoundException {
    Class.forName("com.mysql.jdbc.Driver");
        int comprobacion = 0;
        int idUser = 0;
        try{
            Connection conn = connect();
            sql = conn.prepareCall("CALL verificaInscripcion(?, ?)");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
            sql.setInt(1, user.getIdUser());
            sql.setInt(2, activity.getIdAct());
            
            try{
                reader = sql.executeQuery();
                if(reader.next()){
                    idUser = reader.getInt("idUsuario");
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

            if(idUser != 0){
                comprobacion++;         
            }else{
                comprobacion = 0;
            }
        
        
        return comprobacion;
    }

    @Override
    public List<Usuario> getParticipantes(Actividad activity) throws PersistenceException, ClassNotFoundException {
    Class.forName("com.mysql.jdbc.Driver");
    //<editor-fold defaultstate="collapsed" desc="Atributs">
        Connection conn = connect();
        int idUser = 0;
        List<Participante> partId = new ArrayList<>();
        List<Usuario> participantes = new ArrayList<>();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            sql = conn.prepareCall("CALL getParticipantes(?)");
            sql.setInt(1, activity.getIdAct());
            reader = sql.executeQuery();
            
            while(reader.next()){
                partId.add(JDBCUtils.getParticipante(reader));
            }
            
            for(int j=0; j < partId.size(); j++){
                sql2 = conn.prepareCall("CALL getUserById(?)");
                sql2.setEscapeProcessing(true);
                sql2.setQueryTimeout(90);
                sql2.setInt(1, partId.get(j).getIdUsuario());
                
                reader = sql2.executeQuery();
                while(reader.next()){
                    participantes.add(JDBCUtils.getUsuario(reader));
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
    return participantes;    
    }

    @Override
    public List<Actividad> getInscripcionesUser(Usuario user) throws PersistenceException, ClassNotFoundException {
    Class.forName("com.mysql.jdbc.Driver");
    //<editor-fold defaultstate="collapsed" desc="Atributs">
        Connection conn = connect();
        int idUser = 0;
        List<Participante> partId = new ArrayList<>();
        List<Actividad> inscripciones = new ArrayList<>();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            sql = conn.prepareCall("CALL getMisInscripciones(?)");
            sql.setInt(1, user.getIdUser());
            reader = sql.executeQuery();
            
            while(reader.next()){
                partId.add(JDBCUtils.getParticipante(reader));
            }
            
            for(int j=0; j < partId.size(); j++){
                sql2 = conn.prepareCall("CALL getActividadById(?)");
                sql2.setEscapeProcessing(true);
                sql2.setQueryTimeout(90);
                sql2.setInt(1, partId.get(j).getIdActividad());
                
                reader = sql2.executeQuery();
                while(reader.next()){
                    inscripciones.add(JDBCUtils.getActividad(reader));
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
    return inscripciones;   
    }
    
}
