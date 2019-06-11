/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import configuration_server.GenericController;
import configuration_server.Rol;
import entidades.Alquiler;
import entidades.Cliente;
import excepciones.ServerExecutionException;
import excepciones.MapperException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import servidor.ClienteInfo;
import util.HibernateManager;
import util.Util;

/**
 *
 * Clase donde estará mapeado las clases de sesión
 * 
 * @author agarcia.gonzalez
 */
public class UsuarioController extends GenericController implements IUsuarioController {
    
    
    @Override
    public Map<String, String> login(Map<String, String> parameters) throws ServerExecutionException {
        String nick = parameters.get("nick");
        String pass = parameters.get("pass");
        Integer idThread = Integer.parseInt(parameters.get("idThread"));
        
        // Añadimos los criterios para que Hibernate haga la búsqueda
        Map<String,String> criterios = new HashMap<>();
        criterios.put("nick", nick);
        criterios.put("pass", pass);
        
        Cliente cliente = (Cliente) this.getHManager().getObjectCriterio("Cliente", criterios);
        
        if (cliente==null) {
            throw new ServerExecutionException ("Nombre o contraseña erronea");
        }
        
        Map<String, String> result = Util.convertObjectToMap(cliente);
        
        // Generamos, almacenamos y enviamos el token. Tiene que ser aleatoria.
        ClienteInfo info = new ClienteInfo ();
        info.setNombre(cliente.getNick());
        info.setId(cliente.getId());
        info.setRol(Rol.CLIENTE);
        info.setIdThread(idThread);
        
        String token = this.getServer().conectarUsuario(info);
        result.put("token", token);
        
        Alquiler alquiler = this.getServer().getAlquiler(nick);
        
        if (alquiler!=null) {
            // Si el valor es 1 significa que está reservando, si el valor es 2 el valor es 
            result.put("state", alquiler.getEstadoalquiler().getId()==2 ? "1" : "2");
            // Coges el tiempo que llevabas
            result.put("time", alquiler.getFechaInicio().getTime()+"");
            // Coges el noSerie de la scooter de la moto alquilada
            result.put("scooterID", alquiler.getScooter().getId()+"");
        } else {
            // Estado normal
            result.put("state", "0");
        }
        
        return result;
    }
    
    public Map<String, String> desconectar(Map<String, String> parameters) throws ServerExecutionException {
        Integer idThread = Integer.parseInt(parameters.get("idThread"));
        
        boolean desconectado = this.getServer().desconectarUsuario(idThread);
        
        if (!desconectado)
            throw new ServerExecutionException ("Error al intentar desconectar del servidor");
        
        Map<String, String> result = new HashMap<>();
        result.put("status", "ok");
        
        return result;
    }

    /**
     * Registra a un usuario en la plataforma
     */
    @Override
    public Map<String, String> register(Map<String, String> parameters) throws ServerExecutionException {
        // Saber si el email está actualmente en uso
        String email = parameters.get("email");
        // COMPROBAR SI EL EMAIL ESTÁ BIEN FORMADO
        Map<String,String> criterios = new HashMap<>();
        criterios.put("email", email);
        Cliente cliente = (Cliente) this.getHManager().getObjectCriterio("Cliente", criterios);
        if (cliente!=null) {
            throw new ServerExecutionException ("El email " + email+ " está actualmente en uso.");
        }
        
        // Saber si el nick del cliente ya existe
        String nick = parameters.get("nick");
        criterios = new HashMap<>();
        criterios.put("nick", nick);
        cliente = (Cliente) this.getHManager().getObjectCriterio("Cliente", criterios);
        if (cliente!=null) {
            throw new ServerExecutionException ("El nombre " + nick+ " existe actualmente. Elije otro.");
        }
        
        
        String pass = parameters.get("pass");
        
        Cliente nUsuario = new Cliente();
        nUsuario.setNick(nick);
        nUsuario.setEmail(email);
        nUsuario.setPass(pass);
        
        nUsuario.setNombre(parameters.get("nombre"));
        nUsuario.setApellido1(parameters.get("apellido1"));
        nUsuario.setApellido2(parameters.get("apellido2"));
        nUsuario.setFechaCreacion(new Date(System.currentTimeMillis()));
        nUsuario.setMinutos(0);
        nUsuario.setActivada((byte)1);
        
        Integer id = this.getHManager().addObject(nUsuario);
        
        if (id==null || id<=0) {
            throw new ServerExecutionException("No se ha podido crear la cuenta. Intentelo de nuevo más tarde");
        }
        
        criterios.put("pass", pass);
        
        // Por último nos logueamos
        criterios.put("idThread", parameters.get("idThread"));
        Map<String,String> clienteInfo = login(criterios);
        
        return clienteInfo;
    }
}
