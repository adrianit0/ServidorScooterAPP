/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import excepciones.ExecuteError;
import java.util.Map;

/**
 *
 * @author agarcia.gonzalez
 */
public interface IUsuarioController {
    
    
    public Map<String,String> login (Map<String, String> parameters) throws ExecuteError;
    
    public Map<String, String> register (Map<String, String> parameters);
}
