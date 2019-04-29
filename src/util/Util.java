/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 * @author adrian
 */
public class Util {
    
    /**
     * Convierte codigos dificiles de enviar por letras distinguibles.
     * 
     * Además permite modificar el codigo sin que este afecte al cliente y servidor
     * siempre y cuando ambos usen la misma versión del protocolo.
     * 
     * Igualmente no se recomienda modificar los códigos de error una vez ha sido
     * seleccionado
     */
    public enum CODIGO {
        // 0 - 99. Juego
        desconocido             (-1),
        error                   (3),
        ponerMenu               (30),
        
        // 400 - 449. Errores del cliente
        forbidden               (403),  // Intentar acceder sin tener privilegios necesarios
        notFound                (404),
        sessionExpired          (440),
        
        // 450 - 499. Errores del servidor
        internalError           (450),
        
        // 500 - 599. UDP
        identificarse           (500),
        conectado               (501),
        loginFailed             (502),
        
        registrarse             (550),
        registrado              (551),
        
        desconectar             (580)   // Cierra la sesión eliminando el token.
        
        ;
        
        int codigo;
        CODIGO (int codigo) {
            this.codigo = codigo;
        }
        
        public int getCodigo() {
            return codigo;
        }
        
        public static CODIGO fromCode (int code) {
            CODIGO[] cod = CODIGO.values();
            for (CODIGO c : cod) {
                if (c.getCodigo()==code)
                    return c;
            }
            
            return CODIGO.desconocido;
        }
        
        public static CODIGO fromCode (String code) {
            CODIGO cod = CODIGO.desconocido;
            try {
                cod = Util.CODIGO.fromCode(Integer.parseInt(code));
            } catch (NumberFormatException e) {
                cod = Util.CODIGO.desconocido;
                System.err.println("Error de parseo del texto "+code+". Error: "+e.getMessage());
            }
            return cod;
        }
    };
    
    // TODO: Poner al menos una manera de codificar el texto de manera online.
    // Es más seguro que ir en texto plano. Luego tiene que volver a ser igual
    // en el decode.
    public enum ENCRIPTADOR {
        plain, hybridCriptography //<- Este es el que usaré
    };

    private static final String separator = ";";
    
    /**
     * Convierte el texto del servidor a una sola linea
     */
    public static String encode (CODIGO code) {
        return encriptar(ENCRIPTADOR.plain, code.getCodigo()+"");
    }
    
    /**
     * Convierte el texto del servidor a una sola linea
     */
    public static String encode (CODIGO code, String... contenido) {
        return encriptar(ENCRIPTADOR.plain, code.getCodigo()+separator+String.join(separator, contenido));
    }
    
    /**
     * Desencripta el contenido de la cadena en un array de String
     */
    public static String[] decode (String cadena) {
        return desencriptar(ENCRIPTADOR.plain,cadena).replace("\0","").split(separator);
    }
    
    /**
     * Encripta el código para presentar una mejor seguridad en la transferencia
     * de datos del servidor-cliente.
     * 
     * De momento no encripta nada, pero el método ya está creado.
     * 
     * TODO: Utilizar encriptación asimétrica (clave publica-privada) para la encriptación.
     */
    private static String encriptar (ENCRIPTADOR cod, String texto) {
        return texto;
    }
    
    
    /**
     * Desencripta un texto. El método de encriptacion debe ser el mismo que lleva.
     * 
     */
    private static String desencriptar (ENCRIPTADOR cod, String texto) {
        return texto;
    }
    
    // Mejoraría cambiar el cifrado
    public static String crearTokenUsuario () {
        return Integer.toString(Math.abs(Double.toString(Math.random()*Math.random()).hashCode()));
    }
}