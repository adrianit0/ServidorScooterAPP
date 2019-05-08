/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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
        
        // 200 - 299. Mensajes de confirmación (Status OK)
        ok                      (200),
        
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
    private static final String separatorArgs = "[:]";
    private static final ENCRIPTADOR encriptacion = ENCRIPTADOR.plain;
    
    /**
     * Convierte una cadena de texto en un paquete 
     * 
     * TODO: Convertir el paquete en un DTO
     */
    public static PaqueteServidor unpackToServer (String cadena) {
        String[] decoded = decode(cadena);
        
        if (decoded==null || decoded.length<3) {
            System.err.println("Error Util::unpack: El paquete no se ha formado correctamente.");
            //throw new Exception();
            return null;
        }
        
        // Extraemos el contenido del paquete
        String idPaquete = decoded[0];
        String nick = decoded[1];
        String token = decoded[2];
        String uri = decoded[3];
        Map<String,String> parametros = new HashMap<>();
        if (decoded.length>=4) {
            for (int i = 4; i < decoded.length; i++) {
                String[] type = decoded[i].split(separatorArgs);
                if (type.length<2) {
                    // NO ES UN ARGUMENTO.
                    // TODO: devolver correctamente el mensaje de error
                    System.err.println("La variable " + decoded[i] + " no es un parametro");
                    continue;
                }
                parametros.put(type[0], type[1]);
            }
        }
        
        // Lo almacenamos en un objeto de tipo Paquete
        PaqueteServidor pack = new PaqueteServidor();
        pack.setIdPaquete(idPaquete);
        pack.setNick(nick);
        pack.setToken(token);
        pack.setUri(uri);
        pack.setArgumentos(parametros);
        
        // Lo devolvemos
        return pack;
    }
    
    /**
     * Convierte un paquete en un String.
     * 
     * TODO: Convertir el paquete en un DTO
     */
    public static String packFromServer (PaqueteServidor paquete) {
        String parametros = "";
        for (Map.Entry<String, String> entry : paquete.getArgumentos().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            
            parametros += key + ":" + value + ";";
        }
        
        String encoded = encode (paquete.getIdPaquete(), paquete.getNick(), paquete.getToken(), paquete.getUri(), parametros);
        
        return encoded;
    }
    
    /**
     * Convierte una cadena de texto en un paquete 
     * 
     * TODO: Convertir el paquete en un DTO
     */
    public static PaqueteCliente unpackToCliente (String cadena) {
        String[] decoded = decode(cadena);
        
        if (decoded==null || decoded.length<3) {
            System.err.println("Error Util::unpack: El paquete no se ha formado correctamente.");
            //throw new Exception();
            return null;
        }
        
        // Extraemos el contenido del paquete
        CODIGO codigo = CODIGO.fromCode(decoded[0]);
        String idPaquete = decoded[1];
        Map<String,String> parametros = new HashMap<>();
        if (decoded.length>2) {
            for (int i = 2; i < decoded.length; i++) {
                String[] type = decoded[i].split(separatorArgs);
                if (type.length<2) {
                    // NO ES UN ARGUMENTO.
                    // TODO: devolver correctamente el mensaje de error
                    System.err.println("La variable " + decoded[i] + " no es un parametro");
                    continue;
                }
                parametros.put(type[0], type[1]);
            }
        }
        
        // Lo almacenamos en un objeto de tipo Paquete
        PaqueteCliente pack = new PaqueteCliente();
        pack.setCodigo(codigo);
        pack.setIdPaquete(idPaquete);
        pack.setArgumentos(parametros);
        
        // Lo devolvemos
        return pack;
    }
    
    /**
     * Convierte un paquete en un String.
     * 
     * TODO: Convertir el paquete en un DTO
     */
    public static String packFromClient (PaqueteCliente paquete) {
        String parametros = "";
        for (Map.Entry<String, String> entry : paquete.getArgumentos().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            
            parametros += key + ":" + value + ";";
        }
        
        String encoded = encode (paquete.getCodigo(), paquete.getIdPaquete(), parametros);
        
        return encoded;
    }
    
    /**
     * Convierte el texto del servidor a una sola linea
     */
    private static String encode (CODIGO code) {
        return encriptar(encriptacion, code.getCodigo()+"");
    }
    
    /**
     * Convierte el texto del servidor a una sola linea
     */
    private static String encode (CODIGO code, String... contenido) {
        return encriptar(encriptacion, code.getCodigo()+separator+String.join(separator, transformar(contenido)));
    }
    
    private static String encode (String... contenido) {
        return encriptar(encriptacion, String.join(separator, transformar(contenido)));
    }
    
    /**
     * Desencripta el contenido de la cadena en un array de String
     */
    private static String[] decode (String cadena) {
        return destransformar(desencriptar(encriptacion,cadena).replace("\0","").split(separator));
    }
    
    // Es probable que dentro del texto contenga información que pueda corromperse
    // debido a la estructura interna de la trama de datos, por eso lo vamos a sustituir
    // usando un sistema de entidades parecidas a la que utiliza HTML
    private static String[] transformar (String[] textos) {
        for (int i = 0; i < textos.length; i++)
            textos[i]=textos[i].replaceAll("[&]", "&a").replaceAll("[|]", "&p").replaceAll("["+separator+"]", "&c").replaceAll("["+separatorArgs+"]", "&d");
        
        return textos;
    }
    
    // Vuelve a convertir de las entidades al que habia antes
    private static String[] destransformar (String[] textos) {
        for (int i = 0; i < textos.length; i++) 
            textos[i] = textos[i].replaceAll("&a", "&").replaceAll("&p", "|").replaceAll("&c", separator).replaceAll("&d", separatorArgs);
        
        return textos;
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
    
    public static Map<String,String> convertObjectToMap (Object obj) {
        Class clase = obj.getClass();;
        Field[] campos = clase.getDeclaredFields();
        
        Map<String,String> parametros = new HashMap<>();
        
        for (Field f : campos) {
            String genericType = f.getGenericType().toString().split(" ")[0];
            
            if (!genericType.equals("interface")) {
                try {
                    boolean ignore = false;
                    Annotation[] anotaciones = f.getAnnotations();
                    for (Annotation a : anotaciones) {
                        if (a.annotationType().getSimpleName().equals("Ignore")){
                            ignore=true;
                            break;
                        }
                    }
                    if (ignore) continue;
                    
                    String methodName = f.getName();
                    methodName = "get" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
                    
                    Method metodo = clase.getMethod (methodName);
                    Object o = metodo.invoke(obj);
                    
                    parametros.put(f.getName(), o==null?null:o.toString());
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    System.err.println("Error en "+ f.getName() + ": "+ex.getMessage() + " ("+ex.getClass().getName()+")");
                } 
                
            }
        }
        
        return parametros;
    }
    
    // Mejoraría cambiar el cifrado
    public static String crearTokenUsuario () {
        return Integer.toString(Math.abs(Double.toString(Math.random()*Math.random()).hashCode()));
    }
}