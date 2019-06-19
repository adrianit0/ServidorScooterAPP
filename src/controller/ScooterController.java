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
import entidades.Modelo;
import entidades.Scooter;
import excepciones.AlquilerException;
import excepciones.ServerExecutionException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import servidor.ClienteInfo;
import util.HibernateManager;
import util.Util.CODIGO;

/**
 *
 * @author agarcia.gonzalez
 */
public class ScooterController extends GenericController {
    
    public Map<String,String> login (Map<String,String> parametros) throws ServerExecutionException {
        float bateria;
        double latitud;
        double longitud;
        int codigo;
        int idThread;
        boolean bloqueado;
        String noSerie;
        
        try {
            bateria = Float.parseFloat(parametros.get("bateria"));
            latitud = Double.parseDouble(parametros.get("latitud"));
            longitud = Double.parseDouble(parametros.get("longitud"));
            codigo = Integer.parseInt(parametros.get("codigo"));
            idThread = Integer.parseInt(parametros.get("idThread"));
            noSerie = parametros.get("noSerie");
            bloqueado = Boolean.parseBoolean(parametros.get("bloqueado"));
        } catch (NumberFormatException e) {
            throw new ServerExecutionException ("Error de parseo");
        }
        
        if (noSerie==null || noSerie.isEmpty()) {
            throw new ServerExecutionException("Scooter no tiene no de serie");
        }
        
        Map<String,String> criterio = new HashMap<>();
        criterio.put("codigo", codigo+"");
        Scooter scooter = (Scooter) this.getMh().getObjectCriterioWithoutLazyObjects("Scooter", criterio, "getModelo");
        
        // La scooter no existe
        if (scooter==null) {
            System.out.println("La scooter con noSerie "+ noSerie+" no existe, se procederá a registrarla");
            Scooter nScooter = new Scooter();
            
            criterio = new HashMap<>();
            criterio.put("codigo", codigo+"");
            scooter=(Scooter) this.getMh().getObjectCriterioWithoutLazyObjects("Scooter", criterio, "getModelo");
            if (scooter!=null) {
                throw new ServerExecutionException("Se intenta crear una Scooter con un código que tiene otra Scooter. Cambiale el código.");
            }
            
            Modelo modeloScooter = (Modelo) getMh().getObject(Modelo.class, 1);
            
            if (modeloScooter==null)
                throw new ServerExecutionException("No existe el modelo actual de la Scooter");
            
            nScooter.setCodigo(codigo);
            nScooter.setModelo(modeloScooter);
            nScooter.setMatricula(codigo+""); // CAMBIAR POR MATRICULA EN AJUSTES, EL SOFTWARE DE LA SCOOTER NO SABE LA MATRICULA QUE TIENE
            nScooter.setNoSerie(noSerie);
            nScooter.setPosicionLon(longitud);
            nScooter.setPosicionLat(latitud);
            nScooter.setPrecioCompra(0);
            nScooter.setFechaCompra(new Date(System.currentTimeMillis()));
            
            Integer id = this.getMh().addObject(nScooter);
            
            if (id==null || id<0) {
                throw new ServerExecutionException("No se ha podido registrar la Scooter en la base de datos. No Serie: " + noSerie);
            }
            
            nScooter.setId(id);
            scooter = nScooter;
        } else if (scooter.getFechaBaja()!=null) {
            throw new ServerExecutionException ("La scooter se ha dado de baja el día: " + scooter.getFechaBaja().toString(), util.Util.CODIGO.notFound);
        } else {
            scooter.setPosicionLon(longitud);
            scooter.setPosicionLat(latitud);
            
            this.getHManager().updateObject(scooter);
        }
        
        scooter.setBateria(bateria);
        scooter.setBloqueada(false);
        scooter.setIdThread(idThread);
        
        ClienteInfo info = new ClienteInfo();
        info.setId(scooter.getId());
        info.setNombre(noSerie);
        info.setRol(Rol.SCOOTER);
        info.setIdThread(idThread);
        String token = this.getServer().conectarUsuario(info);
        
        this.getServer().addScooter(scooter);
        
        Map<String,String> result = new HashMap<>();
        result.put("token", token);
        
        return result;
    }
    
    public Map<String,String> getScootersCercanas (Map<String,String> parametros) throws ServerExecutionException {
        Double lat;
        Double lon;
        
        final Double diferencia = 0.001d; // No se cuanta diferencia es esto
        
        if (!parametros.containsKey("lat")|| !parametros.containsKey("lon")) {
            throw new ServerExecutionException ("Faltan parametros para realizar la consulta");
        }
        
        try {
            lat = Double.parseDouble(parametros.get("lat"));
            lon = Double.parseDouble(parametros.get("lon"));
        } catch (NumberFormatException e) {
            throw new ServerExecutionException ("Valores de Latitud y Longitud dados incorrectamente");
        }
        
        List<Scooter> scooters = this.getServer().getScooters();
        
        // FILTRAR
        List<Scooter> encontradas = new ArrayList<>();
        for (Scooter s : scooters) {
            if (s.isBloqueada() && 
                    isInRange(lat, s.getPosicionLat()-diferencia, s.getPosicionLat()+diferencia) &&
                    isInRange(lon, s.getPosicionLon()-diferencia, s.getPosicionLon()+diferencia) )
                continue;
            encontradas.add(s);
        }
        
        Map<String,String> lista = util.Util.convertListToMap(encontradas);
        int i = 0;
        for (Scooter s : encontradas) {
            lista.put("modelo[" + i + "]", s.getModelo().toString());
            i++;
        }
        
        return lista;
    }
    
