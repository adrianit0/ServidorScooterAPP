package entidades;

import java.util.HashSet;
import java.util.Set;


public class Tipotarea  implements java.io.Serializable {


     private Integer id;
     private String nombre;
     private Set tareas = new HashSet(0);

    public Tipotarea() {
    }
    
    public Tipotarea(Integer id) {
        this.id=id;
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


