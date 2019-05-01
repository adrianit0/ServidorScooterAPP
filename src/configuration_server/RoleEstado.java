/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration_server;

/**
 *
 * @author agarcia.gonzalez
 */
public class RoleEstado {
    public enum Tipo { ROLE, STATE };
    
    private Tipo tipo;
    private String nombre;

    public RoleEstado(Tipo tipo, String nombre) {
        this.tipo = tipo;
        this.nombre = nombre;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }
    
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
}
