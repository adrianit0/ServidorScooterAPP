/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import configuration_server.GenericController;
import entidades.Cliente;
import excepciones.ExecuteError;
import excepciones.MapperException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import util.HibernateManager;
import util.Util;

/**
 *
 * Clase donde estará mapeado las clases de sesión
 * 
 * @author agarcia.gonzalez
 */
public class UsuarioController extends GenericController implements IUsuarioController {
    
    private HibernateManager mh;
    
    public UsuarioController () {
        mh = new HibernateManager();
    }
    
    @Override
    public Map<String, String> login(Map<String, String> parameters) throws ExecuteError {
        String nick = parameters.get("nick");
        String pass = parameters.get("pass");
        
        // Añadimos los criterios para que Hibernate haga la búsqueda
        Map<String,String> criterios = new HashMap<>();
        criterios.put("nick", nick);
        criterios.put("pass", pass);
        
        Cliente cliente = (Cliente) mh.getObjectCriterio("Cliente", criterios);
        
        if (cliente==null) {
            throw new ExecuteError ("Nombre o contraseña erronea", null);
        }
        
        Map<String, String> result = Util.convertObjectToMap(cliente);
        
        // Generamos, almacenamos y enviamos el token. Tiene que ser aleatoria.
        String token = Util.crearTokenUsuario();
        result.put("token", token);
        
        return result;
    }

    @Override
    public Map<String, String> register(Map<String, String> parameters) {
        return null;
    }
}
