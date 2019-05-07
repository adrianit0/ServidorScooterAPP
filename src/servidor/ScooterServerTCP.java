/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import configuration_server.ConfigurationMapper;
import configuration_server.ConfigurationSAXMapper;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidorbd.ScooterBBDD;
import util.HibernateUtil;

/**
 *
 * @author Lara
 */
public class ScooterServerTCP {
    
    private ScooterBBDD bd;
    private ConfigurationMapper mapper;
    private final int port = 4445;
    
    private boolean listening;
    
    public void execute () throws IOException {
        //Servidor TCP, para las Scooters
        ServerSocket serverSocket = null;
        listening = true;
        
        System.out.println("Servidor inicializandose");
        
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("No puedo escuchar el puerto: " + port);
            return;
        }
        
        System.out.println("Configurando mapeadores de clases y métodos");
        ConfigurationSAXMapper saxFactory = new ConfigurationSAXMapper();
        mapper = saxFactory.createMapper();
        
        System.out.println("Configurando Hibernate");
        
        try {
            Class.forName("util.HibernateUtil");
        } catch (ExceptionInInitializerError e) {
            System.err.println("==============================================================");
            System.err.println("=  ERROR CRITICO DEL SERVIDOR ");
            System.err.println("==============================================================");
            System.err.println("Hibernate no pudo inicializarse debido a un error crítico: " + e.getCause().getMessage());
            System.err.println("Arregla este fallo antes de encender el servidor.");
            e.getCause().printStackTrace();
            System.exit(1);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ScooterServerTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        while (listening) {
            Socket socket = serverSocket.accept();
            new ScooterServerThread(socket, this).start();
        }
	
        serverSocket.close();
    }
    
    public void close() {
        listening = false;
    }
}
