/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excepciones;

import java.util.HashMap;
import java.util.Map;
import util.Util.CODIGO;


/**
 *
 * Esta excepción es muy importante ya que si se tira dentro del controlador el
 * servidor responderá correctamente y devolverá el mensaje de error al cliente.
 * 
 * @author agarcia.gonzalez
 */
public class ServerExecutionException extends Exception{
    Map<String,String> params;
    
    public ServerExecutionException(String message, Map<String,String> params) {
        super(message);
        this.params = params;
        if (this.params==null) 
            this.params=new HashMap<String,String>();
        
        this.params.put("error", message.isEmpty() ? "Error desconocido" : message);
    }

    public ServerExecutionException(Map<String,String> params) {
        this("", params);
    }

    public ServerExecutionException() {
        this("", new HashMap<>());
    }
    
    public ServerExecutionException (String message) {
        this(message, new HashMap<>());
    }
    
    public ServerExecutionException (String message, CODIGO code) {
        this(message, new HashMap<>());
        
        params.put("codeID", code.getCodigo()+"");
        params.put("codeName", code.toString());
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
    
    
}
