/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import configuration_server.ConfigurationMapper;
import configuration_server.ConfigurationMethod;
import configuration_server.ConfigurationSAXMapper;
import excepciones.ServerExecutionException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import util.PaqueteServidor;
import util.Util;
import util.Util.CODIGO;

/**
 *
 * 
 * 
 * @author Adrián García
 */
@Deprecated
public class ScooterServerUDP extends Thread {

    protected DatagramSocket socket = null;
    private ConfigurationMapper mapper;

    public final int port = 4445;

    private LinkedHashMap<String, ScooterInfo> scooterConectadas;
    

    // Constantes
    

    // Valores para el foreach
    private String tokenEncontrado;

    public ScooterServerUDP() throws IOException {
        this("QuoteServerThread");
    }

    public ScooterServerUDP(String name) throws IOException {
        super(name);
        socket = new DatagramSocket(port);

    }

    public void run() {
        ConfigurationSAXMapper saxFactory = new ConfigurationSAXMapper();
        mapper = saxFactory.createMapper();

        while (true) {
            try {
                byte[] bufIn = new byte[1024]; //A buffer to get whatever information the client send with the request
                DatagramPacket packetIn = new DatagramPacket(bufIn, bufIn.length);

                // Esperamos a recibir el paquete
                socket.receive(packetIn);

                //System.out.println("Llegado información: " + new String(bufIn));
                // Cogemos la IP y puerto de la persona que recibimos el mensaje
                InetAddress address = packetIn.getAddress();
                int port = packetIn.getPort();

                // 
                //byte[] valores = ejecutarMetodo(new String(bufIn));

                // Pedimos el datagramPacket
                //DatagramPacket packetOut = new DatagramPacket(valores, valores.length, address, port);
                // Se envia
                //socket.send(packetOut);

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

        //3. Close the DatagramSocket
        socket.close();
    }
}
