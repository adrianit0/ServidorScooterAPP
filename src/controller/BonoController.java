/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import configuration_server.GenericController;
import entidades.Bono;
import entidades.Cliente;
import entidades.Historialcompra;
import excepciones.ExecuteError;
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
    
    public Map<String,String> aumentarBonos (Map<String,String> parametros) throws ExecuteError {
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
            throw new ExecuteError ("No se ha podido realizar la compra");
        historial.setId(id);
        
        boolean realizado = hm.updateObject(cliente);
        if (!realizado)
            throw new ExecuteError ("");
        
        Map<String,String> result = util.Util.convertObjectToMap(historial);
        result.put("status", "ok");
        result.put("minutosTotales", cliente.getMinutos()+"");
        
        return result;
    }
}
