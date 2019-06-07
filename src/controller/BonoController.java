/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import configuration_server.GenericController;
import entidades.Bono;
import entidades.Cliente;
import entidades.Empleado;
import entidades.Historialcompra;
import excepciones.ServerExecutionException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.HibernateManager;

/**
 *
 * @author agarcia.gonzalez
 */
public class BonoController extends GenericController {
    
    
    public Map<String,String> getBonos () {
        List<Bono> bonos = this.getHManager().getObjects("Bono");
        Map<String,String> result = util.Util.convertListToMap(bonos);
        return result;
    }
    
    public Map<String, String> getBono(Map<String,String> parametros) throws ServerExecutionException {
        Integer id = Integer.parseInt(parametros.get("id"));
        Bono bono = (Bono) this.getHManager().getObject(Bono.class, id);
        
        if (bono==null) 
            throw new ServerExecutionException ("No se ha encontrado el bono con ID " + id);
        
        Map<String, String> result = util.Util.convertObjectToMap(bono);
        return result;
    }
    
    public Map<String,String> aumentarBonos (Map<String,String> parametros) throws ServerExecutionException {
        HibernateManager hm = this.getHManager();
        Integer idCliente = Integer.parseInt(parametros.get("idCliente"));
        Integer idBono = Integer.parseInt(parametros.get("idBono"));
        
        Cliente cliente = (Cliente) hm.getObject(Cliente.class, idCliente);
        Bono bono = (Bono) hm.getObject(Bono.class, idBono);
        
        cliente.setMinutos(cliente.getMinutos() + bono.getMinutos());
        
        Historialcompra historial = new Historialcompra();
        
        historial.setBono(bono);
        historial.setCliente(cliente);
        historial.setFecha(new Date (System.currentTimeMillis()));
        
        // Esto podría cambiar con algún descuento
        historial.setMinutos(bono.getMinutos());
        historial.setPrecio(bono.getPrecio());
        
        Integer id = hm.addObject(historial);
        
        if (id==-1) 
            throw new ServerExecutionException ("No se ha podido realizar la compra");
        historial.setId(id);
        
        boolean realizado = hm.updateObject(cliente);
        if (!realizado)
            throw new ServerExecutionException ("");
        
        Map<String,String> result = util.Util.convertObjectToMap(historial);
        result.put("status", "ok");
        result.put("minutosTotales", cliente.getMinutos()+"");
        
        return result;
    }
    
    // CRUD BONO
    public Map<String,String> createBono (Map<String,String> parametros) throws ServerExecutionException {
        Bono bono = (Bono) util.Util.convertMapToObject(Bono.class, parametros);
        
        HibernateManager hm = this.getHManager();
        
        Integer id = hm.addObject(bono);
        
        if (id==-1)
            throw new ServerExecutionException ("No se ha podido crear el bono", parametros);
        
        
        bono.setId(id);
        Map<String,String> result = util.Util.convertObjectToMap(bono);
        return result;
    }
    
    public Map<String,String> updateBono (Map<String,String> parametros) throws ServerExecutionException {
        Bono bono = (Bono) util.Util.convertMapToObject(Bono.class, parametros);
        
        HibernateManager hm = this.getHManager();
        Boolean editado = hm.updateObject(bono);
        
        if (!editado)
            throw new ServerExecutionException ("No se ha podido editar el bono", parametros);
        
        Map<String,String> result = util.Util.convertObjectToMap(bono);
        return result;
    }
    
    public Map<String,String> deleteBono (Map<String,String> parametros) throws ServerExecutionException {
        Integer id = Integer.parseInt(parametros.get("id"));
        
        HibernateManager hm = this.getHManager();
        Bono bono = (Bono) hm.getObject(Bono.class, id);
        Boolean editado = hm.deleteObject(bono);
        
        if (!editado)
            throw new ServerExecutionException ("No se ha podido eliminar el bono", parametros);
        
        Map<String,String> result = util.Util.convertObjectToMap(bono);
        return result;
    }
}
