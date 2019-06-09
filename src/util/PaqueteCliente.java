/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Map;
import java.util.TreeMap;
import util.Util.CODIGO;

/**
 *
 * Paquete que le llegar del servidor al cliente con la información del mismo
 * 
 * @author agarcia.gonzalez
 */
public class PaqueteCliente {
    private CODIGO codigo;
    private String idPaquete;
    
    private Map<String,String> argumentos;

    public PaqueteCliente() { }

    public PaqueteCliente(CODIGO codigo, String idPaquete, Map<String, String> argumentos) {
        this.codigo = codigo;
        this.idPaquete = idPaquete;
        this.argumentos = argumentos;
    }
    
    public CODIGO getCodigo() {
        return codigo;
    }

    public void setCodigo(CODIGO codigo) {
        this.codigo = codigo;
    }

    public String getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(String idPaquete) {
        this.idPaquete = idPaquete;
    }

    public Map<String, String> getArgumentos() {
        return argumentos;
    }

    public void setArgumentos(Map<String, String> argumentos) {
        this.argumentos = argumentos;
    }
}
