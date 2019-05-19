/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import configuration_server.ConfigurationMethod;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import util.PaqueteCliente;
import util.PaqueteServidor;
import util.Util;
import util.Util.CODIGO;

/**
 *
 * @author Adrián García
 */
public class ScooterServerThread extends Thread {

    private Socket socket;
    private ScooterServerTCP servidor;
    
    // Buffered
    private PrintWriter out;
    private BufferedReader in;
    
    private Integer idThread;
    
    private boolean listening;
    
    public ScooterServerThread(Socket socket, Integer idThread, ScooterServerTCP servidor) {
	super("ScooterAPP server");
	this.socket = socket;
        this.idThread = idThread;
        this.servidor = servidor;
        
        this.listening = true;
    }
    
    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("ScooterServerThread::run: No se puede hacer la conexión: " + e.getMessage());
        }
        
        String inputLine, outputLine;

        while (listening) {
            
            try {
                inputLine = in.readLine();

                outputLine = ejecutarMetodo (inputLine);
                
                out.println(outputLine);
                
            } catch (IOException e) {
                System.err.println("ScooterServerThread::run: No se ha podido realizar la consulta: " + e.getMessage());
                listening=false;
            }
        }
        
        try {
            boolean desconectado = servidor.desconectarUsuario(idThread);
            socket.close();
            
            if (desconectado) {
                System.out.println("ScooterServerThread::run: Thread cerrado satisfactoriamente");
            } else {
                System.out.println("ScooterServerThread::run: Thread cerrado, pero no se ha podido eliminar su información de sesión");
            }
            
        } catch (IOException e) {
            System.err.println("ScooterServerThread::run: No se puede cerrar el thread: " + e.getMessage() ) ;
        }
    }
    
    /**
     * Ejecuta el método que solicita el usuario y devuelve la respuesta, en
     * bytes
     */
    private String ejecutarMetodo(String contenido) {
        String error = "Undefined";
        PaqueteServidor packServer = Util.unpackToServer(contenido);

        ConfigurationMethod metodo = servidor.getMethod(packServer.getUri());

        boolean errores = false;

        if (metodo == null) {
            System.err.println("Método " + packServer.getUri() + " no existe");
            errores = true;
            packServer.getArgumentos().put("error", "Método " + packServer.getUri() + " no existe");
        }

        // Si el método necesita token, se tendrá que comprobar que el usuario/token es el mismo
        if (!errores && metodo.isToken()) {
            boolean puedeRealizarAccion = servidor.puedeRealizarAccion(packServer.getToken(), packServer.getNick());

            if (!puedeRealizarAccion) {
                System.out.println("ScooterServerThread::ejecutarMetodo error: No puede ejecutar método. No tiene los permisos");
                errores = true;
                System.out.println("Token: " + packServer.getToken());
                if (packServer.getToken().isEmpty()) {
                    error = "No existe token para esta sesión";
                } else if (!servidor.estaConectado(packServer.getToken())) {
                    error = "No hay token de sesión para este usuario";
                } else {
                    error = "El token de sesión no coincide";
                }
            }
            
            // Añadir la securización de WHITE-LIST y BLACK-LIST
            // Estas listas solo estaran disponible junto con la tokenización
            
        }
        
        PaqueteCliente packCliente = new PaqueteCliente();
        packCliente.setIdPaquete(packServer.getIdPaquete());
        
        // Hacer que el return sea especifico. No como ahora que siempre devolverá un Map<String,String>
        if (!errores) {
            // Incluimos el idthread, necesario para acceder a este Thread desde el controlador
            packServer.getArgumentos().put("idThread", idThread+"");
            Map<String, String> result = (Map<String, String>) metodo.invoke(packServer.getArgumentos());

            if (result==null) {
                result=new HashMap<>();
                result.put("error", "Error: El método " + packServer.getUri() + " se ha ejecutado, pero no ha devuelto nada");
            }
            
            // Guarda los nuevos argumentos devueltos
            packCliente.setArgumentos(result);
            if (result.containsKey("error")) {
                packCliente.setCodigo(CODIGO.error);
            } else {
                packCliente.setCodigo(CODIGO.ok);
            }
        } else {
            packCliente.setCodigo(CODIGO.error);
            Map<String,String> result = new HashMap<>();
            packCliente.setArgumentos(result);
            result.put("error", "No se ha podido ejecutar la consulta: " + error);
        }

        // Se empaqueta
        String packed = Util.packFromClient(packCliente);

        // Se devuelve
        return packed;
    }
}
