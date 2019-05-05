/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration_server;

import excepciones.MapperException;
import excepciones.MethodNotExecutedException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author agarcia.gonzalez
 */
public class ConfigurationSAXMapper {

    private static String FICHERO = "config.xml";

    public ConfigurationMapper createMapper () {
        ConfigurationMapper configuracion = null;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            // SAXParser es quien lee el documento XML
            SAXParser saxParser;
            saxParser = factory.newSAXParser();
            
            ConfigurationHandler handler = new ConfigurationHandler();
            saxParser.parse(new File("src/" + FICHERO), handler);

            // Ya está el manejador funcionando
            configuracion = handler.getMapper();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(ConfigurationSAXMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return configuracion;
    }

    // Necesitamos el manejador.
}

class ConfigurationHandler extends DefaultHandler {

    private ConfigurationMapper mapper;
    private ConfigurationMethod tempMethod;   // Variable temporal para almacenar el método
    
    private Class clase;
    private String nombreMethod;
    
    private ArrayList<Class> tempParams;
    private Map<String, Object> objetosInstanciados;
    
    private StringBuilder buffer;

    public ConfigurationHandler() {
        mapper = new ConfigurationMapper();
        buffer = new StringBuilder();
        objetosInstanciados = new HashMap<String,Object>();
    }
    
    public Class getParamType (String classType) {
        classType = classType.toLowerCase();
        
        switch(classType) {
            case "string":
                return String.class;
            case "int":
            case "integer":
                return Integer.class;
            case "double":
                return Double.class;
            case "bool":
            case "boolean":
                return Boolean.class;
            case "map":
            case "parameter":
            case "parameters":
                return Map.class;
            // TODO: Añadir más tipos primitivos
                
            default:
                // TODO: Añadir tipos DTO creados mediante mapeo
                System.err.println(classType + " no es un tipo compatible con la parametrizazion");
                return null;
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case "configuration-mapper":
            case "properties":
            case "mappers":
                break;
            case "mapping":
                tempMethod = new ConfigurationMethod();
                tempMethod.setToken(attributes.getValue("token").toLowerCase().equals("true"));
                tempMethod.setUri(attributes.getValue("link"));
                tempMethod.setReturnParam(attributes.getValue("return"));
                
                nombreMethod = attributes.getValue("method");
                
                String nombreClase = attributes.getValue("className");
        
                try {
                    clase = Class.forName(nombreClase);
                    Object obj;
                    
                    // Buscamos si ya fue creado previamente el objeto
                    if (objetosInstanciados.containsKey(nombreClase)) {
                        obj = objetosInstanciados.get(nombreClase);
                    } else {
                        obj  = clase.newInstance();
                        objetosInstanciados.put(nombreClase, obj);
                    }
                    
                    // Añadimos la instancia del método
                    tempMethod.setInstance(obj);
                    
                } catch (ClassNotFoundException ex) {
                    System.err.println("Clase "+nombreClase+" no encontrada: " + ex);
                } catch (InstantiationException ex) {
                    System.err.println("Error de instanciación: " + ex);
                } catch (IllegalAccessException ex) {
                    System.err.println("Error de acceso: " +ex);
                }
                break;
            case "params":
                tempParams = new ArrayList<Class>();
                break;
            case "param":
                Class clase = getParamType (attributes.getValue("type"));
                if (clase!=null)
                    tempParams.add(clase);
                break;
            
            // Contenido en el que tiene que leer cosas dentro de las etiquetas
            case "url":
            case "port":
                buffer.delete(0, buffer.length());
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case "mapping":
                mapper.addMethod(tempMethod);
                break;
            case "params":
                tempMethod.setParams(tempParams);
                Class[] clases = new Class[tempParams.size()];
                clases = tempParams.toArray(clases);
                
                try {
                    Method metodo = clase.getMethod(nombreMethod, clases);
                    tempMethod.setMetodo(metodo);
                } catch (NoSuchMethodException | SecurityException ex) {
                    System.err.println("Error de creación del método: " + ex);
                }
                break;
            case "url":
                mapper.setUrl(buffer.toString());
                break;
            case "port":
                mapper.setPort(buffer.toString());
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        buffer.append(ch, start, length);
    }

    public ConfigurationMapper getMapper() {
        return mapper;
    }
}
