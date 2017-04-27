/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.impl.jdbc.UsuariDAO;

import com.instinct.daos.impl.jdbcUtils.JDBCUtils;
import static com.instinct.daos.connection.connectMySQL.closeConnections;
import static com.instinct.daos.connection.connectMySQL.connectToServer;
import static com.instinct.daos.connection.connectMySQL2.connect;
import static com.instinct.daos.connection.connectMySQL2.connectionClose;
import com.instinct.daos.contracts.UsuarioDAO;
import com.instinct.exception.PersistenceException.PersistenceException;
import com.instinct.web.objects.Usuario;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import static java.lang.Boolean.FALSE;
import java.sql.Date;
import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author daw2017
 */
@ManagedBean(name="UsuarioMySQLDAO")
@SessionScoped

public class UsuarioMySQLDAO implements UsuarioDAO {
    
    CallableStatement sql = null;
    //PreparedStatement sql = null;
    ResultSet reader = null;
    @Override
    public String insertUsuario(Usuario user) throws PersistenceException ,ClassNotFoundException{
    //<editor-fold defaultstate="collapsed" desc="Atributos">
        Class.forName("com.mysql.jdbc.Driver");
                
        int i=0;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            Connection conn = connect();
            //<editor-fold defaultstate="collapsed" desc="Fecha de Nacimiento">
            String fnSN = user.getFecNac();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = format.parse(fnSN);
            Date fecNac = new Date(date.getTime());
            //</editor-fold>
            
            //<editor-fold defaultstate="collapsed" desc="Fecha del Registro">
            Timestamp fecRegistro = new Timestamp(System.currentTimeMillis());
            //</editor-fold>
            sql = conn.prepareCall("CALL insertUser(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
                sql.setString(1, user.getNombre());
                sql.setString(2, user.getApellidos());
                sql.setString(3, user.getEmail());
                sql.setString(4, user.getPassword());
                sql.setString(5, user.getNif());
                sql.setString(6, user.getGenero());
                sql.setDate(7, fecNac);
                sql.setBoolean(8, FALSE);
                sql.setBoolean(9, FALSE);
                sql.setInt(10, user.getDiffus());
                sql.setTimestamp(11, fecRegistro);
                
          i = sql.executeUpdate();
            
        }catch(SQLException e){
            throw new PersistenceException(e.getErrorCode());
        } catch (ParseException ex) {
            Logger.getLogger(UsuarioMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
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
            return "index";
        }else{
            return "register";
        }
    //</editor-fold>
    }

    @Override
    public List<Usuario> getUsuarios() throws PersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String login(Usuario user) throws PersistenceException, ClassNotFoundException{
        user = getUser(user);
        user = setUserActivo(user);
        if(user.getActivo() == true){
            guardaSession(user);
            //recogeSession();
        }
        return "index.xhtml?faces-redirect=true";
    }
    
    @Override
    public Usuario getUser(Usuario user) throws PersistenceException, ClassNotFoundException{
        Class.forName("com.mysql.jdbc.Driver");
        try{
            Connection conn = connect();
            sql = conn.prepareCall("CALL login(?, ?)");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
                sql.setString(1, user.getEmail());
                sql.setString(2, user.getPassword());
                
            
            try{
                reader = sql.executeQuery();
                if(reader.next()){
                    user = JDBCUtils.getUsuario(reader);
                    
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
        
        return user;
    }
    
    @Override
    public Usuario setUserActivo(Usuario user) throws PersistenceException, ClassNotFoundException{
        Class.forName("com.mysql.jdbc.Driver");
        int comprovar = 0;
        int nVisitas = 0;
        String fUlt = null;
        try{
            
            //<editor-fold defaultstate="collapsed" desc="Fecha del Registro">
            Timestamp tUlt = new Timestamp(System.currentTimeMillis());
            fUlt = tUlt.toString();
            //</editor-fold>
            
            nVisitas = user.getNumVisitas();
            nVisitas++;
            
            Connection conn = connect();
            sql = conn.prepareCall("CALL setUsuarioActivo(?, ?, ?)");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
                sql.setInt(1, user.getIdUser());
                sql.setTimestamp(2, tUlt);
                sql.setInt(3, nVisitas);
                
            comprovar = sql.executeUpdate();
            
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
            
            if(comprovar > 0){
                user.setNumVisitas(nVisitas);
                user.setFecUltVis(fUlt);
                user.setActivo(true);
            }
            
            //Llama a la funció per a tancar la conexio
            connectionClose();
           //closeConnections();
        }
        
        return user;
    }

    @Override
    public void guardaSession(Usuario user) {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
        HttpSession httpSession = request.getSession(false);
        httpSession.setAttribute("userKey", user);
    }

    @Override
    public Usuario recogeSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
        HttpSession httpSession = request.getSession(false);
        Usuario user = (Usuario) httpSession.getAttribute("JSESSIONID");
        
        return user;
    }
    
    
}
