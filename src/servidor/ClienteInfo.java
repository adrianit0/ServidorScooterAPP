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
    private long id;
    private String nombre;
    private long timeSinceLastAction;   // Tiempo que ha pasado desde la última acción.
    private int rol;                    // Rol que tiene el cliente

    public ClienteInfo(long id, String nombre, long timeSinceLastAction, int rol) {
        this.id = id;
        this.nombre = nombre;
        this.timeSinceLastAction = timeSinceLastAction;
        this.rol = rol;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }
}