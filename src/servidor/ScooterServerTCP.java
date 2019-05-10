/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import configuration_server.ConfigurationMapper;
import configuration_server.ConfigurationMethod;
import configuration_server.ConfigurationSAXMapper;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.HibernateUtil;
import util.Util;

/**
 *
 * @author Lara
 */
public class ScooterServerTCP extends Thread{
    
    private ServerSocket serverSocket;
    private ConfigurationMapper mapper;
    private final int port;
    
    private LinkedHashMap<String, ClienteInfo> usuariosConectados; 
    
    private Thread thisThread= null;
    private boolean listening;
    
    // Constantes
    private final double outTime = 300000; // Tiempo por el que se dará por finalizada la sesión. 5 minutos
    
    private static ScooterServerTCP instance;
    
    public ScooterServerTCP (int port) {
        this.port = port;
        
        usuariosConectados = new LinkedHashMap<>(32);
        
        if (instance==null)
            instance=this;
    }
    
    @Override 
    public void run () {
        exec();
    }
    
    private void exec () {
        //Servidor TCP, para las Scooters y los clientes
        serverSocket = null;
        listening = true;
        
        System.out.println("Servidor inicializandose");
        
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("No puedo escuchar el puerto: " + port);
            return;
        }
        
        System.out.println("Inicializando mapeadores de clases y métodos");
        ConfigurationSAXMapper saxFactory = new ConfigurationSAXMapper();
        mapper = saxFactory.createMapper();
        
        System.out.println("Inicializando Hibernate");
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
        
        
        this.thisThread = getCurrentThread();
        
        System.out.println("Inicializando Servidor");
        opnServerSocket();
        
        System.out.println("Servidor inicializado. Ya puede recibir usuarios.");
        while(isOpen()){
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                if(!isOpen()) {
                    System.out.println("El servidor ha finalizado") ;
                    return;
                }
                throw new RuntimeException("El cliente no puede conectarse - Error", e);
            }
            new ScooterServerThread(clientSocket, this).start();
        }
        System.out.println("Server has Stopped...Please check") ;
        
        stopServer ();
    }
    
    public synchronized ConfigurationMethod getMethod (String uri) {
        return mapper.getMethod(uri);
    }
    
    private synchronized Thread getCurrentThread () {
        return Thread.currentThread();
    }
    
    private synchronized boolean isOpen() {
        return listening;
    }
    
    public synchronized void stopServer(){
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("El servidor no puede finalizar la conexión: ", e);
        }
        this.listening = false;
    }
    
    private void opnServerSocket() {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException("No se puede abrir el puerto "+port, e);
        }
        this.listening = true;
    }
    
    public synchronized boolean puedeRealizarAccion(String token, String usuario) {
        // Miramos si el token recibido existe actualmente entre los
        // conectados. De no existir mandamos un error y obligamos al cliente a 
        // salirse de estar conectado.
        if (!usuariosConectados.containsKey(token)) {
            System.out.println("Usuario con token " + token + " ha intentado entrar al servidor");
            return false;
        }

        ClienteInfo info = usuariosConectados.get(token);

        // Observamos si el token del usuario corresponde al usuario
        // Por seguridad eliminamos el token de sesión.
        if (!info.getNombre().equals(usuario)) {
            System.out.println("Usuario con token " + token + " usa un nick diferente (" + usuario + " != " + info.getNombre() + ")");
            usuariosConectados.remove(token);
            return false;
        }

        long tiempoActual = System.currentTimeMillis();

        // Si el usuario ha estado más de %outTime% sin hacer nada saldrá del servidor
        // Eliminamos el token ya que sabemos que no lo volveremos a utilizar
        if (tiempoActual - info.getTimeSinceLastAction() > outTime) {
            usuariosConectados.remove(token);
            return false;
        }

        // Si está dentro del tiempo entonces actualizamos el tiempo
        info.setTimeSinceLastAction(tiempoActual);

        return true;
    }
    
    
    private synchronized String conectarUsuario(ClienteInfo info) {
        // Si ya estuviera en la lista miramos si no se le ha caducado aún el token
        // de esta manera puede recuperarla
        // ¿Tal vez deba eliminar el token y dar otro para evitar que 2 personas con el mismo token jueguen
        // usando la misma cuenta?
        String token = containsName(info.getNombre());
        if (token != null) {
            boolean vigente = puedeRealizarAccion(token, info.getNombre());
            if (vigente) {
                System.out.println("Usuario con token " + token + " se ha reconectado.");
                return token;
            }
            // En caso contrario cogerá un nuevo token
        }

        token = Util.crearTokenUsuario();
        usuariosConectados.put(token, info);
        System.out.println("Usuario con token " + token + " conectado.");
        return token;
    }
    
    
    /**
     * Un puede realizar acción sin necesidad de dar el nick.
     *
     * Es menos seguro que el que confirma si el nick está presente.
     *
     * Sirve para el sistema UTP en la que ya tiene una conexión.
     */
    public synchronized boolean puedeRealizarAccion(String token) {
        // Miramos si el token recibido existe actualmente entre los
        // conectados. De no existir mandamos un error y obligamos al cliente a 
        // salirse de estar conectado.
        if (!usuariosConectados.containsKey(token)) {
            System.out.println("Usuario con token " + token + " ha intentado entrar al servidor");
            return false;
        }

        ClienteInfo info = usuariosConectados.get(token);

        long tiempoActual = System.currentTimeMillis();

        // Si el usuario ha estado más de %outTime% sin hacer nada saldrá del servidor
        // Eliminamos el token ya que sabemos que no lo volveremos a utilizar
        if (tiempoActual - info.getTimeSinceLastAction() > outTime) {
            usuariosConectados.remove(token);
            return false;
        }

        // Si está dentro del tiempo entonces actualizamos el tiempo
        info.setTimeSinceLastAction(tiempoActual);

        return true;
    }

    // Mira si es o no admin
    public synchronized boolean esAdmin(String token) {
        // Para tener privilegios de administrador tiene que tener el rol 2 (Administrador)
        // TODO: Cambiar para preguntar directamente a la base de datos
        ClienteInfo info = usuariosConectados.get(token);
        if (info == null) {
            return false;
        }
        return info.getRol() == 2;
    }

    public synchronized String containsName(String nick) {
        for (Map.Entry<String, ClienteInfo> entry : usuariosConectados.entrySet())
            if (nick.contains(entry.getValue().getNombre())) 
                return entry.getKey();
            
        return null;
    }
    
    public synchronized boolean estaConectado (String token) {
        return usuariosConectados.containsKey(token);
    }

    // Obtiene un cliente a partir de su socket
    protected synchronized ClienteInfo getClient(String token) {
        return usuariosConectados.get(token);
    }
    
    public static void main(String[] args) {
        (new ScooterServerTCP(678)).start();
    }
}