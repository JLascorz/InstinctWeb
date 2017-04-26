/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package joaquimmir;

import com.instinct.web.objects.Usuario;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * <p>La classe <b>JDBCUTILS</b> serveix per a crear objectes Alumne i Moduls,
 * a més tambe retorna una string de les dades de la relació.</p>
 * @author Jordi Lascorz
 * @since 03/03/2017
 * @version 1.0
 */
public class JDBCUtils {
    /**
     * El Constructor de JDBCUtils esta buit, i no s'utilitza.
     */
    private JDBCUtils(){}
    /**
     * La funció getAlumne, retorna un alumne que s'ha retornat de les dades de la BD.
     * @param reader ResultSet
     * @return Alumne
     * @throws SQLException 
     */
    public static Usuario getStudent(ResultSet reader) throws SQLException{
            Usuario user = new Usuario(
            reader.getInt("idUser"),
            reader.getString("Nombre"),
            reader.getString("Apellido"),
            reader.getString("Email"),
            reader.getString("Password"),
            reader.getString("Nif"),
            reader.getString("Genero"),
            reader.getString("fecNac"),
            reader.getBoolean("Activo"),
            reader.getBoolean("Baja"),
            reader.getInt("Diffus"),
            reader.getString("fecUltVis"),
            reader.getInt("numVisitas"),
            reader.getString("fecRegistro"));
            
            return user;
    }

}
