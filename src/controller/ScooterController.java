/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import configuration_server.GenericController;
import entidades.Modelo;
import entidades.Scooter;
import excepciones.ServerExecutionException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import servidor.ClienteInfo;

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
        
        Map<String,String> criterios = new HashMap<String,String>();
        
        criterios.put("noSerie", noSerie);
        Scooter scooter = (Scooter) this.getMh().getObjectCriterio("Scooter", criterios);
        
        // La scooter no existe
        if (scooter==null) {
            System.out.println("La scooter con noSerie "+ noSerie+" no existe, se procederá a registrarla");
            Scooter nScooter = new Scooter();
            
            criterios = new HashMap<>();
            criterios.put("codigo", codigo+"");
            scooter=(Scooter) this.getMh().getObjectCriterio("Scooter", criterios);
            if (scooter!=null) {
                throw new ServerExecutionException("Se intenta crear una Scooter con un código que tiene otra Scooter. Cambiale el código.");
            }
            
            Modelo modeloScooter = (Modelo) getMh().getObject(Modelo.class, 1);
            
            if (modeloScooter==null)
                throw new ServerExecutionException("No existe el modelo actual de la Scooter");
            
            nScooter.setCodigo(codigo);
            nScooter.setModelo(modeloScooter);
            nScooter.setMatricula(codigo+""); // CAMBIAR POR MATRICULA
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
        } else {
            scooter.setPosicionLon(longitud);
            scooter.setPosicionLat(latitud);
            
            this.getHManager().updateObject(scooter);
        }
        
        scooter.setBateria(bateria);
        scooter.setEstaBloqueada(false);
        
        ClienteInfo info = new ClienteInfo();
        info.setId(scooter.getId());
        info.setNombre(noSerie);
        info.setRol(ClienteInfo.Rol.SCOOTER);
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
            if (s.isEstaBloqueada())
                continue;
            encontradas.add(s);
        }
        
        Map<String,String> lista = util.Util.convertListToMap(encontradas);
        lista.put("length", encontradas.size()+"");
        
        return lista;
    }
    
    public Map<String,String> reservarScooter (Map<String,String> parametros) throws ServerExecutionException {
        String noSerie = parametros.get("noSerie");
        
        Scooter scooter = this.getServer().getScooter(noSerie);
        
        if (scooter==null)
            throw new ServerExecutionException ("La scooter seleccionada no está disponible actualmente. Seleccione otra o intentelo más tarde.");
        
        if (scooter.isEstaBloqueada()) 
            throw new ServerExecutionException ("La scooter seleccionada ha sido bloqueada por otro usuario. Seleccione otra");
        
        boolean reservado = this.getServer().reservarScooter(scooter);
        
        if (!reservado)
            throw new ServerExecutionException ("La scooter no se ha podido alquilar. Seleccione otra o intentelo más tarde.");
        
        Map<String,String> resultado = new HashMap<>();
        resultado.put("status", "ok");
        
        return resultado;
    }
    
    public Map<String,String> cancelarReservaScooter (Map<String,String> parametros) throws ServerExecutionException {
        String noSerie = parametros.get("noSerie");
        
        Scooter scooter = this.getServer().getScooter(noSerie);
        
        if (scooter==null)
            throw new ServerExecutionException ("La scooter reservada no está disponible actualmente.");
        
        if (!scooter.isEstaBloqueada()) 
            throw new ServerExecutionException ("La reserva ya estaba cancelada.");
        
        boolean cancelado = this.getServer().cancelarReservaScooter(scooter);
        
        if (!cancelado)
            throw new ServerExecutionException ("La scooter no se ha podido cancelar correctamente.");
        
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
        
        if (!scooter.isEstaBloqueada()) 
            throw new ServerExecutionException ("La reserva ha sido cancelada, vuelvelo a intentar");
        
        boolean alquilado = getServer().empezarAlquiler(token, scooter);
        
        if (!alquilado)
            throw new ServerExecutionException ("La scooter no se ha podido alquilar correctamente.");
        
        Map<String,String> resultado = new HashMap<>();
        resultado.put("status", "ok");
        
        return resultado;
    }
    
    public Map<String,String> finalizarAlquiler (Map<String,String> parametros) throws ServerExecutionException {
        return null;
    }
    
}
