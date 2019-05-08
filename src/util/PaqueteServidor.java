/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Map;
import util.Util.CODIGO;

/**
 *
 * Paquete que le llegar del cliente al servidor con la información del mismo
 * 
 * @author agarcia.gonzalez
 */
public class PaqueteServidor {
    private String idPaquete;
    private String nick;
    private String token;
    private String uri;
    
    private Map<String,String> argumentos;

    public PaqueteServidor() { }

    public PaqueteServidor(String idPaquete, String nick, String token, String uri, Map<String, String> argumentos) {
        this.idPaquete = idPaquete;
        this.nick = nick;
        this.token = token;
        this.uri = uri;
        this.argumentos = argumentos;
    }

    public String getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(String idPaquete) {
        this.idPaquete = idPaquete;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Map<String, String> getArgumentos() {
        return argumentos;
    }

    public void setArgumentos(Map<String, String> argumentos) {
        this.argumentos = argumentos;
    }

    
    
    
}
