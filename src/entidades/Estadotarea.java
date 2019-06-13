package entidades;

import java.util.HashSet;
import java.util.Set;

public class Estadotarea  implements java.io.Serializable {


     private Integer id;
     private String nombre;
     private String descripcion;
     private Set tareas = new HashSet(0);

    public Estadotarea() {
    }

	
    public Estadotarea(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    public Estadotarea(String nombre, String descripcion, Set tareas) {
       this.nombre = nombre;
       this.descripcion = descripcion;
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
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Set getTareas() {
        return this.tareas;
    }
    
    public void setTareas(Set tareas) {
        this.tareas = tareas;
    }




}


