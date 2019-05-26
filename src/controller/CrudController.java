/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import configuration_server.GenericController;
import entidades.Empleado;
import excepciones.ExecuteError;
import java.util.Map;
import util.HibernateManager;

/**
 *
 * @author agarcia.gonzalez
 */
public class CrudController extends GenericController {
    
    
    // CRUD EMPLEADO
    public Map<String,String> createEmpleado (Map<String,String> parametros) throws ExecuteError {
        Empleado empleado = (Empleado) util.Util.convertMapToObject(Empleado.class, parametros);
        
        HibernateManager hm = this.getHManager();
        Integer id = hm.addObject(empleado);
        
        if (id==-1)
            throw new ExecuteError ("No se ha podido crear el empleado", parametros);
        
        
        empleado.setId(id);
        Map<String,String> result = util.Util.convertObjectToMap(empleado);
        return result;
    }
    
    public Map<String,String> updateEmpleado (Map<String,String> parametros) throws ExecuteError {
        Empleado empleado = (Empleado) util.Util.convertMapToObject(Empleado.class, parametros);
        
        HibernateManager hm = this.getHManager();
        Boolean editado = hm.updateObject(empleado);
        
        if (!editado)
            throw new ExecuteError ("No se ha podido editar el empleado", parametros);
        
        Map<String,String> result = util.Util.convertObjectToMap(empleado);
        return result;
    }
    
    public Map<String,String> deleteEmpleado (Map<String,String> parametros) throws ExecuteError {
        Empleado empleado = (Empleado) util.Util.convertMapToObject(Empleado.class, parametros);
        
        HibernateManager hm = this.getHManager();
        Boolean editado = hm.deleteObject(empleado);
        
        if (!editado)
            throw new ExecuteError ("No se ha podido eliminar el empleado", parametros);
        
        Map<String,String> result = util.Util.convertObjectToMap(empleado);
        return result;
    }
    
}
