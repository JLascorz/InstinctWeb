/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.daos.impl.jdbc.allMySQLDAO;

import com.instinct.daos.impl.jdbcUtils.JDBCUtils;
import static com.instinct.daos.connection.connectMySQL.closeConnections;
import static com.instinct.daos.connection.connectMySQL.connectToServer;
import static com.instinct.daos.connection.connectMySQL2.connect;
import static com.instinct.daos.connection.connectMySQL2.connectionClose;
import com.instinct.daos.contracts.UsuarioDAO;
import com.instinct.exception.PersistenceException.PersistenceException;
import com.instinct.web.objects.Usuario;
import java.io.IOException;
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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
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
    public String register(Usuario user) throws PersistenceException, ClassNotFoundException {
        String devuelve = null;
        int comprueba = getUserByEmail(user, "registro");
        if(comprueba == 0){
            devuelve = insertUsuario(user);
            return devuelve;
        }else{
            FacesMessage message = new FacesMessage("Este usuario ya esta registrado.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            //return "Este usuario ya esta registrado";
        }
        return null;
    }

    @Override
    public int getUserByEmail(Usuario user, String caller) throws PersistenceException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        int comprobacion = 0;
        int idUs = 0;
        try{
            Connection conn = connect();
            sql = conn.prepareCall("CALL compruebaEmail(?)");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
                sql.setString(1, user.getEmail());
                
            try{
                reader = sql.executeQuery();
                if(reader.next()){
                    idUs = reader.getInt("idUser");
                    
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
        if(caller == "registro"){
            if(idUs != 0){
                comprobacion++;         
            }else{
                comprobacion = 0;
            }
        }else if(caller == "editar") {
            if(idUs == user.getIdUser()){
                comprobacion = 0;
            }else if(idUs == 0){
                comprobacion = 999;
            }else{
                comprobacion = 1;
            }
        }
        
        return comprobacion;
    }
    
    @Override
    public String insertUsuario(Usuario user) throws PersistenceException, ClassNotFoundException{
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
            return "login";
        }else{
            return "register";
        }
    //</editor-fold>
    }

    @Override
    public List<Usuario> getUsuarios() throws PersistenceException, ClassNotFoundException {
    Class.forName("com.mysql.jdbc.Driver");
    //<editor-fold defaultstate="collapsed" desc="Atributs">
        Connection conn = connect();
        List<Usuario> users = new ArrayList<>();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            sql = conn.prepareCall("CALL getUsuarios()");
            reader = sql.executeQuery();
            
            while(reader.next()){
                users.add(JDBCUtils.getUsuario(reader));
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
    return users;    
    }

    @Override
    public String login(Usuario user) throws PersistenceException, ClassNotFoundException{
        user = getUser(user);
        if(user.getNombre() != null){
            if(user.getBaja() != true){
                user = changeActivity(user, true);
                if(user.getActivo() == true){
                  guardaSession(user, "login");
                  //recogeSession();
                  return "index.xhtml?faces-redirect=true";
                }
            }else{
                FacesMessage message = new FacesMessage("Este usuario esta dado de baja.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }else{
            FacesMessage message = new FacesMessage("Email o contraseña incorrecto.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return null;
    }
    
    @Override
    public String logout(Usuario user) throws PersistenceException, ClassNotFoundException{
        user = changeActivity(user, false);
        if(user.getActivo() == false){
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
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
    public Usuario changeActivity(Usuario user, Boolean activity) throws PersistenceException, ClassNotFoundException{
        Class.forName("com.mysql.jdbc.Driver");
        int comprovar = 0;
        int nVisitas = 0;
        String fUlt = null;
        try{
            Timestamp tUlt = new Timestamp(System.currentTimeMillis());
            fUlt = tUlt.toString();
            
            if(activity == true){
                nVisitas = user.getNumVisitas();
                nVisitas++;
            }else{
                nVisitas = user.getNumVisitas();
            }
            
            Connection conn = connect();
            sql = conn.prepareCall("CALL setUsuarioActivo(?, ?, ?, ?)");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
                sql.setInt(1, user.getIdUser());
                sql.setBoolean(2, activity);
                sql.setTimestamp(3, tUlt);
                sql.setInt(4, nVisitas);
                
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
                user.setActivo(activity);
            }
            
            //Llama a la funció per a tancar la conexio
            connectionClose();
           //closeConnections();
        }
        
        return user;
    }

    @Override
    public void guardaSession(Usuario user, String pagina) {
       if(pagina.equals("login") || pagina.equals("modPerfil")){ 
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            httpSession.setAttribute("userKey", user);
       }else{
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
            HttpSession httpSession = request.getSession(false);
            httpSession.setAttribute("userAdm", user);
            
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("edit_backoffice.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(UsuarioMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            FacesContext.getCurrentInstance().responseComplete();
       }     
    }

    @Override
    public Usuario recogeSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
        HttpSession httpSession = request.getSession(false);
        Usuario user = (Usuario) httpSession.getAttribute("JSESSIONID");
        
        return user;
    }

    @Override
    public String callEditar(Usuario user, String pagina) throws PersistenceException, ClassNotFoundException {
        String devuelve = null;
        int comprueba = getUserByEmail(user, "editar");
        if(pagina.equals("editPerfil")){
            if(comprueba == 0 || comprueba == 999){
                devuelve = editarUsuario(user);
                if(devuelve == "perfil"){
                    Usuario temp = getUser(user);
                    guardaSession(temp, "modPerfil");
                }
                return devuelve;
            }else{
                FacesMessage message = new FacesMessage("Ya hay un usuario con este correo.");
                FacesContext.getCurrentInstance().addMessage(null, message);
                //return "Este usuario ya esta registrado";
            }
        }else if(pagina.equals("editAdmin")){
            if(comprueba == 0 || comprueba == 999){
                devuelve = editUserLikeAdmin(user);
                return devuelve;
            }else{
                FacesMessage message = new FacesMessage("Ya hay un usuario con este correo.");
                FacesContext.getCurrentInstance().addMessage(null, message);
                //return "Este usuario ya esta registrado";
            }
        }    
        return null;
    }
    
    @Override
    public String editarUsuario(Usuario user) throws PersistenceException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");

        int comprovar = 0;
        try{
            Connection conn = connect();
            //<editor-fold defaultstate="collapsed" desc="Fecha de Nacimiento">
            
            if(user.getFecNac() == null || user.getFecNac().isEmpty() || user.getFecNac().equals("")){
                sql = conn.prepareCall("CALL editUserUsWoFec(?, ?, ?, ?, ?, ?)");
            }else{
                String fnSN = user.getFecNac();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = format.parse(fnSN);
                Date fecNac = new Date(date.getTime());
                sql = conn.prepareCall("CALL editUserUs(?, ?, ?, ?, ?, ?, ?)");
                sql.setDate(7, fecNac);
            }
            //</editor-fold>
            
            
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
                sql.setInt(1, user.getIdUser());
                sql.setString(2, user.getNombre());
                sql.setString(3, user.getApellidos());
                sql.setString(4, user.getEmail());
                sql.setString(5, user.getNif());
                sql.setString(6, user.getGenero());
                
                
            comprovar = sql.executeUpdate();
            
        }catch(SQLException e){
            throw new PersistenceException(e.getErrorCode());
        } catch (ParseException ex) {
            Logger.getLogger(UsuarioMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
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
        if(comprovar > 0){
                return "perfil";
        }else{
                return "perfil_modificar";
        }
    }

    @Override
    public String callBaja(Usuario user) throws PersistenceException, ClassNotFoundException {
        user = changeBaja(user, true);
        if(user.getBaja() == true){
            logout(user);
        }
        return "index.xhtml?faces-redirect=true";
    }

    @Override
    public Usuario changeBaja(Usuario user, Boolean baja) throws PersistenceException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        int comprovar = 0;
        try{
            
            Connection conn = connect();
            sql = conn.prepareCall("CALL setUsuarioBaja(?, ?)");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
                sql.setInt(1, user.getIdUser());
                sql.setBoolean(2, baja);
                
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
                user.setBaja(baja);
            }
            
            //Llama a la funció per a tancar la conexio
            connectionClose();
           //closeConnections();
        }
        
        return user;    
    }

    @Override
    public int getTotalVisitas() throws PersistenceException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        int visitas = 0;
        try{
            Connection conn = connect();
            sql = conn.prepareCall("CALL getTotalVisitas()");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);

            try{
                reader = sql.executeQuery();
                if(reader.next()){
                    visitas = reader.getInt("cuenta");
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
        
        return visitas;
    }

    @Override
    public int getTotalUsers() throws PersistenceException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        int usuarios = 0;
        try{
            Connection conn = connect();
            sql = conn.prepareCall("CALL getTotalUsuarios()");
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);

            try{
                reader = sql.executeQuery();
                if(reader.next()){
                    usuarios = reader.getInt("cuenta");
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
        
        return usuarios;    
    }

    @Override
    public String editUserLikeAdmin(Usuario user) throws PersistenceException, ClassNotFoundException {
    Class.forName("com.mysql.jdbc.Driver");

        int comprovar = 0;
        try{
            Connection conn = connect();
            //<editor-fold defaultstate="collapsed" desc="Fecha de Nacimiento">
            
            if(user.getFecNac() == null || user.getFecNac().isEmpty() || user.getFecNac().equals("")){
                sql = conn.prepareCall("CALL editUserAdmWoFec(?, ?, ?, ?, ?, ?, ?, ?, ?)");
            }else{
                String fnSN = user.getFecNac();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = format.parse(fnSN);
                Date fecNac = new Date(date.getTime());
                sql = conn.prepareCall("CALL editUserAdm(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                sql.setDate(10, fecNac);
            }
            //</editor-fold>
            
            
            sql.setEscapeProcessing(true);
            sql.setQueryTimeout(90);
                sql.setInt(1, user.getIdUser());
                sql.setString(2, user.getNombre());
                sql.setString(3, user.getApellidos());
                sql.setString(4, user.getGenero());
                sql.setString(5, user.getNif());
                sql.setString(6, user.getEmail());
                sql.setInt(7, user.getDiffus());
                sql.setBoolean(8, user.getActivo());
                sql.setBoolean(9, user.getBaja());
                
            comprovar = sql.executeUpdate();
            
        }catch(SQLException e){
            throw new PersistenceException(e.getErrorCode());
        } catch (ParseException ex) {
            Logger.getLogger(UsuarioMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
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
        if(comprovar > 0){
                return "backoffice";
        }else{
                return "edit_backoffice";
        }    
    }

    

    
    
    
    
}
