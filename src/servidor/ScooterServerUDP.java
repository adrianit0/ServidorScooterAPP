/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.LinkedHashMap;
import util.Util;
import util.Util.CODIGO;

/**
 *
 * @author Adrián García
 */
public class ScooterServerUDP extends Thread{
    protected DatagramSocket socket = null;
    private ScooterBBDD bd;
    public final int port = 4445;
    
    private LinkedHashMap<String, ScooterInfo> scooterConectadas;
    private LinkedHashMap<String, ClienteInfo> usuariosConectados;
    
    // Constantes
    private final double outTime = 300000; // Tiempo por el que se dará por finalizada la sesión. 5 minutos
    
    // Valores para el foreach
    private String tokenEncontrado;

    public ScooterServerUDP() throws IOException {
        this("QuoteServerThread");
    }

    public ScooterServerUDP(String name) throws IOException {
        super(name);
        bd = new ScooterBBDD();
        socket = new DatagramSocket(port);
        
        usuariosConectados = new LinkedHashMap<>(8);
    }

    public void run() {
        while (true) {
            try {
                byte[] bufIn = new byte[256]; //A buffer to get whatever information the client send with the request
                DatagramPacket packetIn = new DatagramPacket(bufIn, bufIn.length);
                
                // Esperamos a recibir el paquete
                socket.receive(packetIn);
                
                //System.out.println("Llegado información: " + new String(bufIn));

                // Cogemos la IP y puerto de la persona que recibimos el mensaje
                InetAddress address = packetIn.getAddress();
                int port = packetIn.getPort();
                
                // 
                byte[] valores = devolverCadena (new String(bufIn)).getBytes();

                // Pedimos el datagramPacket
                DatagramPacket packetOut = new DatagramPacket(valores, valores.length, address, port);
                // Se envia
                socket.send(packetOut);

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

        //3. Close the DatagramSocket
        socket.close();
    }
    
    /*
            OPCION          ; USUARIO ; CONTRASEÑA
                          /- existe?     Añadir token del cliente en el LinkedHashMap
         identificarse   <
                          \- No existe - Mensaje de error
    
                          /- existe?   - Mensaje de error
         registrarse     <
                          \- No existe . Añadir cliente a la bbdd
    
    */
    private String devolverCadena (String texto) {
        String[] contenido = Util.decode(texto);
        Util.CODIGO cod = Util.CODIGO.fromCode(contenido[0]);
        long id;
        String token;
        switch (cod) {
            case identificarse:
                // RECIBE ->   500:<usuario>:<pass::enTextoPlano>
                // ENVIA  ->   501:<rol><token>
                
                id = bd.identificarCliente(contenido[1], contenido[2]);
                if (id>=0) {
                    // DEVOLVER TAMBIEN EL ROL QUE TIENE
                    ClienteInfo nuevoCliente = new ClienteInfo (id, contenido[1], System.currentTimeMillis(), 0); // <- Cambiar
                    token = conectarUsuario(nuevoCliente);
                    return Util.encode(CODIGO.conectado, ""+nuevoCliente.getRol(), token);
                } else {
                    return Util.encode(CODIGO.loginFailed, "Nombre o contraseña equivocado");
                }
                
            case registrarse:
                // RECIBE ->   550:<usuario>:<pass::enTextoPlano>
                // ENVIA  ->   551:<rol><token>
                
                boolean existe = bd.existeCliente(contenido[1]);
                if (existe) {
                    return Util.encode(CODIGO.error, "El usuario "+ contenido[1]+ " ya existe. Elije otro");
                }
                
                id = bd.registrarCliente(contenido[1], contenido[2]);
                if (id>=0) {
                    // DEVOLVER TAMBIEN EL ROL QUE TIENE
                    ClienteInfo nuevoCliente = new ClienteInfo (id, contenido[1], System.currentTimeMillis(), 0); // <- Cambiar
                    token = conectarUsuario(nuevoCliente);   // Cogemos un token para el usuario
                    return Util.encode(CODIGO.registrado, ""+nuevoCliente.getRol(), token);
                } else {
                    return Util.encode(CODIGO.error, "No se ha podido registrar.");
                }
            case desconectar:
                usuariosConectados.remove(contenido[1]);
                // Devuelve el código de desconectar (580).
                return Util.encode(CODIGO.desconectar);
        }
        
        // Devuelve un error desconocido al recibir del cliente un codigo no contemplado
        return Util.encode(CODIGO.desconocido);
    }
    
    private synchronized String conectarUsuario (ClienteInfo info){
        // Si ya estuviera en la lista miramos si no se le ha caducado aún el token
        // de esta manera puede recuperarla
        // ¿Tal vez deba eliminar el token y dar otro para evitar que 2 personas con el mismo token jueguen
        // usando la misma cuenta?
        String token = containsName(info.getNombre());
        if (token!=null) {
            boolean vigente = puedeRealizarAccion (token, info.getNombre());
            if (vigente) {
                System.out.println("Usuario con token "+token+ " se ha reconectado.");
                return token;
            }
            // En caso contrario cogerá un nuevo token
        }
        
        token = Util.crearTokenUsuario();
        usuariosConectados.put(token, info);
        System.out.println("Usuario con token "+token+ " conectado.");
        return token;
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
            System.out.println("Usuario con token "+token+" usa un nick diferente ("+usuario+" != "+info.getNombre()+")");
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
    public synchronized boolean esAdmin (String token) {
        // Para tener privilegios de administrador tiene que tener el rol 2 (Administrador)
        // TODO: Cambiar para preguntar directamente a la base de datos
        ClienteInfo info = usuariosConectados.get(token);
        if (info==null)
            return false;
        return info.getRol()==2;
    }
    
    public synchronized String containsName (String nick) {
        tokenEncontrado = null;
        
        // Utilizamos el foreach para iterar más rapidamente
        usuariosConectados.forEach((token, cliente) -> {
            if (nick.contains(cliente.getNombre())) {
                tokenEncontrado=token;
                return;
            }
        });
        return tokenEncontrado;
    }
    
    // Obtiene un cliente a partir de su socket
    protected synchronized ClienteInfo getClient(String token) {
        return usuariosConectados.get(token);
    }
    
    protected ScooterBBDD getBD () {
        return bd;
    }
}
