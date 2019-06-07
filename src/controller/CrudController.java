/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import configuration_server.GenericController;
import entidades.Bono;
import entidades.Ciudad;
import entidades.Empleado;
import entidades.Puesto;
import entidades.Sede;
import excepciones.ServerExecutionException;
import java.util.Map;
import util.HibernateManager;

/**
 *
 * @author agarcia.gonzalez
 */
public class CrudController extends GenericController {
    
    
    // CRUD EMPLEADO
    public Map<String,String> createEmpleado (Map<String,String> parametros) throws ServerExecutionException {
        Empleado empleado = (Empleado) util.Util.convertMapToObject(Empleado.class, parametros);
        
        HibernateManager hm = this.getHManager();
        
        //AÑADIR ESTO CON COMBOBOX
        Sede sede = (Sede) hm.getObject(Sede.class, 1);
        Ciudad ciudad = (Ciudad) hm.getObject(Ciudad.class, 1);
        Puesto puesto = (Puesto) hm.getObject(Puesto.class, 1);
        
        empleado.setSede(sede);
        empleado.setCiudad (ciudad);
        empleado.setPuesto(puesto);
        
        Integer id = hm.addObject(empleado);
        
        if (id==-1)
            throw new ServerExecutionException ("No se ha podido crear el empleado", parametros);
        
        
        empleado.setId(id);
        Map<String,String> result = util.Util.convertObjectToMap(empleado);
        return result;
    }
    
    public Map<String,String> updateEmpleado (Map<String,String> parametros) throws ServerExecutionException {
        Empleado empleado = (Empleado) util.Util.convertMapToObject(Empleado.class, parametros);
        
        System.out.println("ID: " + empleado);
        
        HibernateManager hm = this.getHManager();
        Empleado lastEmpleado = (Empleado) hm.getObjectWithoutLazyObjects(Empleado.class, empleado.getId(), "getSede", "getCiudad", "getPuesto");
        empleado.setSede(lastEmpleado.getSede());
        empleado.setCiudad(lastEmpleado.getCiudad());
        empleado.setPuesto(lastEmpleado.getPuesto());
        empleado.setPass (lastEmpleado.getPass());
        
        Boolean editado = hm.updateObject(empleado);
        
        if (!editado)
            throw new ServerExecutionException ("No se ha podido editar el empleado", parametros);
        
        Map<String,String> result = util.Util.convertObjectToMap(empleado);
        return result;
    }
    
    public Map<String,String> deleteEmpleado (Map<String,String> parametros) throws ServerExecutionException {
        Integer id = Integer.parseInt(parametros.get("id"));
        
        HibernateManager hm = this.getHManager();
        Empleado empleado = (Empleado) hm.getObject(Empleado.class, id);
        Boolean editado = hm.deleteObject(empleado);
        
        if (!editado)
            throw new ServerExecutionException ("No se ha podido eliminar el empleado", parametros);
        
        Map<String,String> result = util.Util.convertObjectToMap(empleado);
        return result;
    }
    
    
    
    
    
}
