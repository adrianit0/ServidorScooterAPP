/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import configuration_server.ConfigurationMapper;
import configuration_server.ConfigurationSAXMapper;
import java.io.IOException;
import java.net.ServerSocket;
import servidor.ScooterServerThread;
import servidor.ScooterServerUDP;

/**
 *
 * @author agarcia.gonzalez
 */
public class Principal {
    
    
    private void execute () throws IOException {
        
        // Pruebas para el servidor UDP
        ScooterServerUDP servidorUDP = new ScooterServerUDP();
        servidorUDP.start();
        
        //Servidor TCP, para las Scooters
        /*ServerSocket serverSocket = null;
        boolean listening = true;
        
        try {
            serverSocket = new ServerSocket(servidorUDP.port);
        } catch (IOException e) {
            System.err.println("No puedo escuchar el puerto: " + servidorUDP.port);
            return;
        }
        
        System.out.println("Se inicia el servidor, empieza a buscar clientes.");

        while (listening)
	    new ScooterServerThread(serverSocket.accept(), servidorUDP).start();

        serverSocket.close();*/
    }
    
    
    public static void main(String[] args) throws IOException  {
        (new Principal()).execute();
    }
}
