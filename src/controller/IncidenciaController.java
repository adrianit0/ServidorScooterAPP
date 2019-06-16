/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import configuration_server.GenericController;
import entidades.Alquiler;
import entidades.Cliente;
import entidades.Incidencia;
import entidades.Scooter;
import entidades.Tipoincidencia;
import excepciones.ServerExecutionException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import servidor.ClienteInfo;

/**
 *
 * @author Adrián
 */
public class IncidenciaController extends GenericController {
    
    public Map<String,String> enviarIncidencia (Map<String,String> parametros) throws ServerExecutionException {
        Double lat = null;
        Double lon = null;
        String descripcion = parametros.get("descripcion");
        Integer tipoIncidencia = Integer.parseInt(parametros.get("tipoIncidencia"));
        String token = parametros.get("token");
        String codigo = parametros.get("codigo");
        
        if (parametros.containsKey("lat")) {
            lat = Double.parseDouble(parametros.get("lat"));
            lon = Double.parseDouble(parametros.get("lon"));
        }
        
        Map<String,String> criterio = new HashMap<>();
        criterio.put("codigo", codigo);
        Scooter scooter = (Scooter) this.getMh().getObjectCriterio("Scooter", criterio);
        
        if (scooter==null) 
            throw new ServerExecutionException ("El código enviado no corresponde a ninguna Scooter");
            
        ClienteInfo info = this.getServer().getClient(token);
        if (info==null)
            throw new ServerExecutionException("No hay usuario conectado en esta sesión");
        
        // Cogemos el alquiler, si lo tuviera
        Alquiler alquiler = this.getServer().getAlquiler(info.getNombre());
        
        // Creamos la incidencia
        Incidencia incidencia = new Incidencia();
        incidencia.setPosicionLat(lat);
        incidencia.setPosicionLon(lon);
        incidencia.setDescripcion(descripcion);
        incidencia.setTipoincidencia(new Tipoincidencia(tipoIncidencia));
        incidencia.setAlquiler(alquiler);
        incidencia.setCliente(new Cliente ((int) info.getId()));
        incidencia.setScooter(scooter);
        incidencia.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
        
        Integer id = this.getMh().addObject(incidencia);
        
        if(id==null) 
            throw new ServerExecutionException ("No se ha podido registrar la incidencia");
        
        Map<String,String> result = new HashMap<>();
        result.put("status", "ok");
        
        return result;
    }
    
}
