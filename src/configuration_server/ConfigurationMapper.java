/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration_server;

import excepciones.MapperException;
import excepciones.MethodNotExecutedException;
import java.lang.reflect.InvocationTargetException;
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
    
    public void addMethod (ConfigurationMethod metodo) {
        if (this.metodos==null) 
            this.metodos = new ArrayList<ConfigurationMethod>();
        
        this.metodos.add(metodo);
    }
    
    public ConfigurationMethod getMethod (String uri) {
        for (ConfigurationMethod m : metodos) {
            if (m.getUri().equals(uri)) {
                return m;
            }
        }
        return null;
    }
    
    public Object executeMethod (String uri, Object... params) throws MapperException {
        try {
            for (ConfigurationMethod m : metodos) {
                if (m.getUri().equals(uri)) {
                    return m.invoke(params);
                }
            }

            // No ha encontrado ningún método para ejecutar
            throw new MethodNotExecutedException ("No se ha podido ejecutar el método "+uri+", no se encuentra en el mapper");
        } catch (MethodNotExecutedException e) {
            // Centralizamos todos los tipos de excepciones que se podrían generar en una sola excepcion
            throw new MapperException (e.getMessage(), e);
        }
    }
}
