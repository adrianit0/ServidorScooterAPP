/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import configuration_server.GenericController;
import entidades.Cliente;
import entidades.Empleado;
import excepciones.ExecuteError;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.Util;

/**
 *
 * @author agarcia.gonzalez
 */
public class EmpleadoController extends GenericController {
    
    public Map<String, String> getEmpleados() throws ExecuteError {
        List<Empleado> empleados = this.getHManager().getObjects("Empleado");
        
        if (empleados==null || empleados.isEmpty()) {
            throw new ExecuteError ("No se ha encontrado ningún empleado", null);
        }
        
        Map<String, String> result = new HashMap<>();
        result.put("length", empleados.size()+"");
        for (int i = 0; i < empleados.size(); i++) {
             result.putAll(Util.convertObjectToMap(empleados.get(i), "["+i+"]"));
        }
        
        return result;
    }
    
    public Map<String, String> getClientes() throws ExecuteError {
        List<Cliente> clientes = this.getHManager().getObjects("Cliente");
        
        if (clientes==null || clientes.isEmpty()) {
            throw new ExecuteError ("No se ha encontrado ningún cliente", null);
        }
        
        Map<String, String> result = new HashMap<>();
        result.put("length", clientes.size()+"");
        for (int i = 0; i < clientes.size(); i++) {
             result.putAll(Util.convertObjectToMap(clientes.get(i), "["+i+"]"));
        }
        
        return result;
    }
    
}
