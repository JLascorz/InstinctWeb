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
 * UsuarioMySQLDAO classe per englobar les funcions de usuaris.
 * @author Jordi Lascorz
 * @since 12/05/2017
 * @version 1.0
 */
@ManagedBean(name="UsuarioMySQLDAO")
@SessionScoped

public class UsuarioMySQLDAO implements UsuarioDAO {
    
    CallableStatement sql = null;
    ResultSet reader = null;
    
    /**
     * Funcio que engloba altres funcions per a fer el registre dels usuaris
     * @param user
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public String register(Usuario user) throws PersistenceException, ClassNotFoundException {
        String devuelve = null;
        //Comprova si existeix un usuari amb el mateix mail.
        int comprueba = getUserByEmail(user, "registro");
        if(comprueba == 0){
            //Inserta un usuari a la base de dades
            devuelve = insertUsuario(user);
            return devuelve;
        }else{
            //Mostra un missatge d'error si l'usuari amb el mateix mail ya se ha registrat.
            FacesMessage message = new FacesMessage("Este usuario ya esta registrado.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return null;
    }

    /**
     * Funcio que agafa verifica si existeix un usuari amb el mateix mail.
     * @param user
     * @param caller
     * @return int
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public int getUserByEmail(Usuario user, String caller) throws PersistenceException, ClassNotFoundException {
        //<editor-fold defaultstate="collapsed" desc="Atributs">
        Class.forName("com.mysql.jdbc.Driver");
        int comprobacion = 0;
        int idUs = 0;
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            Connection conn = connect();
            //Crida a un procedure per verificar el mail
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
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Return de la Funció">
        //Si la pagina de la que es crida es la del registre
        //Verifica si retorna una id que no sigui 0
        //Canvia l'estat del switch
        if(caller == "registro"){
            if(idUs != 0){
                comprobacion++;         
            }else{
                comprobacion = 0;
            }
        }else if(caller == "editar") {
            //Comprova que si la pagina es la de editar
            //I la id es la mateixa que la de l'usuari
            if(idUs == user.getIdUser()){
                comprobacion = 0;
            }else if(idUs == 0){
                comprobacion = 999;
            }else{
                comprobacion = 1;
            }
        }
        
        return comprobacion;
        //</editor-fold>   
    }
    
    /**
     * Funcio que fa un insert amb les dades d'un usuari.
     * @param user
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
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
                //Canvia la data de naixement per adaptarla a sql
                String fnSN = user.getFecNac();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = format.parse(fnSN);
                Date fecNac = new Date(date.getTime());
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="Fecha del Registro">
                //Agafa la data del moment del registre
                Timestamp fecRegistro = new Timestamp(System.currentTimeMillis());
                //</editor-fold>
                //Crida a un procedure per insertar l'usuari
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

    /**
     * Mostra una llista de tots els usuaris
     * @return Lista de usuarios
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public List<Usuario> getUsuarios() throws PersistenceException, ClassNotFoundException {
    Class.forName("com.mysql.jdbc.Driver");
    //<editor-fold defaultstate="collapsed" desc="Atributs">
        Connection conn = connect();
        List<Usuario> users = new ArrayList<>();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            //Crida a un procedure que retorna tots els usuaris
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

    /**
     * Funció per a fer el login de l'usuari engloba altres funcions
     * @param user
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public String login(Usuario user) throws PersistenceException, ClassNotFoundException{
        //Funció que agafa les dades de l'usuari desde la base de dades gracies
        //al mail i a la password
        user = getUser(user);
        //Si el nom no es null pasa
        if(user.getNombre() != null){
            //Si l'usuari no esta donat de baixa passa
            if(user.getBaja() != true){
                //Canvia l'estat del usuari a actiu
                user = changeActivity(user, true);
                //Una vegada que l'estat hagi canviat redirecciona a l'usuari
                if(user.getActivo() == true){
                  guardaSession(user, "login");
                  //recogeSession();
                  return "index.xhtml?faces-redirect=true";
                }
            }else{
                //Mostra un error en cas de que l'usuari estigui donat de baixa.
                FacesMessage message = new FacesMessage("Este usuario esta dado de baja.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }else{
            //Mostra un error en cas de que el mail o la password siguin incorrectes
            FacesMessage message = new FacesMessage("Email o contraseña incorrecto.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return null;
    }
    
    /**
     * Funció per tancar la sessio d'un usuari.
     * @param user
     * @return
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public String logout(Usuario user) throws PersistenceException, ClassNotFoundException{
        //Crida a una funció per canviar l'estat d'actiu del usuari a false
        user = changeActivity(user, false);
        //Una vegada que l'usuari ya no estigui actiu tanca la sessió i redirecciona a la pagina principal
        if(user.getActivo() == false){
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        }
        return "index.xhtml?faces-redirect=true";
    }
    
    /**
     * Funció per agafar un usuari a partir del mail i la password.
     * @param user
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public Usuario getUser(Usuario user) throws PersistenceException, ClassNotFoundException{
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        Class.forName("com.mysql.jdbc.Driver");
        try{
            Connection conn = connect();
            //Crida a un procedure que fa un login amb el mail i la password
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
        //</editor-fold>
        
    }
    
    /**
     * Funció que canvia l'estat d'un usuari de false a true.
     * @param user
     * @param activity
     * @return Usuario
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public Usuario changeActivity(Usuario user, Boolean activity) throws PersistenceException, ClassNotFoundException{
        //<editor-fold defaultstate="collapsed" desc="Atributs">
        Class.forName("com.mysql.jdbc.Driver");
        int comprovar = 0;
        int nVisitas = 0;
        String fUlt = null;
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            //Canvia la ultima conexió a la data del login o logout
            Timestamp tUlt = new Timestamp(System.currentTimeMillis());
            fUlt = tUlt.toString();
            
            //Canvia el numero de visitas la de l'usuari + 1
            if(activity == true){
                nVisitas = user.getNumVisitas();
                nVisitas++;
            }else{
                nVisitas = user.getNumVisitas();
            }
            
            Connection conn = connect();
            //Canvia l'estat del usuari a actiu o no actiu
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
        //</editor-fold>   
    }

    /**
     * Funció per guardar la sessio d'un usuari depenent de la pagina.
     * @param user
     * @param pagina 
     */
    @Override
    public void guardaSession(Usuario user, String pagina) {
       //Comprova desde quina pagina fa login i assigna una sessió o una altra
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
            
            //Si la pagina es desde la administració assigna una sessió i fa una redirecció
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("edit_backoffice.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(UsuarioMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            FacesContext.getCurrentInstance().responseComplete();
       }     
    }

