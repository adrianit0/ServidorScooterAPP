/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration_server;

import excepciones.ServerExecutionException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author agarcia.gonzalez
 */
public class ConfigurationMethod {
    
    private String uri;
    
    private Object instance;
    private Method metodo;
    
    private ArrayList<Class> params;
    private String returnParam; // null es que devuelve VOID
    
    private boolean token;
    
    private ArrayList<Rol> whiteList;
    private ArrayList<Rol> blackList;

    public ConfigurationMethod() {
        
    }

    public ConfigurationMethod(String uri, Object instance, Method metodo, ArrayList<Class> params, String returnParam, boolean token, ArrayList<Rol> whiteList, ArrayList<Rol> blackList) {
        this.uri = uri;
        this.instance = instance;
        this.metodo = metodo;
        this.params = params;
        this.returnParam = returnParam;
        this.token = token;
        this.whiteList = whiteList;
        this.blackList = blackList;
    }
    
    public Object invoke (Object... par) {
        int size = (params==null)?0:params.size();
        
        if (par.length!=size) {
            if (!(par.length==1 && par[0] instanceof Map)) {
                System.err.println("Los parametros dados no son los mismos");
                return null;
            } else {
                par = new Object[0];
            }
        }
        
        // TODO: Comprobar que los parametros dados son los correctos.
        try {
            return metodo.invoke(instance, par);
        } catch (IllegalAccessException | IllegalArgumentException  ex) {
            System.err.println("Error de invocación del método: " + ex);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof ServerExecutionException) {
                ServerExecutionException error = (ServerExecutionException) e.getCause();
                
                return error.getParams();
            }
            
            System.err.println("ConfigurationMethod::invoke error: " + e.getCause().getMessage());
            if (e!=null && e.getCause()!=null)
                e.getCause().printStackTrace();
            
        }
        return null;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }    
    
    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public Method getMetodo() {
        return metodo;
    }

    public void setMetodo(Method metodo) {
        this.metodo = metodo;
    }

    public ArrayList<Class> getParams() {
        return params;
    }

    public void setParams(ArrayList<Class> params) {
        this.params = params;
    }

    public String getReturnParam() {
        return returnParam;
    }

    public void setReturnParam(String returnParam) {
        this.returnParam = returnParam;
    }

    public boolean isToken() {
        return token;
    }

    public void setToken(boolean token) {
        this.token = token;
    }

    public ArrayList<Rol> getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(ArrayList<Rol> whiteList) {
        this.whiteList = whiteList;
    }

    public ArrayList<Rol> getBlackList() {
        return blackList;
    }

    public void setBlackList(ArrayList<Rol> blackList) {
        this.blackList = blackList;
    }
    
    
}
