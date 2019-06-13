/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import configuration_server.GenericController;
import entidades.Ciudad;
import excepciones.ServerExecutionException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Adrián
 */
public class ItemReducidoController extends GenericController {
    
    public Map<String,String> getCiudades (Map<String,String> parametros) throws ServerExecutionException {
        List<Ciudad> ciudades = this.getMh().getObjects("Ciudad");
        
        Map<String,String> resultado = util.Util.convertListToMap(ciudades);
        return resultado;
    }
    
    public Map<String,String> getPuestos (Map<String,String> parametros) throws ServerExecutionException {
        List puestos = this.getMh().getObjects("Puesto");
        
        Map<String,String> resultado = util.Util.convertListToMap(puestos);
        return resultado;
    }
    
    public Map<String,String> getSedes (Map<String,String> parametros) throws ServerExecutionException {
        List sedes = this.getMh().getObjects("Sede");
        
        Map<String,String> resultado = util.Util.convertListToMap(sedes);
        return resultado;
    }
    
    public Map<String,String> getModelos (Map<String,String> parametros) throws ServerExecutionException {
        List sedes = this.getMh().getObjects("Modelo");
        
        Map<String,String> resultado = util.Util.convertListToMap(sedes);
        return resultado;
    }   
}