    /**
     * Funció per agafar la sessió - No s'utilitza
     * @return user
     */
    @Override
    public Usuario recogeSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
        HttpSession httpSession = request.getSession(false);
        Usuario user = (Usuario) httpSession.getAttribute("JSESSIONID");
        
        return user;
    }

    /**
     * Funcio que engloba altres funcions per editar l'usuari
     * @param user
     * @param pagina
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public String callEditar(Usuario user, String pagina) throws PersistenceException, ClassNotFoundException {
        String devuelve = null;
        //Comprova l'usuari per el mail (També'agafa una data en cas de que no tingui)
        int comprueba = getUserByEmail(user, "editar");
        //Si la pagina es la de editar el perfil entra en el if
        if(pagina.equals("editPerfil")){
            //Comprova el que ha retorna la funció de getUserByEmail
            //En cas de que sigui la mateixa id o no
            if(comprueba == 0 || comprueba == 999){
                //Edita l'usuari amb les dades noves
                devuelve = editarUsuario(user);
                if(devuelve == "perfil"){
                    //Si la edició a anat bé retorna a la pagina de perfil actualitzant la sessió
                    Usuario temp = getUser(user);
                    guardaSession(temp, "modPerfil");
                }
                return devuelve;
            }else{
                //Mostra un error en cas de que ya existeixi un usuari amb el mateix correu.
                FacesMessage message = new FacesMessage("Ya hay un usuario con este correo.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }else if(pagina.equals("editAdmin")){
            if(comprueba == 0 || comprueba == 999){
                //Edita un usuari com a administrador
                devuelve = editUserLikeAdmin(user);
                return devuelve;
            }else{
                //Mostra un error en cas de que ya existeixi un usuari amb el mateix correu.
                FacesMessage message = new FacesMessage("Ya hay un usuario con este correo.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }    
        return null;
    }
    
    /**
     * Funció per editar l'usuari amb les dades insertades.
     * @param user
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public String editarUsuario(Usuario user) throws PersistenceException, ClassNotFoundException {
        //<editor-fold defaultstate="collapsed" desc="Atributs">
        Class.forName("com.mysql.jdbc.Driver");

        int comprovar = 0;
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            Connection conn = connect();
            //<editor-fold defaultstate="collapsed" desc="Fecha de Nacimiento">
            //En cas de que la data estigui buida crida a un procedure o a un altre, per editarla o no.
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
        //</editor-fold>
        
    }

    /**
     * Funció que crida a la funcio de canviar l'estat de l'usuari a baixa.
     * @param user
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public String callBaja(Usuario user) throws PersistenceException, ClassNotFoundException {
        //Crida a una funció que canvia l'estat del usuari i el dona de baixa, despres el fa logout
        user = changeBaja(user, true);
        if(user.getBaja() == true){
            logout(user);
        }
        return "index.xhtml?faces-redirect=true";
    }

    /**
     * Funció que canvia l'estat de l'usuari per a que no es pugui conectar mai.
     * @param user
     * @param baja
     * @return Usuario
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public Usuario changeBaja(Usuario user, Boolean baja) throws PersistenceException, ClassNotFoundException {
        //<editor-fold defaultstate="collapsed" desc="Atributs">
        Class.forName("com.mysql.jdbc.Driver");
        int comprovar = 0;
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            
            Connection conn = connect();
            //Crida a un procedure que dona de baixa a l'usuari
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
        //</editor-fold>
            
    }

    /**
     * Funció que retorna el numero de visitas dels usuaris
     * @return int
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public int getTotalVisitas() throws PersistenceException, ClassNotFoundException {
        //<editor-fold defaultstate="collapsed" desc="Atributs">
        Class.forName("com.mysql.jdbc.Driver");
        int visitas = 0;
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            Connection conn = connect();
            //Crida a un procedure que retorna el numero de visitas
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
        //</editor-fold>
    }

    /**
     * Funció que retorna el numero d'usuaris registrats
     * @return int
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public int getTotalUsers() throws PersistenceException, ClassNotFoundException {
        //<editor-fold defaultstate="collapsed" desc="Atributs">
        Class.forName("com.mysql.jdbc.Driver");
        int usuarios = 0;
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            Connection conn = connect();
            //Agafa el numero de usuaris registrats
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
        //</editor-fold>
            
    }

    /**
     * Funció per editar un usuari com administrador.
     * @param user
     * @return String
     * @throws PersistenceException
     * @throws ClassNotFoundException 
     */
    @Override
    public String editUserLikeAdmin(Usuario user) throws PersistenceException, ClassNotFoundException {
        //<editor-fold defaultstate="collapsed" desc="Atributs">
        Class.forName("com.mysql.jdbc.Driver");
        int comprovar = 0;
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Try/Catch">
        try{
            Connection conn = connect();
            //<editor-fold defaultstate="collapsed" desc="Fecha de Nacimiento">
            //Crida a un procedure o a una altra depenent de si se li ha passat la data
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
        //</editor-fold>
            
    }


}
