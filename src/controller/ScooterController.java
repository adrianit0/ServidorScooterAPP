/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import configuration_server.GenericController;
import entidades.Modelo;
import entidades.Scooter;
import excepciones.ExecuteError;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import servidor.ClienteInfo;

/**
 *
 * @author agarcia.gonzalez
 */
public class ScooterController extends GenericController {
    
    
    public Map<String,String> login (Map<String,String> parametros) throws ExecuteError {
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
            throw new ExecuteError ("Error de parseo", null);
        }
        
        if (noSerie==null || noSerie.isEmpty()) {
            throw new ExecuteError("Scooter no tiene no de serie");
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
                throw new ExecuteError("Se intenta crear una Scooter con un código que tiene otra Scooter. Cambiale el código.");
            }
            
            Modelo modeloScooter = (Modelo) getMh().getObject(Modelo.class, 1);
            
            if (modeloScooter==null)
                throw new ExecuteError("No existe el modelo actual de la Scooter");
            
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
                throw new ExecuteError("No se ha podido registrar la Scooter en la base de datos. No Serie: " + noSerie);
            }
            
            nScooter.setId(id);
            scooter = nScooter;
        } else {
            scooter.setPosicionLon(longitud);
            scooter.setPosicionLat(latitud);
            
            this.getHManager().updateObject(scooter);
        }
        
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
    
    public Map<String,String> getScootersCercanas (Map<String,String> parametros) throws ExecuteError {
        Double lat;
        Double lon;
        
        final Double diferencia = 0.001d; // No se cuanta diferencia es esto
        
        if (!parametros.containsKey("lat")|| !parametros.containsKey("lon")) {
            throw new ExecuteError ("Faltan parametros para realizar la consulta");
        }
        
        try {
            lat = Double.parseDouble(parametros.get("lat"));
            lon = Double.parseDouble(parametros.get("lon"));
        } catch (NumberFormatException e) {
            throw new ExecuteError ("Valores de Latitud y Longitud dados incorrectamente");
        }
        
        List<Scooter> scooters = this.getServer().getScooters();
        
        // FILTRAR
        List<Scooter> encontradas = new ArrayList<>();
        for (Scooter s : scooters) {
            
        }
        
        Map<String,String> lista = util.Util.convertListToMap(scooters);
        lista.put("length", scooters.size()+"");
        
        return lista;
    }
    
}
