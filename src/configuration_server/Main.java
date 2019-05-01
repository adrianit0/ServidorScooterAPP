/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration_server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author agarcia.gonzalez
 */
public class Main {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        Class clase = Class.forName("test.Ejecutador");
        
        //System.out.println(clase.getSimpleName());
        
        Method metodo = clase.getMethod("Metodo", String.class);
        
        //System.out.println(metodo.getName());
        
        Object obj = clase.newInstance();
        
        String result = (String) metodo.invoke(obj, "hola xdd");
        
        System.out.println(result);
    }
}
