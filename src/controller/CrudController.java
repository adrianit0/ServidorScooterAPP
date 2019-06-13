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
import java.sql.Date;
import util.HibernateManager;
import util.Util;

/**
 *
 * @author agarcia.gonzalez
 */
public class CrudController extends GenericController {
    
    
    // CRUD EMPLEADO
    public Map<String,String> createEmpleado (Map<String,String> parametros) throws ServerExecutionException {
        Empleado empleado = (Empleado) util.Util.convertMapToObject(Empleado.class, parametros);
        
        HibernateManager hm = this.getHManager();
        
        Integer sedeId = Util.parseInt(parametros.get("sedeId"));
        Integer ciudadId = Util.parseInt(parametros.get("ciudadId"));
        Integer puestoId = Util.parseInt(parametros.get("puestoId"));
        
        Sede sede = (Sede) hm.getObject(Sede.class, sedeId);
        Ciudad ciudad = (Ciudad) hm.getObject(Ciudad.class, ciudadId);
        Puesto puesto = (Puesto) hm.getObject(Puesto.class, puestoId);
        
        empleado.setSede(sede);
        empleado.setCiudad (ciudad);
        empleado.setPuesto(puesto);
        empleado.setFechaAlta(new Date(System.currentTimeMillis()));
        
        Integer id = hm.addObject(empleado);
        
        if (id==-1)
            throw new ServerExecutionException ("No se ha podido crear el empleado", parametros);
        
        
        empleado.setId(id);
        Map<String,String> result = util.Util.convertObjectToMap(empleado);
        return result;
    }
    
    public Map<String,String> updateEmpleado (Map<String,String> parametros) throws ServerExecutionException {
        Empleado empleado = (Empleado) util.Util.convertMapToObject(Empleado.class, parametros);
        
        System.out.println("ID: " + empleado.getId());
        
        HibernateManager hm = this.getHManager();
        Empleado lastEmpleado = (Empleado) hm.getObjectWithoutLazyObjects(Empleado.class, empleado.getId(), "getSede", "getCiudad", "getPuesto");
        
        System.out.println("Fecha alta: " + lastEmpleado.getFechaAlta());
        
        Integer sedeId = Util.parseInt(parametros.get("sedeId"));
        Integer ciudadId = Util.parseInt(parametros.get("ciudadId"));
        Integer puestoId = Util.parseInt(parametros.get("puestoId"));
        empleado.setSede(sedeId!=null? new Sede(sedeId) : lastEmpleado.getSede());
        empleado.setCiudad(ciudadId!=null? new Ciudad(ciudadId) : lastEmpleado.getCiudad());
        empleado.setPuesto(puestoId!=null? new Puesto(puestoId) : lastEmpleado.getPuesto());
        empleado.setPass (lastEmpleado.getPass());
        empleado.setFechaAlta (lastEmpleado.getFechaAlta());
        empleado.setFechaBaja(empleado.getFechaBaja());
        
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
    
    public Map<String,String> darBajaEmpleado (Map<String,String> parametros) throws ServerExecutionException {
        Integer id = Integer.parseInt(parametros.get("id"));
        
        HibernateManager hm = this.getHManager();
        Empleado empleado = (Empleado) hm.getObject(Empleado.class, id);
        
        if (empleado==null)
            throw new ServerExecutionException("El empleado no existe");
        
        if (empleado.getFechaBaja()!=null)
            throw new ServerExecutionException("El empleado ya estaba dado de baja");
        
        empleado.setFechaBaja(new Date(System.currentTimeMillis()));
        boolean editado = hm.updateObject(empleado);
        
        if (!editado)
            throw new ServerExecutionException ("No se ha podido dar de baja al empleado", parametros);
        
        Map<String,String> result = util.Util.convertObjectToMap(empleado);
        return result;
    }
    
    
    
    
    
}
