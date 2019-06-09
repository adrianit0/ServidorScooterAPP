/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excepciones;

/**
 *
 * @author Adrián
 */
public class AlquilerException extends Exception {

    public AlquilerException(String message) {
        super(message);
    }

    public AlquilerException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlquilerException(Throwable cause) {
        super(cause);
    }

    public AlquilerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
