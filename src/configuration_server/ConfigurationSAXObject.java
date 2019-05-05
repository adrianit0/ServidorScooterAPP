/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration_server;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author agarcia.gonzalez
 */
public class ConfigurationSAXObject {

    public Object createDTOMapper (String direccion) {
        Object dto = null;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            // SAXParser es quien lee el documento XML
            SAXParser saxParser;
            saxParser = factory.newSAXParser();
            
            ObjectHandler handler = new ObjectHandler();
            saxParser.parse(new File("src/" + direccion), handler);
            
            // Añadir el DTO
            //dto = handler.getDTO();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        } 
        
        return dto;
    }
}

class ObjectHandler extends DefaultHandler {
    // Crear el manejador para DTO
    
    // Cada parametro creado debe tener 2 métodos: el set y el get.
    // La regla utiliza será la siguiente:
    
    // get o set seguido del nombre del parametro con la primera letra en mayuscula.
    // Si es booleano, el get será remplazo por is
}