    private static boolean isInRange (Double valor, Double valorMenor, Double valorMayor) {
        return valor>=valorMenor && valor<=valorMayor;
    }
    
    public Map<String,String> getScooterById (Map<String,String> parametros ) throws ServerExecutionException {
        int id = Integer.parseInt(parametros.get("id"));
        Scooter scooter = (Scooter) this.getHManager().getObject(Scooter.class, id);
        if (scooter==null)
            throw new ServerExecutionException("No se ha encontrado la Scooter con ID " + id);
        scooter = getServer().getScooter(scooter.getNoSerie());
        Map<String,String> resultado = util.Util.convertObjectToMap(scooter);
        return resultado;
    }
    
    public Map<String, String> getScootersBBDD() throws ServerExecutionException {
        List<Scooter> scooters = this.getHManager().getObjects("Scooter");
        
        if (scooters==null || scooters.isEmpty()) {
            throw new ServerExecutionException ("No se ha encontrado ninguna scooter");
        }
        
        Map<String, String> result = util.Util.convertListToMap(scooters);
        return result;
    }
    
    public Map<String, String> getScooterBBDD (Map<String,String> parametros) throws ServerExecutionException {
        Integer id = Integer.parseInt(parametros.get("id"));
        
        Scooter scooter = (Scooter) this.getHManager().getObjectWithoutLazyObjects(Scooter.class, id, "getModelo");
        
        if (scooter==null) 
            throw new ServerExecutionException ("No se ha encontrado a la scooter con ID " + id);
        
        Map<String, String> result = util.Util.convertObjectToMap(scooter);
        result.put("modelo", scooter.getModelo().getId()+"");
        return result;
    }
    
    public Map<String,String> reservarScooter (Map<String,String> parametros) throws ServerExecutionException {
        String noSerie = parametros.get("noSerie");
        String token = parametros.get("token");
        
        Scooter scooter = this.getServer().getScooter(noSerie);
        
        if (scooter==null)
            throw new ServerExecutionException ("La scooter seleccionada no está disponible actualmente. Seleccione otra o intentelo más tarde.");
        
        if (scooter.isBloqueada()) 
            throw new ServerExecutionException ("La scooter seleccionada ha sido bloqueada por otro usuario. Seleccione otra");
        
        try {
            this.getServer().reservarScooter(token, scooter);
        } catch (AlquilerException e) {
            throw new ServerExecutionException (e.getMessage());
        }
        
        Map<String,String> resultado = new HashMap<>();
        resultado.put("status", "ok");
        
        return resultado;
    }
    
    public Map<String,String> cancelarReservaScooter (Map<String,String> parametros) throws ServerExecutionException {
        String noSerie = parametros.get("noSerie");
        String token = parametros.get("token");
        
        Scooter scooter = this.getServer().getScooter(noSerie);
        
        if (scooter==null)
            throw new ServerExecutionException ("La scooter reservada no está disponible actualmente.");
        
        if (!scooter.isBloqueada()) 
            throw new ServerExecutionException ("La reserva ya estaba cancelada.");
        
        try {
            this.getServer().cancelarReservaScooter(token, scooter);
        } catch (AlquilerException e) {
            throw new ServerExecutionException (e.getMessage());
        }
        
        Map<String,String> resultado = new HashMap<>();
        resultado.put("status", "ok");
        
        return resultado;
    }
    
    public Map<String,String> empezarAlquiler (Map<String,String> parametros) throws ServerExecutionException {
        String noSerie = parametros.get("noSerie");
        String token = parametros.get("token");
        int codigo;
        try {
            codigo =  Integer.parseInt(parametros.get("codigo"));
        } catch (NumberFormatException e) {
            throw new ServerExecutionException ("Trama de datos mal formada: " + e.getMessage());
        }
        
        Scooter scooter = this.getServer().getScooter(noSerie);
        
        if (scooter==null)
            throw new ServerExecutionException ("La scooter reservada no está disponible actualmente.");
        
        if (scooter.getCodigo() != codigo)
            throw new ServerExecutionException ("El código no coincide, asegurate de seleccionar la Scooter correcta.");
        
        if (!scooter.isBloqueada()) 
            throw new ServerExecutionException ("La reserva ha sido cancelada, vuelvelo a intentar");
        
        try {
            getServer().empezarAlquiler(token, scooter);
        } catch (AlquilerException e) {
            throw new ServerExecutionException (e.getMessage());
        }
        
        // Le mandamos la señal a la scooter para que se encienda
        Map<String,String> scooterParametros = new HashMap<>();
        scooterParametros.put("status", "ok");
        String idPaquete = "desbloquear"+scooter.getCodigo();
        CODIGO cod = CODIGO.ok;
        
        try {
            this.getServer().sendMessageToSocket(scooter.getNoThread(), idPaquete, scooterParametros, cod);
        } catch (ServerExecutionException e) {
            System.err.println("Error thread: " + e.getMessage());
            throw new ServerExecutionException ("No se ha podido encender la scooter");
        }
        // fin señal
        
        Map<String,String> resultado = new HashMap<>();
        resultado.put("status", "ok");
        
        return resultado;
    }
    
