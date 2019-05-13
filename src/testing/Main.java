/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;

import configuration_server.ConfigurationMapper;
import configuration_server.ConfigurationSAXMapper;
import entidades.Cliente;
import excepciones.MapperException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import util.HibernateManager;
import util.HibernateUtil;
import util.PaqueteCliente;
import util.PaqueteServidor;

/**
 *
 * @author agarcia.gonzalez
 */
public class Main {
    public static void main(String[] args)  {
        
        //String converted = "200;null;n&pick&djose;apellido2&dGonzalez;activada&d1;apellido1&dGarcía;fechaCreacion&d2019-05-05 00&d20&d52.0;id&d2;minutos&d60;nombre&dJose;email&dadriangarciagon@hotmail.com;token&d280347450";
        
        
        /*String texto = "abc&phola";
        String converted = texto.replaceAll("[&]", "&a").replaceAll("[|]", "&p").replaceAll("[;]", "&c").replaceAll("[:]", "&d");
        
        String reconverted = converted.replaceAll("&p", "|").replaceAll("&c", ";").replaceAll("&d", ":").replaceAll("&a", "&");
        
        
        System.out.println(reconverted);*/
        //System.out.println((texto.equals(reconverted)?"Son iguales":"No son iguales"));
        
        String texto = "0;1;2;login;nick:jose;scooter:{id:1;modelo:{id:1;marca:fiat}};valor:{credito:euros;cantidad:2}";
        
        Map<String,String> objetos = new HashMap<>();
        
        int firstPos = -1;
        int lastPos = -1;
        int founds = 0;
        int changed = 0;
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c=='{') {
                if (founds==0) {
                    firstPos=i;
                    
                }
                founds++;
                System.out.println("ENCONTRADO " + founds);
                
            } else if (founds>0 && c=='}') {
                founds--;
                if (founds==0) {
                    lastPos=i;
                    
                    String replace = texto.substring(firstPos+1, lastPos);
                    String toReplace = "OBJECT0#"+changed;
                    
                    objetos.put(toReplace, replace);
                    
                    
                    changed++;
                    
                    texto = texto.replaceFirst(Pattern.quote(replace), toReplace);
                    
                    i=firstPos+1;
                    
                    firstPos=-1;
                    lastPos=-1;
                    
                    //System.out.println("founds: " + founds);
                }
            }
        }
        
        
        System.out.println(texto);
        
        for (Map.Entry<String, String> entry : objetos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            
            System.out.println(key+ ": " + value);
            
        }

        PaqueteCliente pCliente = util.Util.unpackToCliente(texto);
        
        PaqueteServidor pServer = new PaqueteServidor();
        
        pServer.setArgumentos(pCliente.getArgumentos());
        pServer.setIdPaquete(pCliente.getIdPaquete());
        pServer.setNick("Adri");
        pServer.setToken("123");
        pServer.setUri("login");
        
        String regreso = util.Util.packFromServer(pServer);
        
        for (Map.Entry<String, String> entry : objetos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            
            regreso = regreso.replaceFirst(key, value);
        }
        
        System.out.println(regreso);
        
        TreeMap<String,String> tm = new TreeMap<>();
        
        tm.put("array2", "que");
        tm.put("array1", "Hola");
        tm.put("array3", "hace");
        
        for (Map.Entry<String, String> entry : tm.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            
            System.out.println(key + " - " + value);
        }
        
    }
    
    private void ejecutarMetodo () throws MapperException {
        ConfigurationSAXMapper saxFactory = new ConfigurationSAXMapper();
        
        ConfigurationMapper mapper = saxFactory.createMapper();
        
        // Almaceno lo que devuelve el método creado reflexivamente mediante el mapeo
        Map<String, String> parametros = new HashMap<>();
        parametros.put("nick", "jose");
        parametros.put("pass", "1234");
        
        //Map<String, String> map = (Map) mapper.executeMethod("test", parametros);
        String texto = (String) mapper.executeMethod("test", "Hoola");
        
        System.out.println("Ejecutado: " + texto);
    }
    
    private void addObjectToAMap() {
        Class clase = Cliente.class;
        Field[] campos = clase.getDeclaredFields();
        
        Cliente c = new Cliente();
        c.setNick("Adrian");
        
        Map<String,String> parametros = new HashMap<>();
        
        for (Field f : campos) {
            String genericType = f.getGenericType().toString().split(" ")[0];
            
            if (!genericType.equals("interface")) {
                try {
                    boolean ignore = false;
                    Annotation[] anotaciones = f.getAnnotations();
                    for (Annotation a : anotaciones) {
                        if (a.annotationType().getSimpleName().equals("Ignore")){
                            ignore=true;
                            break;
                        }
                    }
                    if (ignore) continue;
                    
                    String methodName = f.getName();
                    methodName = "get" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
                    
                    Method metodo = clase.getMethod (methodName);
                    Object o = metodo.invoke(c);
                    
                    
                    
                    parametros.put(f.getName(), o==null?null:o.toString());
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    System.err.println("Error en "+ f.getName() + ": "+ex.getMessage() + " ("+ex.getClass().getName()+")");
                } 
                
            }
        }
        
        System.out.println("Campos: "+parametros.size());
        for (Map.Entry<String, String> entry : parametros.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            
            System.out.println(key + ": " + value);
        }
    }
    
    private void addObjectHibernateTest () {
        Cliente c = new Cliente();
        c.setNombre ("Jose");
        c.setApellido1("García");
        c.setApellido2("Gonzalez");
        
        c.setNick("adri");
        c.setEmail("adriangarciagon@hotmail.com");
        c.setPass("1234");
        
        c.setMinutos(60);
        c.setActivada((byte)1);
       
        c.setFechaCreacion(new Date (System.currentTimeMillis()));
        
        HibernateManager mh = new HibernateManager();
        
        mh.addObject(c);
    }
    
    private void getObjectsHibernate () {
        HibernateManager mh = new HibernateManager();
        
        Map<String,String> criterios = new HashMap<String,String>();
        criterios.put("nick", "test0");
        criterios.put("pass", "1234");
        
        List<Cliente> cuentas = mh.getObjectsCriterio("Cliente", criterios);
        
        System.out.println("Clientes: ");
        for (Iterator<Cliente> iterator = cuentas.iterator(); iterator.hasNext();) {
            Cliente next = iterator.next();
            
            System.out.println("-> "+ next.getNombre());
        }
        
        HibernateUtil.getSessionFactory().close();
    }
}