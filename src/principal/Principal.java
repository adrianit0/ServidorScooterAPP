/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import java.io.IOException;
import java.net.ServerSocket;
import servidor.ScooterServerThread;
import servidor.ScooterServerUDP;

/**
 *
 * @author agarcia.gonzalez
 */
public class Principal {
    public static void main(String[] args) throws IOException {
        ScooterServerUDP servidorUDP = new ScooterServerUDP();
        servidorUDP.start();
        
        //Creamos el socket para UTP, para las partidas
        ServerSocket serverSocket = null;
        boolean listening = true;

        try {
            serverSocket = new ServerSocket(servidorUDP.port);
        } catch (IOException e) {
            System.err.println("No puedo escuchar el puerto: 4444.");
            return;
        }

        while (listening)
	    new ScooterServerThread(serverSocket.accept(), servidorUDP).start();

        serverSocket.close();
    }
}
