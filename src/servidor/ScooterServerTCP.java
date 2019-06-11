/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import configuration_server.ConfigurationMapper;
import configuration_server.ConfigurationMethod;
import configuration_server.ConfigurationSAXMapper;
import configuration_server.Rol;
import entidades.Alquiler;
import entidades.Cliente;
import entidades.Estadoalquiler;
import entidades.Scooter;
import excepciones.AlquilerException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Constantes;
import util.HibernateManager;
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
    
    private ArrayList<Scooter> scooters;
    private LinkedHashMap<String, ClienteInfo> usuariosConectados; 
    private LinkedHashMap<Integer, Socket> socketsConectados;
    private LinkedHashMap<String, Alquiler> alquileres;
    
    private HibernateManager hibernateManager;
    
    private boolean listening;
    
    // Constantes
    private final double outTime = 300000; // Tiempo por el que se dará por finalizada la sesión. 5 minutos
    
    private static ScooterServerTCP instance;
    
    public ScooterServerTCP (int port) {
        this.port = port;
        
        socketsConectados = new LinkedHashMap<>(32);
        usuariosConectados = new LinkedHashMap<>(32);
        scooters = new ArrayList<>();
        alquileres = new LinkedHashMap<>(32);
        
        hibernateManager = new HibernateManager();
        
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
        
        System.out.println("Inicializando mapeadores de clases y métodos");
        ConfigurationSAXMapper saxFactory = new ConfigurationSAXMapper(this);
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
        
        System.out.println("Inicializando Servidor");
        opnServerSocket();
        
        System.out.println("Tomando los alquileres que siguen en curso");
        List<Alquiler> alquileres = this.hibernateManager.getObjects("Alquiler as a", "WHERE a.estadoalquiler.id=3");
        if (alquileres.size()>0) {
            // Se cancelaran los alquileres actualmente a medias, aunque habría que poner los criterios por los que continuar algunos alquileres.
            System.out.println(alquileres.size() + " encontrados sin finalizar. Se procederá a cancelarlos");
            for (Alquiler a : alquileres) {
                a.setEstadoalquiler(new Estadoalquiler(5));
                hibernateManager.updateObject(a);
            }
        }
        
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
            Integer idThread = clientSocket.hashCode();
            socketsConectados.put(idThread, clientSocket);
            new ScooterServerThread(clientSocket, idThread, this).start();
        }
        System.out.println("Servidor apagando...") ;
        
        stopServer ();
    }
    
    public synchronized void addScooter (Scooter s) {
        scooters.add(s);
    }
    
    public synchronized List getScooters () {
        return scooters;
    }
    
    public synchronized Scooter getScooter (String serie) {
        for (Scooter s : scooters) {
            if (s.getNoSerie().equals(serie)) {
                return s;
            }
        }
        return null;
    }
    
    public synchronized void reservarScooter (String token, Scooter scooter) throws AlquilerException {
        ClienteInfo info = getClient(token);
        
        if (info==null) {
            System.err.println("ScooterServerTCP::reservarScooter error: token inexistente");
            throw new AlquilerException ("El cliente no está conectado");
        }
        
        if (!scooters.contains(scooter) || scooter.isBloqueada()) {
            System.err.println("ScooterServerTCP::reservarScooter error: La scooter no se encuentra o ya está bloqueada");
            throw new AlquilerException ("La scooter no se encuentra o ya ha sido bloqueada por otro usuario");
        }
        
        Alquiler alquiler = alquileres.get(info.getNombre());
        if (alquiler!=null) {
            System.err.println("ScooterServerTCP::reservarScooter error: Ya tienes un alquiler");
            throw new AlquilerException ("Ya tienes un alquiler");
        }
        
        scooter.setBloqueada(true);
        
        Cliente cliente = (Cliente) hibernateManager.getObject(Cliente.class, (int) info.getId());
        Timestamp fechaInicio = new Timestamp(System.currentTimeMillis());
        
        alquiler = new Alquiler();
        alquiler.setCliente(cliente);
        alquiler.setScooter(scooter);
        alquiler.setEstadoalquiler(new Estadoalquiler(1));
        alquiler.setFechaInicio(fechaInicio);
        alquiler.setCosteTotal(0); // EL coste inicial será de 0
        
        Integer id = hibernateManager.addObject(alquiler);
        alquiler.setId(id);
        
        alquileres.put(info.getNombre(), alquiler);
    }
    
    public synchronized void cancelarReservaScooter (String token, Scooter scooter) throws AlquilerException {
        ClienteInfo info = getClient(token);
        if (info==null){
            System.err.println("ScooterServerTCP::cancelarReservaScooter error: El cliente no está conectado");
            throw new AlquilerException ("El cliente no está conectado");
        }
        
        Alquiler alquiler = alquileres.get(info.getNombre());
        if (alquiler==null) {
            System.err.println("ScooterServerTCP::cancelarReservaScooter error: Alquiler no encontrado");
            throw new AlquilerException ("Alquiler no encontrado");
        }
        
        if (alquiler.getEstadoalquiler().getId()!=1) {
            System.err.println("ScooterServerTCP::cancelarReservaScooter error: El estado del alquiler es incorrecto. " + alquiler.getEstadoalquiler().getId());
            throw new AlquilerException ("El estado del alquiler es incorrecto. " + alquiler.getEstadoalquiler().getId());
        }
        
        if (!scooters.contains(scooter) || !scooter.isBloqueada()) {
            System.err.println("ScooterServerTCP::cancelarReservaScooter error: no hay scooters o no está bloqueada");
            throw new AlquilerException ("No hay scooters o no está bloqueada");
        }
        
        scooter.setBloqueada(false);
        
        Timestamp fechaFin = new Timestamp(System.currentTimeMillis());
        
        alquiler.setFechaFin(fechaFin);
        alquiler.setEstadoalquiler(new Estadoalquiler(2));
        
        // Lo eliminamos de la lista de alquileres.
        alquileres.remove(info.getNombre());
        
        boolean actualizado = hibernateManager.updateObject(alquiler);
        if (!actualizado) {
            System.err.println("ScooterServerTCP::cancelarReservaScooter error: no se ha actualizado el alquiler");
            throw new AlquilerException ("No se ha podido actualizar el alquiler");
        }
        
        
    }
    
    public synchronized void empezarAlquiler (String token, Scooter scooter) throws AlquilerException {
        ClienteInfo info = getClient(token);
        
        if (info==null) {
            System.err.println("ScooterServerTCP::empezarAlquiler error: El cliente no está conectado");
            throw new AlquilerException ("El cliente no está conectado");
        }
        
        if (scooter==null){
            System.err.println("ScooterServerTCP::empezarAlquiler error: No hay scooter para realizar el alquiler");
            throw new AlquilerException ("No hay scooter para realizar el alquiler");
        }
        
        Alquiler alquiler = alquileres.get(info.getNombre());
        if (alquiler==null) {
            System.err.println("ScooterServerTCP::empezarAlquiler error: Alquiler no encontrado");
            throw new AlquilerException ("Alquiler no encontrado");
        }
        
        if (alquiler.getEstadoalquiler().getId()!=1) {
            System.err.println("ScooterServerTCP::empezarAlquiler error: El estado del alquiler es incorrecto. " + alquiler.getEstadoalquiler().getId());
            throw new AlquilerException ("El estado del alquiler es incorrecto. " + alquiler.getEstadoalquiler().getId());
        }
        
        Cliente cliente = (Cliente) hibernateManager.getObject(Cliente.class, (int) info.getId());
        Timestamp fechaInicio = new Timestamp(System.currentTimeMillis());
        
        alquiler.setFechaInicio(fechaInicio); // Esta es la fecha inicio real del alquiler
        alquiler.setEstadoalquiler(new Estadoalquiler(3));
        
        boolean actualizado = hibernateManager.updateObject(alquiler);
        if (!actualizado) {
            System.err.println("ScooterServerTCP::empezarAlquiler error: no se ha actualizado el alquiler");
            throw new AlquilerException ("No se ha actualizado el alquiler");
        }
    }
    
    public synchronized Alquiler terminarAlquiler (String token) throws AlquilerException {
        ClienteInfo info = getClient(token);
        
        if (info==null) {
            System.err.println("ScooterServerTCP::terminarAlquiler error: El cliente no está conectado");
            throw new AlquilerException ("El cliente no está conectado");
        }
        
        Alquiler alquiler = alquileres.get(info.getNombre());
        if (alquiler==null) {
            System.err.println("ScooterServerTCP::terminarAlquiler error: No hay alquiler para este usuario");
            throw new AlquilerException ("No hay alquiler para este usuario");
        }
        
        if (alquiler.getEstadoalquiler().getId()!=3) {
            System.err.println("ScooterServerTCP::terminarAlquiler error: El estado del alquiler es incorrecto. " + alquiler.getEstadoalquiler().getId());
            throw new AlquilerException ("El estado del alquiler es incorrecto. " + alquiler.getEstadoalquiler().getId());
        }
            
        
        Timestamp fechaInicio = alquiler.getFechaInicio();
        Timestamp fechaFin = new Timestamp(System.currentTimeMillis());
        
        long diferencia = fechaFin.getTime() - fechaInicio.getTime();
        
        long segundos = diferencia/1000;
        int minutos = (int) Math.ceil(segundos/60);
        
        
        // Volvemos a coger el cliente, por si tuviera una cantidad de minutos diferentes con las que empezó el alquiler.
        Cliente cliente = (Cliente) hibernateManager.getObject(Cliente.class, alquiler.getCliente().getId());
        
        int minutosCliente = cliente.getMinutos();
        int minutosConsumidos;
        
        double costeMinuto = Constantes.precioBase;
        double costeFinal = 0;
        
        if (minutos>minutosCliente) {
            // Ha sobrepasado los minutos, por lo que el coste final no será gratis
            cliente.setMinutos(0);
            minutosConsumidos = minutosCliente;
            costeFinal = (minutos - minutosCliente) * costeMinuto;
        } else {
            // Se cobra los minutos y el viaje no tendrá coste añadido
            cliente.setMinutos(minutosCliente-minutos);
            minutosConsumidos = minutos;
        }
        
        Scooter scooter = alquiler.getScooter();
        scooter.setBloqueada(false); // Vuelve a estar disponible para alquilar
        
        alquiler.setFechaFin(fechaFin);
        alquiler.setMinutosConducidos(minutos);
        alquiler.setCosteTotal(costeFinal);
        alquiler.setMinutosConsumidos(minutosConsumidos);
        alquiler.setEstadoalquiler(new Estadoalquiler(4));
        
        hibernateManager.updateObject(alquiler);
        hibernateManager.updateObject(cliente);
        
        // Lo eliminamos de la lista de alquileres.
        alquileres.remove(info.getNombre());
        
        alquiler.setCliente(cliente);
        
        return alquiler;
    }
    
    public Alquiler getAlquiler (String nick) {
        return alquileres.get(nick);
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
            this.listening = true;
        } catch (IOException e) {
            this.listening = false;
            throw new RuntimeException("No se puede abrir el puerto "+port, e);
        }
    }
    
    public synchronized Rol getRoleUsuario (String token) {
        ClienteInfo info = getClient(token);
        if (info==null)
            return null;
        
        return info.getRol();
    }
    
    public synchronized boolean puedeRealizarAccion(String token, String usuario) {
        // Miramos si el token recibido existe actualmente entre los
        // conectados. De no existir mandamos un error y obligamos al cliente a 
        // salirse de estar conectado.
        if (!usuariosConectados.containsKey(token)) {
            System.out.println("ScooterServerTCP::puedeRealizarAccion error: Usuario con token " + token + " ha intentado entrar al servidor pero no existe en el servidor");
            return false;
        }

        ClienteInfo info = usuariosConectados.get(token);

        // Observamos si el token del usuario corresponde al usuario
        // Por seguridad eliminamos el token de sesión.
        if (!info.getNombre().equals(usuario)) {
            System.out.println("ScooterServerTCP::puedeRealizarAccion error: Usuario con token " + token + " usa un nick diferente (" + usuario + " != " + info.getNombre() + ")");
            usuariosConectados.remove(token);
            return false;
        }

        long tiempoActual = System.currentTimeMillis();

        
        boolean puedeCerrarSesion = true;
        
        // Alguna reglas por las que no se puede cerrar sesión
        if (info.getRol() == Rol.SCOOTER)
            puedeCerrarSesion=false;
        else if (info.getRol() == Rol.CLIENTE && alquileres.get(info.getNombre())!=null)
            puedeCerrarSesion=false;
        
        // Si el usuario ha estado más de %outTime% sin hacer nada saldrá del servidor
        // Eliminamos el token ya que sabemos que no lo volveremos a utilizar
        // Las Scooters no tendran tiempo de TimeOut
        // Tampoco los usuarios que esté en alquiler
        if (puedeCerrarSesion && tiempoActual - info.getTimeSinceLastAction() > outTime) {
            usuariosConectados.remove(token);
            return false;
        }

        // Si está dentro del tiempo entonces actualizamos el tiempo
        info.setTimeSinceLastAction(tiempoActual);

        return true;
    }
    
    
    public synchronized String conectarUsuario(ClienteInfo info) {
        // Si ya estuviera en la lista miramos si no se le ha caducado aún el token
        // de esta manera puede recuperarla
        // ¿Tal vez deba eliminar el token y dar otro para evitar que 2 personas con el mismo token jueguen
        // usando la misma cuenta?
        String token = containsName(info.getNombre());
        if (token != null) {
            boolean vigente = puedeRealizarAccion(token, info.getNombre());
            if (vigente) {
                usuariosConectados.get(token).setIdThread(info.getIdThread());
                System.out.println("Usuario con token " + token + " se ha reconectado.");
                return token;
            }
            // En caso contrario cogerá un nuevo token
        }

        token = Util.crearTokenUsuario();
        info.setTimeSinceLastAction(System.currentTimeMillis());
        usuariosConectados.put(token, info);
        System.out.println("Usuario con token " + token + " conectado.");
        return token;
    }
    
    public synchronized boolean desconectarUsuario (Integer idThread) {
        if (idThread==null) {
            System.err.println("ScooterServerTCP::desconectarUsuario: No se ha enviado ningún id Thread");
            return false;
        }
        
        if (socketsConectados.containsKey(idThread)) {
            ClienteInfo info = null;
            // Buscamos el usuario si está incluido
            for (Map.Entry<String, ClienteInfo> entry : usuariosConectados.entrySet()) {
                ClienteInfo value = entry.getValue();
                
                if (value!=null && value.getIdThread()!=null && value.getIdThread().equals(idThread)) {
                    info = value;
                    break;
                }
            }
            
            if (info==null)
                return false;
            
            // Si es una scooter desconectamos tambien su estado
            if (info.getRol()==Rol.SCOOTER) {
                for (int i = 0; i < scooters.size(); i++) {
                    if (scooters.get(0).getId().equals(info.getId())) {
                        scooters.remove(scooters.get(0));
                        break;
                    }
                }
            }
            
            // No desconectaremos el infoClient, tan solo dejaremos idThread en null
            info.setIdThread(null);
            
            return true;
        }
        
        return false;
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
        return info.getRol() == Rol.ADMINISTRADOR;
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
        (new ScooterServerTCP(4444)).start();
    }
}