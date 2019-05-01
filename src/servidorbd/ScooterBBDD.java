/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorbd;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Adrián García
 */
public class ScooterBBDD {
    // Comprueba que un cliente existe en la base de datos
    public boolean existeCliente (String cliente) {
        ConexionBBDD s = new ConexionBBDD ("SELECT * FROM cliente WHERE nick=? LIMIT 1", "scooterapp", cliente);
        
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
    
    public long registrarCliente (String nombre, String apellido1, String apellido2, String nick, String email, String pass) {
        ConexionBBDD s = new ConexionBBDD (
                "INSERT INTO cliente (nombre, apellido1, apellido2, nick, email, pass, minutos, activada, fechaCreacion) VALUES (?, ?, ?, ?, ?, ?, 0, 1, now())", 
                "scooterapp", nombre, apellido1, apellido2, nick, email, pass);
        
        int valor = s.realizarUpdate();
        
        boolean error = s.closeWithoutError();
        
        if (error) {
            System.out.println("Ha habido un error con el cliente "+nombre);
        }
        
        // Devuelve el valor si y solo si no hay errores.
        return (valor>0 && !error) ? valor : -1;
    }
    
    public long identificarCliente (String cliente, String pass) {
        ConexionBBDD s = new ConexionBBDD ("SELECT * FROM cliente WHERE nick=? AND pass=?", "scooterapp", cliente, pass);
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