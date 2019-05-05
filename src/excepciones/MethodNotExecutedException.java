/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excepciones;

/**
 *
 * @author agarcia.gonzalez
 */
public class MethodNotExecutedException extends Exception{

    public MethodNotExecutedException() {
    }

    public MethodNotExecutedException(String message) {
        super(message);
    }

    public MethodNotExecutedException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodNotExecutedException(Throwable cause) {
        super(cause);
    }

    public MethodNotExecutedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
