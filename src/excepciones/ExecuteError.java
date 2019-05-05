/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excepciones;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author agarcia.gonzalez
 */
public class ExecuteError extends Exception{
    Map<String,String> params;

    public ExecuteError(Map<String,String> params) {
        this.params = params;
    }

    public ExecuteError() {
    }

    public ExecuteError(String message, Map<String,String> params) {
        super(message);
        this.params = params;
        if (this.params==null) 
            this.params=new HashMap<String,String>();
        
        this.params.put("error", message);
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
    
    
}
