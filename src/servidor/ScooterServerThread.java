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
import util.Util;
import util.Util.CODIGO;

/**
 *
 * @author Adrián García
 */
public class ScooterServerThread extends Thread {

    private Socket socket = null;
    private ScooterServerUDP servidor;
    // Token del cliente
    private String token;

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
                
                outputLine = processInput(inputLine);
                out.println(outputLine);
                
                String[] mensaje = Util.decode(inputLine);
                CODIGO cod = (mensaje.length>0) ? CODIGO.fromCode(mensaje[0]) : CODIGO.desconectar;
                
                if (cod==CODIGO.desconectar)
                    break;
            }
            socket.close();
            System.out.println("Thread cerrado");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    public String processInput(String in) {
        String out = "";

        boolean puedeRealizarAccion = servidor.puedeRealizarAccion(in);
        if (puedeRealizarAccion) {
            token = in;
        } else {
            return Util.encode(CODIGO.error, "Token no permitido");
        }
        
        return out;
    }
}
