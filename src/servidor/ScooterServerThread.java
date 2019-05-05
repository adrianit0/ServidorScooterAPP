/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import util.Paquete;
import util.Util;
import util.Util.CODIGO;

/**
 *
 * @author Adrián García
 */
public class ScooterServerThread extends Thread {

    private Socket socket = null;
    private ScooterServerUDP servidor;
    
    public ScooterServerThread(Socket socket, ScooterServerUDP servidor) {
	super("AhorcadoMultiServer");
	this.socket = socket;
        this.servidor = servidor;
    }
    
    @Override
    public void run() {
        try (   PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); ) {
            
            String inputLine, outputLine;
            
            while ((inputLine = in.readLine()) != null) {
                
                //outputLine = processInput(inputLine);
                //out.println(outputLine);
                
                Paquete paquete = Util.unpack(inputLine);
                CODIGO cod = paquete.getCodigo();
                if (cod==null || cod==CODIGO.desconectar) {
                    System.err.println("Thread desconectado, COD: " + cod);
                    break;
                }
                
                
                //out.println(outputLine);
            }
            socket.close();
            System.out.println("Thread cerrado");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
}
