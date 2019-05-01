/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration_server;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 *
 * @author agarcia.gonzalez
 */
public class ConfigurationMapper {
    
    private String url;
    private String port;
    
    private ArrayList<ConfigurationMethod> metodos;

    public ConfigurationMapper() {
    }

    public ConfigurationMapper(String url, String port, ArrayList<ConfigurationMethod> metodos) {
        this.url = url;
        this.port = port;
        this.metodos = metodos;
    }

    public ConfigurationMapper(String url, String port) {
        this.url = url;
        this.port = port;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public ArrayList<ConfigurationMethod> getMetodos() {
        return metodos;
    }

    public void setMetodos(ArrayList<ConfigurationMethod> metodos) {
        this.metodos = metodos;
    }
}
