/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import servidorUtil.Servidor;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Adrián García
 */
public class ScooterBBDD {
    // Comprueba que un cliente existe en la base de datos
    public boolean existeCliente (String jugador) {
        Servidor s = new Servidor ("SELECT * FROM cliente WHERE nick=? LIMIT 1", "scooterapp", jugador);
        
        ResultSet rs = s.realizarQuery();
        
        boolean existe = false;
        try {
            existe = rs.next();
            s.close();
        } catch (SQLException e) {
            return false;
        }
        
        return existe;
    }
    
    public long registrarCliente (String jugador, String pass) {
        Servidor s = new Servidor ("INSERT INTO cliente (nick, pass) VALUES (?,?)", "scooterapp", jugador, pass);
        
        int valor = s.realizarUpdate();
        
        boolean error = s.closeWithoutError();
        
        if (error) {
            System.out.println("Ha habido un error con el jugador "+jugador);
        }
        
        // Devuelve el valor si y solo si no hay errores.
        return (valor>0 && !error) ? valor : -1;
    }
    
    public long identificarCliente (String jugador, String pass) {
        Servidor s = new Servidor ("SELECT * FROM cliente WHERE nick=? AND pass=?", "scooterapp", jugador, pass);
        ResultSet rs = s.realizarQuery();
        
        try {
            rs.next();
            long id = rs.getLong(1);
            s.close();
            
            return id;
        } catch (SQLException e) {
            //System.err.println("Error: "+e.getMessage());
            // Si no existe el usuario, lanzamos -1
            return -1;
        }
    }
    
}
