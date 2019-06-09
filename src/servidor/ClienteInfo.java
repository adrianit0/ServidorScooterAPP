/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

/**
 *
 * Contiene la información del cliente que tiene a partir de su token.
 * 
 * @author Adrián García
 */
public class ClienteInfo {
    public enum Rol {
        CLIENTE, ADMINISTRADOR, TECNICO, SCOOTER
    };
    private long id;
    private String nombre;
    private long timeSinceLastAction;   // Tiempo que ha pasado desde la última acción.
    private Rol rol;                    // Rol que tiene el cliente
    
    private Integer idThread;

    public ClienteInfo() {
    }

    
    
    public ClienteInfo(long id, String nombre, long timeSinceLastAction, Rol rol, Integer idThread) {
        this.id = id;
        this.nombre = nombre;
        this.timeSinceLastAction = timeSinceLastAction;
        this.rol = rol;
        this.idThread = idThread;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Obtiene el nickname del usuario
     */
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getTimeSinceLastAction() {
        return timeSinceLastAction;
    }

    public void setTimeSinceLastAction(long timeSinceLastAction) {
        this.timeSinceLastAction = timeSinceLastAction;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Integer getIdThread() {
        return idThread;
    }

    public void setIdThread(Integer idThread) {
        this.idThread = idThread;
    }
}