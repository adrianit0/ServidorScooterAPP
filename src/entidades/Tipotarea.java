package entidades;
// Generated 28-abr-2019 23:19:03 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Tipotarea generated by hbm2java
 */
public class Tipotarea  implements java.io.Serializable {


     private Integer id;
     private String nombre;
     private Set tareas = new HashSet(0);

    public Tipotarea() {
    }

	
    public Tipotarea(String nombre) {
        this.nombre = nombre;
    }
    public Tipotarea(String nombre, Set tareas) {
       this.nombre = nombre;
       this.tareas = tareas;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Set getTareas() {
        return this.tareas;
    }
    
    public void setTareas(Set tareas) {
        this.tareas = tareas;
    }




}

