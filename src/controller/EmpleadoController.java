/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import configuration_server.GenericController;
import configuration_server.Rol;
import entidades.Empleado;
import excepciones.ServerExecutionException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import servidor.ClienteInfo;
import util.Util;

/**
 *
 * @author agarcia.gonzalez
 */
public class EmpleadoController extends GenericController {
    
    public Map<String, String> loginAsAdministrador (Map<String, String> parameters) throws ServerExecutionException {
        return login(parameters, Rol.ADMINISTRADOR);
    }
    
    public Map<String, String> loginAsEmpleado (Map<String, String> parameters) throws ServerExecutionException {
        return login(parameters, Rol.EMPLEADO);
    }
    
    // YAGNI!!!
    public Map<String, String> loginAsDirectivo (Map<String, String> parameters) throws ServerExecutionException {
        return login(parameters, Rol.DIRECTIVO);
    }
    
    private Map<String,String> login (Map<String,String> parameters, Rol rol) throws ServerExecutionException {
        String nick = parameters.get("email");
        String pass = parameters.get("pass");
        Integer idThread = Integer.parseInt(parameters.get("idThread"));
        
        // Añadimos los criterios para que Hibernate haga la búsqueda
        Map<String,String> criterios = new HashMap<>();
        criterios.put("email", nick);
        criterios.put("pass", pass);
        
        Empleado empleado = (Empleado) this.getHManager().getObjectCriterioWithoutLazyObjects("Empleado", criterios, "getSede", "getCiudad", "getPuesto");
        
        if (empleado==null) {
            throw new ServerExecutionException ("Nombre o contraseña erronea");
        }
        
        if (empleado.getFechaBaja()!=null && empleado.getFechaBaja().getTime()<(new Date(System.currentTimeMillis())).getTime()) {
            throw new ServerExecutionException ("No se puede loguear con este empleado porque ha sido dado de baja del sistema.");
        }
        
        Map<String, String> result = Util.convertObjectToMap(empleado);
        
        // Generamos, almacenamos y enviamos el token. Tiene que ser aleatoria.
        ClienteInfo info = new ClienteInfo ();
        info.setNombre(empleado.getEmail());
        info.setId(empleado.getId());
        info.setRol(rol);
        info.setIdThread(idThread);
        
        String token = this.getServer().conectarUsuario(info);
        result.put("token", token);
        result.put("nick", empleado.getEmail());
        result.put("sede", empleado.getSede().getNombre());
        result.put("ciudad", empleado.getCiudad().toString());
        result.put("puesto", empleado.getPuesto().getNombre());
        
        return result;
    }
    
    public Map<String, String> desconectar(Map<String, String> parameters) throws ServerExecutionException {
        Integer idThread = Integer.parseInt(parameters.get("idThread"));
        
        int desconectado = this.getServer().desconectarUsuario(idThread);
        
        if (desconectado==0)
            throw new ServerExecutionException ("Error al intentar desconectar del servidor");
        
        Map<String, String> result = new HashMap<>();
        result.put("status", "ok");
        
        return result;
    }
    
    public Map<String, String> getEmpleados() throws ServerExecutionException {
        List<Empleado> empleados = this.getHManager().getObjects("Empleado");
        
        if (empleados==null || empleados.isEmpty()) {
            throw new ServerExecutionException ("No se ha encontrado ningún empleado");
        }
        
        Map<String, String> result = new HashMap<>();
        result.put("length", empleados.size()+"");
        for (int i = 0; i < empleados.size(); i++) {
             result.putAll(Util.convertObjectToMap(empleados.get(i), "["+i+"]"));
        }
        
        return result;
    }
    
    public Map<String, String> getEmpleado(Map<String,String> parametros) throws ServerExecutionException {
        Integer id = Integer.parseInt(parametros.get("id"));
        
        Empleado empleado = (Empleado) this.getHManager().getObjectWithoutLazyObjects(Empleado.class, id, "getPuesto", "getCiudad", "getSede");
        
        if (empleado==null) 
            throw new ServerExecutionException ("No se ha encontrado al empleado con ID " + id);
        
        Map<String, String> result = util.Util.convertObjectToMap(empleado);
        result.put("ciudad", empleado.getCiudad().getId()+"");
        result.put("sede", empleado.getSede().getId()+"");
        result.put("puesto", empleado.getPuesto().getId()+"");
        return result;
    }
}