    public Map<String,String> finalizarAlquiler (Map<String,String> parametros) throws ServerExecutionException {
        String token = parametros.get("token");
        
        Alquiler alquiler;
        try {
            alquiler = getServer().terminarAlquiler(token);
        } catch (AlquilerException e) {
            throw new ServerExecutionException (e.getMessage());
        }
        
        
        if (alquiler==null)
            throw new ServerExecutionException ("No existe alquiler para este usuario");
        
        // Le mandamos la señal a la scooter para que se encienda
        Map<String,String> scooterParametros = new HashMap<>();
        scooterParametros.put("status", "ok");
        String idPaquete = "bloquear"+alquiler.getScooter().getCodigo();
        CODIGO cod = CODIGO.ok;
        try {
            this.getServer().sendMessageToSocket(alquiler.getScooter().getNoThread(), idPaquete, scooterParametros, cod);
        } catch (ServerExecutionException e) {
            System.err.println("Error thread: " + e.getMessage());
            throw new ServerExecutionException ("No se ha podido apagar la scooter");
        }
        // fin señal
        
        Cliente cliente = alquiler.getCliente();
        
        Map<String,String> resultado = util.Util.convertObjectToMap(alquiler);
        resultado.put("minutosRestante", cliente.getMinutos()+""); 
        resultado.put("status", "ok");
        
        return resultado;
    }
    
    public Map<String,String> updateScooter (Map<String,String> parametros) throws ServerExecutionException {
        Scooter scooter = (Scooter) util.Util.convertMapToObject(Scooter.class, parametros);
        
        if (scooter.getMatricula().length()>7) {
            throw new ServerExecutionException ("La longitud de la matricula es demasiado grande");
        }
        
        HibernateManager hm = this.getHManager();
        Scooter lastScooter = (Scooter) hm.getObjectWithoutLazyObjects(Scooter.class, scooter.getId(), "getModelo");
        
        Integer modeloId = util.Util.parseInt(parametros.get("modeloId"));
        
        scooter.setModelo(modeloId!=null? new Modelo(modeloId) : lastScooter.getModelo());
        scooter.setFechaCompra(lastScooter.getFechaCompra());
        scooter.setPosicionLat(lastScooter.getPosicionLat());
        scooter.setPosicionLon(lastScooter.getPosicionLon());
        
        if (parametros.get("precioCompra")==null)
            scooter.setPrecioCompra(lastScooter.getPrecioCompra());
        
        Boolean editado = hm.updateObject(scooter);
        
        if (!editado)
            throw new ServerExecutionException ("No se ha podido editar la scooter", parametros);
        
        Map<String,String> result = util.Util.convertObjectToMap(scooter);
        return result;
    }
    
    public Map<String,String> deleteScooter (Map<String,String> parametros) throws ServerExecutionException {
        Integer id = Integer.parseInt(parametros.get("id"));
        
        HibernateManager hm = this.getHManager();
        
        Scooter scooter = (Scooter) hm.getObject(Scooter.class, id);
        Boolean editado = false;
        try {
            editado = hm.deleteObject(scooter);
        } catch (Exception e) {
            throw new ServerExecutionException ("No se puede eliminar la scooter porque tiene dependencia con otras tablas. Eliminalas antes que esta.");
        }
        
        if (!editado)
            throw new ServerExecutionException ("No se ha podido eliminar la scooter", parametros);
        
        Map<String,String> result = util.Util.convertObjectToMap(scooter);
        return result;
    }
    
    public Map<String,String> darBajaScooter (Map<String,String> parametros) throws ServerExecutionException {
        Integer id = Integer.parseInt(parametros.get("id"));
        
        HibernateManager hm = this.getHManager();
        Scooter scooter = (Scooter) hm.getObject(Scooter.class, id);
        
        if (scooter==null)
            throw new ServerExecutionException("La scooter no existe");
        
        if (scooter.getFechaBaja()!=null)
            throw new ServerExecutionException("La scooter ya estaba dado de baja");
        
        scooter.setFechaBaja(new Date(System.currentTimeMillis()));
        boolean editado = hm.updateObject(scooter);
        
        if (!editado)
            throw new ServerExecutionException ("No se ha podido dar de baja a la scooter", parametros);
        
        Map<String,String> result = new HashMap<>();
        result.put("status", "ok");
        
        return result;
    }
    
}
