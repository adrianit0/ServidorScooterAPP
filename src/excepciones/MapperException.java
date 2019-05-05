/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excepciones;

import java.util.logging.Logger;

/**
 *
 * @author agarcia.gonzalez
 */
public class MapperException extends Exception {

    private static final Logger LOG = Logger.getLogger(MapperException.class.getName());
    
    private Exception parentException;

    public MapperException() {
        parentException = null;
    }

    public MapperException(String message, Exception e) {
        super(message + " -> " + e.getMessage());
       parentException = e;
    }

    public MapperException(String message, Throwable cause, Exception e) {
        super(message, cause);
        parentException = e;
    }

    public MapperException(Throwable cause, Exception e) {
        super(cause);
        parentException = e;
    }

    public MapperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Exception e) {
        super(message, cause, enableSuppression, writableStackTrace);
        parentException = e;
    }

    public Exception getParentException() {
        return parentException;
    }

    public void setParentException(Exception parentException) {
        this.parentException = parentException;
    }
    
    
}
