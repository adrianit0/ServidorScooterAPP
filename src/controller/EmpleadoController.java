/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import configuration_server.GenericController;
import entidades.Cliente;
import excepciones.ExecuteError;
import java.util.HashMap;
import java.util.Map;
import util.Util;

/**
 *
 * @author agarcia.gonzalez
 */
public class EmpleadoController extends GenericController {
    
    public Map<String, String> getEmpleados() throws ExecuteError {
        
        // Añadimos los criterios para que Hibernate haga la búsqueda
        /*
        Cliente cliente = (Cliente) this.getHManager();
        
        if (cliente==null) {
            throw new ExecuteError ("Nombre o contraseña erronea", null);
        }
        
        Map<String, String> result = Util.convertObjectToMap(cliente);
        
        // Generamos, almacenamos y enviamos el token. Tiene que ser aleatoria.
        String token = Util.crearTokenUsuario();
        result.put("token", token);
        
        return result;*/
        
        return null;
    }
    
}
